package gov.dot.its.codehub.webapi;

import java.io.PrintStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class CodehubWebapiApplication {

	private static final Logger logger = LoggerFactory.getLogger(CodehubWebapiApplication.class);

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(CodehubWebapiApplication.class);
		app.setLogStartupInfo(false);
		Banner banner = new Banner() {
			static final String MESSAGE_TEMPLATE = "%s = %s";
			static final String ES_TEMPLATE = "%s = %s://%s:%s";
			@Override
			public void printBanner(Environment environment, Class<?> sourceClass, PrintStream out) {
				out.println(new String(new char[80]).replace("\0", "_"));
				out.println();
				out.println("  *****                       *     *                  *     *                  *             ");
				out.println(" *     *  ****  *****  ****** *     * *    * *****     *  *  * ****** *****    * *   *****  * ");
				out.println(" *       *    * *    * *      *     * *    * *    *    *  *  * *      *    *  *   *  *    * * ");
				out.println(" *       *    * *    * *****  ******* *    * *****     *  *  * *****  *****  *     * *    * * ");
				out.println(" *       *    * *    * *      *     * *    * *    *    *  *  * *      *    * ******* *****  * ");
				out.println(" *     * *    * *    * *      *     * *    * *    *    *  *  * *      *    * *     * *      * ");
				out.println("  *****   ****  *****  ****** *     *  ****  *****      ** **  ****** *****  *     * *      * ");
				out.println();
				out.println("CodeHub WebAPI");
				out.println();
				out.println(String.format(MESSAGE_TEMPLATE, "Port", environment.getProperty("server.port")));
				out.println(String.format(
						ES_TEMPLATE, "Elasticsearch",
						environment.getProperty("codehub.webapi.es.scheme"),
						environment.getProperty("codehub.webapi.es.host"),
						environment.getProperty("codehub.webapi.es.port")
						));
				out.println(String.format(MESSAGE_TEMPLATE, "Target Index", environment.getProperty("codehub.webapi.es.index.repositories")));
				out.println(String.format(MESSAGE_TEMPLATE, "Debug", environment.getProperty("codehub.webapi.debug")));
				out.println(String.format(MESSAGE_TEMPLATE, "Tomcat.max-threads", environment.getProperty("server.tomcat.max-threads")));
				out.println(String.format(MESSAGE_TEMPLATE, "Servlet.context-path", environment.getProperty("server.servlet.context-path")));

				out.println(new String(new char[80]).replace("\0", "_"));
			}

		};
		app.setBanner(banner);
		app.run();
		logger.info("ITS CodeHub WebAPI Started");
	}

}
