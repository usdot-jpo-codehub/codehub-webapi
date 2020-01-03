package gov.dot.its.codehub.webapi.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CHRepository {
	private String id;
	private CHSourceData sourceData;
	private CHGeneratedData generatedData;
	private CHCodehubData codehubData;
	private Map<String, List<String>> highlights;

	public CHRepository() {
		this.sourceData = new CHSourceData();
		this.generatedData = new CHGeneratedData();
		this.codehubData = new CHCodehubData();
		this.highlights = new HashMap<>();
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public CHSourceData getSourceData() {
		return sourceData;
	}

	public void setSourceData(CHSourceData sourceData) {
		this.sourceData = sourceData;
	}

	public CHGeneratedData getGeneratedData() {
		return generatedData;
	}

	public void setGeneratedData(CHGeneratedData generatedData) {
		this.generatedData = generatedData;
	}

	public CHCodehubData getCodehubData() {
		return codehubData;
	}

	public void setCodehubData(CHCodehubData codehubData) {
		this.codehubData = codehubData;
	}

	public Map<String, List<String>> getHighlights() {
		return highlights;
	}

	public void setHighlights(Map<String, List<String>> highlights) {
		this.highlights = highlights;
	}


}
