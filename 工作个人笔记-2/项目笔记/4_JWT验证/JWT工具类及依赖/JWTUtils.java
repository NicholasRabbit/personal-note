


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jeesite.modules.flyl.entity.FlylUser;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class JWTUtils {

    public static final String signature = "29afdf609aec11ecac8700163e0dff42";

    /**
     * 生成token
     * */
    public static String getToken (Map<String,String> map) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR,12);  //设置token过期时间
        JWTCreator.Builder builder = JWT.create();
        Map<String,Object> jwtHeader = new HashMap<>();
        jwtHeader.put("alg","HMAC256");
        jwtHeader.put("type","JWT");
        builder.withHeader(jwtHeader);
        map.forEach( (k,v) -> {
            builder.withClaim(k,v);
        });
        String token = builder.withExpiresAt(cal.getTime()).sign(Algorithm.HMAC256(signature));
        return token;
    }

    /**
     * 验证token是否合法
     * */
    public static DecodedJWT verify(String token){
        return JWT.require(Algorithm.HMAC256(signature)).build().verify(token);
    }

    /**
     * 解析token
     * */
    public static DecodedJWT getTokenInfo(String token){
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(signature)).build().verify(token);
        return decodedJWT;
    }

    //解析token
    public static Map<String,Object> tokenInfo(String token){
        FlylUser flylUser = new FlylUser();
        Map<String,Object>  map = new HashMap<>();
        try{
            DecodedJWT verify = JWTUtils.verify(token);
            String id = verify.getClaim("id").asString();
            String userName = verify.getClaim("userName").asString();
            String openid = verify.getClaim("openid").asString();
            String parentId = verify.getClaim("parentId").asString();
            if(id == null) throw new Exception();
            flylUser.setId(id);
            flylUser.setUserName(userName);
            flylUser.setWxOpenid(openid);
            flylUser.setParentId(parentId);
        } catch (SignatureVerificationException e){
            e.printStackTrace();
            map.put("success",false);
            map.put("message","invalid signature");
        } catch (TokenExpiredException e) {
            e.printStackTrace();
            map.put("success",false);
            map.put("message","token is out of date");
        } catch (AlgorithmMismatchException e){
            e.printStackTrace();
            map.put("message","Algorithm mismatch");
        } catch (Exception e) {
            e.printStackTrace();
            e.printStackTrace();
            map.put("success",false);
            map.put("message","invalid token");
        }
        map.put("success",true);
        map.put("message","token verify passed");
        map.put("flylUser",flylUser);
        return  map;
    }

}
