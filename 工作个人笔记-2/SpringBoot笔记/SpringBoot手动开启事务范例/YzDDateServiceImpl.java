/**
 *
 */
package com.jeesite.modules.yzdoctordate.service;

import com.jeesite.common.enums.YzDoctorDateTypeEnum;
import com.jeesite.common.response.MobileBaseRequest;
import com.jeesite.common.response.MobileBaseResponse;
import com.jeesite.modules.inquiry.service.InquiryService;
import com.jeesite.modules.sys.dao.CompanyDao;
import com.jeesite.modules.sys.entity.Company;
import com.jeesite.modules.sys.entity.MemberSession;
import com.jeesite.modules.sys.service.CompanyService;
import com.jeesite.modules.yzdhrelations.dao.YzDoctorHospitalRelationshipDao;
import com.jeesite.modules.yzdhrelations.entity.YzDoctorHospitalRelationship;
import com.jeesite.modules.yzdhrelations.service.YzDoctorHospitalRelationshipService;
import com.jeesite.modules.yzdoctordate.dao.YzDoctorDateDao;
import com.jeesite.modules.yzdoctordate.entity.YzDDateRequest;
import com.jeesite.modules.yzdoctordate.entity.YzDoctorDate;
import com.jeesite.modules.yzmobilelogin.service.YzMobileLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Service
public class YzDDateServiceImpl implements YzDDateService {

    @Autowired
    YzMobileLoginService yzMobileLoginService;
    @Autowired
    CompanyService companyService;
    @Autowired
    YzDoctorHospitalRelationshipService yzDoctorHospitalRelationshipService;
    @Autowired
    YzDoctorHospitalRelationshipDao yzDoctorHospitalRelationshipDao;
    @Autowired
    YzDoctorDateService yzDoctorDateService;
    @Autowired
    YzDoctorDateDao yzDoctorDateDao;
    @Resource
    private DataSourceTransactionManager transactionManager;
    @Autowired
    InquiryService inquiryService;
    /**
     * 修改设置
     * @param mobileRequest
     * @return
     */
    @Override
    public MobileBaseResponse settings(MobileBaseRequest<YzDDateRequest> mobileRequest) {
        MobileBaseResponse mobileBaseResponse = new MobileBaseResponse();
        if (null == mobileRequest.getData()) {
            return new MobileBaseResponse(MobileBaseResponse.Status.ERROR, mobileBaseResponse, "访问接口失败，未设置参数");
        }
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);
        try{
            YzDDateRequest data = mobileRequest.getData();
            Company company = companyService.get(data.getCompanyCode());
            if (company == null){
                mobileBaseResponse.setStatus(MobileBaseResponse.Status.ERROR);
                mobileBaseResponse.setMessage("该医馆不存在，请检查");
                mobileBaseResponse.setShow(true);
                transactionManager.rollback(status);
                return mobileBaseResponse;
            }
            MemberSession memberSession = yzMobileLoginService.getMemberSession(mobileRequest.getToken());
            YzDoctorHospitalRelationship yzDoctorHospitalRelationship = new YzDoctorHospitalRelationship();
            yzDoctorHospitalRelationship.setCompany(company);
            yzDoctorHospitalRelationship.setMemberId(memberSession.getId());
            YzDoctorHospitalRelationship yzDoctorHospitalRelationship1 = yzDoctorHospitalRelationshipDao.getByEntity(yzDoctorHospitalRelationship);
            if (yzDoctorHospitalRelationship1 == null){
                mobileBaseResponse.setMessage("暂无绑定关系，请先绑定医馆");
                mobileBaseResponse.setStatus(MobileBaseResponse.Status.ERROR);
                transactionManager.rollback(status);
                return mobileBaseResponse;
            }

            //直接设置日期类型
            if (data.getType() == YzDoctorDateTypeEnum.Day.getFlag()) {
                Date now=new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String monthStart = dateFormat.format(now);

                Calendar c = Calendar.getInstance();
                c.add(Calendar.DATE, 14);//计算14天后的时间
                String monthEnd =dateFormat.format(c.getTime());

                if (data.getDayList().size() <= 0) {
                    yzDoctorDateDao.deleteMonthDate(memberSession.getId(), company.getCompanyCode(), monthStart, monthEnd);
                    mobileBaseResponse.setStatus(MobileBaseResponse.Status.OK);
                    transactionManager.commit(status);
                    return mobileBaseResponse;
                }
                List<YzDoctorDate> yzDoctorDates = yzDoctorDateDao.checkDay(memberSession.getId(),company.getCompanyCode(), data.getDayList());
                if (!yzDoctorDates.isEmpty()) {
                    mobileBaseResponse.setMessage("错误，当前时间段已设置了其他医馆，请检查");
                    mobileBaseResponse.setShow(true);
                    transactionManager.rollback(status);
                    mobileBaseResponse.setStatus(MobileBaseResponse.Status.ERROR);
                    return mobileBaseResponse;
                }


                yzDoctorDateDao.deleteMonthDate(memberSession.getId(), company.getCompanyCode(), monthStart, monthEnd);
                List<Date> dayList = data.getDayList();
                for (Date date : dayList) {
                    YzDoctorDate yzDoctorDate = new YzDoctorDate();
                    yzDoctorDate.setMemberId(memberSession.getId());
                    yzDoctorDate.setCompany(company);
                    yzDoctorDate.setType(data.getType());
                    yzDoctorDate.setDay(date);
                    yzDoctorDateService.insert(yzDoctorDate);
                }
                transactionManager.commit(status);
                mobileBaseResponse.setStatus(MobileBaseResponse.Status.OK);
                return mobileBaseResponse;
            }
            transactionManager.rollback(status);
            mobileBaseResponse.setMessage("请联系系统管理员");
            mobileBaseResponse.setStatus(MobileBaseResponse.Status.ERROR);
            return mobileBaseResponse;
            /*YzDoctorDate yzDoctorDate1 = yzDoctorDateDao.getByEntity(yzDoctorDate);
            yzDoctorDate.setType(data.getType());
            if (data.getType() == YzDoctorDateTypeEnum.Week.getFlag())
                yzDoctorDate.setWeek(data.getWeek());
            if (data.getType() == YzDoctorDateTypeEnum.Date.getFlag())
                yzDoctorDate.setDate(data.getDate());

            if (yzDoctorDate1 == null){
                yzDoctorDateService.insert(yzDoctorDate);
                mobileBaseResponse.setMessage("已设置");
                mobileBaseResponse.setStatus(MobileBaseResponse.Status.OK);
                return mobileBaseResponse;
            }*/
            /*if (data.getType() == YzDoctorDateTypeEnum.Week.getFlag())
                yzDoctorDate1.setWeek(data.getWeek());
            if (data.getType() == YzDoctorDateTypeEnum.Date.getFlag())
                yzDoctorDate1.setDate(data.getDate());
            yzDoctorDateService.update(yzDoctorDate1);
            mobileBaseResponse.setStatus(MobileBaseResponse.Status.OK);
            mobileBaseResponse.setMessage("已设置");
            return mobileBaseResponse;*/
        } catch (Exception e) {
            e.printStackTrace();
            transactionManager.rollback(status);
            mobileBaseResponse.setStatus(MobileBaseResponse.Status.ERROR);
            mobileBaseResponse.setMessage("接口异常！请联系系统管理员");
            return mobileBaseResponse;
        }
    }

    /**
     * 修改设置类型
     * @param mobileRequest
     * @return
     */
    @Override
    public MobileBaseResponse settingsType(MobileBaseRequest<YzDDateRequest> mobileRequest) {
        MobileBaseResponse mobileBaseResponse = new MobileBaseResponse();
        if (null == mobileRequest.getData()) {
            return new MobileBaseResponse(MobileBaseResponse.Status.ERROR, mobileBaseResponse, "访问接口失败，未设置参数");
        }
        try{
            YzDDateRequest data = mobileRequest.getData();
            MemberSession memberSession = yzMobileLoginService.getMemberSession(mobileRequest.getToken());
            if (data.getType() == YzDoctorDateTypeEnum.Date.getFlag() || data.getType() == YzDoctorDateTypeEnum.Week.getFlag()
                    || data.getType() == YzDoctorDateTypeEnum.Day.getFlag()){
                /*yzDoctorDateDao.updateType(data.getType(),memberSession.getId());*/
                mobileBaseResponse.setStatus(MobileBaseResponse.Status.OK);
                mobileBaseResponse.setMessage("已设置");
                return mobileBaseResponse;
            }
            mobileBaseResponse.setStatus(MobileBaseResponse.Status.ERROR);
            mobileBaseResponse.setMessage("类型设置出错");
            return mobileBaseResponse;
        } catch (Exception e) {
            e.printStackTrace();
            mobileBaseResponse.setStatus(MobileBaseResponse.Status.ERROR);
            mobileBaseResponse.setMessage("接口异常！请联系系统管理员");
            return mobileBaseResponse;
        }
    }

    /**
     * 获取当前坐馆医馆
     * @param mobileRequest
     * @return
     */
    @Override
    public MobileBaseResponse getCurrentCompany(MobileBaseRequest<YzDDateRequest> mobileRequest) {
        MobileBaseResponse mobileBaseResponse = new MobileBaseResponse();
        try {
            MemberSession memberSession = yzMobileLoginService.getMemberSession(mobileRequest.getToken());

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String day = dateFormat.format(new Date());
            YzDoctorDate yzDoctorDate = yzDoctorDateDao.getCurrentCompanyCode(memberSession.getId(), day);
            if(yzDoctorDate==null){
                YzDoctorDate yzDate=new YzDoctorDate();
                Company doctorCompany = inquiryService.getDoctorCompany(memberSession.getId());
                if (doctorCompany == null){
                    mobileBaseResponse.setData(null);
                    mobileBaseResponse.setStatus(MobileBaseResponse.Status.OK);
                }
                yzDate.setCompany(doctorCompany);
                yzDate.setDay(new Date());
                mobileBaseResponse.setData(yzDate);
                mobileBaseResponse.setStatus(MobileBaseResponse.Status.OK);
            }else{
                Company company=new Company();
                company.setCompanyCode(yzDoctorDate.getCompany().getCompanyCode());
                List<Company> list = companyService.findList(company);
                if(list!=null&&list.size()>0){
                    yzDoctorDate.getCompany().setArea(list.get(0).getArea());
                }
                mobileBaseResponse.setData(yzDoctorDate);
                mobileBaseResponse.setStatus(MobileBaseResponse.Status.OK);
            }
            return mobileBaseResponse;
        } catch (Exception e) {
            e.printStackTrace();
            mobileBaseResponse.setStatus(MobileBaseResponse.Status.ERROR);
            mobileBaseResponse.setMessage("接口异常！请联系系统管理员");
            return mobileBaseResponse;
        }
    }

}