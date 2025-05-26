package Automation.SmokeTestExecution;
import java.util.Optional;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

public class SmokeTestFunction {
	 @FunctionName("SmokeTestExecution")
	    public HttpResponseMessage run(
	        @HttpTrigger(name = "req", methods = {HttpMethod.GET, HttpMethod.POST}, authLevel = AuthorizationLevel.FUNCTION)
	        HttpRequestMessage<Optional<String>> request,
	        final ExecutionContext context) {
	        
	        context.getLogger().info("Azure Function triggered to execute smoke tests.");
	        
	        try {
	            SmokeTestExecution smokeTest = new SmokeTestExecution();
	            EmailTrigger emailTrigger = new EmailTrigger();
	            // Run test environments
	            smokeTest.testProdEnvironment();
	            smokeTest.testUatEnvironment();
	            smokeTest.testTestEnvironment();


	            emailTrigger.Email_Test();
	            return request.createResponseBuilder(HttpStatus.OK)
	                          .body("Smoke tests executed successfully. Email sent to the team.")
	                          .build();

	        } catch (Exception e) {
	            context.getLogger().severe("Error executing smoke tests: " + e.getMessage());
	            return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
	                          .body("Failed to execute smoke tests.")
	                          .build();
	        }
	    }
}
