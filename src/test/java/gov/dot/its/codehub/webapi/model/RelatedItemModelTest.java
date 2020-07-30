package gov.dot.its.codehub.webapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class RelatedItemModelTest {

	@Test
	public void testInstance() {
		RelatedItemModel relatedItemModel = new RelatedItemModel();
		assertNotNull(relatedItemModel);
	}

	@Test
	public void testId() {
		RelatedItemModel relatedItemModel = new RelatedItemModel();
		relatedItemModel.setId("id");
		assertEquals("id", relatedItemModel.getId());
	}

	@Test
	public void testName() {
		RelatedItemModel relatedItemModel = new RelatedItemModel();
		relatedItemModel.setName("name");
		assertEquals("name", relatedItemModel.getName());
	}

	@Test
	public void testUrl() {
		RelatedItemModel relatedItemModel = new RelatedItemModel();
		relatedItemModel.setUrl("url");
		assertEquals("url", relatedItemModel.getUrl());
	}
}
