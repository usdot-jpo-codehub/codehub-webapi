package gov.dot.its.codehub.webapi.model;

@SuppressWarnings({"squid:S00116","squid:S00100","squid:S00117"})
public class CHSonarMetrics {
	private CHSonarMetric bugs;
	private CHSonarMetric code_smells;
	private CHSonarMetric reliability_rating;
	private CHSonarMetric security_rating;
	private CHSonarMetric sqale_index;
	private CHSonarMetric sqale_rating;
	private CHSonarMetric violations;
	private CHSonarMetric vulnerabilities;
	private CHSonarMetric complexity;
	
	public CHSonarMetrics() {
		this.bugs = new CHSonarMetric();
		this.code_smells = new CHSonarMetric();
		this.reliability_rating = new CHSonarMetric();
		this.security_rating = new CHSonarMetric();
		this.sqale_index = new CHSonarMetric();
		this.sqale_rating = new CHSonarMetric();
		this.violations = new CHSonarMetric();
		this.vulnerabilities = new CHSonarMetric();
		this.complexity = new CHSonarMetric();
	}

	public CHSonarMetric getBugs() {
		return bugs;
	}
	public void setBugs(CHSonarMetric bugs) {
		this.bugs = bugs;
	}
	public CHSonarMetric getCode_smells() {
		return code_smells;
	}
	public void setCode_smells(CHSonarMetric code_smells) {
		this.code_smells = code_smells;
	}
	public CHSonarMetric getReliability_rating() {
		return reliability_rating;
	}
	public void setReliability_rating(CHSonarMetric reliability_rating) {
		this.reliability_rating = reliability_rating;
	}
	public CHSonarMetric getSecurity_rating() {
		return security_rating;
	}
	public void setSecurity_rating(CHSonarMetric security_rating) {
		this.security_rating = security_rating;
	}
	public CHSonarMetric getSqale_index() {
		return sqale_index;
	}
	public void setSqale_index(CHSonarMetric sqale_index) {
		this.sqale_index = sqale_index;
	}
	public CHSonarMetric getSqale_rating() {
		return sqale_rating;
	}
	public void setSqale_rating(CHSonarMetric sqale_rating) {
		this.sqale_rating = sqale_rating;
	}
	public CHSonarMetric getViolations() {
		return violations;
	}
	public void setViolations(CHSonarMetric violations) {
		this.violations = violations;
	}
	public CHSonarMetric getVulnerabilities() {
		return vulnerabilities;
	}
	public void setVulnerabilities(CHSonarMetric vulnerabilities) {
		this.vulnerabilities = vulnerabilities;
	}
	public CHSonarMetric getComplexity() {
		return complexity;
	}
	public void setComplexity(CHSonarMetric complexity) {
		this.complexity = complexity;
	}


}
