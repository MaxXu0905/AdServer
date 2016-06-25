package com.ailk.api;

import com.ailk.api.impl.BaseRequest;
import com.ailk.api.impl.BaseResponse;

public interface CacheIfc {

	/**
	 * 重新加载缓存
	 * 
	 * @param request
	 *            请求
	 * @return 存在则返回正确，否则失败
	 */
	public BaseResponse reload(BaseRequest request);

}
