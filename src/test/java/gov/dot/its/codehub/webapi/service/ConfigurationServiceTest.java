package gov.dot.its.codehub.webapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;

import gov.dot.its.codehub.webapi.dao.ConfigurationDao;
import gov.dot.its.codehub.webapi.model.ApiError;
import gov.dot.its.codehub.webapi.model.ApiResponse;
import gov.dot.its.codehub.webapi.model.CHCategory;
import gov.dot.its.codehub.webapi.model.CHEngagementPopup;
import gov.dot.its.codehub.webapi.utils.ApiUtils;


@RunWith(SpringRunner.class)
public class ConfigurationServiceTest {

	@InjectMocks
	private ConfigurationServiceImpl configurationService;

	@Mock
	private ConfigurationDao configurationDao;

	@Mock
	private ApiUtils apiUtils;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testCategoriesData() throws IOException {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");

		final List<CHCategory> categories = this.getFakeCategories();
		when(configurationDao.getCategories()).thenReturn(categories);

		final ApiResponse<List<CHCategory>> apiResponse = configurationService.categories(request);

		assertNotNull(apiResponse);
		assertEquals(HttpStatus.OK.value(), apiResponse.getCode());
		assertFalse(apiResponse.getResult().isEmpty());
	}

	@Test
	public void testCategoriesNoData() throws IOException {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");

		final List<CHCategory> categories = new ArrayList<>();
		when(configurationDao.getCategories()).thenReturn(categories);

		final ApiResponse<List<CHCategory>> apiResponse = configurationService.categories(request);

		assertNotNull(apiResponse);
		assertEquals(HttpStatus.NO_CONTENT.value(), apiResponse.getCode());
		assertNull(apiResponse.getResult());
	}

	@Test
	public void testCategoriesNoDataNull() throws IOException {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");

		when(configurationDao.getCategories()).thenReturn(null);

		final ApiResponse<List<CHCategory>> apiResponse = configurationService.categories(request);

		assertNotNull(apiResponse);
		assertEquals(HttpStatus.NO_CONTENT.value(), apiResponse.getCode());
		assertNull(apiResponse.getResult());
	}

	@Test
	public void testCategoriesError() throws IOException {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");

		final String errorMsg = "Test error";
		final List<ApiError> errors  = new ArrayList<>();
		errors.add(new ApiError(errorMsg));

		when(apiUtils.getErrorsFromException(anyList(), any(Exception.class))).thenReturn(errors);
		when(configurationDao.getCategories()).thenThrow(new IOException("Test exception"));

		final ApiResponse<List<CHCategory>> apiResponse = configurationService.categories(request);

		assertNotNull(apiResponse);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), apiResponse.getCode());
		assertNull(apiResponse.getResult());
		assertFalse(apiResponse.getErrors().isEmpty());
	}

	@Test
	public void testCategoryData() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");

		CHCategory category = this.getFakeCategory(1);
		when(configurationDao.getCategoryById(any(String.class))).thenReturn(category);

		ApiResponse<CHCategory> apiResponse = configurationService.category(request, "1");
		assertNotNull(apiResponse);
		assertEquals(HttpStatus.OK.value(), apiResponse.getCode());
		assertNotNull(apiResponse.getResult());
	}

	@Test
	public void testCategoryNoData() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");

		when(configurationDao.getCategoryById(any(String.class))).thenReturn(null);

		ApiResponse<CHCategory> apiResponse = configurationService.category(request, "1");
		assertNotNull(apiResponse);
		assertEquals(HttpStatus.NOT_FOUND.value(), apiResponse.getCode());
		assertNull(apiResponse.getResult());
	}

	@Test
	public void testCategoryErrorData() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");

		String msg = "internal error message";
		List<ApiError> errors = new ArrayList<>();
		errors.add(new ApiError(msg));

		when(apiUtils.getErrorsFromException(anyList(), any(Exception.class))).thenReturn(errors);
		when(configurationDao.getCategoryById(any(String.class))).thenThrow(new IOException(msg));

		ApiResponse<CHCategory> apiResponse = configurationService.category(request, "TestId");
		assertNotNull(apiResponse);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), apiResponse.getCode());
		assertNull(apiResponse.getResult());
		assertFalse(apiResponse.getErrors().isEmpty());
	}

	@Test
	public void testEngagementPopupData() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");

		List<CHEngagementPopup> engagementPopups = this.getFakeEngagementPopups();
		when(configurationDao.getEngagementPopups()).thenReturn(engagementPopups);

		ApiResponse<List<CHEngagementPopup>> apiResponse = configurationService.engagementpopups(request);

		assertNotNull(apiResponse);
		assertEquals(HttpStatus.OK.value(), apiResponse.getCode());
		assertFalse(apiResponse.getResult().isEmpty());
	}

	@Test
	public void testEngagementPopupNoData() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");

		when(configurationDao.getEngagementPopups()).thenReturn(new ArrayList<>());

		ApiResponse<List<CHEngagementPopup>> apiResponse = configurationService.engagementpopups(request);

		assertNotNull(apiResponse);
		assertEquals(HttpStatus.NO_CONTENT.value(), apiResponse.getCode());
		assertNull(apiResponse.getResult());
	}

	@Test
	public void testEngagementPopupNoDataNull() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");

		when(configurationDao.getEngagementPopups()).thenReturn(null);

		ApiResponse<List<CHEngagementPopup>> apiResponse = configurationService.engagementpopups(request);

		assertNotNull(apiResponse);
		assertEquals(HttpStatus.NO_CONTENT.value(), apiResponse.getCode());
		assertNull(apiResponse.getResult());
	}

	@Test
	public void testEngagementPopupErrorData() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");

		String msg = "Error Message for Exception";
		List<ApiError> errors = new ArrayList<>();
		errors.add(new ApiError(msg));

		when(apiUtils.getErrorsFromException(anyList(), any(Exception.class))).thenReturn(errors);
		when(configurationDao.getEngagementPopups()).thenThrow(new IOException(msg));

		ApiResponse<List<CHEngagementPopup>> apiResponse = configurationService.engagementpopups(request);

		assertNotNull(apiResponse);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), apiResponse.getCode());
		assertNull(apiResponse.getResult());
		assertFalse(apiResponse.getErrors().isEmpty());
	}

	private List<CHEngagementPopup> getFakeEngagementPopups() {
		List<CHEngagementPopup> result = new ArrayList<>();
		for(int i=1; i<=3; i++) {
			CHEngagementPopup engagementPopup = this.getFakeEngagementPopup(i);
			result.add(engagementPopup);
		}
		return result;
	}

	private CHEngagementPopup getFakeEngagementPopup(int index) {
		CHEngagementPopup engagementPopup = new CHEngagementPopup();
		engagementPopup.setActive(index == 1);
		engagementPopup.setContent(String.format("Content-%s", index));
		engagementPopup.setControlsColor("black");
		engagementPopup.setControlsShadow("white");
		engagementPopup.setDescription(String.format("Description-%s", index));
		engagementPopup.setId(String.valueOf(index));
		engagementPopup.setLastModified(new Date());
		engagementPopup.setName(String.format("Name-%s", index));
		return engagementPopup;
	}

	private List<CHCategory> getFakeCategories() {
		final List<CHCategory> categories = new ArrayList<>();
		for(int i=0; i<3; i++) {
			final CHCategory category = this.getFakeCategory(i);
			categories.add(category);
		}
		return categories;
	}

	private CHCategory getFakeCategory(final int index) {
		final CHCategory category = new CHCategory();
		category.setDescription(String.format("Description %s", index));
		category.setEnabled(true);
		category.setId(String.valueOf(index));
		category.setImageFileName(String.format("ImageFileName%s", index));
		category.setLastModified(new Date());
		category.setName(String.format("Name%s", index));
		category.setOrderPopular(new Long(index));
		category.setPopular(true);
		return category;
	}
}
