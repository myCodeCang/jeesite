/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.user.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 支付报文Entity
 * @author kevin
 * @version 2018-03-30
 */
public class PayMess extends DataEntity<PayMess> {
	
	private static final long serialVersionUID = 1L;
	

	
	private String orderNo;		// 订单号
	private String billNo;		// 流水号
	private String payType;		// 支付类型
	private String message;		// 报文
	private String defLag;		// def_lag
	
	public PayMess() {
		super();
	}

	public PayMess(String id){
		super(id);
	}

	@Length(min=1, max=255, message="订单号长度必须介于 1 和 255 之间")
	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	@Length(min=1, max=255, message="流水号长度必须介于 1 和 255 之间")
	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	
	@Length(min=1, max=11, message="支付类型长度必须介于 1 和 11 之间")
	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}
	
	@Length(min=1, max=255, message="报文长度必须介于 1 和 255 之间")
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	@Length(min=0, max=1, message="def_lag长度必须介于 0 和 1 之间")
	public String getDefLag() {
		return defLag;
	}

	public void setDefLag(String defLag) {
		this.defLag = defLag;
	}
	
	

	
}