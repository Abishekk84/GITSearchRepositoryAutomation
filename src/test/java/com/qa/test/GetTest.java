package com.qa.test;

import static org.testng.Assert.assertEquals;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.qa.base.TestBase;
import com.qa.restclient.RestClient;
import com.qa.util.TestUtil;

public class GetTest extends TestBase {

	public String serverUrl;
	public CloseableHttpResponse httpResponse;

	@BeforeMethod
	public void setup() throws IOException {
		TestBase testBase = new TestBase();
		serverUrl = prop.getProperty("serverUrl");
	}

	// 1. Test Scenarios to verify different Status codes:

	@DataProvider(name = "StatusCodes")
	public Object[][] statusCodeData() {
		return TestUtil.getExcelData("StatusCode");
	}

	@Test(priority = 1, dataProvider = "StatusCodes")
	public void validateGetStatusCode(String url, String statusCode) throws ParseException, IOException {
		RestClient restClient = new RestClient();
		try {
			httpResponse = restClient.getUrl(serverUrl + url);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Assert.assertEquals(restClient.getStatusCode(httpResponse), Integer.parseInt(statusCode));
	}

	// 2. Test Scenarios to verify different header Values

	@DataProvider(name = "HeaderValues")
	public Object[][] headerData() {
		return TestUtil.getExcelData("Headers");
	}

	@Test(priority = 2, dataProvider = "HeaderValues")
	public void validateGetHeader(String url, String content_type, String link, String status,
			String x_content_type_options) throws ParseException, IOException {
		RestClient restClient = new RestClient();
		try {
			httpResponse = restClient.getUrl(serverUrl + url);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		HashMap<String, String> headers = restClient.getHeaders(httpResponse);
		Assert.assertEquals(restClient.getHeaderByKey(httpResponse, "Content-Type"), content_type);
		if (!(link.length() == 0)) {
			Assert.assertEquals(restClient.getHeaderByKey(httpResponse, "Link"), link);
		}
		Assert.assertEquals(restClient.getHeaderByKey(httpResponse, "Status"), status);
		Assert.assertEquals(restClient.getHeaderByKey(httpResponse, "X-Content-Type-Options"), x_content_type_options);

	}

	// 3. Test Scenarios to verify the JSON response data

	@DataProvider(name = "JSONData")
	public Object[][] jsonData() {
		return TestUtil.getExcelData("JSONData");
	}

	// @Test(priority = 3, dataProvider = "JSONData")
	public void validateJSONResponse(String url, String totalCount, String incomplete_Results, String items0_Id,
			String items0_Name, String items0_Owner_Login) throws ParseException, IOException {
		JSONObject jsonResponse = new JSONObject();
		RestClient restClient = new RestClient();
		httpResponse = restClient.getUrl(serverUrl + url);
		jsonResponse = restClient.getJsonResponse(httpResponse);
		Assert.assertEquals(restClient.getJsonByJpath(jsonResponse, "/incomplete_results"), incomplete_Results);
		Assert.assertEquals(restClient.getJsonByJpath(jsonResponse, "/items[0]/id"), items0_Id);
		Assert.assertEquals(restClient.getJsonByJpath(jsonResponse, "/items[0]/name"), items0_Name);
		Assert.assertEquals(restClient.getJsonByJpath(jsonResponse, "/items[0]/owner/login"), items0_Owner_Login);
	}

	// 4. Test Scenarios to verify the user name(Author)

	@DataProvider(name = "UserData")
	public Object[][] userData() {
		return TestUtil.getExcelData("UserName");
	}

	@Test(priority = 4, dataProvider = "UserData")
	public void validateUserName(String url, String statusCode, String status, String total_Count, String message,
		String userName, String userType) throws ParseException, IOException {
		JSONObject jsonResponse = new JSONObject();
		RestClient restClient = new RestClient();
		httpResponse = restClient.getUrl(serverUrl + url);
		jsonResponse = restClient.getJsonResponse(httpResponse);
		Assert.assertEquals(restClient.getStatusCode(httpResponse), Integer.parseInt(statusCode));
		Assert.assertEquals(restClient.getHeaderByKey(httpResponse, "Status"), status);

		if (!(total_Count.length() == 0)) {
			Assert.assertEquals(restClient.getJsonByJpath(jsonResponse, "/total_count"), total_Count);
		}
		if (!(message.length() == 0)) {
			Assert.assertEquals(restClient.getJsonByJpath(jsonResponse, "/message"), message);
		}
		if (!(userName.length() == 0)) {
			Assert.assertEquals(restClient.getJsonByJpath(jsonResponse, "/items[0]/owner/login"), userName);
		}
		if (!(userType.length() == 0)) {
			Assert.assertEquals(restClient.getJsonByJpath(jsonResponse, "/items[0]/owner/type"), userType);
		}
	}

	@DataProvider(name = "sortData")
	public Object[][] sortData() {
		return TestUtil.getExcelData("sortData");
	}

	
	//5. Test Scenarios to verify the sort and order parameter
	
	@Test(priority = 5, dataProvider = "sortData")
	public void validateSort(String url, String sortParam, String sortOrder)
			throws ClientProtocolException, IOException {
		JSONObject jsonResponse = new JSONObject();
		RestClient restClient = new RestClient();
		httpResponse = restClient.getUrl(serverUrl + url + "&sort=" + sortParam + "&order=" + sortOrder);
		jsonResponse = restClient.getJsonResponse(httpResponse);
		JSONArray itemsJsonArray = jsonResponse.optJSONArray("items");
		List<Double> list = new ArrayList<Double>();
		for (int i = 0; i < itemsJsonArray.length(); i++) {
			JSONObject obj = itemsJsonArray.optJSONObject(i);
			if (sortParam.length() == 0) {
				list.add(Double.parseDouble(obj.get("score").toString()));
			} else {
				if (sortParam.equals("stars")) {
					list.add(Double.parseDouble(obj.get("stargazers_count").toString()));
				} else if (sortParam.equals("forks")) {
					list.add(Double.parseDouble(obj.get("forks").toString()));
				}
			}
		}
		List<Double> sortedList = new ArrayList<Double>(list);
		if (sortOrder.equals("asc")) {
			Collections.sort(sortedList);
		} else {
			Collections.sort(sortedList);
			Collections.reverse(sortedList);
		}

		if (sortedList.equals(list)) {
			Assert.assertTrue(true);
		} else {
			Assert.assertTrue(false);
		}

	}

}
