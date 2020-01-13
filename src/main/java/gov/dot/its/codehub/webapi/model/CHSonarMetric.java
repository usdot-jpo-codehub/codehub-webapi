package gov.dot.its.codehub.webapi.model;

@SuppressWarnings({"squid:S00116","squid:S00100","squid:S00117"})
public class CHSonarMetric {
	private String frmt_val;
	private String key;
	private float val;

	public String getFrmt_val() {
		return frmt_val;
	}
	public void setFrmt_val(String frmt_val) {
		this.frmt_val = frmt_val;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public float getVal() {
		return val;
	}
	public void setVal(float val) {
		this.val = val;
	}


}
