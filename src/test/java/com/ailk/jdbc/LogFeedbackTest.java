package com.ailk.jdbc;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import java.sql.Timestamp;
import java.util.List;


import org.junit.Test;

import com.ailk.jdbc.LogFeedbackManager;
import com.ailk.jdbc.entity.LogFeedback;

public class LogFeedbackTest {
	
	@Test
	public void testApi() {
		LogFeedbackManager mgr = LogFeedbackManager.getInstance();
		LogFeedback logFeedback = newInstance();
		
		// ≤‚ ‘save()
		mgr.save(logFeedback);
		
		// ≤‚ ‘get()
		LogFeedback entity = mgr.get(logFeedback.getId());
		assertTrue(entity.equals(logFeedback));
		
		// ≤‚ ‘getBy
		List<LogFeedback> result = mgr.getByUserId(logFeedback.getUserId(), Integer.MAX_VALUE);
		assertNotNull(result);
		for (LogFeedback item : result) {
			assertEquals(item.getUserId(), logFeedback.getUserId());
		}
	}
	
	static private LogFeedback newInstance() {
		LogFeedback logFeedback = new LogFeedback();
		
		logFeedback.setUserId(1L);
		logFeedback.setContent("content");
		logFeedback.setActionDate(new Timestamp(System.currentTimeMillis() / 1000 * 1000));
		logFeedback.setReason("reason");
		
		return logFeedback;
	}

}
