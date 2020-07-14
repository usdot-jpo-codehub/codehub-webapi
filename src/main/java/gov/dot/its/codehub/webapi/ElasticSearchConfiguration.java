package gov.dot.its.codehub.webapi;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
@ConfigurationProperties(prefix = "codehub.webapi.es")
public class ElasticSearchConfiguration extends AbstractFactoryBean<RestHighLevelClient> {

	private static final Logger msgLogger = LoggerFactory.getLogger(ElasticSearchConfiguration.class);

	@Value("${codehub.webapi.es.host}")
	private String host;
	@Value("${codehub.webapi.es.port}")
	private int port;
	@Value("${codehub.webapi.es.scheme}")
	private String scheme;

	private RestHighLevelClient restHighLevelClient;

	@Override
	public void destroy() {
		try {
			if (restHighLevelClient != null) {
				restHighLevelClient.close();
			}
		} catch (final Exception e) {
			msgLogger.error("Error closing ElasticSearch client: ", e);
		}
	}

	@Override
	public Class<RestHighLevelClient> getObjectType() {
		return RestHighLevelClient.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

	@Override
	public RestHighLevelClient createInstance() {
		return buildClient();
	}

	private RestHighLevelClient buildClient() {
		try {
			restHighLevelClient = new RestHighLevelClient(
					RestClient.builder(
							new HttpHost(host, port, scheme)
							));
		} catch (Exception e) {
			msgLogger.error(e.getMessage());
		}
		return restHighLevelClient;
	}
}

