package gov.dot.its.codehub.webapi.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CHMetrics {
	private Integer numberOfOrganizations;
	private Integer numberOfProjects;
	private Long bugsVulnerabilities;
	private Long technicalDebt;
	private List<String> organizations;
	private Map<String, Integer> languageCountsStat;
	private Map<String, Float> languagePercentageStat;
	private CHMetricsSummary metricsSummary;

	public CHMetrics() {
		this.organizations = new ArrayList<>();
		this.languageCountsStat = new HashMap<>();
		this.languagePercentageStat = new HashMap<>();
		this.metricsSummary = new CHMetricsSummary();
	}

	public Integer getNumberOfOrganizations() {
		return numberOfOrganizations;
	}

	public void setNumberOfOrganizations(Integer numberOfOrganizations) {
		this.numberOfOrganizations = numberOfOrganizations;
	}

	public Integer getNumberOfProjects() {
		return numberOfProjects;
	}

	public void setNumberOfProjects(Integer numberOfProjects) {
		this.numberOfProjects = numberOfProjects;
	}

	public Long getBugsVulnerabilities() {
		return bugsVulnerabilities;
	}

	public void setBugsVulnerabilities(Long bugsVulnerabilities) {
		this.bugsVulnerabilities = bugsVulnerabilities;
	}

	public Long getTechnicalDebt() {
		return technicalDebt;
	}

	public void setTechnicalDebt(Long technicalDebt) {
		this.technicalDebt = technicalDebt;
	}

	public List<String> getOrganizations() {
		return organizations;
	}

	public void setOrganizations(List<String> organizations) {
		this.organizations = organizations;
	}

	public Map<String, Integer> getLanguageCountsStat() {
		return languageCountsStat;
	}

	public void setLanguageCountsStat(Map<String, Integer> languageCountsStat) {
		this.languageCountsStat = languageCountsStat;
	}

	public Map<String, Float> getLanguagePercentageStat() {
		return languagePercentageStat;
	}

	public void setLanguagePercentageStat(Map<String, Float> languagePercentageStat) {
		this.languagePercentageStat = languagePercentageStat;
	}

	public CHMetricsSummary getMetricsSummary() {
		return metricsSummary;
	}

	public void setMetricsSummary(CHMetricsSummary metricsSummary) {
		this.metricsSummary = metricsSummary;
	}


}
