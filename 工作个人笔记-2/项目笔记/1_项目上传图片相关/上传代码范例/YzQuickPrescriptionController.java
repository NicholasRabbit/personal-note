package com.jeesite.modules.controller;

import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.response.MobileBaseResponse;
import com.jeesite.modules.common.utill.UploadUtil;
import com.jeesite.modules.prescription.entity.YzQuickPrescription;
import com.jeesite.modules.prescription.service.YzQuickPrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Controller
@RequestMapping("/quick")
public class YzQuickPrescriptionController {

    @Autowired
    private YzQuickPrescriptionService yzQuickPrescriptionService;

	//上传图片接口
    @ResponseBody
    @RequestMapping("/save")
    public MobileBaseResponse<YzQuickPrescription> image(HttpServletRequest request) {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());
        String askContent = null;
        String memberId = request.getSession().getAttribute("memberId").toString();
        String phone = request.getSession().getAttribute("phone").toString();
        if (StringUtils.isNotEmpty(request.getParameter("askContent"))) {
            askContent = request.getParameter("askContent").toString();
        }
        String imgPath = "";
        if (multipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            Iterator<String> iter = multiRequest.getFileNames();
            while (iter.hasNext()) {
                MultipartFile multipartFile = multiRequest.getFile(iter.next());
                String fileName = multipartFile.getOriginalFilename();
                try {
                    InputStream inputStream = multipartFile.getInputStream();
                    Map<String, String> map = new HashMap<>();
                    map.put("img", "img");
                    String image = UploadUtil.getInstance().uploadFileReturn(inputStream, fileName, "pic", UploadUtil.COMMON_IMAGE_UPLOAD_PATH, map);
                    imgPath += UploadUtil.COMMON_IMAGE_DETAIL_PATH + image + ";";
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        imgPath = imgPath.substring(0, imgPath.length() - 1);
        YzQuickPrescription yzQuickPrescription = new YzQuickPrescription();
        yzQuickPrescription.setYzQuickPrescriptionRemark(askContent);
        yzQuickPrescription.setYzQuickPrescriptionPic(imgPath);
        MobileBaseResponse<YzQuickPrescription> add = yzQuickPrescriptionService.add(yzQuickPrescription, memberId, phone);
        return add;
    }
}
