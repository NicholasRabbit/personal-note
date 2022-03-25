import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.alibaba.fastjson.JSON;
import java.util.*;
import com.aliyuncs.dysmsapi.model.v20170525.*;

public class DescribeImages {

    public static void main(String[] args) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAjVUwKznS*****", "BNPO1zoNSi484oizGM9fzzwJJ*****");
        IAcsClient client = new DefaultAcsClient(profile);

        // 创建API请求并设置参数
        AddShortUrlRequest request = new AddShortUrlRequest();
        
        request.setResourceOwnerAccount("your_value"); // 该参数值为假设值，请您根据实际情况进行填写
        
        request.setResourceOwnerId(1); // 该参数值为假设值，请您根据实际情况进行填写
        
        request.setSourceUrl("your_value"); // 该参数值为假设值，请您根据实际情况进行填写
        
        request.setShortUrlName("your_value"); // 该参数值为假设值，请您根据实际情况进行填写
        

        try {
            AddShortUrlResponse response = client.getAcsResponse(request);
            System.out.println(JSON.toJSONString(response));
            // 打印您需要的返回值，此处打印的是此次请求的 RequestId
            System.out.println(response.getRequestId());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            // 打印错误码
            System.out.println("ErrCode:" + e.getErrCode());
            System.out.println("ErrMsg:" + e.getErrMsg());
            System.out.println("RequestId:" + e.getRequestId());
        }

    }
}