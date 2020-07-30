package gov.dot.its.codehub.webapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class RelatedModelTest {

	@Test
	public void testInstance() {
		RelatedModel relatedModel = new RelatedModel();
		assertNotNull(relatedModel);
	}

	@Test
	public void testId() {
		RelatedModel relatedModel = new RelatedModel();
		relatedModel.setId("id");
		assertEquals("id", relatedModel.getId()); 
	}

	@Test
	public void testName() {
		RelatedModel relatedModel = new RelatedModel();
		relatedModel.setName("name");
		assertEquals("name", relatedModel.getName());
	}

	@Test
	public void testUrls() {
		RelatedItemModel relatedItemModel = new RelatedItemModel();
		relatedItemModel.setId("id");
		relatedItemModel.setName("name");

		List<RelatedItemModel> urls = new ArrayList<>();
		urls.add(relatedItemModel);

		RelatedModel relatedModel = new RelatedModel();
		relatedModel.setUrls(urls);

		assertFalse(relatedModel.getUrls().isEmpty());
		assertEquals(1, relatedModel.getUrls().size());
		assertEquals("id", relatedModel.getUrls().get(0).getId());
	}
}
