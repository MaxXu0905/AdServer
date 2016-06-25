package com.ailk.jdbc;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import com.ailk.jdbc.entity.SystemProperties;

public class SystemPropertiesTest {

	@Test
	public void testApi() {
		SystemProperties entity1 = new SystemProperties();
		entity1.setPropertyName(SystemPropertiesManager.ANDROID_VERSION);
		entity1.setPropertyValue("1");
		
		SystemProperties entity2 = new SystemProperties();
		entity2.setPropertyName(SystemPropertiesManager.ANDROID_VERSION_NAME);
		entity2.setPropertyValue("1.0");
		
		SystemProperties entity3 = new SystemProperties();
		entity3.setPropertyName(SystemPropertiesManager.ANDROID_INFO);
		entity3.setPropertyValue("�°汾1");
		
		int partition = 0;
		Session session = HibernateUtil.getSessionFactory(partition).openSession();
		Transaction t = session.beginTransaction();
		try {
			session.save(entity1);
			session.save(entity2);
			t.commit();
		} finally {
			if (t.isActive())
				t.rollback();
			session.close();
		} 
	}

}
