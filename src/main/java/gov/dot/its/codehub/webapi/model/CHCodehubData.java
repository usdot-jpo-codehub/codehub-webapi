package gov.dot.its.codehub.webapi.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CHCodehubData {
	private String etag;
	private boolean isVisible;
	private boolean isIngested;
	private boolean isIngestionEnabled;
	private String source;
	private Date lastModified;
	private Date lastIngested;
	private CHBadges badges;

	public CHCodehubData() {
		this.badges = new CHBadges();
	}

	public String getEtag() {
		return etag;
	}
	public void setEtag(String etag) {
		this.etag = etag;
	}
	@JsonProperty("isVisible")
	public boolean isVisible() {
		return isVisible;
	}
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
	@JsonProperty("isIngested")
	public boolean isIngested() {
		return isIngested;
	}
	public void setIngested(boolean isIngested) {
		this.isIngested = isIngested;
	}
	@JsonProperty("isIngestionEnabled")
	public boolean isIngestionEnabled() {
		return isIngestionEnabled;
	}
	public void setIngestionEnabled(boolean isIngestionEnabled) {
		this.isIngestionEnabled = isIngestionEnabled;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public Date getLastModified() {
		return lastModified;
	}
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}
	public Date getLastIngested() {
		return lastIngested;
	}
	public void setLastIngested(Date lastIngested) {
		this.lastIngested = lastIngested;
	}
	public CHBadges getBadges() {
		return badges;
	}
	public void setBadges(CHBadges badges) {
		this.badges = badges;
	}

}
