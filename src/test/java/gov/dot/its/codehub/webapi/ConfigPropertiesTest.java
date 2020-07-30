package gov.dot.its.codehub.webapi;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class ConfigPropertiesTest {

	@Test
	public void testInstance() {
		ConfigProperties configProperties = new ConfigProperties();
		assertNotNull(configProperties);
	}

	@Test
	public void testIsDebug() {
		ConfigProperties configProperties = new ConfigProperties();
		configProperties.setDebug(true);
		assertTrue(configProperties.isDebug());
	}
}
