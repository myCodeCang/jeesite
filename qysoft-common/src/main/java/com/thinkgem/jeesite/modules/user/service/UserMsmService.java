/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.user.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.exception.ValidationException;
import com.thinkgem.jeesite.common.utils.StringUtils2;
import com.thinkgem.jeesite.modules.sys.utils.MsmUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.user.entity.UserMsm;
import com.thinkgem.jeesite.modules.user.dao.UserMsmDao;

/**
 * 保存用户短信Service
 * @author yankai
 * @version 2017-05-28
 */
@Service
@Transactional(readOnly = false,propagation= Propagation.REQUIRED,rollbackForClassName={"RuntimeException","Exception","ValidationException"})
public class UserMsmService extends CrudService<UserMsmDao, UserMsm> {

	public UserMsm get(String id) {
		return super.get(id);
	}
	
	public List<UserMsm> findList(UserMsm userMsm) {
		return super.findList(userMsm);
	}
	
	public Page<UserMsm> findPage(Page<UserMsm> page, UserMsm userMsm) {
		return super.findPage(page, userMsm);
	}
	

	public void save(UserMsm userMsm) throws ValidationException {
		super.save(userMsm);
	}

	public void delete(UserMsm userMsm) throws ValidationException {
		super.delete(userMsm);
	}


	public Optional<UserMsm> addOrUpdateMsm(String mobile) throws ValidationException {
		if (StringUtils2.isEmpty(mobile)) {
			return Optional.empty();
		}

		String verifyCode = UserMsm.generateVerifyCode(6);
		UserMsm userMsm = dao.getByMobile(mobile);
		if (userMsm == null) {
			userMsm = new UserMsm();
			userMsm.setMobile(mobile);
		}
		userMsm.setMsg(verifyCode);
		userMsm.setCreateTime(new Date());
		if (StringUtils2.isEmpty(userMsm.getId()) || "0".equals(userMsm.getId())) {
			save(userMsm);
			userMsm = dao.getByMobile(mobile);
		} else {
			dao.updateMsg(verifyCode, new Date(), mobile);
		}

		return Optional.of(userMsm);
	}


	public Optional<UserMsm> getByUserName(String userName) {
		if (StringUtils2.isEmpty(userName)) {
			return Optional.empty();
		}

		UserMsm byUserName = dao.getByMobile(userName);
		if (null == byUserName) {
			return Optional.empty();
		}

		return Optional.of(byUserName);
	}

	/**
	 * 发送短信
	 * @param mobile
	 * @return
	 */

	public String sendVerifyCode(String mobile) throws ValidationException {
		Optional<UserMsm> userMsmOptional = addOrUpdateMsm(mobile);
		if (!userMsmOptional.isPresent()) {
			return StringUtils2.EMPTY;
		}

		UserMsm userMsm = userMsmOptional.get();
		String msg = userMsm.getMsg();
		String msgTemplate = Global.getOption("system_sms", "lk_msg_template");
		if (!StringUtils2.isEmpty(msgTemplate)) {
			msg = msgTemplate.replace("{{phoneNum}}", msg);
		}

		boolean sendSuccess = MsmUtils.getInstance().lingkaiSendMsg(mobile, msg);
		if (sendSuccess) {
			return userMsm.getMsg();
		}

		return StringUtils2.EMPTY;
	}

	/**
	 * 发送短信
	 * @param mobile
	 * @return
	 */

	public String pushMessage(String mobile,String userName,String userNiceName,String mobileMode) throws ValidationException {
		Optional<UserMsm> userMsmOptional = addOrUpdateMsm(mobile);
		if (!userMsmOptional.isPresent()) {
			return StringUtils2.EMPTY;
		}
		UserMsm userMsm = userMsmOptional.get();
		//String msg = userMsm.getMsg();
		String msg = mobileMode;
		if (!StringUtils2.isEmpty(msg)) {
			msg = msg.replace("{{userName}}", userName);
			msg = msg.replace("{{userNiceName}}", userNiceName);
		}
		boolean sendSuccess = MsmUtils.getInstance().lingkaiSendMsg(mobile, msg);
		if (sendSuccess) {
			return userMsm.getMsg();
		}
		return StringUtils2.EMPTY;
	}

	/**
	 * 发送短信
	 * @param mobile
	 * @return
	 */

	public String pushMessage(String mobile,String userName,String mobileMode) throws ValidationException {
		Optional<UserMsm> userMsmOptional = addOrUpdateMsm(mobile);
		if (!userMsmOptional.isPresent()) {
			return StringUtils2.EMPTY;
		}
		UserMsm userMsm = userMsmOptional.get();
		//String msg = userMsm.getMsg();
		String msg = mobileMode;
		boolean sendSuccess = MsmUtils.getInstance().lingkaiSendMsg(mobile, msg);
		if (sendSuccess) {
			return userMsm.getMsg();
		}
		return StringUtils2.EMPTY;
	}
	/**
	 * 用户注册发送短信
	 * @param mobile
	 * @return
	 */

	public String smsToUser(String mobile,String userName, String userPass) throws ValidationException {
		Optional<UserMsm> userMsmOptional = addOrUpdateMsm(mobile);
		if (!userMsmOptional.isPresent()) {
			return StringUtils2.EMPTY;
		}

		UserMsm userMsm = userMsmOptional.get();
		String msg = userMsm.getMsg();
		String msgTemplate = Global.getOption("system_sms", "lk_reg_template");
		if (!StringUtils2.isEmpty(msgTemplate)) {
			msgTemplate = msgTemplate.replace("{{userName}}", userName);
			msg = msgTemplate.replace("{{password}}", userPass);
		}

		boolean sendSuccess = MsmUtils.getInstance().lingkaiSendMsg(mobile, msg);
		if (sendSuccess) {
			return userMsm.getMsg();
		}

		return StringUtils2.EMPTY;
	}

	/**
	 * 校验验证码是否有效
	 * @param mobile
	 * @param verifyCode
	 * @return
	 */

	public boolean checkVerifyCode(String mobile, String verifyCode) {
		if (StringUtils2.isEmpty(mobile, verifyCode)) {
			return false;
		}

		Optional<UserMsm> userMsmOptional = getByUserName(mobile);
		if (!userMsmOptional.isPresent()) {
			return false;
		}

		UserMsm userMsm = userMsmOptional.get();
		if (!verifyCode.equals(userMsm.getMsg())) {
			return false;
		}

		LocalDateTime now = LocalDateTime.now();
		LocalDateTime createTime = LocalDateTime.ofInstant(userMsm.getCreateTime().toInstant(), ZoneId.systemDefault());
		long minutes = Duration.between(createTime, now).toMinutes();
		if (minutes > 10) {
			return false;
		}
		return true;
	}
}