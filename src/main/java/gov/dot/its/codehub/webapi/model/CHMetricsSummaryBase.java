package gov.dot.its.codehub.webapi.model;

import java.util.HashMap;
import java.util.Map;

public class CHMetricsSummaryBase<T> {
	private Map<String, T> values;

	public CHMetricsSummaryBase() {
		this.values = new HashMap<>();
	}

	public Map<String, T> getValues() {
		return values;
	}

	public void setValues(Map<String, T> values) {
		this.values = values;
	}

	public void addKeyValue(String key, T value) {
		if (values.containsKey(key.toLowerCase())) {
			return;
		}

		values.put(key.toLowerCase(), value);
	}


	public void setKeyValue(String key, T value) {
		values.put(key, value);
	}
	
	public T getValue(String key) {
		if (!values.containsKey(key)) {
			return null;
		}
		return values.get(key);
	}

	public void initializeValues(Map<String, T> initialKeys) {
		if (initialKeys == null) {
			return;
		}

		for(Map.Entry<String, T> entry: initialKeys.entrySet()) {
			this.setKeyValue(entry.getKey(), entry.getValue());
		}
	}
}
