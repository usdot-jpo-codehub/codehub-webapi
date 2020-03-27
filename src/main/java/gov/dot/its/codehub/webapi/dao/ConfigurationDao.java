package gov.dot.its.codehub.webapi.dao;

import java.io.IOException;
import java.util.List;

import gov.dot.its.codehub.webapi.model.CHCategory;
import gov.dot.its.codehub.webapi.model.CHConfiguration;
import gov.dot.its.codehub.webapi.model.CHEngagementPopup;


public interface ConfigurationDao {

	CHConfiguration getConfiguration() throws IOException;
	
	List<CHCategory> getCategories() throws IOException;

	CHCategory getCategoryById(String id) throws IOException;

	List<CHEngagementPopup> getEngagementPopups() throws IOException;

}
