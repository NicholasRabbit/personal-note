package com.jeesite.modules.weixin.service;

import com.jeesite.common.constant.Constants;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.modules.common.util.JWTUtils;
import com.jeesite.modules.common.util.JedisUtils;
import com.jeesite.modules.common.util.UUIDUtil;
import com.jeesite.modules.flyl.entity.FlylUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenService {

    // 令牌自定义标识
    @Value("${token.header}")
    private String header;

    // 令牌秘钥
    @Value("${token.secret}")
    private String secret;

    // 令牌有效期（默认30分钟）
    @Value("${token.expireTime}")
    private int expireTime;

    @Autowired
    public RedisTemplate redisTemplate;

    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private static final Long MILLIS_MINUTE_TEN = 20 * 60 * 1000L;

    //检查token有效期，距到期20分钟内刷新redis
    public void verifyToken(FlylUser flylUser){
        long expireTime = flylUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if(currentTime - expireTime <= MILLIS_MINUTE_TEN){
            refreshToken(flylUser);
        }
    }

    public void refreshToken(FlylUser flylUser){
        flylUser.setLoginTime(System.currentTimeMillis());
        flylUser.setExpireTime(flylUser.getLoginTime() + 30 * 60 * 1000L);
        String tokenKey = getTokenKey(flylUser.getTokenKey());
        JedisUtils.setObject(tokenKey,flylUser,30 * 60);  //失效时间30分

    }


    public String createToken(String uuid){
        //生成token并返回
        Map<String,String> payload = new HashMap<>();
        payload.put(Constants.LOGIN_TOKEN_KEY,uuid);
        String token = JWTUtils.getToken(payload);
        return Constants.TOKEN_PREFIX + token;
    }


    //获取登录用户信息
    public FlylUser getLoginUser(HttpServletRequest request){
        String token = getToken(request);
        if(StringUtils.isNotEmpty(token)){
            try{
                JWTUtils.verify(token);
                Map<String, Object> tokenMap = JWTUtils.tokenInfo(token);
                String uuid = (String)tokenMap.get(Constants.LOGIN_TOKEN_KEY);
                String userKey = getTokenKey(uuid);
                Object obj = JedisUtils.getObject(userKey);
                if(obj instanceof FlylUser){
                    FlylUser loginUser = (FlylUser)obj;
                    return loginUser;
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return null;

    }

    //解析token
    private String getToken(HttpServletRequest request){
        String token = request.getHeader(this.header);
        if(StringUtils.isNotEmpty(token) && token.startsWith(Constants.TOKEN_PREFIX)){
            token = token.replace(Constants.TOKEN_PREFIX, "");
        }
        return  token;
    }

    private String getTokenKey(String uuid)
    {
        return Constants.LOGIN_TOKEN_KEY + uuid;
    }

    public Map<String,Object> tokenResult(FlylUser flylUser,String uuid){
        Map<String,Object> tokenMap = new HashMap<>();
        //设置失效时间
        flylUser.setLoginTime(System.currentTimeMillis());
        flylUser.setExpireTime(flylUser.getLoginTime() + 30 * 60 * 1000);
        flylUser.setTokenKey(uuid);
        JedisUtils.setObject(Constants.LOGIN_TOKEN_KEY + uuid,flylUser,30 * 60);
        String token = createToken(uuid);
        tokenMap.put("Authorization",token);
        return tokenMap;
    }


}
