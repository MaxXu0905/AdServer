package com.ailk.api;

public interface TLSClientIfc {

	/**
	 * 生成对称密钥
	 * 
	 * @return 用公钥加密的对称密钥
	 */
	public byte[] generateKey();

	/**
	 * 获取签名
	 * 
	 * @return 签名
	 */
	public String getSignature();

	/**
	 * 用对称密钥加密
	 * 
	 * @param data
	 *            加密前的数据
	 * @return 加密后的数据，加密失败则返回null
	 */
	public byte[] encrypt(byte[] data);

	/**
	 * 用对称密钥解密
	 * 
	 * @param data
	 *            解密前的数据
	 * @return 解密后的数据，解密失败则返回null
	 */
	public byte[] decrypt(byte[] data);

}
