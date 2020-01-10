package gov.dot.its.codehub.webapi.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.elasticsearch.ElasticsearchStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import gov.dot.its.codehub.webapi.dao.RelatedDao;
import gov.dot.its.codehub.webapi.dao.RepositoriesDao;
import gov.dot.its.codehub.webapi.model.ApiError;
import gov.dot.its.codehub.webapi.model.ApiMessage;
import gov.dot.its.codehub.webapi.model.ApiResponse;
import gov.dot.its.codehub.webapi.model.CHMetrics;
import gov.dot.its.codehub.webapi.model.CHRepository;
import gov.dot.its.codehub.webapi.model.CHSearchRequest;
import gov.dot.its.codehub.webapi.model.CHSonarMetrics;
import gov.dot.its.codehub.webapi.model.RelatedItemModel;
import gov.dot.its.codehub.webapi.utils.ApiUtils;

@Service
public class RepositoriesServiceImpl implements RepositoriesService {

	private static final Logger logger = LoggerFactory.getLogger(RepositoriesServiceImpl.class);

	private static final String MESSAGE_TEMPLATE = "%s : %s ";

	@Value("${codehub.webapi.es.limit}")
	private int esDefaultLimit;

	@Autowired
	private ApiUtils apiUtils;

	@Autowired
	private RepositoriesDao repositoriesDao;

	@Autowired
	private RelatedDao relatedDao;

	@Override
	public ApiResponse<List<CHRepository>> getRepositories(HttpServletRequest request, Map<String, String> params) {

		ApiResponse<List<CHRepository>> apiResponse = new ApiResponse<>();
		List<ApiError> errors = new ArrayList<>();

		int limit = apiUtils.getQueryParamInteger(params, "limit", esDefaultLimit);
		String rank = apiUtils.getQueryParamString(params, "rank", null);
		String owner = apiUtils.getQueryParamString(params, "owner", null);
		String order = apiUtils.getQueryParamString(params, "order", "desc");

		logger.info(String.format("GET Repositories: limit=%s, rank=%s, owner=%s", String.valueOf(limit), rank, owner));

		try {
			List<CHRepository> data = repositoriesDao.getRepositories(limit, rank, owner, order);

			if (data != null && !data.isEmpty()) {
				logger.info(String.format(MESSAGE_TEMPLATE, HttpStatus.OK, String.valueOf(data.size())));
				return apiResponse.setResponse(HttpStatus.OK, data, null, null, request);
			}
			logger.info(String.format(MESSAGE_TEMPLATE, HttpStatus.NO_CONTENT, "[]"));
			return apiResponse.setResponse(HttpStatus.NO_CONTENT, null, null, null, request);

		} catch(ElasticsearchStatusException | IOException e) {
			logger.info(String.format(MESSAGE_TEMPLATE, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
			return apiResponse.setResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, null, apiUtils.getErrorsFromException(errors, e), request);
		}

	}

	@Override
	public ApiResponse<List<CHRepository>> getRepositoriesByIds(HttpServletRequest request, String[] ids) {
		ApiResponse<List<CHRepository>> apiResponse = new ApiResponse<>();
		List<ApiError> errors = new ArrayList<>();
		List<ApiMessage> messages = new ArrayList<>();
		logger.info(String.format(MESSAGE_TEMPLATE, "Get Repositories by IDs", ids));
		if (ids.length == 0) {
			logger.info(String.format(MESSAGE_TEMPLATE, HttpStatus.NO_CONTENT, ids));
			return apiResponse.setResponse(HttpStatus.NO_CONTENT, null, null, null, request);
		}

		List<CHRepository> repositories = new ArrayList<>();
		boolean partialContent = false;
		for(String id: ids) {
			try {
				CHRepository repository = repositoriesDao.getRepository(id);
				if(repository != null) {
					logger.info(String.format(MESSAGE_TEMPLATE, HttpStatus.OK, id));
					messages.add(new ApiMessage(String.format(MESSAGE_TEMPLATE, HttpStatus.OK, id)));

					repository.setRelated(this.getRelatedInformation(id));

					repositories.add(repository);
				} else {
					partialContent = true;
					logger.info(String.format(MESSAGE_TEMPLATE, HttpStatus.NO_CONTENT, id));
					messages.add(new ApiMessage(String.format(MESSAGE_TEMPLATE, HttpStatus.NO_CONTENT, id)));
				}


			} catch(ElasticsearchStatusException | IOException e) {
				logger.info(String.format(MESSAGE_TEMPLATE, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
				errors.add(new ApiError(String.format("%s : %s : %s", HttpStatus.INTERNAL_SERVER_ERROR, id, e.getMessage())));
			}
		}

		HttpStatus httpStatus = HttpStatus.OK;
		if (!errors.isEmpty() || partialContent) {
			httpStatus = HttpStatus.PARTIAL_CONTENT;
		}

		return apiResponse.setResponse(httpStatus, repositories, messages, errors, request);

	}


	@Override
	public ApiResponse<CHMetrics> getMetrics(HttpServletRequest request, String[] owners) {
		ApiResponse<CHMetrics> apiResponse = new ApiResponse<>();
		List<ApiError> errors = new ArrayList<>();

		try {
			List<CHRepository> repositories = repositoriesDao.getRepositoriesMetrics(esDefaultLimit, owners);
			if(repositories == null || repositories.isEmpty()) {
				logger.info(String.format(MESSAGE_TEMPLATE, HttpStatus.NO_CONTENT, "Repository data not available."));
				return apiResponse.setResponse(HttpStatus.NO_CONTENT, null, null, null, request);
			}

			CHMetrics metrics = this.calculateMetrics(repositories);
			if (metrics == null) {
				logger.info(String.format(MESSAGE_TEMPLATE, HttpStatus.NO_CONTENT, "Empty metrics data."));
				return apiResponse.setResponse(HttpStatus.NO_CONTENT, null, null, null, request);
			}

			logger.info(String.format("%s : %s %s", HttpStatus.OK, "Metrics", owners == null ? "" : " Owner: "+String.join(", ", owners)));
			return apiResponse.setResponse(HttpStatus.OK, metrics, null, null, request);

		} catch(ElasticsearchStatusException | IOException e) {
			logger.info(String.format(MESSAGE_TEMPLATE, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
			return apiResponse.setResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, null, apiUtils.getErrorsFromException(errors, e), request);
		}
	}

	private CHMetrics calculateMetrics(List<CHRepository> repositories) {
		if (repositories == null || repositories.isEmpty()) {
			return null;
		}

		CHMetrics metrics = new CHMetrics();
		Map<String, Long> initialKeys = new HashMap<>();
		initialKeys.put("A", 0L);
		initialKeys.put("B", 0L);
		initialKeys.put("C", 0L);
		initialKeys.put("D", 0L);
		initialKeys.put("E", 0L);
		metrics.getMetricsSummary().getSecurity().initializeValues(initialKeys);
		metrics.getMetricsSummary().getReliability().initializeValues(initialKeys);
		metrics.getMetricsSummary().getMaintainability().initializeValues(initialKeys);

		float technicalDebt = 0;
		float bugsAndVulnerabilities = 0;
		int numberOfProjects = 0;
		for(CHRepository repository: repositories) {

			numberOfProjects += 1;
			//Gets unique organizations/owners
			if (!metrics.getOrganizations().contains(repository.getSourceData().getOwner().getName())) {
				metrics.getOrganizations().add(repository.getSourceData().getOwner().getName());
			}

			//calculate languages count
			if (repository.getSourceData().getLanguage() != null) {
				if(!metrics.getLanguageCountsStat().containsKey(repository.getSourceData().getLanguage())) {
					metrics.getLanguageCountsStat().put(repository.getSourceData().getLanguage(),1);
				} else {
					metrics.getLanguageCountsStat().put(repository.getSourceData().getLanguage(),
							metrics.getLanguageCountsStat().get(repository.getSourceData().getLanguage()) + 1);
				}
			}

			//Get sonar data
			CHSonarMetrics sonar = repository.getGeneratedData().getSonarMetrics();

			//calculate Technical Debt
			technicalDebt += sonar.getSqale_index().getVal();

			//calculate bugs and vulnerabilities
			bugsAndVulnerabilities += sonar.getBugs().getVal() + sonar.getVulnerabilities().getVal();

			//calculate security_rating
			String securityKey = sonar.getSecurity_rating().getFrmt_val();
			if (securityKey != null) {
				Long securityCurrent = metrics.getMetricsSummary().getSecurity().getValue(securityKey);
				metrics.getMetricsSummary().getSecurity().setKeyValue(securityKey, securityCurrent == null ? 1 : securityCurrent + 1);
			}

			//calculate reliability_rating
			String reliabilityKey = sonar.getReliability_rating().getFrmt_val();
			if (reliabilityKey != null) {
				Long reliabilityCurrent = metrics.getMetricsSummary().getReliability().getValue(reliabilityKey);
				metrics.getMetricsSummary().getReliability().setKeyValue(reliabilityKey, reliabilityCurrent == null ? 1 : reliabilityCurrent + 1);
			}

			//calculate sqale_rating
			String sqaleKey = sonar.getSqale_rating().getFrmt_val();
			if (sqaleKey != null) {
				Long sqaleCurrent = metrics.getMetricsSummary().getMaintainability().getValue(sqaleKey);
				metrics.getMetricsSummary().getMaintainability().setKeyValue(sqaleKey, sqaleCurrent == null ? 1 : sqaleCurrent + 1);
			}

		}

		//calculate language percentage
		int totalLanguageCount = 0;
		for(Map.Entry<String, Integer> entry: metrics.getLanguageCountsStat().entrySet()) { totalLanguageCount += entry.getValue(); }
		for(Map.Entry<String, Integer> entry: metrics.getLanguageCountsStat().entrySet()) {
			int val = metrics.getLanguageCountsStat().get(entry.getKey());
			float percentage = 100 * (val/(float)totalLanguageCount);
			metrics.getLanguagePercentageStat().put(entry.getKey(), percentage);
		}

		metrics.setNumberOfOrganizations(metrics.getOrganizations().size());
		metrics.setNumberOfProjects(numberOfProjects);
		metrics.setTechnicalDebt((long)technicalDebt);
		metrics.setBugsVulnerabilities((long)bugsAndVulnerabilities);

		return metrics;
	}

	@Override
	public ApiResponse<List<CHRepository>> search(HttpServletRequest request, CHSearchRequest chSearchRequest) {
		ApiResponse<List<CHRepository>> apiResponse = new ApiResponse<>();
		List<ApiError> errors = new ArrayList<>();
		List<ApiMessage> messages = new ArrayList<>();
		logger.info("Search request");

		if (StringUtils.isEmpty(chSearchRequest.getTerm())) {
			String msg = String.format(MESSAGE_TEMPLATE, HttpStatus.NO_CONTENT, "Empty search term");
			logger.info(msg);
			messages.add(new ApiMessage(msg));
			return apiResponse.setResponse(HttpStatus.NO_CONTENT, null, messages, null, request);
		}

		try {
			List<CHRepository> repositories = repositoriesDao.searchRepositories(chSearchRequest);
			if(repositories == null || repositories.isEmpty()) {
				logger.info(String.format(MESSAGE_TEMPLATE, HttpStatus.NO_CONTENT, "Repository data not available."));
				return apiResponse.setResponse(HttpStatus.NO_CONTENT, null, null, null, request);
			}
			String msg = String.format(MESSAGE_TEMPLATE, HttpStatus.OK, repositories.size());
			logger.info(msg);
			messages.add(new ApiMessage(msg));
			return apiResponse.setResponse(HttpStatus.OK, repositories, messages, null, request);

		} catch(ElasticsearchStatusException | IOException e) {
			logger.info(String.format(MESSAGE_TEMPLATE, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
			return apiResponse.setResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, null, apiUtils.getErrorsFromException(errors, e), request);
		}
	}

	private List<RelatedItemModel> getRelatedInformation(String id) {
		List<RelatedItemModel> relatedItems = new ArrayList<>();
		try {
			relatedItems = relatedDao.getRelatedItems(id);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return relatedItems;
	}

}
