package com.ailk.jdbc;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import com.ailk.jdbc.cache.InfoPeriodValue;

public class InfoPeriodTest {
	
	@Test
	public void testApi() {
		GlobalManager globalManager = GlobalManager.getInstance();
		InfoPeriodManager mgr = InfoPeriodManager.getInstance(globalManager);
		
		// ≤‚ ‘get()
		List<InfoPeriodValue> values = mgr.get();
		assertNotNull(values);
		assertFalse(values.isEmpty());
	}
	
}
