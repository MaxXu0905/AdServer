package com.ailk.main.geo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;

/**
 * 根据经纬度获取区域位置，更新用户位置
 * 
 * @author xugq
 * 
 */
public class ParseLocation {

	private static Gson gson = new Gson();

	public static void main(String[] args) {
		ParseLocation instance = new ParseLocation();
		GeoLocation location = new GeoLocation();
		
		location.setLat(39.983424);
		location.setLng(116.322987);
		GeoReverseResponse response = instance.getLocation(location);
		System.out.println(response.getResult().getAddressComponent().getCity());
	}
	
	public GeoReverseResponse getLocation(GeoLocation location) {
		try {
			StringBuilder builder = new StringBuilder();
			builder.append("http://api.map.baidu.com/geocoder/v2/?ak=982570957E7bA589545fd19310d4be9e&location=");
			builder.append(location.lat);
			builder.append(",");
			builder.append(location.lng);
			builder.append("&output=json");
			String urlStr = builder.toString();

			URL url = new URL(urlStr);
			HttpURLConnection urlConn = (HttpURLConnection) url
					.openConnection();
			urlConn.setDoOutput(true);
			urlConn.setRequestMethod("GET");

			InputStream input = urlConn.getInputStream();
			InputStreamReader isr = new InputStreamReader(input, "utf-8");
			BufferedReader read = new BufferedReader(isr);
			String str = "";
			String s;
			while ((s = read.readLine()) != null)
				str += s;

			return (GeoReverseResponse) gson.fromJson(str, GeoReverseResponse.class);
		} catch (Exception e) {
			return null;
		}
	}

}
