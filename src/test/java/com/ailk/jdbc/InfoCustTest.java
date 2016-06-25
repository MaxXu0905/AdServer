package com.ailk.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.sql.Timestamp;


import org.junit.Before;
import org.junit.Test;

import com.ailk.jdbc.InfoCustManager;
import com.ailk.jdbc.entity.InfoCust;

public class InfoCustTest {
	
	@Before
	public void init() {
		try {
			InfoCustManager mgr = InfoCustManager.getInstance();
			
			InfoCust infoCust = newInstance();
			mgr.delete(infoCust);
		} catch (Exception e) {
		}
	}
	
	@Test
	public void testApi() {
		InfoCustManager mgr = InfoCustManager.getInstance();
		InfoCust infoCust = newInstance();
		
		// ≤‚ ‘save()
		mgr.save(infoCust);
		
		// ≤‚ ‘get()
		InfoCust value = mgr.get(infoCust.getCustId());
		assertEquals(infoCust, value);
		
		// ≤‚ ‘get()
		value = mgr.get(infoCust.getPhoneNo());
		assertEquals(infoCust, value);
		
		// ≤‚ ‘get()
		value = mgr.get(infoCust.getEmail());
		assertEquals(infoCust, value);
		
		// ≤‚ ‘update()
		infoCust.setEmail("email2");
		mgr.update(infoCust);
		
		// ≤‚ ‘get()
		value = mgr.get(infoCust.getEmail());
		assertEquals(infoCust, value);
		
		// ≤‚ ‘delete()
		mgr.delete(infoCust);
		value = mgr.get(infoCust.getCustId());
		assertNull(value);
	}
	
	static private InfoCust newInstance() {
		InfoCust infoCust = new InfoCust();
		
		infoCust.setCustType((short) 1);
		infoCust.setCustName("custName");
		infoCust.setLicType((short) 2);
		infoCust.setLicLoc("licLoc");
		infoCust.setContact("contact");
		infoCust.setAddress("address");
		infoCust.setPhoneNo("phoneNo");
		infoCust.setEmail("email");
		infoCust.setPassword("password");
		infoCust.setRegDate(new Timestamp(System.currentTimeMillis() / 1000 * 1000));
		
		return infoCust;
	}

}
