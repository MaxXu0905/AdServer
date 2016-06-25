package com.ailk.jdbc;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import java.sql.Timestamp;
import java.util.List;


import org.junit.Test;

import com.ailk.jdbc.LogBehaviorManager;
import com.ailk.jdbc.entity.LogBehavior;

public class LogBehaviorTest {
	
	@Test
	public void testApi() {
		LogBehaviorManager mgr = LogBehaviorManager.getInstance();
		LogBehavior logBehavior = newInstance();
		
		// ≤‚ ‘save()
		mgr.save(logBehavior);
		
		// ≤‚ ‘get()
		LogBehavior entity = mgr.get(logBehavior.getId());
		assertTrue(entity.equals(logBehavior));
		
		// ≤‚ ‘getBy
		List<LogBehavior> result = mgr.getByUserId(logBehavior.getUserId(), Integer.MAX_VALUE);
		assertNotNull(result);
		for (LogBehavior item : result) {
			assertEquals(item.getUserId(), logBehavior.getUserId());
		}
	}
	
	static private LogBehavior newInstance() {
		LogBehavior logBehavior = new LogBehavior();
		
		logBehavior.setUserId(1L);
		logBehavior.setBehavior(2);
		logBehavior.setContent("content");
		logBehavior.setActionDate(new Timestamp(System.currentTimeMillis() / 1000 * 1000));
		
		return logBehavior;
	}

}
