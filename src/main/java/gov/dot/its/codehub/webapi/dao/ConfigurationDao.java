package gov.dot.its.codehub.webapi.dao;

import java.io.IOException;
import java.util.List;

import gov.dot.its.codehub.webapi.model.CHCategory;
import gov.dot.its.codehub.webapi.model.CHConfiguration;


public interface ConfigurationDao {

	CHConfiguration getConfiguration() throws IOException;
	
	List<CHCategory> getCategories() throws IOException;

	CHCategory getCategoryById(String id) throws IOException;

}
