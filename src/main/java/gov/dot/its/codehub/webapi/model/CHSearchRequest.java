package gov.dot.its.codehub.webapi.model;

public class CHSearchRequest {
	private String term;
	private Integer limit;
	private Boolean matchAll;

	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public Integer getLimit() {
		return limit;
	}
	public void setLimit(Integer limit) {
		this.limit = limit;
	}
	public Boolean getMatchAll() {
		return matchAll;
	}
	public void setMatchAll(Boolean matchAll) {
		this.matchAll = matchAll;
	}


}
