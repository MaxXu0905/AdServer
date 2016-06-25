package com.ailk.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.sql.Timestamp;

import org.junit.Before;
import org.junit.Test;

import com.ailk.jdbc.entity.InfoUser;
import com.ailk.jdbc.entity.NameUser;

public class InfoUserTest {
	
	@Before
	public void init() {
		try {
			NameUserManager nameUserManager = NameUserManager.getInstance();
			InfoUserManager mgr = InfoUserManager.getInstance();
			
			NameUser nameUser = newNameUser();
			long userId = nameUserManager.get(nameUser.getNameUserPK()).getUserId();
			
			InfoUser infoUser = newInstance();
			infoUser.setUserId(userId);
			mgr.delete(infoUser);
		} catch (Exception e) {
		}
	}
	
	@Test
	public void testApi() {
		NameUserManager nameUserManager = NameUserManager.getInstance();
		InfoUserManager mgr = InfoUserManager.getInstance();
		InfoUser infoUser = newInstance();
		NameUser nameUser = newNameUser();
		
		// ≤‚ ‘save()
		mgr.save(infoUser);
		nameUser.setUserId(infoUser.getUserId());
		nameUserManager.save(nameUser);
		
		// ≤‚ ‘get()
		InfoUser value = mgr.get(infoUser.getUserId());
		
		assertNotNull(value);
		assertEquals(infoUser, value);
		
		// ≤‚ ‘update()
		infoUser.setUserName("userName2");
		nameUserManager.delete(nameUser);
		mgr.update(infoUser);
		nameUser.getNameUserPK().setUserName("userName2");
		nameUserManager.save(nameUser);
		
		// ≤‚ ‘delete()
		mgr.delete(infoUser);
		value = mgr.get(infoUser.getUserId());
		assertNull(value);
	}
	
	@Test
	public void testConstraints() {
		InfoUserManager mgr = InfoUserManager.getInstance();
		InfoUser infoUser = newInstance();
		
		// ≤‚ ‘Œ®“ªÀ˜“˝
		mgr.save(infoUser);
		
		try {
			mgr.save(infoUser);
			fail();
		} catch (Exception e) {
			System.out.println(e);
		}
		
		// ª÷∏¥
		mgr.delete(infoUser);
	}
	
	static private NameUser newNameUser() {
		NameUser nameUser = new NameUser();
		
		nameUser.getNameUserPK().setUserName("userName");
		nameUser.getNameUserPK().setRegistered(false);
		
		return nameUser;
	}
	
	static private InfoUser newInstance() {
		InfoUser infoUser = new InfoUser();
		
		infoUser.setUserName("userName");
		infoUser.setRegistered(false);
		infoUser.setPassword("password");
		infoUser.setNickName("nickName");
		infoUser.setDevId("devId");
		infoUser.setPhoneType((short) 1);
		infoUser.setGender((short) 1);
		infoUser.setAge((short) 30);
		infoUser.setLocation("location");
		infoUser.setProduct("product");
		infoUser.setCpu("cpu");
		infoUser.setModel("model");
		infoUser.setOsType((short) 2);
		infoUser.setOsVersion("osVersion");
		infoUser.setSdk(15);
		infoUser.setApps("apps");
		infoUser.setFavor("favor");
		infoUser.setPayType((short) 3);
		infoUser.setAccount("xxx@alipay.com");
		infoUser.setRegDate(new Timestamp(System.currentTimeMillis() / 1000 * 1000));
		
		return infoUser;
	}

}
