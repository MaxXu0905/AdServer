package com.ailk.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.Timestamp;
import java.util.List;

import org.junit.Test;

import com.ailk.jdbc.entity.LogPromotion;

public class LogPromotionTest {
	
	@Test
	public void testApi() {
		LogPromotionManager mgr = LogPromotionManager.getInstance();
		LogPromotion logPromotion = newInstance();
		
		// ≤‚ ‘save()
		mgr.save(logPromotion);
		
		// ≤‚ ‘get
		List<LogPromotion> result = mgr.getByUserId(logPromotion.getUserId());
		assertNotNull(result);
		for (LogPromotion item : result) {
			assertEquals(item.getUserId(), logPromotion.getUserId());
		}
	}
	
	static private LogPromotion newInstance() {
		LogPromotion logPromotion = new LogPromotion();
		
		logPromotion.setUserId(1L);
		logPromotion.setAdId(2);
		logPromotion.setActionDate(new Timestamp(System.currentTimeMillis() / 1000 * 1000));
		logPromotion.setAnswers("3");
		logPromotion.setLastUpdate(null);
		
		return logPromotion;
	}

}
