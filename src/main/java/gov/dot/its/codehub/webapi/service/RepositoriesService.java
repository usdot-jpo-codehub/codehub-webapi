package gov.dot.its.codehub.webapi.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import gov.dot.its.codehub.webapi.model.ApiResponse;
import gov.dot.its.codehub.webapi.model.CHMetrics;
import gov.dot.its.codehub.webapi.model.CHRepository;
import gov.dot.its.codehub.webapi.model.CHSearchRequest;

public interface RepositoriesService {

	ApiResponse<List<CHRepository>> getRepositories(HttpServletRequest request, Map<String, String> params);

	ApiResponse<List<CHRepository>> getRepositoriesByIds(HttpServletRequest request, String[] ids);

	ApiResponse<CHMetrics> getMetrics(HttpServletRequest request, String[] owners);

	ApiResponse<List<CHRepository>> search(HttpServletRequest request, CHSearchRequest chSearchRequest);

}
