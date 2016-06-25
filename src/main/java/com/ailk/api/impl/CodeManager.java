package com.ailk.api.impl;

import java.util.List;

import org.apache.log4j.Logger;

import com.ailk.api.CodeIfc;
import com.ailk.jdbc.GlobalManager;
import com.ailk.jdbc.InfoCityManager;
import com.ailk.jdbc.InfoCodeManager;
import com.ailk.jdbc.InfoPeriodManager;
import com.ailk.jdbc.cache.InfoCityValue;
import com.ailk.jdbc.cache.InfoCodeValue;
import com.ailk.jdbc.cache.InfoGroupValue;
import com.ailk.jdbc.cache.InfoPeriodValue;

public class CodeManager implements CodeIfc {

	private static final Logger logger = Logger.getLogger(CodeManager.class);

	public CodeManager() {
	}

	@Override
	public GroupResponse getGroup(String groupName) {
		GroupResponse response = new GroupResponse();
		GlobalManager globalManager = GlobalManager.getInstance();
		InfoCodeManager infoCodeManager = InfoCodeManager.getInstance(globalManager);

		List<InfoGroupValue> value = infoCodeManager.get(groupName);
		if (value == null) {
			response.setErrorCode(BaseResponse.ENOENT);
			logger.error("找不到代码信息，" + groupName);
		} else {
			response.setItems(value);
		}

		return response;
	}

	@Override
	public BaseResponse setGroup(GroupRequest request) {
		GroupResponse response = new GroupResponse();
		GlobalManager globalManager = GlobalManager.getInstance();
		InfoCodeManager infoCodeManager = InfoCodeManager.getInstance(globalManager);

		try {
			infoCodeManager.setGroup(request.getGroupName(), request.getItems());
		} catch (Exception e) {
			logger.error("系统异常，" + e);
			response.setErrorCode(BaseResponse.ESYSTEM);
		}

		return response;
	}

	@Override
	public CodeResponse getCode(CodeRequest request) {
		CodeResponse response = new CodeResponse();
		GlobalManager globalManager = GlobalManager.getInstance();
		InfoCodeManager infoCodeManager = InfoCodeManager.getInstance(globalManager);

		InfoCodeValue value = infoCodeManager.get(request.getGroupName(), request.getCodeName());
		if (value == null) {
			logger.error("找不到代码，groupName = " + request.getGroupName() + ", codeName = " + request.getCodeName());
			response.setErrorCode(BaseResponse.ENOENT);
		} else {
			response.setDesc(value.getDesc());
			response.setItems(value.getItems());
		}

		return response;
	}

	@Override
	public CitiesResponse getCities() {
		CitiesResponse response = new CitiesResponse();
		GlobalManager globalManager = GlobalManager.getInstance();
		InfoCityManager infoCityManager = InfoCityManager.getInstance(globalManager);

		List<InfoCityValue> items = infoCityManager.get();
		if (items == null) {
			logger.error("找不到城市代码信息");
			response.setErrorCode(BaseResponse.ENOENT);
		} else {
			response.setItems(items);
		}

		return response;
	}

	@Override
	public PeriodsResponse getPeriods() {
		PeriodsResponse response = new PeriodsResponse();
		GlobalManager globalManager = GlobalManager.getInstance();
		InfoPeriodManager infoPeriodManager = InfoPeriodManager.getInstance(globalManager);

		List<InfoPeriodValue> items = infoPeriodManager.get();
		if (items == null) {
			logger.error("找不到时段信息");
			response.setErrorCode(BaseResponse.ENOENT);
		} else {
			response.setItems(items);
		}

		return response;
	}

}
