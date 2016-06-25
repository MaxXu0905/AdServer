package com.ailk.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.ailk.jdbc.cache.InfoGroupValue;
import com.ailk.jdbc.entity.InfoCode;
import com.ailk.jdbc.entity.InfoCodeExt;
import com.ailk.jdbc.entity.InfoCodeExtPK;
import com.ailk.jdbc.entity.InfoCodePK;

public class InfoCodeTest {
	
	@Before
	public void init() {
		try {
			GlobalManager globalManager = GlobalManager.getInstance();
			InfoCodeManager mgr = InfoCodeManager.getInstance(globalManager);
			
			InfoCode infoCode = newInstance();
			mgr.delete(infoCode);
			
			List<InfoCodeExt> items = newExtInstance();
			for (InfoCodeExt item: items) {
				mgr.delete(item);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	@Test
	public void testApi() {
		GlobalManager globalManager = GlobalManager.getInstance();
		InfoCodeManager mgr = InfoCodeManager.getInstance(globalManager);
		InfoCode infoCode = newInstance();
		List<InfoCodeExt> items = newExtInstance();
		
		// ≤‚ ‘save()
		mgr.save(infoCode);
		for (InfoCodeExt item: items) {
			mgr.save(item);
		}
		
		mgr.load();
		
		// ≤‚ ‘get()
		InfoCodePK infoCodePK = infoCode.getInfoCodePK();

		List<InfoGroupValue> value = mgr.get(infoCodePK.getGroupName());
		assertNotNull(value);
		assertEquals(value.get(0).getItems().size(), items.size());
		
		// ≤‚ ‘update()
		infoCode.setDesc("desc2");
		mgr.update(infoCode);
		
		mgr.load();
		value = mgr.get(infoCodePK.getGroupName());
		assertEquals(value.get(0).getDesc(), infoCode.getDesc());
		
		// ≤‚ ‘delete()
		mgr.delete(infoCode);
		for (InfoCodeExt item: items) {
			mgr.delete(item);
		}
		
		mgr.load();
		value = mgr.get(infoCodePK.getGroupName());
		assertNull(value);
	}
	
	static private InfoCode newInstance() {
		InfoCode infoCode = new InfoCode();
		InfoCodePK infoCodePK = new InfoCodePK();
		
		infoCodePK.setGroupName("groupName");
		infoCodePK.setCodeName("codeName");
		infoCode.setInfoCodePK(infoCodePK);
		infoCode.setDesc("desc");
				
		return infoCode;
	}
	
	static private List<InfoCodeExt> newExtInstance() {
		List<InfoCodeExt> items = new ArrayList<InfoCodeExt>();
		
		for (int i = 0; i < 10; i++) {
			InfoCodeExt item = new InfoCodeExt();
			InfoCodeExtPK infoCodeExtPK = new InfoCodeExtPK();
			
			infoCodeExtPK.setGroupName("groupName");
			infoCodeExtPK.setCodeName("codeName");
			infoCodeExtPK.setCodeValue(i);
			
			item.setInfoCodeExtPK(infoCodeExtPK);
			item.setDesc("desc");
			items.add(item);
		}
				
		return items;
	}

}
