package com.ailk.api.impl;

import java.sql.Timestamp;

import org.apache.log4j.Logger;

import com.ailk.api.OpenIfc;
import com.ailk.jdbc.UserPushManager;
import com.ailk.jdbc.entity.UserPush;

/**
 * 推送管理
 * 
 * @author xugq
 * 
 */
public class OpenManager implements OpenIfc {

	private static final Logger logger = Logger.getLogger(OpenManager.class);

	@Override
	public BaseResponse bindBdPushUser(BindBdPushUserRequest request) {
		BaseResponse response = new BaseResponse();
		UserPushManager userPushManager = UserPushManager.getInstance();

		try {
			UserPush userPush = new UserPush();
			userPush.setDevId(request.getDevId());
			userPush.setBdUserId(request.getUserId());
			userPush.setLastUpdate(new Timestamp(System.currentTimeMillis()));
			userPushManager.saveOrUpdate(userPush);
		} catch (Exception e) {
			logger.error("系统异常，" + e);
			response.setErrorCode(BaseResponse.ESYSTEM);
		}

		return response;
	}
}
