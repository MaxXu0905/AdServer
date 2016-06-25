package com.ailk.api;

import org.junit.Test;

import com.ailk.api.impl.AuditManager;

/**
 * 
 * @author
 * @version 1.0
 * @since 1.0
 */
public class AuditTest {

	@Test
	public void testApi() throws Exception {
		AuditManager mgr = AuditManager.getInstance();

		mgr.syslog(1L, "SAVE MESSAGE");
		mgr.audit(2L, "LOG MESSAGE");
	}

}