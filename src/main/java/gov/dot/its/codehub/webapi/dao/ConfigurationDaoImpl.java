package gov.dot.its.codehub.webapi.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import gov.dot.its.codehub.webapi.model.CHCategory;
import gov.dot.its.codehub.webapi.model.CHConfiguration;
import gov.dot.its.codehub.webapi.model.CHEngagementPopup;


@Repository
public class ConfigurationDaoImpl implements ConfigurationDao {

	@Value("${codehub.webapi.configurations.index}")
	private String configurationsIndex;

	@Value("${codehub.webapi.configurations.default}")
	private String configurationId;

	private RestHighLevelClient restHighLevelClient;

	public ConfigurationDaoImpl(RestHighLevelClient restHighLevelClient) {
		this.restHighLevelClient = restHighLevelClient;
	}

	@Override
	public CHConfiguration getConfiguration() throws IOException {
		GetRequest getRequest = new GetRequest(configurationsIndex, "_doc", configurationId);
		GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
		if (!getResponse.isExists()) {
			return null;
		}

		Map<String, Object> sourceMap = getResponse.getSourceAsMap();

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		return mapper.convertValue(sourceMap, CHConfiguration.class);
	}

	@Override
	public List<CHCategory> getCategories() throws IOException {
		GetRequest getRequest = new GetRequest(configurationsIndex, "_doc", configurationId);
		GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
		if (!getResponse.isExists()) {
			return new ArrayList<>();
		}

		Map<String, Object> sourceMap = getResponse.getSourceAsMap();

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		CHConfiguration configuration = mapper.convertValue(sourceMap, CHConfiguration.class);

		return configuration.getCategories().stream().filter(CHCategory::isEnabled).collect(Collectors.toList());

	}

	@Override
	public CHCategory getCategoryById(String id) throws IOException {
		CHConfiguration configuration = getConfiguration();
		if (configuration == null) {
			return null;
		}

		for(CHCategory category: configuration.getCategories()) {
			if (category.getId().equalsIgnoreCase(id) && category.isEnabled()) {
				return category;
			}
		}

		return null;
	}

	@Override
	public List<CHEngagementPopup> getEngagementPopups() throws IOException {
		GetRequest getRequest = new GetRequest(configurationsIndex, "_doc", configurationId);
		GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
		if (!getResponse.isExists()) {
			return new ArrayList<>();
		}

		Map<String, Object> sourceMap = getResponse.getSourceAsMap();

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		CHConfiguration configuration = mapper.convertValue(sourceMap, CHConfiguration.class);

		return configuration.getEngagementPopups().stream().filter(CHEngagementPopup::isActive).collect(Collectors.toList());
	}

}
