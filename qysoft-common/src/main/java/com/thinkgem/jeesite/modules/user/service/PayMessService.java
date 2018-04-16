/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.user.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.user.entity.PayMess;
import com.thinkgem.jeesite.modules.user.dao.PayMessDao;

/**
 * 支付报文Service
 * @author kevin
 * @version 2018-03-30
 */
@Service
@Transactional(readOnly = false,propagation= Propagation.REQUIRED,rollbackForClassName={"RuntimeException","Exception","ValidationException"})
public class PayMessService extends CrudService<PayMessDao, PayMess> {

	public PayMess get(String id) {
		return super.get(id);
	}
	
	public List<PayMess> findList(PayMess payMess) {
		return super.findList(payMess);
	}
	
	public Page<PayMess> findPage(Page<PayMess> page, PayMess payMess) {
		return super.findPage(page, payMess);
	}
	

	public void save(PayMess payMess) {
		super.save(payMess);
	}
	

	public void delete(PayMess payMess) {
		super.delete(payMess);
	}
	
}