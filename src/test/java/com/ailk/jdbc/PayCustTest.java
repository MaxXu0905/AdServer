package com.ailk.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.util.List;


import org.junit.Test;

import com.ailk.jdbc.PayCustManager;
import com.ailk.jdbc.entity.PayCust;

public class PayCustTest {
	
	@Test
	public void testApi() {
		PayCustManager mgr = PayCustManager.getInstance();
		PayCust payCust = newInstance();
		
		// ²âÊÔsave()
		mgr.save(payCust);
		
		List<PayCust> result = mgr.get(payCust.getCustId(), Integer.MAX_VALUE);
		for (PayCust item: result) {
			assertEquals(item.getCustId(), payCust.getCustId());
		}
		
		// ²âÊÔ¸üĞÂ
		payCust.setPayAmount(10L);
		mgr.update(payCust);
		
		result = mgr.get(payCust.getId(), Integer.MAX_VALUE);
		for (PayCust item: result) {
			assertEquals(item.getPayAmount(), payCust.getPayAmount());
		}
		
		// ²âÊÔdelete()
		mgr.delete(payCust);
		
		result = mgr.get(payCust.getCustId(), Integer.MAX_VALUE);
		assertTrue(result.isEmpty());
	}
	
	static private PayCust newInstance() {
		PayCust payCust = new PayCust();
		
		payCust.setCustId(1L);
		payCust.setPayAmount(2L);
		payCust.setPayType((short) 3);
		payCust.setPayDate(new Timestamp(System.currentTimeMillis() / 1000 * 1000));
		
		return payCust;
	}

}
