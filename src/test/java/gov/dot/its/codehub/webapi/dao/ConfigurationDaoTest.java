package gov.dot.its.codehub.webapi.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import gov.dot.its.codehub.webapi.MockDataConfiguration;
import gov.dot.its.codehub.webapi.MockESUtils;
import gov.dot.its.codehub.webapi.model.CHCategory;
import gov.dot.its.codehub.webapi.model.CHConfiguration;
import gov.dot.its.codehub.webapi.model.CHEngagementPopup;

@RunWith(SpringRunner.class)
public class ConfigurationDaoTest {

	private MockESUtils mockESUtils;
	private MockDataConfiguration mockData;

	@InjectMocks
	ConfigurationDaoImpl configurationDao;

	@Mock
	ESClientDao esClientDao;

	@Before
	public void setUp() {
		ReflectionTestUtils.setField(configurationDao, "configurationsIndex", "unitTestIndex");
		ReflectionTestUtils.setField(configurationDao, "configurationId", "defaultId");

		this.mockData = new MockDataConfiguration();
		this.mockESUtils = new MockESUtils();
	}

	@Test
	public void testGetConfigurationNoData() throws IOException {
		GetResponse getResponse = mockESUtils.generateFakeGetResponse(null);
		when(esClientDao.get(any(GetRequest.class), any(RequestOptions.class))).thenReturn(getResponse);

		CHConfiguration configuration = configurationDao.getConfiguration();
		assertNull(configuration);
	}

	@Test
	public void testGetConfigurationData() throws IOException {
		CHConfiguration fakeConfiguration = mockData.generateFakeConfiguration();
		GetResponse getResponse = mockESUtils.generateFakeGetResponse(fakeConfiguration);
		when(esClientDao.get(any(GetRequest.class), any(RequestOptions.class))).thenReturn(getResponse);

		CHConfiguration configuration = configurationDao.getConfiguration();
		assertNotNull(configuration);
		assertNotNull(configuration.getCategories());
		assertFalse(configuration.getCategories().isEmpty());
		assertNotNull(configuration.getEngagementPopups());
		assertFalse(configuration.getEngagementPopups().isEmpty());
	}

	@Test
	public void testGetCategoriesNoData() throws IOException {
		GetResponse getResponse = mockESUtils.generateFakeGetResponse(null);
		when(esClientDao.get(any(GetRequest.class), any(RequestOptions.class))).thenReturn(getResponse);

		List<CHCategory> categories = configurationDao.getCategories();
		assertNotNull(categories);
		assertTrue(categories.isEmpty());
	}

	@Test
	public void testGetCategoriesData() throws IOException {
		CHConfiguration fakeConfiguration = mockData.generateFakeConfiguration();
		GetResponse getResponse = mockESUtils.generateFakeGetResponse(fakeConfiguration);
		when(esClientDao.get(any(GetRequest.class), any(RequestOptions.class))).thenReturn(getResponse);

		List<CHCategory> categories = configurationDao.getCategories();
		assertNotNull(categories);
		assertFalse(categories.isEmpty());
	}

	@Test
	public void testGetCategoryByIdNoConfiguration() throws IOException {
		GetResponse getResponse = mockESUtils.generateFakeGetResponse(null);
		when(esClientDao.get(any(GetRequest.class), any(RequestOptions.class))).thenReturn(getResponse);

		CHCategory category = configurationDao.getCategoryById("id");
		assertNull(category);
	}

	@Test
	public void testGetCategoryByIdNotFound() throws IOException {
		CHConfiguration fakeConfiguration = mockData.generateFakeConfiguration();
		GetResponse getResponse = mockESUtils.generateFakeGetResponse(fakeConfiguration);
		when(esClientDao.get(any(GetRequest.class), any(RequestOptions.class))).thenReturn(getResponse);

		CHCategory category = configurationDao.getCategoryById("none");
		assertNull(category);
	}

	@Test
	public void testGetCategoryByIdFoundId() throws IOException {
		CHConfiguration fakeConfiguration = mockData.generateFakeConfiguration();
		GetResponse getResponse = mockESUtils.generateFakeGetResponse(fakeConfiguration);
		when(esClientDao.get(any(GetRequest.class), any(RequestOptions.class))).thenReturn(getResponse);

		CHCategory category = configurationDao.getCategoryById("id-1");
		assertNotNull(category);
		assertEquals("id-1", category.getId());;
	}

	@Test
	public void testGetEngagementPopupsNoData() throws IOException {
		GetResponse getResponse = mockESUtils.generateFakeGetResponse(null);
		when(esClientDao.get(any(GetRequest.class), any(RequestOptions.class))).thenReturn(getResponse);

		List<CHEngagementPopup> engagementPopups = configurationDao.getEngagementPopups();
		assertNotNull(engagementPopups);
		assertTrue(engagementPopups.isEmpty());
	}

	@Test
	public void testGetEngagementPopupsData() throws IOException {
		CHConfiguration configuration = mockData.generateFakeConfiguration();
		GetResponse getResponse = mockESUtils.generateFakeGetResponse(configuration);
		when(esClientDao.get(any(GetRequest.class), any(RequestOptions.class))).thenReturn(getResponse);

		List<CHEngagementPopup> engagementPopups = configurationDao.getEngagementPopups();
		assertNotNull(engagementPopups);
		assertFalse(engagementPopups.isEmpty());
	}
}
