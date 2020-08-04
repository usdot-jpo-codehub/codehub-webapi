package gov.dot.its.codehub.webapi;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class CodehubWebapiApplicationTests {

	@Test
	public void testMain() {
		CodehubWebapiApplication.main(new String[]{});
		assertTrue(true);
	}

}
