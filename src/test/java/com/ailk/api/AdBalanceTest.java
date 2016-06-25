package com.ailk.api;

import org.junit.Test;

import com.ailk.jdbc.AdBalanceManager;

/**
 * 
 * @author
 * @version 1.0
 * @since 1.0
 */
public class AdBalanceTest {

	@Test
	public void testApi() throws Exception {
		AdBalanceManager mgr = AdBalanceManager.getInstance();

		mgr.lock(1, 100L);
	}

}