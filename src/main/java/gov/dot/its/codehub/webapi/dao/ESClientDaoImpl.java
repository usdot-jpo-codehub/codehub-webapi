package gov.dot.its.codehub.webapi.dao;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import org.elasticsearch.action.ActionListener;
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
		CompletableFuture<SearchResponse> cf = new CompletableFuture<>();
		restHighLevelClient.searchAsync(searchRequest, requestOptions, new ActionListener<SearchResponse>(){

			@Override
			public void onResponse(SearchResponse response) {
				cf.complete(response);
			}

			@Override
			public void onFailure(Exception e) {
				cf.completeExceptionally(e);
			}
		});
		return cf.join();
	}

	@Override
	public GetResponse get(GetRequest getRequest, RequestOptions requestOptions) throws IOException {

		CompletableFuture<GetResponse> cf = new CompletableFuture<>();
		restHighLevelClient.getAsync(getRequest, requestOptions, new ActionListener<GetResponse>(){

			@Override
			public void onResponse(GetResponse response) {
				cf.complete(response);
			}

			@Override
			public void onFailure(Exception e) {
				cf.completeExceptionally(e);
			}

		} );

		return cf.join();
	}

}
