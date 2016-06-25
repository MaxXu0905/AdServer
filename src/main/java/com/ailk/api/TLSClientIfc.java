package com.ailk.api;

public interface TLSClientIfc {

	/**
	 * ���ɶԳ���Կ
	 * 
	 * @return �ù�Կ���ܵĶԳ���Կ
	 */
	public byte[] generateKey();

	/**
	 * ��ȡǩ��
	 * 
	 * @return ǩ��
	 */
	public String getSignature();

	/**
	 * �öԳ���Կ����
	 * 
	 * @param data
	 *            ����ǰ������
	 * @return ���ܺ�����ݣ�����ʧ���򷵻�null
	 */
	public byte[] encrypt(byte[] data);

	/**
	 * �öԳ���Կ����
	 * 
	 * @param data
	 *            ����ǰ������
	 * @return ���ܺ�����ݣ�����ʧ���򷵻�null
	 */
	public byte[] decrypt(byte[] data);

}
