package gov.dot.its.codehub.webapi.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CHSourceData {
	private long commits;
	private long stars;
	private long watchers;
	private String description;
	private String language;
	private String name;
	private String repositoryUrl;
	private String defaultBranch;
	private Date createdAt;
	private Date lastPush;
	private CHOwner owner;
	private CHReadme readme;
	private List<CHFork> forks;
	private List<CHContributor> contributors;
	private List<CHRelease> releases;
	private Map<String, Long> languages;

	public CHSourceData() {
		this.owner = new CHOwner();
		this.readme = new CHReadme();
		this.forks = new ArrayList<>();
		this.contributors = new ArrayList<>();
		this.releases = new ArrayList<>();
		this.languages = new HashMap<>();
	}

	public long getCommits() {
		return commits;
	}

	public void setCommits(long commits) {
		this.commits = commits;
	}

	public long getStars() {
		return stars;
	}

	public void setStars(long stars) {
		this.stars = stars;
	}

	public long getWatchers() {
		return watchers;
	}

	public void setWatchers(long watchers) {
		this.watchers = watchers;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRepositoryUrl() {
		return repositoryUrl;
	}

	public void setRepositoryUrl(String repositoryUrl) {
		this.repositoryUrl = repositoryUrl;
	}

	public String getDefaultBranch() {
		return defaultBranch;
	}

	public void setDefaultBranch(String defaultBranch) {
		this.defaultBranch = defaultBranch;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getLastPush() {
		return lastPush;
	}

	public void setLastPush(Date lastPush) {
		this.lastPush = lastPush;
	}

	public CHOwner getOwner() {
		return owner;
	}

	public void setOwner(CHOwner owner) {
		this.owner = owner;
	}

	public CHReadme getReadme() {
		return readme;
	}

	public void setReadme(CHReadme readme) {
		this.readme = readme;
	}

	public List<CHContributor> getContributors() {
		return contributors;
	}

	public void setContributors(List<CHContributor> contributors) {
		this.contributors = contributors;
	}

	public List<CHFork> getForks() {
		return forks;
	}

	public void setForks(List<CHFork> forks) {
		this.forks = forks;
	}

	public List<CHRelease> getReleases() {
		return releases;
	}

	public void setReleases(List<CHRelease> releases) {
		this.releases = releases;
	}

	public Map<String, Long> getLanguages() {
		return languages;
	}

	public void setLanguages(Map<String, Long> languages) {
		this.languages = languages;
	}




}
