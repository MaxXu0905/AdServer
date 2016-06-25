package com.ailk.api;

import org.junit.Test;

import com.ailk.jdbc.entity.InfoCust;
import com.google.gson.Gson;


/**
 * 
 * @author 
 * @version 1.0
 * @since 1.0
 */
public class GsonTest {

	@Test
	public void testApi() throws Exception {
		Gson gson = new Gson();
		
		InfoCust infoCust = new InfoCust();
		infoCust.setCustName("dddd");
		infoCust.setContact("dddd");
		System.out.println(gson.toJson(infoCust));
	}
}