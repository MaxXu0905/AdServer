package com.ailk.security;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

import com.ailk.security.ECCCoder;


/**
 * 
 * @author
 * @version 1.0
 * @since 1.0
 */
public class ECCCoderTest {

	@Test
	public void test() throws Exception {
		String inputStr = "abc";
		byte[] data = inputStr.getBytes();

		Map<String, Object> keyMap = ECCCoder.initKey();

		String publicKey = ECCCoder.getPublicKey(keyMap);
		String privateKey = ECCCoder.getPrivateKey(keyMap);
		System.err.println("��Կ: \n" + publicKey);
		System.err.println("˽Կ�� \n" + privateKey);

		byte[] encodedData = ECCCoder.encrypt(data, publicKey);

		byte[] decodedData = ECCCoder.decrypt(encodedData, privateKey);

		String outputStr = new String(decodedData);
		System.err.println("����ǰ: " + inputStr + "\n\r" + "���ܺ�: " + outputStr);
		assertEquals(inputStr, outputStr);
	}
}
