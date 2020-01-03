package gov.dot.its.codehub.webapi.dao;

import java.io.IOException;
import java.util.List;

import gov.dot.its.codehub.webapi.model.CHRepository;
import gov.dot.its.codehub.webapi.model.CHSearchRequest;

public interface RepositoriesDao {

	List<CHRepository> getRepositories(int limit, String rank, String owner, String order) throws IOException;

	CHRepository getRepository(String id) throws IOException;

	List<CHRepository> getRepositoriesMetrics(int limit, String[] owners) throws IOException;

	List<CHRepository> searchRepositories(CHSearchRequest chSearchRequest) throws IOException;

}
