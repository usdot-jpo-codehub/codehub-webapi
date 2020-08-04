package gov.dot.its.codehub.webapi.dao;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import gov.dot.its.codehub.webapi.MockDataRepositories;
import gov.dot.its.codehub.webapi.MockESUtils;
import gov.dot.its.codehub.webapi.model.CHRepository;
import gov.dot.its.codehub.webapi.model.CHSearchRequest;

@RunWith(SpringRunner.class)
public class RepositoriesDaoTest {

	final String TEST_ID = "id";
	final String TEST_OWNER = "owner";
	final String TEST_NAME = "name";
	final String TEST_SORT = "desc";
	final int TEST_LIMIT = 1000;

	private MockDataRepositories mockData;
	private MockESUtils mockEsUtils;

	@InjectMocks
	RepositoriesDaoImpl repositoriesDao;

	@Mock
	ESClientDao esClientDao;

	@Before
	public void setUp() {
		ReflectionTestUtils.setField(repositoriesDao, "reposIndex", "unitTestIndex");
		ReflectionTestUtils.setField(repositoriesDao, "includedFields", new String[]{"*"});
		ReflectionTestUtils.setField(repositoriesDao, "includedFieldsMetrics", new String[]{"*"});
		ReflectionTestUtils.setField(repositoriesDao, "esDefaultLimit", 1000);

		this.mockData = new MockDataRepositories();
		this.mockEsUtils = new MockESUtils();
	}

	@Test
	public void testGetRepositoriesNoData() throws IOException {
		SearchResponse searchResponse = mockEsUtils.generateFakeSearchResponse(null);

		when(esClientDao.search(any(SearchRequest.class), any(RequestOptions.class))).thenReturn(searchResponse);

		List<CHRepository> repositories =  repositoriesDao.getRepositories(TEST_LIMIT, null, null, TEST_SORT);
		assertNotNull(repositories);
		assertTrue(repositories.isEmpty());
	}

	@Test
	public void testGetRepositoriesData() throws IOException {
		CHRepository fakeRepo = mockData.generateFakeRepository(TEST_ID, TEST_NAME, TEST_OWNER);
		SearchResponse searchResponse = mockEsUtils.generateFakeSearchResponse(fakeRepo);

		when(esClientDao.search(any(SearchRequest.class), any(RequestOptions.class))).thenReturn(searchResponse);

		List<CHRepository> repositories =  repositoriesDao.getRepositories(TEST_LIMIT, null, null, TEST_SORT);
		assertNotNull(repositories);
		assertFalse(repositories.isEmpty());
	}

	@Test
	public void testGetRespositoryNoId() throws IOException {
		CHRepository repository = repositoriesDao.getRepository(null);
		assertNull(repository);
	}

	@Test
	public void testGetRepositoryNotExists() throws IOException {
		GetResponse getResponse = mockEsUtils.generateFakeGetResponse(null);
		when(esClientDao.get(any(GetRequest.class), any(RequestOptions.class))).thenReturn(getResponse);

		CHRepository repository = repositoriesDao.getRepository(TEST_ID);
		assertNull(repository);
	}

	@Test
	public void testGetRepositoryData() throws IOException {
		CHRepository fakeRepo = mockData.generateFakeRepository(TEST_ID, TEST_NAME, TEST_OWNER);
		GetResponse getResponse = mockEsUtils.generateFakeGetResponse(fakeRepo);
		when(esClientDao.get(any(GetRequest.class), any(RequestOptions.class))).thenReturn(getResponse);

		CHRepository repository = repositoriesDao.getRepository(TEST_ID);
		assertNotNull(repository);
	}

	@Test
	public void testGetRepositoriesMetricsNoDataNoOwner() throws IOException {
		SearchResponse searchResponse = mockEsUtils.generateFakeSearchResponse(null);
		when(esClientDao.search(any(SearchRequest.class), any(RequestOptions.class))).thenReturn(searchResponse);

		List<CHRepository> repositories = repositoriesDao.getRepositoriesMetrics(1000, null);
		assertNotNull(repositories);
		assertTrue(repositories.isEmpty());
	}

	@Test
	public void testGetRepositoriesMetricsNoDataWithOwner() throws IOException {
		SearchResponse searchResponse = mockEsUtils.generateFakeSearchResponse(null);
		when(esClientDao.search(any(SearchRequest.class), any(RequestOptions.class))).thenReturn(searchResponse);
		String[] owners = new String[]{TEST_OWNER};
		List<CHRepository> repositories = repositoriesDao.getRepositoriesMetrics(1000, owners);
		assertNotNull(repositories);
		assertTrue(repositories.isEmpty());
	}

	@Test
	public void testGetRepositoriesMetricsData() throws IOException {
		CHRepository fakeRepo = mockData.generateFakeRepository(TEST_ID, TEST_NAME, TEST_OWNER);
		SearchResponse searchResponse = mockEsUtils.generateFakeSearchResponse(fakeRepo);
		when(esClientDao.search(any(SearchRequest.class), any(RequestOptions.class))).thenReturn(searchResponse);
		String[] owners = new String[]{TEST_OWNER};
		List<CHRepository> repositories = repositoriesDao.getRepositoriesMetrics(1000, owners);
		assertNotNull(repositories);
		assertFalse(repositories.isEmpty());
	}

	@Test
	public void testSearchRepositoriesNoData() throws IOException {
		CHSearchRequest chSearchRequest = new CHSearchRequest();
		chSearchRequest.setLimit(TEST_LIMIT);
		chSearchRequest.setMatchAll(true);

		SearchResponse searchResponse = mockEsUtils.generateFakeSearchResponse(null);
		when(esClientDao.search(any(SearchRequest.class), any(RequestOptions.class))).thenReturn(searchResponse);
		List<CHRepository> repositories = repositoriesDao.searchRepositories(chSearchRequest);
		assertNotNull(repositories);
		assertTrue(repositories.isEmpty());
	}

	@Test
	public void testSearchRepositoriesData() throws IOException {
		CHSearchRequest chSearchRequest = new CHSearchRequest();
		chSearchRequest.setLimit(TEST_LIMIT);
		chSearchRequest.setMatchAll(false);

		CHRepository fakeRepo = mockData.generateFakeRepository(TEST_ID, TEST_NAME, TEST_OWNER);
		SearchResponse searchResponse = mockEsUtils.generateFakeSearchResponse(fakeRepo);
		when(esClientDao.search(any(SearchRequest.class), any(RequestOptions.class))).thenReturn(searchResponse);
		List<CHRepository> repositories = repositoriesDao.searchRepositories(chSearchRequest);
		assertNotNull(repositories);
		assertFalse(repositories.isEmpty());
	}
}
