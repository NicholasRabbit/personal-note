package com.jeesite.modules.recycle.service;

import com.jeesite.modules.common.utill.UUIDUtil;
import com.jeesite.modules.recycle.dao.*;
import com.jeesite.modules.recycle.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

//开启事务手动提交，用于多个表的同时保存（个人范例）。
@Service
public class TransactionManualService {

    @Resource
    private DataSourceTransactionManager transactionManager;
    @Autowired
    TransactionDefinition transactionDefinition;
    @Autowired
    RecycleOrderDao recycleOrderDao;
    @Autowired
    RecycleUserOrderDao recycleUserOrderDao;
    @Autowired
    RecycleOrderCollectionDao recycleOrderCollectionDao;
    @Autowired
    RecycleAddressDao recycleAddressDao;
    @Autowired
    RecycleSellerDao recycleSellerDao;
    @Autowired
    RecycleOrderAddressDao  recycleOrderAddressDao;
    //用户保存订单
    public Long saveAll(RecycleOrder recycleOrder, RecycleUserOrder recycleUserOrder,
                           List<RecycleOrderCollection> rocList) {

        TransactionStatus status = null;
        Long count = 0L;
        try{
            //手动开启事务
            DefaultTransactionDefinition def = new DefaultTransactionDefinition();
            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
            status = transactionManager.getTransaction(def);

            //业务代码==============
			//....

            //批量添加订单类型
            if(rocList.size() > 0)
                count += recycleOrderCollectionDao.insertAllCollections(rocList);
           //业务代码==============

            transactionManager.commit(status);  //手动提交
        } catch(Exception e){
            transactionManager.rollback(status);  //强制回滚
            e.printStackTrace();
        }
        return count;
    }

   

}
