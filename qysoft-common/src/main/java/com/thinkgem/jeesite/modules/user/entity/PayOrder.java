/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.user.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 支付订单Entity
 * @author kevin
 * @version 2018-03-30
 */
public class PayOrder extends DataEntity<PayOrder> {
	
	private static final long serialVersionUID = 1L;
	

	
	private String userName;		// 用户名
	private String orderNo;		// 订单号
	private String payType;		// 支付类型
	private String money;		// 支付金额
	private String defLag;		// def_lag
	
	public PayOrder() {
		super();
	}

	public PayOrder(String id){
		super(id);
	}

	@Length(min=1, max=255, message="用户名长度必须介于 1 和 255 之间")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	@Length(min=1, max=11, message="订单号长度必须介于 1 和 11 之间")
	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	@Length(min=1, max=255, message="支付类型长度必须介于 1 和 255 之间")
	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}
	
	@Length(min=1, max=255, message="支付金额长度必须介于 1 和 255 之间")
	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}
	
	@Length(min=0, max=1, message="def_lag长度必须介于 0 和 1 之间")
	public String getDefLag() {
		return defLag;
	}

	public void setDefLag(String defLag) {
		this.defLag = defLag;
	}
	
	

	
}