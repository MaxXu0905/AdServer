package com.ailk.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.util.List;


import org.junit.Test;

import com.ailk.jdbc.PayUserManager;
import com.ailk.jdbc.entity.PayUser;

public class PayUserTest {
	
	@Test
	public void testApi() {
		PayUserManager mgr = PayUserManager.getInstance();
		PayUser payUser = newInstance();
		
		// ≤‚ ‘save()
		mgr.save(payUser);
		
		List<PayUser> result = mgr.get(payUser.getUserId(), Integer.MAX_VALUE);
		for (PayUser item: result) {
			assertEquals(item.getUserId(), payUser.getUserId());
		}
		
		// ≤‚ ‘∏¸–¬
		payUser.setPayAmount(10);
		mgr.update(payUser);
		
		result = mgr.get(payUser.getId(), Integer.MAX_VALUE);
		for (PayUser item: result) {
			assertEquals(item.getPayAmount(), payUser.getPayAmount());
		}
		
		// ≤‚ ‘delete()
		mgr.delete(payUser);
		
		result = mgr.get(payUser.getUserId(), Integer.MAX_VALUE);
		assertTrue(result.isEmpty());
	}
	
	static private PayUser newInstance() {
		PayUser payUser = new PayUser();
		
		payUser.setUserId(1L);
		payUser.setPayAmount(2);
		payUser.setPayType((short) 3);
		payUser.setPayDate(new Timestamp(System.currentTimeMillis() / 1000 * 1000));
		
		return payUser;
	}

}
