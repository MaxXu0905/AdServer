package com.ailk.api;

public interface AuditIfc {

	/**
	 * ������ֻ��ͻ��˷��͹�������־��Ϣ
	 * 
	 * @param userId
	 *            �û�ID
	 * @param data
	 *            ��־��Ϣ
	 * @return �Ƿ�ɹ�
	 */
	public void syslog(long userId, String data);

	/**
	 * ��¼��Ҫ����Ϣ���������ݻ���
	 * 
	 * @param userId
	 *            �û�ID
	 * @param data
	 *            ����
	 */
	public void audit(long userId, String data);

}
