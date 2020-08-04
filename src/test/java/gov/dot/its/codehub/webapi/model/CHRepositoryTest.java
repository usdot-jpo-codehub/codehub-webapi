package gov.dot.its.codehub.webapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class CHRepositoryTest {

	@Test
	public void testInstance() {
		CHRepository chRepository = new CHRepository();
		assertNotNull(chRepository);
	}

	@Test
	public void testId() {
		CHRepository chRepository = new CHRepository();
		chRepository.setId("id");
		assertEquals("id", chRepository.getId());
	}

	@Test
	public void testSourceData() {
		CHSourceData sourceData = new CHSourceData();
		sourceData.setName("name");

		CHRepository chRepository = new CHRepository();
		chRepository.setSourceData(sourceData);

		assertNotNull(chRepository.getSourceData());
		assertEquals("name",chRepository.getSourceData().getName());
	}

	@Test
	public void testGeneratedData() {
		CHGeneratedData chGeneratedData = new CHGeneratedData();
		chGeneratedData.setRank(1);

		CHRepository chRepository = new CHRepository();
		chRepository.setGeneratedData(chGeneratedData);

		assertNotNull(chRepository.getGeneratedData());
		assertEquals(1, chRepository.getGeneratedData().getRank());
	}

	@Test
	public void testCodeHubData() {
		CHCodehubData codehubData = new CHCodehubData();
		codehubData.setEtag("etag");

		CHRepository chRepository = new CHRepository();
		chRepository.setCodehubData(codehubData);

		assertNotNull(chRepository.getCodehubData());
		assertEquals("etag", chRepository.getCodehubData().getEtag());
	}

	@Test
	public void testHighlights() {
		List<String> values = new ArrayList<>();
		values.add("one");
		values.add("two");
		Map<String, List<String>> highlights = new HashMap<>();
		highlights.put("key", values);

		CHRepository chRepository = new CHRepository();
		chRepository.setHighlights(highlights);

		assertNotNull(chRepository.getHighlights());
		assertFalse(chRepository.getHighlights().get("key").isEmpty());
		assertEquals("one", chRepository.getHighlights().get("key").get(0));
	}

	@Test
	public void testRelated() {
		RelatedItemModel relatedItemModel = new RelatedItemModel();
		relatedItemModel.setId("id");

		List<RelatedItemModel> related = new ArrayList<>();
		related.add(relatedItemModel);

		CHRepository chRepository = new CHRepository();
		chRepository.setRelated(related);

		assertFalse(chRepository.getRelated().isEmpty());
		assertEquals(1, chRepository.getRelated().size());
		assertNotNull(chRepository.getRelated().get(0));
		assertEquals("id", chRepository.getRelated().get(0).getId());
	}
}
