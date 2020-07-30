package gov.dot.its.codehub.webapi.dao;

import java.io.IOException;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Repository;

@Repository
public class ESClientDaoImpl implements ESClientDao {

	private RestHighLevelClient restHighLevelClient;

	public ESClientDaoImpl(RestHighLevelClient restHighLevelClient) {
		this.restHighLevelClient = restHighLevelClient;
	}

	@Override
	public SearchResponse search(SearchRequest searchRequest, RequestOptions requestOptions) throws IOException {
		return restHighLevelClient.search(searchRequest, requestOptions);
	}

	@Override
	public GetResponse get(GetRequest getRequest, RequestOptions requestOptions) throws IOException {
		return restHighLevelClient.get(getRequest, requestOptions);
	}

}
