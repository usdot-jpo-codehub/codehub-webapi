package gov.dot.its.codehub.webapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class CHMetricsSummaryBaseTest {

	@Test
	public void testInstance() {
		CHMetricsSummaryBase<Object> metricsSummaryBase = new CHMetricsSummaryBase<>();
		assertNotNull(metricsSummaryBase);
	}

	@Test
	public void testValues() {
		String msg = "Testing";
		CHMetricsSummaryBase<String> metricsSummaryBase = new CHMetricsSummaryBase<>();
		metricsSummaryBase.setKeyValue("test", msg);
		assertEquals(msg, metricsSummaryBase.getValue("test"));
	}

	@Test
	public void testKeyValue() {
		CHMetricsSummaryBase<Integer> metricsSummaryBase = new CHMetricsSummaryBase<>();
		metricsSummaryBase.addKeyValue("one", 1);
		metricsSummaryBase.addKeyValue("one", 2);
		assertEquals(1, metricsSummaryBase.getValue("one"));
	}

	@Test
	public void testGetValue() {
		CHMetricsSummaryBase<Integer> metricsSummaryBase = new CHMetricsSummaryBase<>();
		metricsSummaryBase.addKeyValue("uno", 1);
		assertEquals(1, metricsSummaryBase.getValue("uno"));
	}

	@Test
	public void testGetValueInvalidKey() {
		CHMetricsSummaryBase<Integer> metricsSummaryBase = new CHMetricsSummaryBase<>();
		metricsSummaryBase.addKeyValue("uno", 1);
		assertNull(metricsSummaryBase.getValue("dos"));
	}

	@Test
	public void testInitializeValuesInvalid() {
		CHMetricsSummaryBase<Integer> metricsSummaryBase = new CHMetricsSummaryBase<>();
		metricsSummaryBase.initializeValues(null);
		assertEquals(0, metricsSummaryBase.getValues().size());
	}

	@Test
	public void testInitializeValues() {
		CHMetricsSummaryBase<Integer> metricsSummaryBase = new CHMetricsSummaryBase<>();
		Map<String, Integer> testValues = new HashMap<>();
		testValues.put("uno", 1);
		testValues.put("dos", 2);
		metricsSummaryBase.initializeValues(testValues);
		assertEquals(2, metricsSummaryBase.getValues().size());
		assertEquals(1, metricsSummaryBase.getValue("uno"));
	}
}