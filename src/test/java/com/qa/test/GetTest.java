package com.qa.test;

import static org.testng.Assert.assertEquals;
import java.io.IOException;
import java.util.HashMap;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
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

	@DataProvider(name = "StatusCodes")
	public Object[][] statusCodeData() {
		return TestUtil.getExcelData("StatusCode");
	}

	//@Test(priority = 1, dataProvider = "StatusCodes")
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

	@DataProvider(name = "HeaderValues")
	public Object[][] headerData() {
		return TestUtil.getExcelData("Headers");
	}

	//@Test(priority = 2, dataProvider = "HeaderValues")
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
		System.out.println(headers);
		Assert.assertEquals(restClient.getHeaderByKey(httpResponse, "Content-Type"), content_type);
		if (!(link.length() == 0)) {
			Assert.assertEquals(restClient.getHeaderByKey(httpResponse, "Link"), link);
		} else {
			Assert.assertNull(restClient.getHeaderByKey(httpResponse, "Link"));
		}
		Assert.assertEquals(restClient.getHeaderByKey(httpResponse, "Status"), status);
		Assert.assertEquals(restClient.getHeaderByKey(httpResponse, "X-Content-Type-Options"), x_content_type_options);

	}

	@DataProvider(name = "JSONData")
	public Object[][] JSONData() {
		return TestUtil.getExcelData("JSONData");
	}
	@Test(priority = 3, dataProvider = "JSONData")
	public void ValidateJSONResponse(String url, String totalCount, String incomplete_Results, String items0_Id,
			String items0_Name, String items0_Owner_Login) throws ParseException, IOException {
		JSONObject jsonResponse = new JSONObject();
		RestClient restClient = new RestClient();
		httpResponse = restClient.getUrl(serverUrl + url);
		jsonResponse = restClient.getJsonResponse(httpResponse);
		System.out.println(jsonResponse);
		Assert.assertEquals(restClient.getJsonByJpath(jsonResponse, "/incomplete_results"), incomplete_Results);
		Assert.assertEquals(restClient.getJsonByJpath(jsonResponse, "/items[0]/id"), items0_Id);
		Assert.assertEquals(restClient.getJsonByJpath(jsonResponse, "/items[0]/name"), items0_Name);
		Assert.assertEquals(restClient.getJsonByJpath(jsonResponse, "/items[0]/owner/login"), items0_Owner_Login);
	}
	
	
}
