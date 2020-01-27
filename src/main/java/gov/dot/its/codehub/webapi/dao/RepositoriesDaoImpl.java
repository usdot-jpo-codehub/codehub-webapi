package gov.dot.its.codehub.webapi.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import gov.dot.its.codehub.webapi.model.CHRepository;
import gov.dot.its.codehub.webapi.model.CHSearchRequest;

@Repository
public class RepositoriesDaoImpl implements RepositoriesDao {

	@Value("${codehub.webapi.es.index.repositories}")
	private String reposIndex;
	@Value("${codehub.webapi.es.repositories.fields}")
	private String[] includedFields;
	@Value("${codehub.webapi.es.metrics.fields}")
	private String[] includedFieldsMetrics;
	@Value("${codehub.webapi.es.limit}")
	private int esDefaultLimit;

	private static final String HIGHLIGHTER_TYPE = "plain";
	private static final int FRAGMENT_SIZE = 5000;
	private static final int NUMBER_OF_FRAGMENTS = 5;
	private static final String CODEHUBDATA_IS_INGESTED = "codehubData.isIngested";
	private static final String CODEHUBDATA_IS_VISIBLE = "codehubData.isVisible";

	private RestHighLevelClient restHighLevelClient;

	public RepositoriesDaoImpl(RestHighLevelClient restHighLevelClient) {
		this.restHighLevelClient = restHighLevelClient;
	}

	@Override
	public List<CHRepository> getRepositories(int limit, String rank, String owner, String order) throws IOException {
		List<CHRepository> result = new ArrayList<>();
		SearchRequest searchRequest = new SearchRequest(reposIndex);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.size(limit);
		searchSourceBuilder.fetchSource(includedFields, new String[] {});

		QueryBuilder queryBuilder = this.generateQuery(rank, owner);
		searchSourceBuilder.query(queryBuilder);

		List<SortBuilder<?>> sortBuilders = this.generateSortOrder(rank, order);
		for(SortBuilder<?> sortBuilder: sortBuilders) {
			searchSourceBuilder.sort(sortBuilder);
		}

		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		SearchHit[] searchHits = searchResponse.getHits().getHits();
		for (SearchHit hit : searchHits) {
			Map<String, Object> sourceAsMap = hit.getSourceAsMap();

			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			CHRepository chRepository = mapper.convertValue(sourceAsMap, CHRepository.class);
			result.add(chRepository);
		}

		return result;
	}

	private List<SortBuilder<?>> generateSortOrder(String rank, String order) {
		List<SortBuilder<?>> sorts = new ArrayList<>();
		SortOrder sortOrder = SortOrder.DESC;
		if (!StringUtils.isEmpty(order) && order.equalsIgnoreCase("asc")) {
			sortOrder = SortOrder.ASC;
		}

		if (StringUtils.isEmpty(rank)) {
			sorts.add(new ScoreSortBuilder().order(sortOrder));
			return sorts;
		}

		switch(rank.toLowerCase()) {
		case "popular":
		case "featured":
			sorts.add(new FieldSortBuilder("generatedData.rank").order(sortOrder));
			return sorts;
		case "healthiest":
			sorts.add(new FieldSortBuilder("generatedData.sonarMetrics.reliability_rating.val").order(sortOrder));
			sorts.add(new FieldSortBuilder("generatedData.sonarMetrics.security_rating.val").order(sortOrder));
			sorts.add(new FieldSortBuilder("generatedData.sonarMetrics.sqale_rating.val").order(sortOrder));
			return sorts;
		default:
			sorts.add(new ScoreSortBuilder().order(sortOrder));
			return sorts;
		}
	}

	private QueryBuilder generateQuery(String rank, String owner) {
		QueryBuilder isIngestedQueryBuilder = QueryBuilders.termQuery(CODEHUBDATA_IS_INGESTED, true);
		QueryBuilder isVisibleQueryBuilder = QueryBuilders.termQuery(CODEHUBDATA_IS_VISIBLE, true);
		if (StringUtils.isEmpty(rank) && StringUtils.isEmpty(owner)) {
			return QueryBuilders.boolQuery()
					.must(isIngestedQueryBuilder)
					.must(isVisibleQueryBuilder);
		}

		QueryBuilder rankQuery = this.getQueryBuilderForRank(rank);
		QueryBuilder ownerQuery = this.getQueryBuilderForOwner(owner);

		if (rankQuery == null && ownerQuery == null) {
			return QueryBuilders.boolQuery()
					.must(isIngestedQueryBuilder)
					.must(isVisibleQueryBuilder);
		} else if (rankQuery != null && ownerQuery == null) {
			return QueryBuilders.boolQuery()
					.must(isVisibleQueryBuilder)
					.must(isIngestedQueryBuilder)
					.must(rankQuery);
		} else if (rankQuery == null) {
			return QueryBuilders.boolQuery()
					.must(isVisibleQueryBuilder)
					.must(isIngestedQueryBuilder)
					.must(ownerQuery);
		} else {
			return QueryBuilders.boolQuery()
					.must(isVisibleQueryBuilder)
					.must(isIngestedQueryBuilder)
					.must(ownerQuery)
					.must(rankQuery);
		}

	}

	private QueryBuilder getQueryBuilderForOwner(String owner) {
		if(StringUtils.isEmpty(owner)) {
			return null;
		}

		return QueryBuilders.termQuery("sourceData.owner.name.keyword", owner);
	}

	private QueryBuilder getQueryBuilderForRank(String rank) {
		if (StringUtils.isEmpty(rank)) {
			return null;
		}

		switch(rank.toLowerCase()) {
		case "popular": 
			return QueryBuilders.existsQuery("generatedData.rank");
		case "featured":
			return QueryBuilders.termQuery("codehubData.badges.isFeatured", true);
		case "healthiest":
			return QueryBuilders.existsQuery("generatedData.sonarMetrics.reliability_rating.val");

		default:
			return null;
		}

	}

	@Override
	public CHRepository getRepository(String id) throws IOException {
		if (StringUtils.isEmpty(id)) {
			return null;
		}

		GetRequest getRequest = new GetRequest(reposIndex, "_doc", id);
		GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
		if(!getResponse.isExists()) {
			return null;
		}

		Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		return mapper.convertValue(sourceAsMap, CHRepository.class);
	}

	@Override
	public List<CHRepository> getRepositoriesMetrics(int limit, String[] owners) throws IOException {
		SearchRequest searchRequest = new SearchRequest(reposIndex);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.size(limit);

		QueryBuilder queryBuilder = this.generateQueryForOwners(owners);
		searchSourceBuilder.query(queryBuilder);
		searchSourceBuilder.fetchSource(includedFieldsMetrics, new String[] {});
		searchRequest.source(searchSourceBuilder);

		SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		SearchHit[] searchHits = searchResponse.getHits().getHits();

		List<CHRepository> result = new ArrayList<>();
		for (SearchHit hit : searchHits) {
			Map<String, Object> sourceAsMap = hit.getSourceAsMap();

			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			CHRepository chRepository = mapper.convertValue(sourceAsMap, CHRepository.class);
			result.add(chRepository);
		}

		return result;
	}

	private QueryBuilder generateQueryForOwners(String[] owners) {
		QueryBuilder isIngestedQueryBuilder = QueryBuilders.termQuery(CODEHUBDATA_IS_INGESTED, true);
		QueryBuilder isVisibleQueryBuilder = QueryBuilders.termQuery(CODEHUBDATA_IS_VISIBLE, true);
		if (owners == null || owners.length==0) {
			return QueryBuilders.boolQuery()
					.must(isIngestedQueryBuilder)
					.must(isVisibleQueryBuilder);

		}

		return QueryBuilders.boolQuery()
				.must(isIngestedQueryBuilder)
				.must(isVisibleQueryBuilder)
				.must(QueryBuilders.termsQuery("sourceData.owner.name.keyword", owners));
	}

	@Override
	public List<CHRepository> searchRepositories(CHSearchRequest chSearchRequest) throws IOException {
		SearchRequest searchRequest = new SearchRequest(reposIndex);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.size(chSearchRequest.getLimit() == null ? esDefaultLimit : chSearchRequest.getLimit());

		QueryBuilder queryBuilder = this.generateSearchQuery(chSearchRequest);
		searchSourceBuilder.query(queryBuilder);

		searchSourceBuilder.fetchSource(includedFields, new String[] {});
		HighlightBuilder highlightBuilder = this.generateHighlightBuilder();
		searchSourceBuilder.highlighter(highlightBuilder);
		searchRequest.source(searchSourceBuilder);

		SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		SearchHit[] searchHits = searchResponse.getHits().getHits();

		List<CHRepository> result = new ArrayList<>();
		for (SearchHit hit : searchHits) {
			Map<String, Object> sourceAsMap = hit.getSourceAsMap();

			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			CHRepository chRepository = mapper.convertValue(sourceAsMap, CHRepository.class);

			Map<String, HighlightField> highlightFields = hit.getHighlightFields();
			for(Map.Entry<String, HighlightField> entry : highlightFields.entrySet()) {
				HighlightField field = entry.getValue();
				for(Text text : field.fragments()) {
					if (chRepository.getHighlights() != null && chRepository.getHighlights().containsKey(entry.getKey())) {
						chRepository.getHighlights().get(entry.getKey()).add(text.toString());
					} else {
						List<String> frags = new ArrayList<>();
						frags.add(text.toString());
						chRepository.getHighlights().put(entry.getKey(), frags);
					}
				}
			}

			result.add(chRepository);
		}

		return result;
	}

	private QueryBuilder generateSearchQuery(CHSearchRequest chSearchRequest) {
		QueryBuilder isIngestedQueryBuilder = QueryBuilders.termQuery(CODEHUBDATA_IS_INGESTED, true);
		QueryBuilder isVisibleQueryBuilder = QueryBuilders.termQuery(CODEHUBDATA_IS_VISIBLE, true);
		if (StringUtils.isEmpty(chSearchRequest.getTerm())) {
			return QueryBuilders.boolQuery()
					.must(isVisibleQueryBuilder)
					.must(isIngestedQueryBuilder);
		}

		Operator queryOperator = Operator.OR;
		if (chSearchRequest.getMatchAll() != null && chSearchRequest.getMatchAll()) {
			queryOperator = Operator.AND;
		}

		return QueryBuilders.boolQuery()
				.must(isVisibleQueryBuilder)
				.must(isIngestedQueryBuilder)
				.must(QueryBuilders.multiMatchQuery(chSearchRequest.getTerm(), "sourceData.name", "sourceData.description", "sourceData.readme.content").operator(queryOperator));
	}

	private HighlightBuilder generateHighlightBuilder() {
		HighlightBuilder highlightBuilder = new HighlightBuilder();
		highlightBuilder.preTags("<em class=\"search-highlight\">");
		highlightBuilder.postTags("</em>");

		HighlightBuilder.Field highlightName = new HighlightBuilder.Field("sourceData.name");
		highlightName.highlighterType(HIGHLIGHTER_TYPE);
		highlightName.fragmentSize(FRAGMENT_SIZE);
		highlightName.numOfFragments(NUMBER_OF_FRAGMENTS);
		highlightBuilder.field(highlightName);

		HighlightBuilder.Field highlightDescription = new HighlightBuilder.Field("sourceData.description");
		highlightDescription.highlighterType(HIGHLIGHTER_TYPE);
		highlightDescription.fragmentSize(FRAGMENT_SIZE);
		highlightDescription.numOfFragments(NUMBER_OF_FRAGMENTS);
		highlightBuilder.field(highlightDescription);

		HighlightBuilder.Field highlightTags = new HighlightBuilder.Field("sourceData.readme.content");
		highlightTags.highlighterType(HIGHLIGHTER_TYPE);
		highlightTags.fragmentSize(FRAGMENT_SIZE);
		highlightTags.numOfFragments(NUMBER_OF_FRAGMENTS);
		highlightBuilder.field(highlightTags);

		return highlightBuilder;
	}

}
