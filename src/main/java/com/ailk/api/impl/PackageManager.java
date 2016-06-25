package com.ailk.api.impl;

import com.ailk.api.PackageIfc;
import com.ailk.common.ConstVariables;
import com.ailk.jdbc.GlobalManager;
import com.ailk.jdbc.SystemPropertiesManager;

/**
 * 安装包管理
 * 
 * @author xugq
 * 
 */
public class PackageManager implements PackageIfc {

	@Override
	public GetVersionResponse getVersion(GetVersionRequest request) {
		GlobalManager globalManager = GlobalManager.getInstance();
		SystemPropertiesManager systemPropertiesManager = SystemPropertiesManager.getInstance(globalManager);
		GetVersionResponse response = new GetVersionResponse();

		switch (request.getOsType()) {
		case ConstVariables.OS_TYPE_ANDROID:
			if (request.getVersion() < systemPropertiesManager.getAndroidVersion()) {
				response.setVersionName(systemPropertiesManager.getAndroidVersionName());
				response.setInfo(systemPropertiesManager.getAndroidInfo());
			}
			break;
		case ConstVariables.OS_TYPE_IOS:
		default:
			break;
		}

		return response;
	}
}
