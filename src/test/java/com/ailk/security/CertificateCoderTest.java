package com.ailk.security;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

import org.junit.Test;

import com.ailk.security.CertificateCoder;

/**
 * 
 * @author
 * @version 1.0
 * @since 1.0
 */
public class CertificateCoderTest {
	private String password = "123456";
	private String alias = "www.zlex.org";
	private String certificatePath = "d:/zlex.cer";
	private String keyStorePath = "d:/zlex.keystore";

	@Test
	public void test() throws Exception {
		System.err.println("公钥加密——私钥解密");
		String inputStr = "Ceritifcate";
		byte[] data = inputStr.getBytes();

		PublicKey publicKey = CertificateCoder.getPublicKey(certificatePath);
		byte[] encrypt = CertificateCoder.encryptByPublicKey(data, publicKey);

		PrivateKey privateKey = CertificateCoder.getPrivateKey(keyStorePath, alias, password);
		byte[] decrypt = CertificateCoder.decryptByPrivateKey(encrypt, privateKey);
		String outputStr = new String(decrypt);

		System.err.println("加密前: " + inputStr + "\n\r" + "解密后: " + outputStr);

		// 验证数据一致
		assertArrayEquals(data, decrypt);

		// 验证证书有效
		assertTrue(CertificateCoder.verifyCertificate(certificatePath));
	}

	@Test
	public void testSign() throws Exception {
		System.err.println("私钥加密——公钥解密");

		String inputStr = "sign";
		byte[] data = inputStr.getBytes();

		PrivateKey privateKey = CertificateCoder.getPrivateKey(keyStorePath, alias, password);
		byte[] encodedData = CertificateCoder.encryptByPrivateKey(data, privateKey);

		PublicKey publicKey = CertificateCoder.getPublicKey(certificatePath);
		byte[] decodedData = CertificateCoder.decryptByPublicKey(encodedData, publicKey);

		String outputStr = new String(decodedData);
		System.err.println("加密前: " + inputStr + "\n\r" + "解密后: " + outputStr);
		assertEquals(inputStr, outputStr);

		System.err.println("私钥签名——公钥验证签名");
		// 产生签名
		X509Certificate x509Certificate = (X509Certificate) CertificateCoder.getCertificate(keyStorePath, alias, password);
		String sign = CertificateCoder.sign(encodedData, x509Certificate, privateKey);
		System.err.println("签名:\r" + sign);

		// 验证签名
		boolean status = CertificateCoder.verify(encodedData, sign,
				certificatePath);
		System.err.println("状态:\r" + status);
		assertTrue(status);
	}

}
