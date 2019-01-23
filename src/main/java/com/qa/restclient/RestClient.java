package com.qa.restclient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.qa.util.TestUtil;

public class RestClient {

	// 1.Get Method

	// 1.a Get URL
	public CloseableHttpResponse getUrl(String url) throws ClientProtocolException, IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
		return httpResponse;
	}

	// 1.b Get Status Code

	public int getStatusCode(CloseableHttpResponse httpResponse) {
		return httpResponse.getStatusLine().getStatusCode();
	}

	// 1.c Get All Headers

	public HashMap<String, String> getHeaders(CloseableHttpResponse httpResponse) {
		Header[] headerArray = httpResponse.getAllHeaders();
		HashMap<String, String> headerResponse = new HashMap<String, String>();
		for (Header h : headerArray) {
			headerResponse.put(h.getName(), h.getValue());
		}
		return headerResponse;
	}

	// 1.d Get specific Header by passing key

	public String getHeaderByKey(CloseableHttpResponse httpResponse, String key) {
		Header[] headerArray = httpResponse.getAllHeaders();
		HashMap<String, String> headerResponse = new HashMap<String, String>();
		for (Header h : headerArray) {
			headerResponse.put(h.getName(), h.getValue());
		}
		return headerResponse.get(key);
	}

	// 1.e Get full JSON Response

	public JSONObject getJsonResponse(CloseableHttpResponse httpResponse) throws ParseException, IOException {
		String response = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
		JSONObject jsonResponse = new JSONObject(response);
		return jsonResponse;
	}

	// 1.f Get Specific JSON Value by passing JSON Path

	public String getJsonByJpath(JSONObject jsonResponse, String jpath) throws ParseException, IOException {
		Object obj = jsonResponse;
		for (String s : jpath.split("/"))
			if (!s.isEmpty())
				if (!(s.contains("[") || s.contains("]")))
					obj = ((JSONObject) obj).get(s);
				else if (s.contains("[") || s.contains("]"))
					obj = ((JSONArray) ((JSONObject) obj).get(s.split("\\[")[0]))
							.get(Integer.parseInt(s.split("\\[")[1].replace("]", "")));
		return obj.toString();
	}


}