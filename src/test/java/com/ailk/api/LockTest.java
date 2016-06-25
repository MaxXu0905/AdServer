package com.ailk.api;

import org.hibernate.LockOptions;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import com.ailk.jdbc.HibernateUtil;
import com.ailk.jdbc.UserStatusManager;

/**
 * 
 * @author
 * @version 1.0
 * @since 1.0
 */
public class LockTest {

	@Test
	public void testApi() throws Exception {
		long userId = 1L;
		int partition = HibernateUtil.getPartition(userId);
		Session session = HibernateUtil.getSessionFactory(partition).openSession();
		Transaction t = session.beginTransaction();

		UserStatusManager userStatusManager = UserStatusManager.getInstance();
		userStatusManager.get(session, userId, LockOptions.UPGRADE);

		t.commit();
	}

}