/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.user.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.user.entity.PayMess;

/**
 * 支付报文DAO接口
 * @author kevin
 * @version 2018-03-30
 */
@MyBatisDao
public interface PayMessDao extends CrudDao<PayMess> {
	
}