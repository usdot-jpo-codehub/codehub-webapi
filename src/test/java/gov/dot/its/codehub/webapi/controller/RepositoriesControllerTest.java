package gov.dot.its.codehub.webapi.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.springframework.http.MediaType;
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
import gov.dot.its.codehub.webapi.model.CHAsset;
import gov.dot.its.codehub.webapi.model.CHBadges;
import gov.dot.its.codehub.webapi.model.CHCodehubData;
import gov.dot.its.codehub.webapi.model.CHContributor;
import gov.dot.its.codehub.webapi.model.CHFork;
import gov.dot.its.codehub.webapi.model.CHGeneratedData;
import gov.dot.its.codehub.webapi.model.CHMetrics;
import gov.dot.its.codehub.webapi.model.CHMetricsSummary;
import gov.dot.its.codehub.webapi.model.CHMetricsSummaryBase;
import gov.dot.its.codehub.webapi.model.CHOwner;
import gov.dot.its.codehub.webapi.model.CHReadme;
import gov.dot.its.codehub.webapi.model.CHRelease;
import gov.dot.its.codehub.webapi.model.CHReportedFiles;
import gov.dot.its.codehub.webapi.model.CHRepository;
import gov.dot.its.codehub.webapi.model.CHSearchRequest;
import gov.dot.its.codehub.webapi.model.CHSonarMetric;
import gov.dot.its.codehub.webapi.model.CHSonarMetrics;
import gov.dot.its.codehub.webapi.model.CHSourceData;
import gov.dot.its.codehub.webapi.model.CHVScan;
import gov.dot.its.codehub.webapi.service.RepositoriesService;

@RunWith(SpringRunner.class)
@WebMvcTest(RepositoriesController.class)
@AutoConfigureRestDocs(outputDir = "target/generated-snippets", uriHost="example.com", uriPort=3000, uriScheme="http")
@SuppressWarnings({"squid:S00116","squid:S00100","squid:S00117"})
class RepositoriesControllerTest {

	private static final String TEST_DATAASSETS_URL = "%s/v1/repositories";
	private static final String TEST_METRICS_URL = "%s/v1/metrics";
	private static final String TEST_SEARCH_URL = "%s/v1/search";
	private static final String SERVER_SERVLET_CONTEXT_PATH = "server.servlet.context-path";
	private static final String HEADER_HOST = "Host";
	private static final String HEADER_CONTENT_LENGTH = "Content-Length";
	private static final String MSG_INTERNAL_SERVER_ERROR = "Internal server error.";
	private static final String PROGRAMMING_LANGUAGE = "Javascript";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private Environment env;

	@MockBean
	private RepositoriesService repositoriesService;

	private Random random;
	public RepositoriesControllerTest() {
		this.random = new Random();
	}

	@Test
	void testRepositoriesData() throws Exception { //NOSONAR
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");

		List<CHRepository> repositories = this.generateFakeRepositories();

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
		assertTrue(responseApi.getErrors() == null);
		assertTrue(responseApi.getMessages() == null);
		assertTrue(responseApi.getResult() != null);

	}

	@Test
	void testRepositoriesNoData() throws Exception { //NOSONAR
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
		assertTrue(responseApi.getErrors() == null);
		assertTrue(responseApi.getResult() == null);
		assertTrue(responseApi.getMessages() == null);
	}

	@Test
	void testRepositoriesError() throws Exception { //NOSONAR
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
		assertTrue(responseApi.getResult() == null);
		assertTrue(responseApi.getErrors() != null);
		assertTrue(responseApi.getMessages() == null);
	}

	@Test
	void testMetricsData() throws Exception { //NOSONAR
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");

		CHMetrics metrics = this.getFakeMetrics();

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
		assertTrue(responseApi.getResult() != null);
		assertTrue(responseApi.getErrors() == null);
		assertTrue(responseApi.getMessages() == null);
	}

	@Test
	void testMetricsNoData() throws Exception { //NOSONAR
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
		assertTrue(responseApi.getErrors() == null);
		assertTrue(responseApi.getResult() == null);
		assertTrue(responseApi.getMessages() == null);
	}

	@Test
	void testMetricsError() throws Exception { //NOSONAR
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
		assertTrue(responseApi.getResult() == null);
		assertTrue(responseApi.getMessages() == null);
		assertTrue(responseApi.getErrors() != null);
	}

	@Test
	void testSearchData() throws Exception { //NOSONAR
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("POST");

		List<CHRepository> repositories = this.generateFakeRepositories();

		ApiResponse<List<CHRepository>> apiResponse = new ApiResponse<>();
		apiResponse.setResponse(HttpStatus.OK, repositories, null, null, request);

		when(repositoriesService.search(any(HttpServletRequest.class), any(CHSearchRequest.class))).thenReturn(apiResponse);

		CHSearchRequest chSearchRequest = this.createSearchRequest(1000,"data",false);

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
		assertTrue(responseApi.getResult() != null);
		assertTrue(responseApi.getMessages() == null);
		assertTrue(responseApi.getErrors() == null);
	}

	@Test
	void testSearchNoData() throws Exception { //NOSONAR
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("POST");

		ApiResponse<List<CHRepository>> apiResponse = new ApiResponse<>();
		apiResponse.setResponse(HttpStatus.NO_CONTENT, null, null, null, request);

		when(repositoriesService.search(any(HttpServletRequest.class), any(CHSearchRequest.class))).thenReturn(apiResponse);

		CHSearchRequest chSearchRequest = this.createSearchRequest(1000, "data", false);
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
		assertTrue(responseApi.getResult() == null);
		assertTrue(responseApi.getErrors() == null);
		assertTrue(responseApi.getMessages() == null);
	}

	@Test
	void testSearchError() throws Exception { //NOSONAR
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("POST");

		List<ApiError> errors = new ArrayList<>();
		errors.add(new ApiError(MSG_INTERNAL_SERVER_ERROR));
		ApiResponse<List<CHRepository>> apiResponse = new ApiResponse<>();
		apiResponse.setResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, null, errors, request);

		when(repositoriesService.search(any(HttpServletRequest.class), any(CHSearchRequest.class))).thenReturn(apiResponse);

		CHSearchRequest chSearchRequest = this.createSearchRequest(1000, "data", false);
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
		assertTrue(responseApi.getResult() == null);
		assertTrue(responseApi.getErrors() != null);
		assertTrue(responseApi.getMessages() == null);
	}

	private List<CHRepository> generateFakeRepositories() {
		List<CHRepository> repositories = new ArrayList<>();
		for(int i=1; i<=2; i++) {
			String name = String.format("repository%s", i);
			String owner = String.format("owner%s", i);
			String id = UUID.randomUUID().toString().replace("-", "");

			CHRepository repository = this.generateFakeRepository(id, name, owner);
			repositories.add(repository);
		}
		return repositories;
	}

	private CHRepository generateFakeRepository(String id, String name, String ownerName) {
		CHRepository repository = new CHRepository();
		repository.setId(id);

		CHCodehubData codehubData = this.getFakeCodehubData();
		repository.setCodehubData(codehubData);

		CHGeneratedData generatedData = this.getFakeGeneratedData();
		repository.setGeneratedData(generatedData);

		CHSourceData sourceData = this.getFakeSourceData(name, ownerName);
		repository.setSourceData(sourceData);

		return repository;

	}


	private CHCodehubData getFakeCodehubData() {
		CHCodehubData codehubData = new CHCodehubData();

		CHBadges badges = new CHBadges();
		badges.setFeatured(this.random.nextBoolean());
		badges.setStatus("Active");
		codehubData.setBadges(badges);


		codehubData.setEtag(UUID.randomUUID().toString().replace("-", ""));
		codehubData.setIngested(this.random.nextBoolean());
		codehubData.setIngestionEnabled(random.nextBoolean());
		codehubData.setLastIngested(new Date());
		codehubData.setLastModified(new Date());
		codehubData.setSource("Github");
		codehubData.setVisible(this.random.nextBoolean());

		return codehubData;
	}

	private CHGeneratedData getFakeGeneratedData() {
		CHGeneratedData generatedData = new CHGeneratedData();
		generatedData.setRank(this.random.nextInt(5000));

		CHSonarMetrics sonarMetrics = new CHSonarMetrics();
		CHSonarMetric bugs = new CHSonarMetric();
		long numberBugs = this.random.nextInt(1000);
		bugs.setFrmt_val(String.valueOf(numberBugs));
		bugs.setKey("bugs");
		bugs.setVal(numberBugs);
		sonarMetrics.setBugs(bugs);

		CHSonarMetric code_smells = new CHSonarMetric();
		long codeSmells = this.random.nextInt(1000);
		code_smells.setFrmt_val(String.valueOf(codeSmells));
		code_smells.setKey("code_smells");
		code_smells.setVal(codeSmells);
		sonarMetrics.setCode_smells(code_smells);

		CHSonarMetric complexity = new CHSonarMetric();
		long complexityNumber = this.random.nextInt(100);
		complexity.setFrmt_val(String.valueOf(complexityNumber));
		complexity.setKey("complexity");
		complexity.setVal(complexityNumber);
		sonarMetrics.setComplexity(complexity);

		CHSonarMetric reliability_rating = new CHSonarMetric();
		long reliabilityNumber = (long)this.random.nextInt(5) + 1;
		char ch = (char)(64 + reliabilityNumber);
		reliability_rating.setFrmt_val(String.valueOf(ch));
		reliability_rating.setKey("reliability_rating");
		reliability_rating.setVal(reliabilityNumber);
		sonarMetrics.setReliability_rating(reliability_rating);

		CHSonarMetric security_rating = new CHSonarMetric();
		long securityNumber = (long)this.random.nextInt(5) + 1;
		char chSecurity = (char)(64 + securityNumber);
		security_rating.setFrmt_val(String.valueOf(chSecurity));
		security_rating.setKey("security_rating");
		security_rating.setVal(securityNumber);
		sonarMetrics.setSecurity_rating(security_rating);

		CHSonarMetric sqale_index = new CHSonarMetric();
		float sqaleIndex = (float)this.random.nextInt(5) + 1;
		char chSqaleIndex = (char)(64 + sqaleIndex);
		sqale_index.setFrmt_val(String.valueOf(chSqaleIndex));
		sqale_index.setKey("sqale_index");
		sqale_index.setVal(sqaleIndex*20);
		sonarMetrics.setSqale_index(sqale_index);

		CHSonarMetric sqale_rating = new CHSonarMetric();
		long sqaleRating = (long)this.random.nextInt(5) + 1;
		char chSqaleRating = (char)(64 + sqaleRating);
		sqale_rating.setFrmt_val(String.valueOf(chSqaleRating));
		sqale_rating.setKey("sqale_rating");
		sqale_rating.setVal(chSqaleRating);
		sonarMetrics.setSqale_rating(sqale_rating);

		CHSonarMetric violations = new CHSonarMetric();
		long violationsNumber = this.random.nextInt(1000);
		violations.setFrmt_val(String.valueOf(violationsNumber));
		violations.setKey("violations");
		violations.setVal(violationsNumber);
		sonarMetrics.setViolations(violations);

		CHSonarMetric vulnerabilities = new CHSonarMetric();
		long vulnerabilitiesNumber = this.random.nextInt(800);
		vulnerabilities.setFrmt_val(String.valueOf(vulnerabilitiesNumber));
		vulnerabilities.setKey("vulnerabilities");
		vulnerabilities.setVal(vulnerabilitiesNumber);
		sonarMetrics.setVulnerabilities(vulnerabilities);

		generatedData.setSonarMetrics(sonarMetrics);

		CHVScan vscan = new CHVScan();
		int size = this.random.nextInt(100);
		vscan.setData_scanned(String.format("%s MB", size));
		vscan.setInfected_files(1);
		vscan.setLastScan(new Date());
		List<CHReportedFiles> reported_files = new ArrayList<>();
		CHReportedFiles reportedFiles = new CHReportedFiles();
		reportedFiles.setFilename(String.format("filename%s", this.random.nextInt(1000)));
		reportedFiles.setVirus(String.format("WORM%s", this.random.nextInt(1000)));
		reported_files.add(reportedFiles);
		vscan.setReported_files(reported_files);
		vscan.setScanned_directories(this.random.nextInt(100));
		vscan.setScanned_files(this.random.nextInt(1000));
		vscan.setTime(String.format("%s sec", this.random.nextInt(360)));
		generatedData.setVscan(vscan);

		return generatedData;
	}

	private CHSourceData getFakeSourceData(String name, String ownerName) {
		CHSourceData sourceData = new CHSourceData();
		sourceData.setCommits(this.random.nextInt(3000));

		List<CHContributor> contributors = new ArrayList<>();
		CHContributor contributor = new CHContributor();
		int contributorId = this.random.nextInt(20);
		contributor.setAvatarUrl(String.format("http://contributor%s.avatar.url",contributorId));
		contributor.setProfileUrl(String.format("http://contributor%s.profile.url",contributorId));
		contributor.setUsername(String.format("contributor%s",contributorId));
		contributor.setUserType("User");
		contributors.add(contributor);
		sourceData.setContributors(contributors);

		sourceData.setCreatedAt(new Date());
		sourceData.setDescription("Description of the repository");

		List<CHFork> forks = new ArrayList<>();
		CHFork fork = new CHFork();
		int forkId = this.random.nextInt(11);
		fork.setId(String.valueOf(forkId));
		fork.setName(String.format("%s%s", name, forkId));
		fork.setOwner("fork-owner");
		forks.add(fork);
		sourceData.setForks(forks);

		sourceData.setLanguage(PROGRAMMING_LANGUAGE);
		Map<String, Long> languages = new HashMap<>();
		languages.put(PROGRAMMING_LANGUAGE, (long)this.random.nextInt(1000));
		languages.put("HTML", (long)this.random.nextInt(500));
		languages.put("CSS", (long)this.random.nextInt(300));
		sourceData.setLanguages(languages);

		sourceData.setLastPush(new Date());
		sourceData.setName(name);
		CHOwner owner = new CHOwner();
		owner.setAvatarUrl(String.format("http://%s.avatar.url",ownerName));
		owner.setName(ownerName);
		owner.setType("Organization");
		owner.setUrl(String.format("http://%s.url", ownerName));
		sourceData.setOwner(owner);

		CHReadme readme = new CHReadme();
		readme.setContent("[README Content]");
		readme.setUrl(String.format("http://%s/readme.url", name));
		sourceData.setReadme(readme);

		List<CHRelease> releases = new ArrayList<>();
		CHRelease release = new CHRelease();
		int releaseId = this.random.nextInt(20);
		release.setId(String.valueOf(releaseId));
		release.setName(String.format("Rel%s", releaseId));
		release.setTagName(String.format("Tag%s", releaseId));
		long downloads = this.random.nextInt(3000);
		release.setTotalDownloads(downloads);
		List<CHAsset> assets = new ArrayList<>();
		CHAsset asset = new CHAsset();
		asset.setDownloadCount(downloads);
		int assetId = this.random.nextInt(20);
		asset.setId(String.format("Asset%s", assetId));
		asset.setLabel(String.format("Label%s", assetId));
		asset.setName(String.format("Asset%s", assetId));
		asset.setSize(this.random.nextInt(200000000));
		assets.add(asset);
		release.setAssets(assets);
		releases.add(release);
		sourceData.setReleases(releases);

		sourceData.setRepositoryUrl(String.format("http://%s/%s.url", ownerName, name));
		sourceData.setStars(this.random.nextInt(5000));
		sourceData.setWatchers(this.random.nextInt(1000));
		return sourceData;
	}

	private CHMetrics getFakeMetrics() {
		CHMetrics metrics = new CHMetrics();
		metrics.setBugsVulnerabilities((long)this.random.nextInt(10000));

		Map<String, Integer> languageCountStat = new HashMap<>();
		languageCountStat.put("Java", 5);
		languageCountStat.put(PROGRAMMING_LANGUAGE, 3);
		languageCountStat.put("Python", 2);
		metrics.setLanguageCountsStat(languageCountStat);

		Map<String, Float> languagePercentageStat = new HashMap<>();
		languagePercentageStat.put("Java", 50.0F);
		languagePercentageStat.put(PROGRAMMING_LANGUAGE, 30.0F);
		languagePercentageStat.put("Python", 20F);
		metrics.setLanguagePercentageStat(languagePercentageStat);


		CHMetricsSummary metricsSummary = new CHMetricsSummary();
		CHMetricsSummaryBase<Long> maintainability = new CHMetricsSummaryBase<>();
		maintainability.setKeyValue("A", 3L);
		maintainability.setKeyValue("B", 2L);
		maintainability.setKeyValue("C", 2L);
		maintainability.setKeyValue("D", 2L);
		maintainability.setKeyValue("E", 1L);
		metricsSummary.setMaintainability(maintainability);

		CHMetricsSummaryBase<Long> releasibility = new CHMetricsSummaryBase<>();
		releasibility.setKeyValue("OK", 8L);
		releasibility.setKeyValue("WARN", 2L);
		releasibility.setKeyValue("ERROR", 0L);
		metricsSummary.setReleasibility(releasibility);

		CHMetricsSummaryBase<Long> reliability = new CHMetricsSummaryBase<>();
		reliability.setKeyValue("A", 5L);
		reliability.setKeyValue("B", 2L);
		reliability.setKeyValue("C", 3L);
		reliability.setKeyValue("D", 0L);
		reliability.setKeyValue("E", 0L);
		metricsSummary.setReliability(reliability);

		CHMetricsSummaryBase<Long> security = new CHMetricsSummaryBase<>();
		security.setKeyValue("A", 8L);
		security.setKeyValue("B", 2L);
		security.setKeyValue("C", 0L);
		security.setKeyValue("D", 0L);
		security.setKeyValue("E", 0L);
		metricsSummary.setSecurity(security);

		metrics.setMetricsSummary(metricsSummary);

		metrics.setNumberOfOrganizations(2);
		metrics.setNumberOfProjects(10);
		List<String> organizations = new ArrayList<>();
		organizations.add("Organization1");
		organizations.add("Organization2");
		metrics.setOrganizations(organizations);
		metrics.setTechnicalDebt((long)this.random.nextInt(5000));

		return metrics;
	}

	private CHSearchRequest createSearchRequest(int limit, String term, boolean matchAll ) {
		CHSearchRequest chSearchRequest = new CHSearchRequest();
		chSearchRequest.setLimit(limit);
		chSearchRequest.setTerm(term);
		chSearchRequest.setMatchAll(matchAll);
		return chSearchRequest;
	}
}
