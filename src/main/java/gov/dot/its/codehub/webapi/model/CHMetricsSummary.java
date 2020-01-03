package gov.dot.its.codehub.webapi.model;


public class CHMetricsSummary {
	private CHMetricsSummaryBase<Long> releasibility;
	private CHMetricsSummaryBase<Long> reliability;
	private CHMetricsSummaryBase<Long> security;
	private CHMetricsSummaryBase<Long> maintainability;

	public CHMetricsSummary() {
		this.releasibility = new CHMetricsSummaryBase<Long>();
		this.reliability = new CHMetricsSummaryBase<Long>();
		this.security = new CHMetricsSummaryBase<Long>();
		this.maintainability = new CHMetricsSummaryBase<Long>();
	}

	public CHMetricsSummaryBase<Long> getReleasibility() {
		return releasibility;
	}
	public void setReleasibility(CHMetricsSummaryBase<Long> releasibility) {
		this.releasibility = releasibility;
	}
	public CHMetricsSummaryBase<Long> getReliability() {
		return reliability;
	}
	public void setReliability(CHMetricsSummaryBase<Long> reliability) {
		this.reliability = reliability;
	}
	public CHMetricsSummaryBase<Long> getSecurity() {
		return security;
	}
	public void setSecurity(CHMetricsSummaryBase<Long> security) {
		this.security = security;
	}
	public CHMetricsSummaryBase<Long> getMaintainability() {
		return maintainability;
	}
	public void setMaintainability(CHMetricsSummaryBase<Long> maintainability) {
		this.maintainability = maintainability;
	}



}
