package com.ailk.jdbc;

import java.sql.Timestamp;

import org.junit.Before;
import org.junit.Test;

import com.ailk.common.GlobalVariables;
import com.ailk.jdbc.entity.UserAdStatus;


public class UserAdStatusTest {
	
	@Before
	public void init() {
		try {
			UserAdStatusManager userAdStatusMgr = UserAdStatusManager.getInstance();
			UserAdStatus entity = newInstance();
			
			userAdStatusMgr.delete(entity);
		} catch (Exception e) {
		}
	}
	
	@Test
	public void testSingle() throws Exception {
		UserAdStatusManager userAdStatusMgr = UserAdStatusManager.getInstance();
		UserAdStatus entity = newInstance();
		
		userAdStatusMgr.save(entity);
	}
	
	private UserAdStatus newInstance() {
		UserAdStatus entity = new UserAdStatus();
		
		entity.getUserAdStatusPK().setUserId(1L);
		entity.getUserAdStatusPK().setAdId(2);
		entity.setSysdate(GlobalVariables.sysdate);
		entity.setTimes(4);
		entity.setLockAmount(5L);
		entity.setLastUpdate(new Timestamp(System.currentTimeMillis()));
		
		return entity;
	}

}
