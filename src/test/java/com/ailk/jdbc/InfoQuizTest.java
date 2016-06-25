package com.ailk.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.ailk.jdbc.cache.InfoQuizValue;
import com.ailk.jdbc.entity.InfoQuiz;

public class InfoQuizTest {
	
	@Test
	public void testApi() {
		GlobalManager globalManager = GlobalManager.getInstance();
		InfoQuizManager mgr = InfoQuizManager.getInstance(globalManager);
		InfoQuiz infoQuiz = newInstance();
		
		// ≤‚ ‘save()
		mgr.save(infoQuiz);
		// ≤‚ ‘reload()
		mgr.load();
		
		// ≤‚ ‘get()
		InfoQuizValue value = mgr.get(infoQuiz.getQuizId());
		
		assertNotNull(value);
		assertEquals(infoQuiz.getTitle(), value.getTitle());
		
		assertEquals(infoQuiz.getOptions(), value.getOptions());
		assertEquals(infoQuiz.getAnswers(), value.getAnswers());
		
		// ≤‚ ‘delete()
		mgr.delete(infoQuiz);
		mgr.load();
		value = mgr.get(infoQuiz.getQuizId());
		assertNull(value);
	}
	
	@Test
	public void testCache() {
		GlobalManager globalManager = GlobalManager.getInstance();
		InfoQuizManager mgr = InfoQuizManager.getInstance(globalManager);
		InfoQuiz infoQuiz = newInstance();
		
		// ≤‚ ‘save()
		mgr.save(infoQuiz);
		
		// ≤‚ ‘get()
		InfoQuizValue value = mgr.get(infoQuiz.getQuizId());
		
		assertNotNull(value);
		assertEquals(infoQuiz.getTitle(), value.getTitle());
		assertEquals(infoQuiz.getOptions(), value.getOptions());
		assertEquals(infoQuiz.getAnswers(), value.getAnswers());
		
		// ≤‚ ‘update()
		infoQuiz.setAnswers("2");
		mgr.update(infoQuiz);
		
		value = mgr.get(infoQuiz.getQuizId());
		assertEquals(infoQuiz.getTitle(), value.getTitle());
		assertEquals(infoQuiz.getOptions(), value.getOptions());
		assertEquals(infoQuiz.getAnswers(), value.getAnswers());
		
		// ≤‚ ‘delete()
		mgr.delete(infoQuiz);
		mgr.load();
		value = mgr.get(infoQuiz.getQuizId());
		assertNull(value);
	}
	
	static private InfoQuiz newInstance() {
		InfoQuiz infoQuiz = new InfoQuiz();
		
		infoQuiz.setQuizId(1);
		infoQuiz.setTitle("title");
		infoQuiz.setOptions("option1,option2");
		infoQuiz.setAnswers("1");
		infoQuiz.setAdId(2);
		
		return infoQuiz;
	}

}
