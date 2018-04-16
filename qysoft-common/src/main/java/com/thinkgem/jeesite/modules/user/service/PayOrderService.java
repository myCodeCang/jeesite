/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.user.service;

import java.util.List;

import com.thinkgem.jeesite.modules.user.entity.UserUserinfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.user.entity.PayOrder;
import com.thinkgem.jeesite.modules.user.dao.PayOrderDao;

/**
 * 支付订单Service
 * @author kevin
 * @version 2018-03-30
 */
@Service
@Transactional(readOnly = false,propagation= Propagation.REQUIRED,rollbackForClassName={"RuntimeException","Exception","ValidationException"})
public class PayOrderService extends CrudService<PayOrderDao, PayOrder> {

	public PayOrder get(String id) {
		return super.get(id);
	}

	public PayOrder getByOrderNo(String billNo) {
		PayOrder payOrder = dao.getByOrderNo(billNo);
		return payOrder;
	}
	
	public List<PayOrder> findList(PayOrder payOrder) {
		return super.findList(payOrder);
	}
	
	public Page<PayOrder> findPage(Page<PayOrder> page, PayOrder payOrder) {
		return super.findPage(page, payOrder);
	}
	

	public void save(PayOrder payOrder) {
		super.save(payOrder);
	}
	

	public void delete(PayOrder payOrder) {
		super.delete(payOrder);
	}
	
}