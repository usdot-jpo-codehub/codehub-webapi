package gov.dot.its.codehub.webapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CHBadges {
	private boolean featured;
	private String status;

	@JsonProperty("isFeatured")
	public boolean isFeatured() {
		return featured;
	}
	public void setFeatured(boolean featured) {
		this.featured = featured;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
