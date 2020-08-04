package gov.dot.its.codehub.webapi.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import gov.dot.its.codehub.webapi.MockDataRepositories;
import gov.dot.its.codehub.webapi.model.ApiError;
import gov.dot.its.codehub.webapi.model.ApiResponse;
import gov.dot.its.codehub.webapi.model.CHMetrics;
import gov.dot.its.codehub.webapi.model.CHRepository;
import gov.dot.its.codehub.webapi.model.CHSearchRequest;
import gov.dot.its.codehub.webapi.service.RepositoriesService;

@RunWith(SpringRunner.class)
@WebMvcTest(RepositoriesController.class)
@AutoConfigureRestDocs(outputDir = "target/generated-snippets", uriHost="example.com", uriPort=3000, uriScheme="http")
@SuppressWarnings({"squid:S00116","squid:S00100","squid:S00117"})
public class RepositoriesControllerTest {

	private static final String TEST_DATAASSETS_URL = "%s/v1/repositories";
	private static final String TEST_METRICS_URL = "%s/v1/metrics";
	private static final String TEST_SEARCH_URL = "%s/v1/search";
	private static final String SERVER_SERVLET_CONTEXT_PATH = "server.servlet.context-path";
	private static final String HEADER_HOST = "Host";
	private static final String HEADER_CONTENT_LENGTH = "Content-Length";
	private static final String MSG_INTERNAL_SERVER_ERROR = "Internal server error.";

	private MockDataRepositories mockData;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private Environment env;

	@MockBean
	private RepositoriesService repositoriesService;

	public RepositoriesControllerTest() {
		this.mockData = new MockDataRepositories();
	}

	@Test
	public void testRepositoriesData() throws Exception { //NOSONAR
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");

		List<CHRepository> repositories = this.mockData.generateFakeRepositories();

		ApiResponse<List<CHRepository>> apiResponse = new ApiResponse<>();
		apiResponse.setResponse(HttpStatus.OK, repositories, null, null, request);

		when(repositoriesService.getRepositories(any(HttpServletRequest.class), anyMap())).thenReturn(apiResponse);

		ResultActions resultActions = this.mockMvc.perform(
				get(String.format(TEST_DATAASSETS_URL, env.getProperty(SERVER_SERVLET_CONTEXT_PATH)))
				.contextPath(String.format("%s", env.getProperty(SERVER_SERVLET_CONTEXT_PATH)))
				)
				.andExpect(status().isOk())
				.andDo(document("api/v1/repositories/data",
						Preprocessors.preprocessRequest(
								Preprocessors.prettyPrint(),
								Preprocessors.removeHeaders(HEADER_HOST, HEADER_CONTENT_LENGTH)
								),
						Preprocessors.preprocessResponse(
								Preprocessors.prettyPrint(),
								Preprocessors.removeHeaders(HEADER_HOST, HEADER_CONTENT_LENGTH)
								)
						));

		MvcResult result = resultActions.andReturn();
		String objString = result.getResponse().getContentAsString();

		TypeReference<ApiResponse<List<CHRepository>>> valueType = new TypeReference<ApiResponse<List<CHRepository>>>() {};
		ApiResponse<List<CHRepository>> responseApi = objectMapper.readValue(objString, valueType);

		assertEquals(HttpStatus.OK.value(), responseApi.getCode());
		assertNull(responseApi.getErrors());
		assertNull(responseApi.getMessages());
		assertNotNull(responseApi.getResult());

	}

	@Test
	public void testRepositoriesNoData() throws Exception { //NOSONAR
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");

		ApiResponse<List<CHRepository>> apiResponse = new ApiResponse<>();
		apiResponse.setResponse(HttpStatus.NO_CONTENT, null, null, null, request);

		when(repositoriesService.getRepositories(any(HttpServletRequest.class), anyMap())).thenReturn(apiResponse);

		ResultActions resultActions = this.mockMvc.perform(
				get(String.format(TEST_DATAASSETS_URL, env.getProperty(SERVER_SERVLET_CONTEXT_PATH)))
				.contextPath(String.format("%s", env.getProperty(SERVER_SERVLET_CONTEXT_PATH)))
				)
				.andExpect(status().isOk())
				.andDo(document("api/v1/repositories/no-data",
						Preprocessors.preprocessRequest(
								Preprocessors.prettyPrint(),
								Preprocessors.removeHeaders(HEADER_HOST, HEADER_CONTENT_LENGTH)
								),
						Preprocessors.preprocessResponse(
								Preprocessors.prettyPrint(),
								Preprocessors.removeHeaders(HEADER_HOST, HEADER_CONTENT_LENGTH)
								)
						));

		MvcResult result = resultActions.andReturn();
		String objString = result.getResponse().getContentAsString();

		TypeReference<ApiResponse<List<CHRepository>>> valueType = new TypeReference<ApiResponse<List<CHRepository>>>() {};
		ApiResponse<List<CHRepository>> responseApi = objectMapper.readValue(objString, valueType);

		assertEquals(HttpStatus.NO_CONTENT.value(), responseApi.getCode());
		assertNull(responseApi.getErrors());
		assertNull(responseApi.getResult());
		assertNull(responseApi.getMessages());
	}

	@Test
	public void testRepositoriesError() throws Exception { //NOSONAR
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");

		List<ApiError> errors = new ArrayList<>();
		errors.add(new ApiError(MSG_INTERNAL_SERVER_ERROR));
		ApiResponse<List<CHRepository>> apiResponse = new ApiResponse<>();
		apiResponse.setResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, null, errors, request);

		when(repositoriesService.getRepositories(any(HttpServletRequest.class), anyMap())).thenReturn(apiResponse);

		ResultActions resultActions = this.mockMvc.perform(
				get(String.format(TEST_DATAASSETS_URL, env.getProperty(SERVER_SERVLET_CONTEXT_PATH)))
				.contextPath(String.format("%s", env.getProperty(SERVER_SERVLET_CONTEXT_PATH)))
				)
				.andExpect(status().isOk())
				.andDo(document("api/v1/repositories/error",
						Preprocessors.preprocessRequest(
								Preprocessors.prettyPrint(),
								Preprocessors.removeHeaders(HEADER_HOST, HEADER_CONTENT_LENGTH)
								),
						Preprocessors.preprocessResponse(
								Preprocessors.prettyPrint(),
								Preprocessors.removeHeaders(HEADER_HOST, HEADER_CONTENT_LENGTH)
								)
						));

		MvcResult result = resultActions.andReturn();
		String objString = result.getResponse().getContentAsString();

		TypeReference<ApiResponse<List<CHRepository>>> valueType = new TypeReference<ApiResponse<List<CHRepository>>>() {};
		ApiResponse<List<CHRepository>> responseApi = objectMapper.readValue(objString, valueType);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseApi.getCode());
		assertNull(responseApi.getResult());
		assertNotNull(responseApi.getErrors());
		assertNull(responseApi.getMessages());
	}

	@Test
	public void testRepositoriesByIdData() throws Exception { //NOSONAR
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");

		List<CHRepository> repositories = this.mockData.generateFakeRepositories();

		ApiResponse<List<CHRepository>> apiResponse = new ApiResponse<>();
		apiResponse.setResponse(HttpStatus.OK, repositories, null, null, request);

		when(repositoriesService.getRepositoriesByIds(any(HttpServletRequest.class), any(String[].class))).thenReturn(apiResponse);

		ResultActions resultActions = this.mockMvc.perform(
				get(String.format(TEST_DATAASSETS_URL+"/id", env.getProperty(SERVER_SERVLET_CONTEXT_PATH)))
				.contextPath(String.format("%s", env.getProperty(SERVER_SERVLET_CONTEXT_PATH)))
				)
				.andExpect(status().isOk())
				.andDo(document("api/v1/repositories/byId/data",
						Preprocessors.preprocessRequest(
								Preprocessors.prettyPrint(),
								Preprocessors.removeHeaders(HEADER_HOST, HEADER_CONTENT_LENGTH)
								),
						Preprocessors.preprocessResponse(
								Preprocessors.prettyPrint(),
								Preprocessors.removeHeaders(HEADER_HOST, HEADER_CONTENT_LENGTH)
								)
						));

		MvcResult result = resultActions.andReturn();
		String objString = result.getResponse().getContentAsString();

		TypeReference<ApiResponse<List<CHRepository>>> valueType = new TypeReference<ApiResponse<List<CHRepository>>>() {};
		ApiResponse<List<CHRepository>> responseApi = objectMapper.readValue(objString, valueType);

		assertEquals(HttpStatus.OK.value(), responseApi.getCode());
		assertNull(responseApi.getErrors());
		assertNull(responseApi.getMessages());
		assertNotNull(responseApi.getResult());

	}

	@Test
	public void testRepositoriesByIdNoIds() throws Exception { //NOSONAR
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");

		List<CHRepository> repositories = this.mockData.generateFakeRepositories();

		ApiResponse<List<CHRepository>> apiResponse = new ApiResponse<>();
		apiResponse.setResponse(HttpStatus.NO_CONTENT, repositories, null, null, request);

		when(repositoriesService.getRepositoriesByIds(any(HttpServletRequest.class), any(String[].class))).thenReturn(apiResponse);

		ResultActions resultActions = this.mockMvc.perform(
				get(String.format(TEST_DATAASSETS_URL+"/ ", env.getProperty(SERVER_SERVLET_CONTEXT_PATH)))
				.contextPath(String.format("%s", env.getProperty(SERVER_SERVLET_CONTEXT_PATH)))
				)
				.andExpect(status().isOk())
				.andDo(document("api/v1/repositories/byId/noids",
						Preprocessors.preprocessRequest(
								Preprocessors.prettyPrint(),
								Preprocessors.removeHeaders(HEADER_HOST, HEADER_CONTENT_LENGTH)
								),
						Preprocessors.preprocessResponse(
								Preprocessors.prettyPrint(),
								Preprocessors.removeHeaders(HEADER_HOST, HEADER_CONTENT_LENGTH)
								)
						));

		MvcResult result = resultActions.andReturn();
		String objString = result.getResponse().getContentAsString();

		TypeReference<ApiResponse<List<CHRepository>>> valueType = new TypeReference<ApiResponse<List<CHRepository>>>() {};
		ApiResponse<List<CHRepository>> responseApi = objectMapper.readValue(objString, valueType);

		assertEquals(HttpStatus.NO_CONTENT.value(), responseApi.getCode());
		assertNull(responseApi.getErrors());
		assertNull(responseApi.getMessages());
		assertNotNull(responseApi.getResult());

	}

	@Test
	public void testMetricsData() throws Exception { //NOSONAR
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");

		CHMetrics metrics = this.mockData.getFakeMetrics();

		ApiResponse<CHMetrics> apiResponse = new ApiResponse<>();
		apiResponse.setResponse(HttpStatus.OK, metrics, null, null, request);

		when(repositoriesService.getMetrics(any(HttpServletRequest.class), any())).thenReturn(apiResponse);

		ResultActions resultActions = this.mockMvc.perform(
				get(String.format(TEST_METRICS_URL, env.getProperty(SERVER_SERVLET_CONTEXT_PATH)))
				.contextPath(String.format("%s", env.getProperty(SERVER_SERVLET_CONTEXT_PATH)))
				)
				.andExpect(status().isOk())
				.andDo(document("api/v1/metrics/data",
						Preprocessors.preprocessRequest(
								Preprocessors.prettyPrint(),
								Preprocessors.removeHeaders(HEADER_HOST, HEADER_CONTENT_LENGTH)
								),
						Preprocessors.preprocessResponse(
								Preprocessors.prettyPrint(),
								Preprocessors.removeHeaders(HEADER_HOST, HEADER_CONTENT_LENGTH)
								)
						));

		MvcResult result = resultActions.andReturn();
		String objString = result.getResponse().getContentAsString();

		TypeReference<ApiResponse<CHMetrics>> valueType = new TypeReference<ApiResponse<CHMetrics>>() {};
		ApiResponse<CHMetrics> responseApi = objectMapper.readValue(objString, valueType);

		assertEquals(HttpStatus.OK.value(), responseApi.getCode());
		assertNotNull(responseApi.getResult());
		assertNull(responseApi.getErrors());
		assertNull(responseApi.getMessages());
	}

	@Test
	public void testMetricsNoData() throws Exception { //NOSONAR
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");

		ApiResponse<CHMetrics> apiResponse = new ApiResponse<>();
		apiResponse.setResponse(HttpStatus.NO_CONTENT, null, null, null, request);

		when(repositoriesService.getMetrics(any(HttpServletRequest.class), any())).thenReturn(apiResponse);

		ResultActions resultActions = this.mockMvc.perform(
				get(String.format(TEST_METRICS_URL, env.getProperty(SERVER_SERVLET_CONTEXT_PATH)))
				.contextPath(String.format("%s", env.getProperty(SERVER_SERVLET_CONTEXT_PATH)))
				)
				.andExpect(status().isOk())
				.andDo(document("api/v1/metrics/no-data",
						Preprocessors.preprocessRequest(
								Preprocessors.prettyPrint(),
								Preprocessors.removeHeaders(HEADER_HOST, HEADER_CONTENT_LENGTH)
								),
						Preprocessors.preprocessResponse(
								Preprocessors.prettyPrint(),
								Preprocessors.removeHeaders(HEADER_HOST, HEADER_CONTENT_LENGTH)
								)
						));

		MvcResult result = resultActions.andReturn();
		String objString = result.getResponse().getContentAsString();

		TypeReference<ApiResponse<CHMetrics>> valueType = new TypeReference<ApiResponse<CHMetrics>>() {};
		ApiResponse<CHMetrics> responseApi = objectMapper.readValue(objString, valueType);

		assertEquals(HttpStatus.NO_CONTENT.value(), responseApi.getCode());
		assertNull(responseApi.getErrors());
		assertNull(responseApi.getResult());
		assertNull(responseApi.getMessages());
	}

	@Test
	public void testMetricsError() throws Exception { //NOSONAR
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");

		List<ApiError> errors = new ArrayList<>();
		errors.add(new ApiError(MSG_INTERNAL_SERVER_ERROR));
		ApiResponse<CHMetrics> apiResponse = new ApiResponse<>();
		apiResponse.setResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, null, errors, request);

		when(repositoriesService.getMetrics(any(HttpServletRequest.class), any())).thenReturn(apiResponse);

		ResultActions resultActions = this.mockMvc.perform(
				get(String.format(TEST_METRICS_URL, env.getProperty(SERVER_SERVLET_CONTEXT_PATH)))
				.contextPath(String.format("%s", env.getProperty(SERVER_SERVLET_CONTEXT_PATH)))
				)
				.andExpect(status().isOk())
				.andDo(document("api/v1/metrics/error",
						Preprocessors.preprocessRequest(
								Preprocessors.prettyPrint(),
								Preprocessors.removeHeaders(HEADER_HOST, HEADER_CONTENT_LENGTH)
								),
						Preprocessors.preprocessResponse(
								Preprocessors.prettyPrint(),
								Preprocessors.removeHeaders(HEADER_HOST, HEADER_CONTENT_LENGTH)
								)
						));

		MvcResult result = resultActions.andReturn();
		String objString = result.getResponse().getContentAsString();

		TypeReference<ApiResponse<CHMetrics>> valueType = new TypeReference<ApiResponse<CHMetrics>>() {};
		ApiResponse<CHMetrics> responseApi = objectMapper.readValue(objString, valueType);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseApi.getCode());
		assertNull(responseApi.getResult());
		assertNull(responseApi.getMessages());
		assertNotNull(responseApi.getErrors());
	}

	@Test
	public void testMetricsByOwnersData() throws Exception { //NOSONAR
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");

		CHMetrics metrics = this.mockData.getFakeMetrics();

		ApiResponse<CHMetrics> apiResponse = new ApiResponse<>();
		apiResponse.setResponse(HttpStatus.OK, metrics, null, null, request);

		when(repositoriesService.getMetrics(any(HttpServletRequest.class), any())).thenReturn(apiResponse);

		ResultActions resultActions = this.mockMvc.perform(
				get(String.format(TEST_METRICS_URL + "/owner", env.getProperty(SERVER_SERVLET_CONTEXT_PATH)))
				.contextPath(String.format("%s", env.getProperty(SERVER_SERVLET_CONTEXT_PATH)))
				)
				.andExpect(status().isOk())
				.andDo(document("api/v1/metrics/byOwner/data",
						Preprocessors.preprocessRequest(
								Preprocessors.prettyPrint(),
								Preprocessors.removeHeaders(HEADER_HOST, HEADER_CONTENT_LENGTH)
								),
						Preprocessors.preprocessResponse(
								Preprocessors.prettyPrint(),
								Preprocessors.removeHeaders(HEADER_HOST, HEADER_CONTENT_LENGTH)
								)
						));

		MvcResult result = resultActions.andReturn();
		String objString = result.getResponse().getContentAsString();

		TypeReference<ApiResponse<CHMetrics>> valueType = new TypeReference<ApiResponse<CHMetrics>>() {};
		ApiResponse<CHMetrics> responseApi = objectMapper.readValue(objString, valueType);

		assertEquals(HttpStatus.OK.value(), responseApi.getCode());
		assertNotNull(responseApi.getResult());
		assertNull(responseApi.getErrors());
		assertNull(responseApi.getMessages());
	}

	@Test
	public void testMetricsByOwnersNoData() throws Exception { //NOSONAR
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");

		ApiResponse<CHMetrics> apiResponse = new ApiResponse<>();
		apiResponse.setResponse(HttpStatus.NO_CONTENT, null, null, null, request);

		when(repositoriesService.getMetrics(any(HttpServletRequest.class), any())).thenReturn(apiResponse);

		ResultActions resultActions = this.mockMvc.perform(
				get(String.format(TEST_METRICS_URL + "/owner", env.getProperty(SERVER_SERVLET_CONTEXT_PATH)))
				.contextPath(String.format("%s", env.getProperty(SERVER_SERVLET_CONTEXT_PATH)))
				)
				.andExpect(status().isOk())
				.andDo(document("api/v1/metrics/byOwner/noData",
						Preprocessors.preprocessRequest(
								Preprocessors.prettyPrint(),
								Preprocessors.removeHeaders(HEADER_HOST, HEADER_CONTENT_LENGTH)
								),
						Preprocessors.preprocessResponse(
								Preprocessors.prettyPrint(),
								Preprocessors.removeHeaders(HEADER_HOST, HEADER_CONTENT_LENGTH)
								)
						));

		MvcResult result = resultActions.andReturn();
		String objString = result.getResponse().getContentAsString();

		TypeReference<ApiResponse<CHMetrics>> valueType = new TypeReference<ApiResponse<CHMetrics>>() {};
		ApiResponse<CHMetrics> responseApi = objectMapper.readValue(objString, valueType);

		assertEquals(HttpStatus.NO_CONTENT.value(), responseApi.getCode());
		assertNull(responseApi.getResult());
		assertNull(responseApi.getErrors());
		assertNull(responseApi.getMessages());
	}

	@Test
	public void testSearchData() throws Exception { //NOSONAR
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("POST");

		List<CHRepository> repositories = this.mockData.generateFakeRepositories();

		ApiResponse<List<CHRepository>> apiResponse = new ApiResponse<>();
		apiResponse.setResponse(HttpStatus.OK, repositories, null, null, request);

		when(repositoriesService.search(any(HttpServletRequest.class), any(CHSearchRequest.class))).thenReturn(apiResponse);

		CHSearchRequest chSearchRequest = this.mockData.createSearchRequest(1000,"data",false);

		String chSearchRequestStr = objectMapper.writeValueAsString(chSearchRequest);

		ResultActions resultActions = this.mockMvc.perform(
				post(String.format(TEST_SEARCH_URL, env.getProperty(SERVER_SERVLET_CONTEXT_PATH)))
				.contentType(MediaType.APPLICATION_JSON)
				.contextPath(String.format("%s", env.getProperty(SERVER_SERVLET_CONTEXT_PATH)))
				.content(chSearchRequestStr)
				)
				.andExpect(status().isOk())
				.andDo(document("api/v1/search/data",
						Preprocessors.preprocessRequest(
								Preprocessors.prettyPrint(),
								Preprocessors.removeHeaders(HEADER_HOST, HEADER_CONTENT_LENGTH)
								),
						Preprocessors.preprocessResponse(
								Preprocessors.prettyPrint(),
								Preprocessors.removeHeaders(HEADER_HOST, HEADER_CONTENT_LENGTH)
								)
						));

		MvcResult result = resultActions.andReturn();
		String objString = result.getResponse().getContentAsString();

		TypeReference<ApiResponse<List<CHRepository>>> valueType = new TypeReference<ApiResponse<List<CHRepository>>>() {};
		ApiResponse<List<CHRepository>> responseApi = objectMapper.readValue(objString, valueType);

		assertEquals(HttpStatus.OK.value(), responseApi.getCode());
		assertNotNull(responseApi.getResult());
		assertNull(responseApi.getMessages());
		assertNull(responseApi.getErrors());
	}

	@Test
	public void testSearchNoData() throws Exception { //NOSONAR
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("POST");

		ApiResponse<List<CHRepository>> apiResponse = new ApiResponse<>();
		apiResponse.setResponse(HttpStatus.NO_CONTENT, null, null, null, request);

		when(repositoriesService.search(any(HttpServletRequest.class), any(CHSearchRequest.class))).thenReturn(apiResponse);

		CHSearchRequest chSearchRequest = this.mockData.createSearchRequest(1000, "data", false);
		String chSearchRequestStr = objectMapper.writeValueAsString(chSearchRequest);

		ResultActions resultActions = this.mockMvc.perform(
				post(String.format(TEST_SEARCH_URL, env.getProperty(SERVER_SERVLET_CONTEXT_PATH)))
				.contentType(MediaType.APPLICATION_JSON)
				.contextPath(String.format("%s", env.getProperty(SERVER_SERVLET_CONTEXT_PATH)))
				.content(chSearchRequestStr)
				)
				.andExpect(status().isOk())
				.andDo(document("api/v1/search/no-data",
						Preprocessors.preprocessRequest(
								Preprocessors.prettyPrint(),
								Preprocessors.removeHeaders(HEADER_HOST, HEADER_CONTENT_LENGTH)
								),
						Preprocessors.preprocessResponse(
								Preprocessors.prettyPrint(),
								Preprocessors.removeHeaders(HEADER_HOST, HEADER_CONTENT_LENGTH)
								)
						));

		MvcResult result = resultActions.andReturn();
		String objString = result.getResponse().getContentAsString();

		TypeReference<ApiResponse<List<CHRepository>>> valueType = new TypeReference<ApiResponse<List<CHRepository>>>() {};
		ApiResponse<List<CHRepository>> responseApi = objectMapper.readValue(objString, valueType);

		assertEquals(HttpStatus.NO_CONTENT.value(), responseApi.getCode());
		assertNull(responseApi.getResult());
		assertNull(responseApi.getErrors());
		assertNull(responseApi.getMessages());
	}

	@Test
	public void testSearchError() throws Exception { //NOSONAR
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("POST");

		List<ApiError> errors = new ArrayList<>();
		errors.add(new ApiError(MSG_INTERNAL_SERVER_ERROR));
		ApiResponse<List<CHRepository>> apiResponse = new ApiResponse<>();
		apiResponse.setResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, null, errors, request);

		when(repositoriesService.search(any(HttpServletRequest.class), any(CHSearchRequest.class))).thenReturn(apiResponse);

		CHSearchRequest chSearchRequest = this.mockData.createSearchRequest(1000, "data", false);
		String chSearchRequestStr = objectMapper.writeValueAsString(chSearchRequest);

		ResultActions resultActions = this.mockMvc.perform(
				post(String.format(TEST_SEARCH_URL, env.getProperty(SERVER_SERVLET_CONTEXT_PATH)))
				.contentType(MediaType.APPLICATION_JSON)
				.contextPath(String.format("%s", env.getProperty(SERVER_SERVLET_CONTEXT_PATH)))
				.content(chSearchRequestStr)
				)
				.andExpect(status().isOk())
				.andDo(document("api/v1/search/error",
						Preprocessors.preprocessRequest(
								Preprocessors.prettyPrint(),
								Preprocessors.removeHeaders(HEADER_HOST, HEADER_CONTENT_LENGTH)
								),
						Preprocessors.preprocessResponse(
								Preprocessors.prettyPrint(),
								Preprocessors.removeHeaders(HEADER_HOST, HEADER_CONTENT_LENGTH)
								)
						));

		MvcResult result = resultActions.andReturn();
		String objString = result.getResponse().getContentAsString();

		TypeReference<ApiResponse<List<CHRepository>>> valueType = new TypeReference<ApiResponse<List<CHRepository>>>() {};
		ApiResponse<List<CHRepository>> responseApi = objectMapper.readValue(objString, valueType);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseApi.getCode());
		assertNull(responseApi.getResult());
		assertNotNull(responseApi.getErrors());
		assertNull(responseApi.getMessages());
	}
}
