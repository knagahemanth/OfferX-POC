package Automation.SmokeTestExecution;

import org.json.JSONArray;
import org.json.JSONObject;

public class ExceptionStorage {
	// Singleton instance of ExceptionStorage
	private static ExceptionStorage instance;
	private JSONArray exceptions;

	// Initializes the 'exceptions' JSONArray
	private ExceptionStorage() {
		exceptions = new JSONArray();
	}

	public static synchronized ExceptionStorage getInstance() {
		if (instance == null) {
			instance = new ExceptionStorage();// Create a new instance
		}
		return instance;
	}

	// Method to store an exception in the 'exceptions' array
	// Takes in the environment, message, and status as parameters
	public void storeException(String environment, String message, String status) {
		JSONObject exceptionJson = new JSONObject();
		exceptionJson.put("environment", environment);
		exceptionJson.put("message", message);
		exceptionJson.put("status", status);
		exceptions.put(exceptionJson);
	}

	// Method to retrieve all stored exceptions
	public JSONArray getExceptions() {
		return exceptions;
	}

	// Method to check if there are any failures
	public boolean hasFailures() {
		return exceptions.length() > 0;
	}
}
