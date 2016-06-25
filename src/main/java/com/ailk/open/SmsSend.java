package com.ailk.open;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.CodingErrorAction;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.MessageConstraints;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;

public class SmsSend {

	private static final String URL_ACCESS_TOKEN = "https://oauth.api.189.cn/emp/oauth2/v2/access_token";
	private static final String URL_SEND_SMS = "http://api.189.cn/v2/emp/templateSms/sendSms";
	private static final int TIMEOUT = 60000;
	private static final String APP_ID = "281147380000033642";
	private static final String APP_SECRET = "f8b44b3782904ba59609b65678ece2d3";
	private static final String TEMPLATE_ID = "91000217";

	private static final Gson gson = new Gson();
	private static PoolingHttpClientConnectionManager connManager;
	private static CloseableHttpClient httpClient;
	private static RequestConfig requestConfig;

	private static Random random;
	private static SimpleDateFormat format;
	private static String accessToken;
	private static long expire;
	
	static {
		connManager = new PoolingHttpClientConnectionManager();
		SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true).build();
		connManager.setDefaultSocketConfig(socketConfig);

		MessageConstraints messageConstraints = MessageConstraints.custom().setMaxHeaderCount(200)
				.setMaxLineLength(2000).build();
		ConnectionConfig connectionConfig = ConnectionConfig.custom().setMalformedInputAction(CodingErrorAction.IGNORE)
				.setUnmappableInputAction(CodingErrorAction.IGNORE).setCharset(Consts.UTF_8)
				.setMessageConstraints(messageConstraints).build();
		connManager.setDefaultConnectionConfig(connectionConfig);
		connManager.setMaxTotal(200);
		connManager.setDefaultMaxPerRoute(20);

		httpClient = HttpClients.custom().setConnectionManager(connManager).build();

		requestConfig = RequestConfig.custom().setSocketTimeout(TIMEOUT).setConnectTimeout(TIMEOUT)
				.setConnectionRequestTimeout(TIMEOUT).build();

		random = new Random(System.currentTimeMillis());
		format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}

	public static String sendSms(String phoneNo) {
		if (accessToken == null || System.currentTimeMillis() >= expire) {
			if (!getAccessToken())
				return "";
		}

		SmsSendParams params = new SmsSendParams();
		params.param1 = String.format("%06d", random.nextInt(1000000));
		String templateParam = gson.toJson(params);

		String timestamp = format.format(new Date());
		HttpPost httpPost = new HttpPost(URL_SEND_SMS);

		try {
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("app_id", APP_ID));
			nvps.add(new BasicNameValuePair("access_token", accessToken));
			nvps.add(new BasicNameValuePair("acceptor_tel", phoneNo));
			nvps.add(new BasicNameValuePair("template_id", TEMPLATE_ID));
			nvps.add(new BasicNameValuePair("template_param", templateParam));
			nvps.add(new BasicNameValuePair("timestamp", timestamp));

			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			httpPost.setConfig(requestConfig);
			CloseableHttpResponse response = httpClient.execute(httpPost);

			try {
				HttpEntity entity = response.getEntity();
				if (entity == null)
					return "";

				byte[] bytes = new byte[1024];
				StringBuffer sb = new StringBuffer();
				int len;

				InputStream is = entity.getContent();
				while ((len = is.read(bytes)) != -1)
					sb.append(new String(bytes, 0, len));

				SendSmsResponse result = gson.fromJson(sb.toString(), SendSmsResponse.class);
				if (result == null || result.res_code != 0)
					return "";

				return params.param1;
			} finally {
				if (response != null)
					response.close();
			}
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		} finally {
			httpPost.releaseConnection();
		}

		return "";
	}

	private static synchronized boolean getAccessToken() {
		HttpPost httpPost = new HttpPost(URL_ACCESS_TOKEN);

		try {
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("grant_type", "client_credentials"));
			nvps.add(new BasicNameValuePair("app_id", APP_ID));
			nvps.add(new BasicNameValuePair("app_secret", APP_SECRET));

			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			httpPost.setConfig(requestConfig);
			CloseableHttpResponse response = httpClient.execute(httpPost);

			try {
				HttpEntity entity = response.getEntity();
				if (entity == null)
					return false;

				byte[] bytes = new byte[1024];
				StringBuffer sb = new StringBuffer();
				int len;

				InputStream is = entity.getContent();
				while ((len = is.read(bytes)) != -1)
					sb.append(new String(bytes, 0, len));

				AccessTokenResponse result = gson.fromJson(sb.toString(), AccessTokenResponse.class);
				if (result == null || result.res_code != 0)
					return false;

				accessToken = result.access_token;
				expire = System.currentTimeMillis() + (result.expires_in - TIMEOUT) * 1000;
				return true;
			} finally {
				if (response != null)
					response.close();
			}
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		} finally {
			httpPost.releaseConnection();
		}

		return false;
	}

	private static class SmsSendParams {
		String param1;
	}

	private static class AccessTokenResponse {
		int res_code;
		String access_token;
		long expires_in;
	}

	private static class SendSmsResponse {
		int res_code;
		@SuppressWarnings("unused")
		String idertifier;
	}

}
