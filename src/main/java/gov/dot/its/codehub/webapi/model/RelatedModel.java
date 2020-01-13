package gov.dot.its.codehub.webapi.model;

import java.util.ArrayList;
import java.util.List;

public class RelatedModel {
	private String id;
	private String name;
	private List<RelatedItemModel> urls;

	public RelatedModel() {
		this.urls = new ArrayList<>();
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
	public List<RelatedItemModel> getUrls() {
		return urls;
	}
	public void setUrls(List<RelatedItemModel> urls) {
		this.urls = urls;
	}

}
