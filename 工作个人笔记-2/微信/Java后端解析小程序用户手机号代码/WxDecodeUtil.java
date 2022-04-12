package com.jeesite.modules.util;

import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.codec.EncodeUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.encoders.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.Security;

/**
 * 微信工具类
 */
public class WxDecodeUtil {



    public static JSONObject getPhoneNumber(String session_key, String encryptedData, String iv) {
        String session_key1=session_key.replace(" ","+");
        String encryptedData1=encryptedData.replace(" ","+");
        String iv1=iv.replace(" ","+");
        byte[] dataByte = EncodeUtils.decodeBase64(encryptedData1);   //注意这里解析Base64导入的包不要导错
        byte[] keyByte = EncodeUtils.decodeBase64(session_key1);
        byte[] ivByte = EncodeUtils.decodeBase64(iv1);
        try {
            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }
            // 初始化
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding","BC");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                String result = new String(resultByte, "UTF-8");
                return JSONObject.parseObject(result);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



}
