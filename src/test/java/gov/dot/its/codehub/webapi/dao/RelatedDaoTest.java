package gov.dot.its.codehub.webapi.dao;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

import gov.dot.its.codehub.webapi.MockDataRelated;
import gov.dot.its.codehub.webapi.MockESUtils;
import gov.dot.its.codehub.webapi.model.RelatedItemModel;
import gov.dot.its.codehub.webapi.model.RelatedModel;

@RunWith(SpringRunner.class)
public class RelatedDaoTest {

	private MockESUtils mockESUtils;
	private MockDataRelated mockData;

	@InjectMocks
	RelatedDaoImpl relatedDao;

	@Mock
	ESClientDao esClientDao;

	@Before
	public void setUp() {
		ReflectionTestUtils.setField(relatedDao, "relatedIndex", "unitTestIndex");
		ReflectionTestUtils.setField(relatedDao, "datahubEndPoint", "https://its.dot.gov/data");
		ReflectionTestUtils.setField(relatedDao, "datahubQueryString",">related=XYZ");

		this.mockESUtils = new MockESUtils();
		this.mockData = new MockDataRelated();
	}

	@Test
	public void testGetRelatedItemsNoData() throws IOException {
		GetResponse getResponse = mockESUtils.generateFakeGetResponse(null);
		when(esClientDao.get(any(GetRequest.class), any(RequestOptions.class))).thenReturn(getResponse);

		List<RelatedItemModel> relatedItems = relatedDao.getRelatedItems("id");
		assertNotNull(relatedItems);
		assertTrue(relatedItems.isEmpty());
	}

	@Test
	public void testGetRelatedItemsData() throws IOException {
		RelatedModel fakeRelatedModel = mockData.generatedFakeRelatedModel();
		GetResponse getResponse = mockESUtils.generateFakeGetResponse(fakeRelatedModel);
		when(esClientDao.get(any(GetRequest.class), any(RequestOptions.class))).thenReturn(getResponse);

		List<RelatedItemModel> relatedItems = relatedDao.getRelatedItems("id");
		assertNotNull(relatedItems);
		assertFalse(relatedItems.isEmpty());
	}
}
