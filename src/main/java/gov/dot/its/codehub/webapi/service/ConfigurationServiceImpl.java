package gov.dot.its.codehub.webapi.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.elasticsearch.ElasticsearchStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import gov.dot.its.codehub.webapi.dao.ConfigurationDao;
import gov.dot.its.codehub.webapi.model.ApiError;
import gov.dot.its.codehub.webapi.model.ApiMessage;
import gov.dot.its.codehub.webapi.model.ApiResponse;
import gov.dot.its.codehub.webapi.model.CHCategory;
import gov.dot.its.codehub.webapi.model.CHEngagementPopup;
import gov.dot.its.codehub.webapi.utils.ApiUtils;

@Service
public class ConfigurationServiceImpl implements ConfigurationService {

	private static final Logger logger = LoggerFactory.getLogger(ConfigurationServiceImpl.class);

	private static final String MESSAGE_TEMPLATE = "{} : {} ";

	@Autowired
	private ConfigurationDao configurationDao;

	@Autowired
	private ApiUtils apiUtils;

	@Value("${codehub.webapi.debug}")
	private boolean debug;


	@Override
	public ApiResponse<List<CHCategory>> categories(HttpServletRequest request) {
		logger.info("Request: categories");
		final String RESPONSE_MSG = "Response: GET Categories. ";

		ApiResponse<List<CHCategory>> apiResponse = new ApiResponse<>();
		List<ApiError> errors = new ArrayList<>();

		try {

			List<CHCategory> categories = configurationDao.getCategories();

			if (categories != null && !categories.isEmpty()) {
				apiResponse.setResponse(HttpStatus.OK, categories, null, null, request);
				logger.info(MESSAGE_TEMPLATE, RESPONSE_MSG,HttpStatus.OK.toString()+" "+ categories.size());
				return apiResponse;
			}

			apiResponse.setResponse(HttpStatus.NO_CONTENT, null, null, null, request);
			logger.info(MESSAGE_TEMPLATE, RESPONSE_MSG, HttpStatus.NO_CONTENT.toString());
			return apiResponse;


		} catch(ElasticsearchStatusException | IOException e) {
			return apiResponse.setResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, null, apiUtils.getErrorsFromException(errors, e), request);
		}
	}


	@Override
	public ApiResponse<CHCategory> category(HttpServletRequest request, String id) {
		logger.info("Request: Get by ID Category");
		final String RESPONSE_MSG = "Response: GET Category. ";

		ApiResponse<CHCategory> apiResponse = new ApiResponse<>();
		List<ApiError> errors = new ArrayList<>();
		List<ApiMessage> messages = new ArrayList<>();

		try {

			CHCategory category = configurationDao.getCategoryById(id);

			if (category != null) {
				apiResponse.setResponse(HttpStatus.OK, category, messages, null, request);
				logger.info(MESSAGE_TEMPLATE, RESPONSE_MSG,HttpStatus.OK.toString());
				return apiResponse;
			}

			apiResponse.setResponse(HttpStatus.NOT_FOUND, null, null, null, request);
			logger.info(MESSAGE_TEMPLATE, RESPONSE_MSG, HttpStatus.NOT_FOUND.toString());
			return apiResponse;


		} catch(ElasticsearchStatusException | IOException e) {
			return apiResponse.setResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, null, apiUtils.getErrorsFromException(errors, e), request);
		}
	}


	@Override
	public ApiResponse<List<CHEngagementPopup>> engagementpopups(HttpServletRequest request) {
		logger.info("Request: Engagement Popups");
		final String RESPONSE_MSG = "Response: GET Engagement Popups. ";

		ApiResponse<List<CHEngagementPopup>> apiResponse = new ApiResponse<>();
		List<ApiError> errors = new ArrayList<>();

		try {

			List<CHEngagementPopup> engagementPopups = configurationDao.getEngagementPopups();

			if (engagementPopups != null && !engagementPopups.isEmpty()) {
				apiResponse.setResponse(HttpStatus.OK, engagementPopups, null, null, request);
				logger.info(MESSAGE_TEMPLATE, RESPONSE_MSG,HttpStatus.OK.toString()+" "+ engagementPopups.size());
				return apiResponse;
			}

			apiResponse.setResponse(HttpStatus.NO_CONTENT, null, null, null, request);
			logger.info(MESSAGE_TEMPLATE, RESPONSE_MSG, HttpStatus.NO_CONTENT.toString());
			return apiResponse;


		} catch(ElasticsearchStatusException | IOException e) {
			return apiResponse.setResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, null, apiUtils.getErrorsFromException(errors, e), request);
		}
	}

}
