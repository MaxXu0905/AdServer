package com.ailk.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.util.List;

import org.junit.Test;

import com.ailk.common.ConstVariables;
import com.ailk.jdbc.PayRqstManager;
import com.ailk.jdbc.entity.PayRqst;

public class PayRqstTest {

	@Test
	public void testApi() {
		PayRqstManager mgr = PayRqstManager.getInstance();
		PayRqst payRqst = newInstance();

		// ≤‚ ‘save()
		mgr.save(payRqst);

		List<PayRqst> result = mgr.get(payRqst.getStatus(), Integer.MAX_VALUE);
		for (PayRqst item : result) {
			assertEquals(item.getStatus(), payRqst.getStatus());
		}

		result = mgr.get(payRqst.getUserId(), 0, Integer.MAX_VALUE);
		for (PayRqst item : result) {
			assertEquals(item.getUserId(), payRqst.getUserId());
		}

		// ≤‚ ‘∏¸–¬
		payRqst.setPayType(ConstVariables.PAY_TYPE_ALIPAY);
		payRqst.setAmount(10);
		mgr.update(payRqst);

		result = mgr.get(payRqst.getId(), 0, Integer.MAX_VALUE);
		for (PayRqst item : result) {
			assertEquals(item.getAmount(), payRqst.getAmount());
		}

		// ≤‚ ‘delete()
		mgr.delete(payRqst);

		result = mgr.get(payRqst.getUserId(), 0, Integer.MAX_VALUE);
		assertTrue(result.isEmpty());
	}

	static private PayRqst newInstance() {
		PayRqst payRqst = new PayRqst();

		payRqst.setUserId(1L);
		payRqst.setPayType(ConstVariables.PAY_TYPE_ALIPAY);
		payRqst.setPayName("payName");
		payRqst.setPayId("payId");
		payRqst.setAmount(2);
		payRqst.setRqstDate(new Timestamp(System.currentTimeMillis() / 1000 * 1000));
		payRqst.setStatus((short) 3);
		payRqst.setReason("reason");
		payRqst.setLastUpdate(payRqst.getRqstDate());

		return payRqst;
	}

}
