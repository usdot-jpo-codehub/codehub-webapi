package gov.dot.its.codehub.webapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;

import gov.dot.its.codehub.webapi.MockDataRepositories;
import gov.dot.its.codehub.webapi.dao.RelatedDao;
import gov.dot.its.codehub.webapi.dao.RepositoriesDao;
import gov.dot.its.codehub.webapi.model.ApiError;
import gov.dot.its.codehub.webapi.model.ApiResponse;
import gov.dot.its.codehub.webapi.model.CHMetrics;
import gov.dot.its.codehub.webapi.model.CHRepository;
import gov.dot.its.codehub.webapi.model.CHSearchRequest;
import gov.dot.its.codehub.webapi.model.RelatedItemModel;
import gov.dot.its.codehub.webapi.utils.ApiUtils;

@RunWith(SpringRunner.class)
public class RepositoriesServiceTest {
	private MockDataRepositories mockData;

	@InjectMocks
	private RepositoriesServiceImpl repositoriesService;

	@Mock
	private RepositoriesDao repositoriesDao;

	@Mock
	private RelatedDao relatedDao;

	@Mock
	private ApiUtils apiUtils;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		this.mockData = new MockDataRepositories();
	}

	@Test
	public void testGetRepositoriesData() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");

		Map<String, String> params = new HashMap<>();
		List<CHRepository> repositories = mockData.generateFakeRepositories();

		when(repositoriesDao.getRepositories(anyInt(), ArgumentMatchers.<String>any(), ArgumentMatchers.<String>any(), ArgumentMatchers.<String>any())).thenReturn(repositories);
		ApiResponse<List<CHRepository>> apiResponse = repositoriesService.getRepositories(request, params);

		assertNotNull(apiResponse);
		assertEquals(HttpStatus.OK.value(), apiResponse.getCode());
		assertFalse(apiResponse.getResult().isEmpty());
	}

	@Test
	public void testGetRepositoriesNoData() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");

		Map<String, String> params = new HashMap<>();

		when(repositoriesDao.getRepositories(anyInt(), ArgumentMatchers.<String>any(), ArgumentMatchers.<String>any(), ArgumentMatchers.<String>any())).thenReturn(new ArrayList<>());
		ApiResponse<List<CHRepository>> apiResponse = repositoriesService.getRepositories(request, params);

		assertNotNull(apiResponse);
		assertEquals(HttpStatus.NO_CONTENT.value(), apiResponse.getCode());
		assertNull(apiResponse.getResult());
	}

	@Test
	public void testGetRepositoriesNoDataNull() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");

		Map<String, String> params = new HashMap<>();

		when(repositoriesDao.getRepositories(anyInt(), ArgumentMatchers.<String>any(), ArgumentMatchers.<String>any(), ArgumentMatchers.<String>any())).thenReturn(null);
		ApiResponse<List<CHRepository>> apiResponse = repositoriesService.getRepositories(request, params);

		assertNotNull(apiResponse);
		assertEquals(HttpStatus.NO_CONTENT.value(), apiResponse.getCode());
		assertNull(apiResponse.getResult());
	}

	@Test
	public void testGetRepositoriesError() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");

		Map<String, String> params = new HashMap<>();
		String msg = "Exception Message for Testing";
		List<ApiError> errors = new ArrayList<>();
		errors.add(new ApiError(msg));

		when(apiUtils.getErrorsFromException(anyList(), any(Exception.class))).thenReturn(errors);
		when(repositoriesDao.getRepositories(anyInt(), ArgumentMatchers.<String>any(), ArgumentMatchers.<String>any(), ArgumentMatchers.<String>any())).thenThrow(new IOException(msg));
		ApiResponse<List<CHRepository>> apiResponse = repositoriesService.getRepositories(request, params);

		assertNotNull(apiResponse);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), apiResponse.getCode());
		assertNull(apiResponse.getResult());
		assertFalse(apiResponse.getErrors().isEmpty());
	}

	@Test
	public void testGetRepositoriesByIdsInvalid() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");

		String[] ids = new String[]{"",""};
		ApiResponse<List<CHRepository>> apiResponse = repositoriesService.getRepositoriesByIds(request, ids);

		assertNotNull(apiResponse);
		assertEquals(HttpStatus.NO_CONTENT.value(), apiResponse.getCode());
		assertNull(apiResponse.getResult());
		assertNull(apiResponse.getErrors());
	}

	@Test
	public void testGetRepositoriesByIdsDataWithRelatedData() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");

		String[] ids = new String[]{"testId"};
		CHRepository repository = mockData.generateFakeRepository("id", "name", "ownerName");
		List<RelatedItemModel> relatedItems = mockData.generateFakeRelatedItems();
		when(relatedDao.getRelatedItems(ArgumentMatchers.<String>any())).thenReturn(relatedItems);
		when(repositoriesDao.getRepository(ArgumentMatchers.<String>any())).thenReturn(repository);
		ApiResponse<List<CHRepository>> apiResponse = repositoriesService.getRepositoriesByIds(request, ids);

		assertNotNull(apiResponse);
		assertEquals(HttpStatus.OK.value(), apiResponse.getCode());
		assertNotNull(apiResponse.getResult());
		assertFalse(apiResponse.getResult().get(0).getRelated().isEmpty());
		assertFalse(apiResponse.getMessages().isEmpty());
		assertNull(apiResponse.getErrors());
	}

	@Test
	public void testGetRepositoriesByIdsDataWithoutRelatedData() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");

		String[] ids = new String[]{"testId"};
		CHRepository repository = mockData.generateFakeRepository("id", "name", "ownerName");

		when(relatedDao.getRelatedItems(ArgumentMatchers.<String>any())).thenReturn(new ArrayList<>());
		when(repositoriesDao.getRepository(ArgumentMatchers.<String>any())).thenReturn(repository);
		ApiResponse<List<CHRepository>> apiResponse = repositoriesService.getRepositoriesByIds(request, ids);

		assertNotNull(apiResponse);
		assertEquals(HttpStatus.OK.value(), apiResponse.getCode());
		assertNotNull(apiResponse.getResult());
		assertTrue(apiResponse.getResult().get(0).getRelated().isEmpty());
		assertFalse(apiResponse.getMessages().isEmpty());
		assertNull(apiResponse.getErrors());
	}

	@Test
	public void testGetRepositoriesByIdsDataRelatedDataError() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");

		String[] ids = new String[]{"testId"};
		CHRepository repository = mockData.generateFakeRepository("id", "name", "ownerName");

		when(relatedDao.getRelatedItems(ArgumentMatchers.<String>any())).thenThrow(new IOException());
		when(repositoriesDao.getRepository(ArgumentMatchers.<String>any())).thenReturn(repository);
		ApiResponse<List<CHRepository>> apiResponse = repositoriesService.getRepositoriesByIds(request, ids);

		assertNotNull(apiResponse);
		assertEquals(HttpStatus.OK.value(), apiResponse.getCode());
		assertNotNull(apiResponse.getResult());
		assertTrue(apiResponse.getResult().get(0).getRelated().isEmpty());
		assertFalse(apiResponse.getMessages().isEmpty());
		assertNull(apiResponse.getErrors());
	}

	@Test
	public void testGetRepositoriesByIdsNoData() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");

		String[] ids = new String[]{"testId"};
		when(relatedDao.getRelatedItems(ArgumentMatchers.<String>any())).thenReturn(new ArrayList<>());
		when(repositoriesDao.getRepository(ArgumentMatchers.<String>any())).thenReturn(null);
		ApiResponse<List<CHRepository>> apiResponse = repositoriesService.getRepositoriesByIds(request, ids);

		assertNotNull(apiResponse);
		assertEquals(HttpStatus.PARTIAL_CONTENT.value(), apiResponse.getCode());
		assertNotNull(apiResponse.getResult());
		assertTrue(apiResponse.getResult().isEmpty());
		assertFalse(apiResponse.getMessages().isEmpty());
		assertNull(apiResponse.getErrors());
	}

	@Test
	public void testGetRepositoriesByIdsError() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");

		String[] ids = new String[]{"testId"};
		String msg = "Test message";
		List<ApiError> errors = new ArrayList<>();
		errors.add(new ApiError(msg));

		when(apiUtils.getErrorsFromException(anyList(), any(Exception.class))).thenReturn(errors);
		when(relatedDao.getRelatedItems(ArgumentMatchers.<String>any())).thenReturn(new ArrayList<>());
		when(repositoriesDao.getRepository(ArgumentMatchers.<String>any())).thenThrow(new IOException(msg));
		ApiResponse<List<CHRepository>> apiResponse = repositoriesService.getRepositoriesByIds(request, ids);

		assertNotNull(apiResponse);
		assertEquals(HttpStatus.PARTIAL_CONTENT.value(), apiResponse.getCode());
		assertNotNull(apiResponse.getResult());
		assertTrue(apiResponse.getResult().isEmpty());
		assertFalse(apiResponse.getErrors().isEmpty());
	}

	@Test
	public void testGetMetricsNoData() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");

		String[] owners = new String[]{};
		when(repositoriesDao.getRepositoriesMetrics(anyInt(), any(String[].class))).thenReturn(new ArrayList<>());
		ApiResponse<CHMetrics> apiResponse = repositoriesService.getMetrics(request, owners);

		assertNotNull(apiResponse);
		assertEquals(HttpStatus.NO_CONTENT.value(), apiResponse.getCode());
		assertNull(apiResponse.getResult());
	}

	@Test
	public void testGetMetricsNoDataNull() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");

		String[] owners = new String[]{};
		when(repositoriesDao.getRepositoriesMetrics(anyInt(), any(String[].class))).thenReturn(null);
		ApiResponse<CHMetrics> apiResponse = repositoriesService.getMetrics(request, owners);

		assertNotNull(apiResponse);
		assertEquals(HttpStatus.NO_CONTENT.value(), apiResponse.getCode());
		assertNull(apiResponse.getResult());
	}

	@Test
	public void testGetMetricsData() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");

		String[] owners = new String[]{};
		List<CHRepository> repositories = this.mockData.generateFakeRepositories();
		when(repositoriesDao.getRepositoriesMetrics(anyInt(), any(String[].class))).thenReturn(repositories);
		ApiResponse<CHMetrics> apiResponse = repositoriesService.getMetrics(request, owners);

		assertNotNull(apiResponse);
		assertEquals(HttpStatus.OK.value(), apiResponse.getCode());
		assertNotNull(apiResponse.getResult());
	}

	@Test
	public void testGetMetricsError() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");

		String[] owners = new String[]{};
		String msg = "Test exception";
		List<ApiError> errors = new ArrayList<>();
		errors.add(new ApiError(msg));

		when(apiUtils.getErrorsFromException(anyList(), any(Exception.class))).thenReturn(errors);
		when(repositoriesDao.getRepositoriesMetrics(anyInt(), any(String[].class))).thenThrow(new IOException(msg));
		ApiResponse<CHMetrics> apiResponse = repositoriesService.getMetrics(request, owners);

		assertNotNull(apiResponse);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), apiResponse.getCode());
		assertNull(apiResponse.getResult());
		assertFalse(apiResponse.getErrors().isEmpty());
	}

	@Test
	public void testSearchEmptyTerm() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");
		CHSearchRequest searchRequest = this.mockData.createSearchRequest(100, null, false);
		ApiResponse<List<CHRepository>> apiResponse = repositoriesService.search(request, searchRequest);

		assertNotNull(apiResponse);
		assertEquals(HttpStatus.NO_CONTENT.value(), apiResponse.getCode());
		assertNull(apiResponse.getResult());
		assertFalse(apiResponse.getMessages().isEmpty());
	}

	@Test
	public void testSearchNoMatches() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");
		CHSearchRequest searchRequest = this.mockData.createSearchRequest(100, "test", false);

		when(repositoriesDao.searchRepositories(any(CHSearchRequest.class))).thenReturn(new ArrayList<>());
		ApiResponse<List<CHRepository>> apiResponse = repositoriesService.search(request, searchRequest);

		assertNotNull(apiResponse);
		assertEquals(HttpStatus.NO_CONTENT.value(), apiResponse.getCode());
		assertNull(apiResponse.getResult());
		assertNull(apiResponse.getErrors());
	}

	@Test
	public void testSearchData() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");
		CHSearchRequest searchRequest = this.mockData.createSearchRequest(100, "test", false);
		List<CHRepository> repositories = this.mockData.generateFakeRepositories();

		when(repositoriesDao.searchRepositories(any(CHSearchRequest.class))).thenReturn(repositories);
		ApiResponse<List<CHRepository>> apiResponse = repositoriesService.search(request, searchRequest);

		assertNotNull(apiResponse);
		assertEquals(HttpStatus.OK.value(), apiResponse.getCode());
		assertNotNull(apiResponse.getResult());
		assertFalse(apiResponse.getMessages().isEmpty());
	}

	@Test
	public void testSearchError() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");
		CHSearchRequest searchRequest = this.mockData.createSearchRequest(100, "test", false);
		String msg = "Test for exception";
		List<ApiError> errors = new ArrayList<>();
		errors.add(new ApiError(msg));
		when(apiUtils.getErrorsFromException(anyList(), any(Exception.class))).thenReturn(errors);
		when(repositoriesDao.searchRepositories(any(CHSearchRequest.class))).thenThrow(new IOException(msg));
		ApiResponse<List<CHRepository>> apiResponse = repositoriesService.search(request, searchRequest);

		assertNotNull(apiResponse);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), apiResponse.getCode());
		assertNull(apiResponse.getResult());
		assertFalse(apiResponse.getErrors().isEmpty());
	}

}