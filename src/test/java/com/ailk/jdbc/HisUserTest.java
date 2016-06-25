package com.ailk.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.sql.Timestamp;


import org.junit.Test;

import com.ailk.jdbc.HisUserManager;
import com.ailk.jdbc.entity.HisUser;

public class HisUserTest {
	
	@Test
	public void testApi() {
		HisUserManager mgr = HisUserManager.getInstance();
		HisUser hisUser = newInstance();
		
		// ≤‚ ‘save()
		mgr.save(hisUser);
		
		// ≤‚ ‘get()
		HisUser value = mgr.get(hisUser.getId());

		assertNotNull(value);
		assertEquals(hisUser, value);
		
		// ≤‚ ‘delete()
		mgr.delete(hisUser);
		value = mgr.get(hisUser.getId());
		assertNull(value);
	}
	
	static private HisUser newInstance() {
		HisUser hisUser = new HisUser();
		
		hisUser.setUserId(1L);
		hisUser.setChangeName("changeName");
		hisUser.setOldValue("oldValue");
		hisUser.setNewValue("newValue");
		hisUser.setChangeDate(new Timestamp(System.currentTimeMillis() / 1000 * 1000));
		
		return hisUser;
	}

}
