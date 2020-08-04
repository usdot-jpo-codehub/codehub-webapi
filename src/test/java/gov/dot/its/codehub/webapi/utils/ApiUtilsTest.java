package gov.dot.its.codehub.webapi.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import gov.dot.its.codehub.webapi.model.ApiError;

@RunWith(SpringRunner.class)
@TestPropertySource(properties = {"codehub.webapi.debug=false"})
public class ApiUtilsTest {
	@Test
	public void testInstance() {
		ApiUtils apiUtils = new ApiUtils();
		assertNotNull(apiUtils);
	}

	@Test
	public void testGetQueryParamStringDefaultValue() {
		Map<String, String> params = new HashMap<>();
		ApiUtils apiUtils = new ApiUtils();
		String result = apiUtils.getQueryParamString(params, "not-there", "default");
		assertEquals("default", result);
	}

	@Test
	public void testGetQueryParamStringValue() {
		Map<String, String> params = new HashMap<>();
		params.put("key", "value");
		ApiUtils apiUtils = new ApiUtils();
		String result = apiUtils.getQueryParamString(params, "key", "default");
		assertEquals("value", result);
	}

	@Test
	public void testGetQueryParamIntegerDefaultValue() {
		Map<String, String> params = new HashMap<>();
		ApiUtils apiUtils = new ApiUtils();
		int result = apiUtils.getQueryParamInteger(params, null, 1);
		assertEquals(1, result);
	}

	@Test
	public void testGetQueryParamIntegerValue() {
		Map<String, String> params = new HashMap<>();
		params.put("key", "3");
		ApiUtils apiUtils = new ApiUtils();
		int result = apiUtils.getQueryParamInteger(params, "key", 1);
		assertEquals(3, result);
	}

	@Test
	public void testGetQueryParamIntegerInvalid() {
		Map<String, String> params = new HashMap<>();
		params.put("key", ",3");
		ApiUtils apiUtils = new ApiUtils();
		int result = apiUtils.getQueryParamInteger(params, "key", 1);
		assertEquals(1, result);
	}

	@Test
	public void testGetMd5Null() {
		ApiUtils apiUtils = new ApiUtils();
		String result = apiUtils.getMd5(null);
		assertNull(result);
	}

	@Test
	public void testGetMd5Value() {
		ApiUtils apiUtils = new ApiUtils();
		String result = apiUtils.getMd5("source");
		assertNotNull(result);
	}

	@Test
	public void testGetCurrentUtc() {
		ApiUtils apiUtils = new ApiUtils();
		Date result = apiUtils.getCurrentUtc();
		assertNotNull(result);
	}

	@Test
	public void testGetTimestampFormat() throws ParseException {
		String testFormat = "yyyy-MM-dd HH:mm:ss";
		
		SimpleDateFormat format = new SimpleDateFormat(testFormat);
		String source = "2020-07-27 13:40:00";
		Date date = format.parse(source);
		
		SimpleDateFormat dateFormat = new SimpleDateFormat(testFormat);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		
		ApiUtils apiUtils = new ApiUtils();
		String result = apiUtils.getTimestampFormat(date, testFormat);
		assertEquals(dateFormat.format(date), result);
	}

	@Test
	public void testGetErrorsFromException() {
		Exception testException = new Exception("Test Exception");
		testException.addSuppressed(new Throwable("Throwable"));

		List<ApiError> errors = new ArrayList<>();
		ApiUtils apiUtils = new ApiUtils();
		errors = apiUtils.getErrorsFromException(errors, testException);

		assertFalse(errors.isEmpty());
		assertEquals(2, errors.size());
		assertEquals("Error : Test Exception ", errors.get(0).getMessage());
	}

	@Test
	public void testStringFormatEqualParams() {
		String template = "%s: %s";

		ApiUtils apiUtils = new ApiUtils();
		String result = apiUtils.stringFormat(template, "one", "two");

		assertEquals("one: two", result);
	}

	@Test
	public void testStringFormatLessParams() {
		String template = "%s: %s %s";

		ApiUtils apiUtils = new ApiUtils();
		String result = apiUtils.stringFormat(template, "one", "two");

		assertEquals("one: two ", result);
	}

	@Test
	public void testStringFormatMoreParams() {
		String template = "%s: %s";

		ApiUtils apiUtils = new ApiUtils();
		String result = apiUtils.stringFormat(template, "one", "two", "three");

		assertEquals("one: two three ", result);
	}

}
