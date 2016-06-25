package com.ailk.open;

import org.junit.Test;

public class SmsSendTest {

	@Test
	public void test() {
		String verifyCode = SmsSend.sendSms("18612259745");
		System.out.println("Verify Code is " + verifyCode);
	}

}
