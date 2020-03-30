package gov.dot.its.codehub.webapi.model;

import java.util.ArrayList;
import java.util.List;

public class CHConfiguration {
	private String id;
	private String name;
	private List<CHCategory> categories;
	private List<CHEngagementPopup> engagementPopups;

	public CHConfiguration() {
		this.categories = new ArrayList<>();
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public List<CHCategory> getCategories() {
		return categories;
	}

	public void setCategories(List<CHCategory> categories) {
		this.categories = categories;
	}

	public List<CHEngagementPopup> getEngagementPopups() {
		return engagementPopups;
	}

	public void setEngagementPopups(List<CHEngagementPopup> engagementPopups) {
		this.engagementPopups = engagementPopups;
	}

}
