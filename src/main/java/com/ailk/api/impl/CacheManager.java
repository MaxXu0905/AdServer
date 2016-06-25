package com.ailk.api.impl;

import com.ailk.api.CacheIfc;
import com.ailk.jdbc.CacheLoader;
import com.ailk.jdbc.GlobalManager;

public class CacheManager implements CacheIfc {

	public CacheManager() {
	}

	@Override
	public BaseResponse reload(BaseRequest request) {
		BaseResponse response = new BaseResponse();

		GlobalManager globalManager = new GlobalManager();
		CacheLoader.inMemLoad(globalManager);
		GlobalManager.setInstance(globalManager);

		return response;
	}

}
