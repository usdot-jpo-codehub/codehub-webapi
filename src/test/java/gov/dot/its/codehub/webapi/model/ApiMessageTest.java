package gov.dot.its.codehub.webapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class ApiMessageTest {
	final String TEST_MESSAGE = "Test";

	@Test
	public void testInstance() {
		ApiMessage apiMessage = new ApiMessage();
		assertNotNull(apiMessage);
	}

	@Test
	public void testInstanceWithMessage() {
		ApiMessage apiMessage = new ApiMessage(TEST_MESSAGE);
		assertNotNull(apiMessage);
	}

	@Test
	public void testGetMessage() {
		ApiMessage apiMessage = new ApiMessage(TEST_MESSAGE);
		assertEquals(TEST_MESSAGE, apiMessage.getMessage());
	}

	@Test
	public void testSetMessage() {
		ApiMessage apiMessage = new ApiMessage();
		apiMessage.setMessage(TEST_MESSAGE);
		assertEquals(TEST_MESSAGE, apiMessage.getMessage());
	}
}
