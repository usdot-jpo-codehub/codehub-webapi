package gov.dot.its.codehub.webapi.model;

public class CHGeneratedData {
	private long rank;
	private CHSonarMetrics sonarMetrics;
	private CHVScan vscan;

	public CHGeneratedData() {
		this.sonarMetrics = new CHSonarMetrics();
		this.vscan = new CHVScan();
	}

	public long getRank() {
		return rank;
	}
	public void setRank(long rank) {
		this.rank = rank;
	}
	public CHSonarMetrics getSonarMetrics() {
		return sonarMetrics;
	}
	public void setSonarMetrics(CHSonarMetrics sonarMetrics) {
		this.sonarMetrics = sonarMetrics;
	}
	public CHVScan getVscan() {
		return vscan;
	}
	public void setVscan(CHVScan vscan) {
		this.vscan = vscan;
	}

}
