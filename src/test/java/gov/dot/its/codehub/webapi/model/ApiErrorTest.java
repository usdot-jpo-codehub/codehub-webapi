package gov.dot.its.codehub.webapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class ApiErrorTest {

	final String TEST_MESSAGE = "Test";

	@Test
	public void testInstance() {
		ApiError apiError = new ApiError();
		assertNotNull(apiError);
	}

	@Test
	public void testIntanceWithValue() {
		ApiError apiError = new ApiError(TEST_MESSAGE);
		assertNotNull(apiError);
	}

	@Test
	public void testGetMessage() {
		ApiError apiError = new ApiError(TEST_MESSAGE);
		assertEquals(TEST_MESSAGE, apiError.getMessage());
	}

	@Test
	public void testSetMessage() {
		ApiError apiError = new ApiError();
		apiError.setMessage(TEST_MESSAGE);
		assertEquals(TEST_MESSAGE, apiError.getMessage());
	}
}
