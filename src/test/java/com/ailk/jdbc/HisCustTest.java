package com.ailk.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.sql.Timestamp;


import org.junit.Test;

import com.ailk.jdbc.HisCustManager;
import com.ailk.jdbc.entity.HisCust;

public class HisCustTest {
	
	@Test
	public void testApi() {
		HisCustManager mgr = HisCustManager.getInstance();
		HisCust hisCust = newInstance();
		
		// ≤‚ ‘save()
		mgr.save(hisCust);
		
		// ≤‚ ‘get()
		HisCust value = mgr.get(hisCust.getId());

		assertNotNull(value);
		assertEquals(hisCust, value);
		
		// ≤‚ ‘delete()
		mgr.delete(hisCust);
		value = mgr.get(hisCust.getId());
		assertNull(value);
	}
	
	static private HisCust newInstance() {
		HisCust hisCust = new HisCust();
		
		hisCust.setCustId(1L);
		hisCust.setChangeName("changeName");
		hisCust.setOldValue("oldValue");
		hisCust.setNewValue("newValue");
		hisCust.setChangeDate(new Timestamp(System.currentTimeMillis() / 1000 * 1000));
		
		return hisCust;
	}

}
