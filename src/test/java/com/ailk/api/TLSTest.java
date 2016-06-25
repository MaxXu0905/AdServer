package com.ailk.api;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.ailk.api.impl.TLSClientManager;
import com.ailk.api.impl.TLSServerManager;
import com.ailk.common.ConstVariables;


/**
 * 
 * @author 
 * @version 1.0
 * @since 1.0
 */
public class TLSTest {

	@Test
	public void testApi() throws Exception {
		TLSServerManager serverMgr = new TLSServerManager();
		TLSClientManager clientMgr = new TLSClientManager();
		TLSServerIfc.HandshakeRequest request = new TLSServerIfc.HandshakeRequest();
		request.setOsType(ConstVariables.OS_TYPE_ANDROID);
		request.setData(new String(clientMgr.generateKey()));
		
		assertEquals(serverMgr.handshake(request).getErrorCode(), 0);
		
		String inputStr = "ºÚµ•º”√‹";
		byte[] inputData = inputStr.getBytes();
		
		byte[] encryptedData = clientMgr.encrypt(inputData);
		byte[] decryptedData = serverMgr.decrypt(encryptedData);
		assertTrue(inputStr.equals(new String(decryptedData)));

		encryptedData = serverMgr.encrypt(inputData);
		decryptedData = clientMgr.decrypt(encryptedData);
		assertTrue(inputStr.equals(new String(decryptedData)));
	}
}