package gov.dot.its.codehub.webapi.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CHEngagementPopup {
	private String id;
	private boolean isActive;
	private String name;
	private String description;
	private Date lastModified;
	private String content;
	private String controlsColor;
	private String controlsShadow;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@JsonProperty("isActive")
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
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
	public Date getLastModified() {
		return lastModified;
	}
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getControlsColor() {
		return controlsColor;
	}
	public void setControlsColor(String controlsColor) {
		this.controlsColor = controlsColor;
	}
	public String getControlsShadow() {
		return controlsShadow;
	}
	public void setControlsShadow(String controlsShadow) {
		this.controlsShadow = controlsShadow;
	}


}
