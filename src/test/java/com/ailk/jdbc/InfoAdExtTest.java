package com.ailk.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.ailk.jdbc.cache.InfoAdExtValue;
import com.ailk.jdbc.entity.InfoAdExt;
import com.ailk.jdbc.entity.InfoAdExtPK;

public class InfoAdExtTest {
	
	@Before
	public void init() {
		GlobalManager globalManager = GlobalManager.getInstance();
		InfoAdExtManager mgr = InfoAdExtManager.getInstance(globalManager);
		List<InfoAdExt> items = newInstance();
		
		for (InfoAdExt infoAdExt: items) {
			try {
				mgr.delete(infoAdExt);
			} catch (Exception e) {
			}
		}
	}
	
	@Test
	public void testApi() {
		GlobalManager globalManager = GlobalManager.getInstance();
		InfoAdExtManager mgr = InfoAdExtManager.getInstance(globalManager);
		List<InfoAdExt> items = newInstance();
		int adId = items.get(0).getInfoAdExtPK().getAdId();
		
		// ≤‚ ‘save()
		for (InfoAdExt infoAdExt: items) {
			mgr.save(infoAdExt);
		}
		
		// ≤‚ ‘reload()
		mgr.load();
		
		// ≤‚ ‘get()
		List<InfoAdExtValue> values = mgr.get(adId);
		assertNotNull(values);
		assertEquals(values.size(), items.size());
		
		// ≤‚ ‘delete()
		for (InfoAdExt infoAdExt: items) {
			mgr.delete(infoAdExt);
		}
		
		mgr.load();
		values = mgr.get(adId);
		assertNull(values);
	}
	
	@Test
	public void testCache() {
		GlobalManager globalManager = GlobalManager.getInstance();
		InfoAdExtManager mgr = InfoAdExtManager.getInstance(globalManager);
		List<InfoAdExt> items = newInstance();
		int adId = items.get(0).getInfoAdExtPK().getAdId();
		
		// ≤‚ ‘save()
		for (InfoAdExt infoAdExt: items) {
			mgr.save(infoAdExt);
		}
		
		List<InfoAdExtValue> values = mgr.get(adId);
		assertNotNull(values);
		assertEquals(values.size(), items.size());
		
		// ≤‚ ‘delete()
		for (InfoAdExt infoAdExt: items) {
			mgr.delete(infoAdExt);
		}
	}
	
	static private List<InfoAdExt> newInstance() {
		List<InfoAdExt> items = new ArrayList<InfoAdExt>();
		
		for (short i = 0; i < 10; i++) {
			InfoAdExt infoAdExt = new InfoAdExt();
			InfoAdExtPK infoAdExtPK = new InfoAdExtPK();
			
			infoAdExtPK.setAdId(1);
			infoAdExtPK.setAttrId(i);
			infoAdExt.setAttrValues("" + i);
			infoAdExt.setInfoAdExtPK(infoAdExtPK);
			
			items.add(infoAdExt);
		}
		
		return items;
	}

}
