package com.ailk.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.sql.Timestamp;
import java.util.List;


import org.junit.Before;
import org.junit.Test;

import com.ailk.jdbc.InfoContractManager;
import com.ailk.jdbc.entity.InfoContract;

public class InfoContractTest {
	
	@Before
	public void init() {
		try {
			InfoContractManager mgr = InfoContractManager.getInstance();
			
			InfoContract infoContract = newInstance();
			mgr.delete(infoContract);
		} catch (Exception e) {
		}
	}
	
	@Test
	public void testApi() {
		InfoContractManager mgr = InfoContractManager.getInstance();
		InfoContract infoContract = newInstance();
		
		// ≤‚ ‘save()
		mgr.save(infoContract);
		
		// ≤‚ ‘get()
		InfoContract value = mgr.get(infoContract.getContractId());
		assertEquals(infoContract, value);
		
		// ≤‚ ‘getByCustId()
		List<InfoContract> values = mgr.getByCustId(infoContract.getCustId(), Integer.MAX_VALUE);
		assertEquals(values.size(), 1);
		assertEquals(infoContract, values.get(0));
		
		// ≤‚ ‘update()
		infoContract.setStatus((short) 20);
		mgr.update(infoContract);
		
		// ≤‚ ‘get()
		value = mgr.get(infoContract.getContractId());
		assertEquals(infoContract, value);
		
		// ≤‚ ‘delete()
		mgr.delete(infoContract);
		value = mgr.get(infoContract.getContractId());
		assertNull(value);
	}
	
	static private InfoContract newInstance() {
		InfoContract infoContract = new InfoContract();
		
		infoContract.setCustId(1L);
		infoContract.setRegDate(new Timestamp(System.currentTimeMillis() / 1000 * 1000));
		infoContract.setStatus((short) 2);
		infoContract.setReason("reason");
		
		return infoContract;
	}

}
