package com.jeesite.modules.weixin.controller;

import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.jeesite.common.constant.Constants;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.modules.common.util.*;
import com.jeesite.modules.flyl.entity.FlylUser;
import com.jeesite.modules.flyl.service.FlylUserService;
import com.jeesite.modules.weixin.entity.LoginBody;
import com.jeesite.modules.weixin.entity.LoginUser;
import com.jeesite.modules.weixin.service.AliSmsNewService;
import com.jeesite.modules.weixin.service.TokenService;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.tomcat.util.bcel.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户登录验证
 * */
@Controller
@RequestMapping(value = "/user")
public class AppLoginApi {

    @Autowired
    AliSmsNewService aliSmsNewService;

    private static Logger log = LoggerFactory.getLogger(AppLoginApi.class);

    @Autowired
    FlylUserService flylUserService;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    TokenService tokenService;

    //发送验证码
    @PostMapping(value="/sendSms")
    @ResponseBody
    public Result sendSms(@RequestBody LoginBody loginBody){
        String phoneNumber = loginBody.getPhoneNumber();
        String code = JedisUtils.get(phoneNumber);
        //查看redis里该手机的验证码是否过期
        if(code!= null){
            return JsonResult.error("验证码已发送，1分钟内勿重复发送");
        }
        code= UUIDUtil.getOctNum(6).toString();
        SendSmsResponse response = aliSmsNewService.sendSms(loginBody.getPhoneNumber(),code);
        log.info("sms response==>" + response.getBody().message);
        JedisUtils.set(loginBody.getPhoneNumber(),code,60 * 5);  //验证码时间20分钟
        return JsonResult.success("验证码已发送，请在5分钟内登录！");
    }

    /**
     * 短信验证登录
     * */
    @PostMapping(value="/login")
    @ResponseBody
    public Result<Map<String,Object>> smsLogin(@RequestBody LoginBody loginBody){
        Result result = JsonResult.success();
        boolean hasCode = verifySms(loginBody.getPhoneNumber(), loginBody.getCode());
        if(!hasCode) {
            result = JsonResult.error("验证码错误或已过期");
            result.setCode(444);
            return result;
        }
        FlylUser user  = new FlylUser();
        String uuid = UUIDUtil.fastUUId();
        if(hasCode){
            Map<String, Object> tokenMap = new HashMap<>();
            user.setPhoneNumber(loginBody.getPhoneNumber());
            user = flylUserService.getUserByPhone(user);
            if(user == null ){
                FlylUser flylUser = new FlylUser();
                flylUser.setPhoneNumber(loginBody.getPhoneNumber());
                flylUser.setState("0");
                flylUser.setPassword(Md5Crypt.apr1Crypt("123456",flylUser.getPhoneNumber()));  //设置初始密码123456
                flylUserService.insert(flylUser);
                flylUser = flylUserService.getUserByPhone(flylUser);
                //生成用户token信息响应体
                tokenMap = tokenService.tokenResult(flylUser,uuid);
                result = JsonResult.success("新用户，登录成功，资格正在审核中！",tokenMap);
                result.setCode(0);
            }else if("0".equals(user.getState())){
                result = JsonResult.success("登录成功，资格正在审核中！",tokenMap);
                result.setCode(0);
            } else if("2".equals(user.getState())){
                result = JsonResult.error("审核未通过，无法登录");
                result.setCode(2);
            } else {
                tokenMap = tokenService.tokenResult(user, uuid);
                JedisUtils.setObject(Constants.LOGIN_TOKEN_KEY + uuid,user,30 * 60);
                result = JsonResult.success("登录成功",tokenMap);
                result.setCode(1);
            }
            return  result;

        }
        return JsonResult.error("验证码错误！");

    }


    /**
     * 用户完善注册信息
     * @param loginUser
     * @return
     */
    @PostMapping(value = "/register")
    public Result register(@RequestBody FlylUser loginUser){
        FlylUser flylUser = flylUserService.getUserByPhone(loginUser);
        if(flylUser == null) return JsonResult.error("用户未注册，请先使用验证码登录");
        flylUser = copyProperties(loginUser, flylUser);
        flylUserService.update(flylUser);
        return JsonResult.success();
    }

    private FlylUser copyProperties(FlylUser origin,FlylUser destEntity){
        //简易加密
        destEntity.setPassword(Md5Crypt.apr1Crypt(origin.getPassword(),origin.getPhoneNumber()));  //以手机号为盐值加密
        destEntity.setUserName(origin.getUserName());
        return destEntity;
    }


    /**
     * app用户手机号密码登录
     * @param flylUser
     * @return
     */
    @PostMapping(value = "/appLogin")
    public Result appLogin(@RequestBody FlylUser flylUser){
        FlylUser user = flylUserService.getUserByPhone(flylUser);
        String uuid = UUIDUtil.fastUUId();
        if(user != null
                && StringUtils.isNotEmpty(user.getPassword())
                && verifyPassword(flylUser.getPhoneNumber(),flylUser.getPassword(),user.getPassword())){
            Map<String, Object> tokenResult = tokenService.tokenResult(user, uuid);
            Result<Map<String, Object>> result = JsonResult.success("登录成功", tokenResult);
            result.setCode(Integer.valueOf(user.getState()));
            return result;
        }
        return JsonResult.error("用户手机号或密码错误");
    }

    @PutMapping(value = "/modifyPassword")
    public Result modifyPassword(@RequestBody FlylUser flylUser){
        //修改密码前验证短信（用户忘记旧密码），或验证旧密码
        FlylUser user = flylUserService.getUserByPhone(flylUser);
        if(user == null) return JsonResult.error("修改失败，请检查手机号和验证码！");
        if(verifySms(flylUser.getPhoneNumber(),flylUser.getCode())
                || verifyPassword(flylUser.getPhoneNumber(),flylUser.getOldPassword(),user.getPassword())){
            user.setPassword(Md5Crypt.apr1Crypt(flylUser.getPassword(), flylUser.getPhoneNumber()));
            flylUserService.update(user);
            return JsonResult.success("修改成功");
        }
        return JsonResult.error("请检查短信验证码或旧密码是否正确");
    }

    /**
     * @param salt 用户手机号为盐值
     * @param inputPassword 用户输入的密码
     * @param password 数据库内密码
     * @return
     */
    private boolean verifyPassword(String salt,String inputPassword,String password){
        if(inputPassword == null || "".equals(inputPassword))
            return false;
        inputPassword = Md5Crypt.apr1Crypt(inputPassword, salt);
        return inputPassword.equals(password);
    }

    //验证短信
    private boolean verifySms(String phoneNumber,String code){
        boolean flag = false;
        //本地redis
        boolean existsPhone = JedisUtils.exists(phoneNumber);
        if(existsPhone) {
            String codeOfRedis = JedisUtils.get(phoneNumber);
            flag = codeOfRedis.equals(code);
        }
        return flag;
    }




}
