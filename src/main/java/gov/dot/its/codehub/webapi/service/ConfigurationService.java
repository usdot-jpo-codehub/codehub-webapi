package gov.dot.its.codehub.webapi.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import gov.dot.its.codehub.webapi.model.ApiResponse;
import gov.dot.its.codehub.webapi.model.CHCategory;

public interface ConfigurationService {

	ApiResponse<List<CHCategory>> categories(HttpServletRequest request);

	ApiResponse<CHCategory> category(HttpServletRequest request, String id);

}
