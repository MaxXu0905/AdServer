package com.ailk.jdbc;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.ailk.jdbc.entity.InfoUserExt;
import com.ailk.jdbc.entity.InfoUserExtPK;

public class InfoUserExtTest {
	
	@Before
	public void init() {
		InfoUserExtManager mgr = InfoUserExtManager.getInstance();
		
		List<InfoUserExt> items = newInstance();
		for (InfoUserExt item: items) {
			try { 
				mgr.delete(item);
			} catch (Exception e) {
			}		
		}
	}
	
	@Test
	public void testApi() {
		InfoUserExtManager mgr = InfoUserExtManager.getInstance();
		List<InfoUserExt> items = newInstance();
		
		// ≤‚ ‘save()
		for (InfoUserExt item: items) {
			mgr.save(item);
		}
		
		// ≤‚ ‘update()
		for (InfoUserExt item: items) {
			item.setAttrValue(item.getAttrValue());
			mgr.update(item);
		}
		
		// ≤‚ ‘delete()
		for (InfoUserExt item: items) {
			mgr.delete(item);
		}
	}
	
	static private List<InfoUserExt> newInstance() {
		List<InfoUserExt> items = new ArrayList<InfoUserExt>();
		
		for (short i = 0; i < 10; i++) {
			InfoUserExt infoUserExt = new InfoUserExt();
			InfoUserExtPK infoUserExtPK = new InfoUserExtPK();
			
			infoUserExtPK.setUserId(1L);
			infoUserExtPK.setAttrId(i);
			infoUserExt.setInfoUserExtPK(infoUserExtPK);
			infoUserExt.setAttrValue(100 + i);
			
			items.add(infoUserExt);
		}
				
		return items;
	}

}
