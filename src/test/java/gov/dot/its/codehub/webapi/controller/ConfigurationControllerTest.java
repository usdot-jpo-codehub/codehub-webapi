package gov.dot.its.codehub.webapi.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import gov.dot.its.codehub.webapi.model.ApiError;
import gov.dot.its.codehub.webapi.model.ApiResponse;
import gov.dot.its.codehub.webapi.model.CHCategory;
import gov.dot.its.codehub.webapi.model.CHEngagementPopup;
import gov.dot.its.codehub.webapi.service.ConfigurationService;

@RunWith(SpringRunner.class)
@WebMvcTest(ConfigurationController.class)
@AutoConfigureRestDocs(outputDir = "target/generated-snippets", uriHost="example.com", uriPort=3000, uriScheme="http")
class ConfigurationControllerTest {

	private static final String TEST_CATEGORIES_URL = "%s/v1/configurations/categories";
	private static final String TEST_CATEGORY_URL = "%s/v1/configurations/categories/:ID";
	private static final String SERVER_SERVLET_CONTEXT_PATH = "server.servlet.context-path";
	private static final String HEADER_HOST = "Host";
	private static final String HEADER_CONTENT_LENGTH = "Content-Length";
	private static final String MSG_INTERNAL_SERVER_ERROR = "Internal server error.";
	private static final String TEST_ENGAGEMENTPOPUP_URL = "%s/v1/configurations/engagementpopups";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private Environment env;

	@MockBean
	private ConfigurationService configurationService;

	private Random random;
	public ConfigurationControllerTest() {
		this.random = new Random();
	}

	@Test
	void testCategoriesOk() throws Exception { //NOSONAR
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");

		List<CHCategory> categories = this.generateFakeCategories();

		ApiResponse<List<CHCategory>> apiResponse = new ApiResponse<>();
		apiResponse.setResponse(HttpStatus.OK, categories, null, null, request);

		when(configurationService.categories(any(HttpServletRequest.class))).thenReturn(apiResponse);

		ResultActions resultActions = this.prepareResultActions(TEST_CATEGORIES_URL, "api/v1/configurations/categories/data-all");

		MvcResult result = resultActions.andReturn();
		String objString = result.getResponse().getContentAsString();

		TypeReference<ApiResponse<List<CHCategory>>> valueType = new TypeReference<ApiResponse<List<CHCategory>>>() {};
		ApiResponse<List<CHCategory>> responseApi = objectMapper.readValue(objString, valueType);

		assertEquals(HttpStatus.OK.value(), responseApi.getCode());
		assertTrue(responseApi.getErrors() == null);
		assertTrue(responseApi.getMessages() == null);
		assertTrue(responseApi.getResult() != null);
	}

	@Test
	void testCategoriesNoData() throws Exception { //NOSONAR
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");

		ApiResponse<List<CHCategory>> apiResponse = new ApiResponse<>();
		apiResponse.setResponse(HttpStatus.NO_CONTENT, null, null, null, request);

		when(configurationService.categories(any(HttpServletRequest.class))).thenReturn(apiResponse);

		ResultActions resultActions = this.prepareResultActions(TEST_CATEGORIES_URL, "api/v1/configurations/categories/no-data-all");

		MvcResult result = resultActions.andReturn();
		String objString = result.getResponse().getContentAsString();

		TypeReference<ApiResponse<List<CHCategory>>> valueType = new TypeReference<ApiResponse<List<CHCategory>>>() {};
		ApiResponse<List<CHCategory>> responseApi = objectMapper.readValue(objString, valueType);

		assertEquals(HttpStatus.NO_CONTENT.value(), responseApi.getCode());
		assertTrue(responseApi.getResult() == null);
		assertTrue(responseApi.getErrors() == null);
		assertTrue(responseApi.getMessages() == null);
	}

	@Test
	void testCategoriesError() throws Exception { //NOSONAR
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");

		List<ApiError> errors = new ArrayList<>();
		errors.add(new ApiError(MSG_INTERNAL_SERVER_ERROR));
		ApiResponse<List<CHCategory>> apiResponse = new ApiResponse<>();
		apiResponse.setResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, null, errors, request);

		when(configurationService.categories(any(HttpServletRequest.class))).thenReturn(apiResponse);

		ResultActions resultActions = this.prepareResultActions(TEST_CATEGORIES_URL, "api/v1/configurations/categories/error-all");

		MvcResult result = resultActions.andReturn();
		String objString = result.getResponse().getContentAsString();

		TypeReference<ApiResponse<List<CHCategory>>> valueType = new TypeReference<ApiResponse<List<CHCategory>>>() {};
		ApiResponse<List<CHCategory>> responseApi = objectMapper.readValue(objString, valueType);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseApi.getCode());
		assertTrue(responseApi.getResult() == null);
		assertTrue(responseApi.getMessages() == null);
		assertTrue(responseApi.getErrors() != null);
	}

	@Test
	void testCategoryOk() throws Exception { //NOSONAR
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");

		CHCategory category = this.generateFakeCategory();

		ApiResponse<CHCategory> apiResponse = new ApiResponse<>();
		apiResponse.setResponse(HttpStatus.OK, category, null, null, request);

		when(configurationService.category(any(HttpServletRequest.class), any(String.class))).thenReturn(apiResponse);

		ResultActions resultActions = this.prepareResultActions(TEST_CATEGORY_URL, "api/v1/configurations/categories/data-id");

		MvcResult result = resultActions.andReturn();
		String objString = result.getResponse().getContentAsString();

		TypeReference<ApiResponse<CHCategory>> valueType = new TypeReference<ApiResponse<CHCategory>>() {};
		ApiResponse<CHCategory> responseApi = objectMapper.readValue(objString, valueType);

		assertEquals(HttpStatus.OK.value(), responseApi.getCode());
		assertTrue(responseApi.getResult() != null);
		assertTrue(responseApi.getErrors() == null);
		assertTrue(responseApi.getMessages() == null);
	}

	@Test
	void testCategoryNoData() throws Exception { //NOSONAR
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");

		ApiResponse<CHCategory> apiResponse = new ApiResponse<>();
		apiResponse.setResponse(HttpStatus.NOT_FOUND, null, null, null, request);

		when(configurationService.category(any(HttpServletRequest.class), any(String.class))).thenReturn(apiResponse);

		ResultActions resultActions = this.prepareResultActions(TEST_CATEGORY_URL, "api/v1/configurations/categories/no-data-id");

		MvcResult result = resultActions.andReturn();
		String objString = result.getResponse().getContentAsString();

		TypeReference<ApiResponse<CHCategory>> valueType = new TypeReference<ApiResponse<CHCategory>>() {};
		ApiResponse<CHCategory> responseApi = objectMapper.readValue(objString, valueType);

		assertEquals(HttpStatus.NOT_FOUND.value(), responseApi.getCode());
		assertTrue(responseApi.getResult() == null);
		assertTrue(responseApi.getMessages() == null);
		assertTrue(responseApi.getErrors() == null);
	}

	@Test
	void testCategoryError() throws Exception { //NOSONAR
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");

		ApiResponse<CHCategory> apiResponse = new ApiResponse<>();
		List<ApiError> errors = new ArrayList<>();

		errors.add(new ApiError(MSG_INTERNAL_SERVER_ERROR));
		apiResponse.setResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, null, errors, request);

		when(configurationService.category(any(HttpServletRequest.class), any(String.class))).thenReturn(apiResponse);

		ResultActions resultActions = this.prepareResultActions(TEST_CATEGORY_URL, "api/v1/configurations/categories/error-id");

		MvcResult result = resultActions.andReturn();
		String objString = result.getResponse().getContentAsString();

		TypeReference<ApiResponse<CHCategory>> valueType = new TypeReference<ApiResponse<CHCategory>>() {};
		ApiResponse<CHCategory> responseApi = objectMapper.readValue(objString, valueType);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseApi.getCode());
		assertTrue(responseApi.getErrors() != null);
		assertTrue(responseApi.getResult() == null);
		assertTrue(responseApi.getMessages() == null);
	}

	@Test
	void testEngagementPopupOk() throws Exception { //NOSONAR
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");

		List<CHEngagementPopup> engagementPopup = this.generateFakeEngagementPopups();

		ApiResponse<List<CHEngagementPopup>> apiResponse = new ApiResponse<>();
		apiResponse.setResponse(HttpStatus.OK, engagementPopup, null, null, request);

		when(configurationService.engagementpopups(any(HttpServletRequest.class))).thenReturn(apiResponse);

		ResultActions resultActions = this.prepareResultActions(TEST_ENGAGEMENTPOPUP_URL, "api/v1/configurations/engagementpopups/data-all");

		MvcResult result = resultActions.andReturn();
		String objString = result.getResponse().getContentAsString();

		TypeReference<ApiResponse<List<CHEngagementPopup>>> valueType = new TypeReference<ApiResponse<List<CHEngagementPopup>>>() {};
		ApiResponse<List<CHEngagementPopup>> responseApi = objectMapper.readValue(objString, valueType);

		assertTrue(responseApi.getErrors() == null);
		assertTrue(responseApi.getMessages() == null);
		assertTrue(responseApi.getResult() != null);
		assertEquals(HttpStatus.OK.value(), responseApi.getCode());
	}

	@Test
	void testEngagementPopupNoData() throws Exception { //NOSONAR
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");

		ApiResponse<List<CHEngagementPopup>> apiResponse = new ApiResponse<>();
		apiResponse.setResponse(HttpStatus.NO_CONTENT, null, null, null, request);

		when(configurationService.engagementpopups(any(HttpServletRequest.class))).thenReturn(apiResponse);

		ResultActions resultActions = this.prepareResultActions(TEST_ENGAGEMENTPOPUP_URL, "api/v1/configurations/engagementpopups/no-data-all");

		MvcResult result = resultActions.andReturn();
		String objString = result.getResponse().getContentAsString();

		TypeReference<ApiResponse<List<CHEngagementPopup>>> valueType = new TypeReference<ApiResponse<List<CHEngagementPopup>>>() {};
		ApiResponse<List<CHEngagementPopup>> responseApi = objectMapper.readValue(objString, valueType);

		assertTrue(responseApi.getResult() == null);
		assertTrue(responseApi.getErrors() == null);
		assertTrue(responseApi.getMessages() == null);
		assertEquals(HttpStatus.NO_CONTENT.value(), responseApi.getCode());
	}

	@Test
	void testEngagementPopupError() throws Exception { //NOSONAR
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");

		List<ApiError> errors = new ArrayList<>();
		errors.add(new ApiError(MSG_INTERNAL_SERVER_ERROR));
		ApiResponse<List<CHEngagementPopup>> apiResponse = new ApiResponse<>();
		apiResponse.setResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, null, errors, request);

		when(configurationService.engagementpopups(any(HttpServletRequest.class))).thenReturn(apiResponse);

		ResultActions resultActions = this.prepareResultActions(TEST_ENGAGEMENTPOPUP_URL, "api/v1/configurations/engagementpopups/error-all");

		MvcResult result = resultActions.andReturn();
		String objString = result.getResponse().getContentAsString();

		TypeReference<ApiResponse<List<CHEngagementPopup>>> valueType = new TypeReference<ApiResponse<List<CHEngagementPopup>>>() {};
		ApiResponse<List<CHEngagementPopup>> responseApi = objectMapper.readValue(objString, valueType);

		assertTrue(responseApi.getResult() == null);
		assertTrue(responseApi.getMessages() == null);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseApi.getCode());
		assertTrue(responseApi.getErrors() != null);
	}


	private ResultActions prepareResultActions(String testUrlTemplate, String documentPath) throws Exception { //NOSONAR
		return this.mockMvc.perform(
				get(String.format(testUrlTemplate, env.getProperty(SERVER_SERVLET_CONTEXT_PATH)))
				.contextPath(String.format("%s", env.getProperty(SERVER_SERVLET_CONTEXT_PATH)))
				)
				.andExpect(status().isOk())
				.andDo(document(documentPath,
						Preprocessors.preprocessRequest(
								Preprocessors.prettyPrint(),
								Preprocessors.removeHeaders(HEADER_HOST, HEADER_CONTENT_LENGTH)
								),
						Preprocessors.preprocessResponse(
								Preprocessors.prettyPrint(),
								Preprocessors.removeHeaders(HEADER_HOST, HEADER_CONTENT_LENGTH)
								)
						));
	}

	private List<CHCategory> generateFakeCategories() {
		List<CHCategory> categories = new ArrayList<>();
		for(int i=0; i<2; i++) {
			CHCategory category = generateFakeCategory();
			categories.add(category);
		}
		return categories;
	}

	private List<CHEngagementPopup> generateFakeEngagementPopups() {
		List<CHEngagementPopup> engagementPopups = new ArrayList<>();
		CHEngagementPopup engagementPopup = generateFakeEngagementPopup();
		engagementPopups.add(engagementPopup);

		return engagementPopups;
	}

	private CHCategory generateFakeCategory() {
		CHCategory category = new CHCategory();
		category.setDescription("This is the description");
		category.setId(UUID.randomUUID().toString());
		category.setEnabled(true);
		category.setLastModified(new Date());
		category.setName(String.format("Category-%s", random.nextInt(100)));
		category.setPopular(true);
		category.setOrderPopular(Long.valueOf(random.nextInt(10)));
		category.setImageFileName(String.format("http://url.to.image/image-%s", random.nextInt(10)));

		return category;
	}

	private CHEngagementPopup generateFakeEngagementPopup() {
		CHEngagementPopup engagementPopup = new CHEngagementPopup();
		engagementPopup.setActive(true);
		engagementPopup.setContent("<h1>Content</h1>");
		engagementPopup.setDescription("Description");
		engagementPopup.setId(UUID.randomUUID().toString());
		engagementPopup.setLastModified(new Date());
		engagementPopup.setName("EngagementPopup");

		return engagementPopup;
	}

}
