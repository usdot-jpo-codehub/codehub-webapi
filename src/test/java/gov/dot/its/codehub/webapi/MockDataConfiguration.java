package gov.dot.its.codehub.webapi;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import gov.dot.its.codehub.webapi.model.CHCategory;
import gov.dot.its.codehub.webapi.model.CHConfiguration;
import gov.dot.its.codehub.webapi.model.CHEngagementPopup;

public class MockDataConfiguration {

	private Random random;

	public MockDataConfiguration() {
		this.random = new Random();
	}

	public CHConfiguration generateFakeConfiguration() {
		CHConfiguration configuration = new CHConfiguration();
		configuration.setId("Configuration-ID");
		configuration.setName("Configuration-Name");

		List<CHCategory> categories = this.generateFakeCategories();
		configuration.setCategories(categories);

		List<CHEngagementPopup> engagementPopups = this.generateFakeEngagementPopups();
		configuration.setEngagementPopups(engagementPopups);

		return configuration;
	}
	
	public List<CHCategory> generateFakeCategories() {
		List<CHCategory> categories = new ArrayList<>();
		for(int i=1; i<3; i++) {
			CHCategory category = generateFakeCategory(i);
			categories.add(category);
		}
		return categories;
	}

	public List<CHEngagementPopup> generateFakeEngagementPopups() {
		List<CHEngagementPopup> engagementPopups = new ArrayList<>();
		for(int i=1; i<3; i++) {
			CHEngagementPopup engagementPopup = generateFakeEngagementPopup(1);
			engagementPopups.add(engagementPopup);
		}

		return engagementPopups;
	}

	public CHCategory generateFakeCategory(int index) {
		CHCategory category = new CHCategory();
		category.setDescription("This is the description");
		category.setId(String.format("id-%s", index));
		category.setEnabled(true);
		category.setLastModified(new Date());
		category.setName(String.format("Category-%s", random.nextInt(100)));
		category.setPopular(true);
		category.setOrderPopular(Long.valueOf(random.nextInt(10)));
		category.setImageFileName(String.format("http://url.to.image/image-%s", random.nextInt(10)));

		return category;
	}

	public CHEngagementPopup generateFakeEngagementPopup(int index) {
		CHEngagementPopup engagementPopup = new CHEngagementPopup();
		engagementPopup.setActive(true);
		engagementPopup.setContent("<h1>Content</h1>");
		engagementPopup.setDescription("Description");
		engagementPopup.setId(String.format("id-%s", index));
		engagementPopup.setLastModified(new Date());
		engagementPopup.setName("EngagementPopup");

		return engagementPopup;
	}
}