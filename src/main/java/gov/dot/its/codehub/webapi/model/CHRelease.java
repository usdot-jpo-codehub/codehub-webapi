package gov.dot.its.codehub.webapi.model;

import java.util.ArrayList;
import java.util.List;

public class CHRelease {
	private String id;
	private String tagName;
	private String name;
	private long totalDownloads;
	private List<CHAsset> assets;

	public CHRelease() {
		this.assets = new ArrayList<>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getTotalDownloads() {
		return totalDownloads;
	}

	public void setTotalDownloads(long totalDownloads) {
		this.totalDownloads = totalDownloads;
	}

	public List<CHAsset> getAssets() {
		return assets;
	}

	public void setAssets(List<CHAsset> assets) {
		this.assets = assets;
	}


}
