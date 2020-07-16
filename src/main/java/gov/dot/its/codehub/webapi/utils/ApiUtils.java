package gov.dot.its.codehub.webapi.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import gov.dot.its.codehub.webapi.model.ApiError;


@Component
public class ApiUtils {

	private static final Logger logger = LoggerFactory.getLogger(ApiUtils.class);
	
	@Value("${codehub.webapi.debug}")
	private boolean debug;
	
	private static final String MESSAGE_TEMPLATE = "%s : %s ";
	private static final String ERROR_LABEL = "Error";

	public String getQueryParamString(Map<String, String> params,String name, String defaultValue) {
		for(Map.Entry<String, String> entry : params.entrySet()) {
			if (entry.getKey().equalsIgnoreCase(name)) {
				return entry.getValue();
			}
		}
		return defaultValue;
	}

	public int getQueryParamInteger(Map<String, String> params, String name, int defaultValue) {
		String val = this.getQueryParamString(params, name, null);
		if (val == null) {
			return defaultValue;
		}

		int result = -1;
		try {
			result = Integer.parseInt(val);
		} catch(NumberFormatException e) {
			result = defaultValue;
		}

		return result;
	}

	public String getMd5(String source) {
		if (source == null) {
			return null;
		}

		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5"); //NOSONAR
			messageDigest.update(source.trim().getBytes());
			byte[] bytes = messageDigest.digest();
			return DatatypeConverter.printHexBinary(bytes).toLowerCase();
		} catch (NoSuchAlgorithmException e) {
			logger.error(e.getMessage());
			return null;
		} 
	}

	public Date getCurrentUtc() {
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		String timstampStr = df.format(date);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		Date parsedDate;
		try {
			parsedDate = dateFormat.parse(timstampStr);
			return parsedDate;
		} catch (ParseException e) {
			return date;
		}
	}

	public String getTimestampFormat(Date date, String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		return dateFormat.format(date);
	}

	public List<ApiError> getErrorsFromException(List<ApiError> errors, Exception e) {
		String msg = stringFormat(MESSAGE_TEMPLATE, ERROR_LABEL, e.getMessage());
		errors.add(new ApiError(msg));
		logger.error(msg);
		if (debug) {
			msg = stringFormat(MESSAGE_TEMPLATE, ERROR_LABEL, e.toString());
			logger.error(msg);
		}
		if (e.getSuppressed().length > 0) {
			for (Throwable x : e.getSuppressed()) {
				msg = stringFormat(MESSAGE_TEMPLATE, ERROR_LABEL, x.toString());
				errors.add(new ApiError(msg));
				logger.error(msg);
			}
		}
		return errors;
	}

	public String stringFormat(String template, Object... values) {
		int n = StringUtils.countOccurrencesOf(template, "%s");
		if (n == values.length) {
			return String.format(template, values);
		}
		Object[] result = new Object[n];
		Arrays.fill(result, "");
		if (n > values.length) {
			System.arraycopy(values, 0, result, 0, values.length);
			return String.format(template, result);
		}

		System.arraycopy(values, 0, result, 0, result.length);
		StringBuilder s = new StringBuilder();
		for(int i=n; i<values.length; i++) {
			s.append(values[i] == null ? "": values[i] + " ");
		}
		result[result.length-1] = (result[result.length-1] == null ? "" : result[result.length-1])+ " " +s.toString();
		return String.format(template, result);
	}
}
