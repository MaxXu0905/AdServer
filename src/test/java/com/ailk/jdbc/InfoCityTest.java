package com.ailk.jdbc;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import com.ailk.jdbc.cache.InfoCityValue;

public class InfoCityTest {

	@Test
	public void testApi() {
		GlobalManager globalManager = GlobalManager.getInstance();
		InfoCityManager mgr = InfoCityManager.getInstance(globalManager);
		
		// ≤‚ ‘get()
		mgr.load();
		List<InfoCityValue> values = mgr.get();
		assertNotNull(values);
		assertFalse(values.isEmpty());
	}

}
