package com.ailk.api;

import com.ailk.api.impl.BaseRequest;
import com.ailk.api.impl.BaseResponse;

public interface CacheIfc {

	/**
	 * ���¼��ػ���
	 * 
	 * @param request
	 *            ����
	 * @return �����򷵻���ȷ������ʧ��
	 */
	public BaseResponse reload(BaseRequest request);

}
