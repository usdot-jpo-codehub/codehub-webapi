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
public class CHConfigurationTest {
	
	@Test
	public void testInstance() {
		CHConfiguration chConfiguration = new CHConfiguration();
		assertNotNull(chConfiguration);
	}

	@Test
	public void testId() {
		CHConfiguration chConfiguration = new CHConfiguration();
		chConfiguration.setId("id");
		assertEquals("id", chConfiguration.getId());
	}

	@Test
	public void testName() {
		CHConfiguration chConfiguration = new CHConfiguration();
		chConfiguration.setName("name");
		assertEquals("name", chConfiguration.getName());
	}

	@Test
	public void testCategories() {
		CHCategory category = new CHCategory();
		category.setId("id");
		category.setName("name");

		List<CHCategory> categories = new ArrayList<>();
		categories.add(category);

		CHConfiguration chConfiguration = new CHConfiguration();
		chConfiguration.setCategories(categories);

		assertFalse(chConfiguration.getCategories().isEmpty());
		assertEquals(1, chConfiguration.getCategories().size());
		assertEquals("id", chConfiguration.getCategories().get(0).getId());
	}

	@Test
	public void testEngagementPopup() {
		CHEngagementPopup engagementPopup = new CHEngagementPopup();
		engagementPopup.setId("id");
		engagementPopup.setName("name");

		List<CHEngagementPopup> engagementPopups = new ArrayList<>();
		engagementPopups.add(engagementPopup);

		CHConfiguration chConfiguration = new CHConfiguration();
		chConfiguration.setEngagementPopups(engagementPopups);

		assertFalse(chConfiguration.getEngagementPopups().isEmpty());
		assertEquals(1, chConfiguration.getEngagementPopups().size());
		assertEquals("name", chConfiguration.getEngagementPopups().get(0).getName());
	}
}
