package com.jeesite.modules.service;

import com.alibaba.fastjson.JSON;
import com.jeesite.modules.recycle.dao.RecycleBuyerDao;
import com.jeesite.modules.recycle.dao.RecycleOrderDao;
import com.jeesite.modules.recycle.dao.RecycleUserOrderDao;
import com.jeesite.modules.recycle.entity.*;
import com.jeesite.modules.util.HttpPostUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Service
public class WmsAsyncService {

    @Autowired
    RecycleOrderDao recycleOrderDao;
    @Autowired
    RecycleUserOrderDao recycleUserOrderDao;
    @Autowired
    RecycleBuyerDao recycleBuyerDao;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Async
    public String transferOrderToWms(RecycleOrder recycleOrder){
        RecycleUserOrder recycleUserOrder = recycleUserOrderDao.getByOrderId(recycleOrder.getId());
        //查询回收员信息
        RecycleBuyer recycleBuyer = new RecycleBuyer();
        recycleBuyer.setOrderId(recycleOrder.getId());
        recycleBuyer = recycleBuyerDao.getBuyerByOrder(recycleBuyer);
        recycleOrder = recycleOrderDao.getOrderDetails(recycleOrder);

        recycleOrder.setBuyerId(recycleUserOrder.getBuyerId());
        recycleOrder.setBuyerName(recycleBuyer.getBuyerName());

        List<RecycleOrderCollection> types = recycleOrder.getTypes();
        List<RecycleOrderCollectionTo> newList=new ArrayList<>();
        Iterator<RecycleOrderCollection> it = types.iterator();
        while(it.hasNext()){
            RecycleOrderCollectionTo roct = new RecycleOrderCollectionTo();
            try {
                BeanUtils.copyProperties(roct,it.next());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            roct.setWeight(roct.getWeight()*1000);
            newList.add(roct);
        }

        String url = "http://localhost:8083/wh/warehousingOrder/saveOrderToWms";
        Map<String,Object> map = new HashMap<>();
        map.put("id",recycleOrder.getId());
        map.put("buyerId",recycleOrder.getBuyerId());
        map.put("buyerName",recycleOrder.getBuyerName());
        map.put("price",recycleOrder.getPrice());
        map.put("weighSum",recycleOrder.getWeightSum());
        map.put("types", newList);

       String message =  HttpPostUtil.doPost(url,map,"UTF-8");
       logger.info("wms return message ==>" + message);
       return  message;

    }
}
