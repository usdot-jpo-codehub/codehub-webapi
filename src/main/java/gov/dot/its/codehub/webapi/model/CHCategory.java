package gov.dot.its.codehub.webapi.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CHCategory {
	private String id;
	private String name;
	private String description;
	private boolean isEnabled;
	private Date lastModified;
	private boolean isPopular;
	private Long orderPopular;
	private String imageFileName;

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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@JsonProperty("isEnabled")
	public boolean isEnabled() {
		return isEnabled;
	}
	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	public Date getLastModified() {
		return lastModified;
	}
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}
	@JsonProperty("isPopular")
	public boolean isPopular() {
		return isPopular;
	}
	public void setPopular(boolean isPopular) {
		this.isPopular = isPopular;
	}
	public Long getOrderPopular() {
		return orderPopular;
	}
	public void setOrderPopular(Long orderPopular) {
		this.orderPopular = orderPopular;
	}
	public String getImageFileName() {
		return imageFileName;
	}
	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}

}
