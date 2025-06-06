package Automation.SmokeTestExecution;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.IExecutionListener;

public class EmailTrigger implements IExecutionListener {

	@Override
	public void onExecutionFinish() {
		try {
			Email_Test();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void Email_Test() throws IOException, URISyntaxException, InterruptedException {
		try {
			Thread.sleep(2000);
			EmailAttachment attachment = new EmailAttachment();
			// Retrieve exceptions stored during test execution
			JSONArray exceptions = ExceptionStorage.getInstance().getExceptions();
			// Check if there were any failures during the execution
			boolean hasFailures = ExceptionStorage.getInstance().hasFailures();
			String statusMessage;
			// Prepare a status message based on whether there were failures
			if (hasFailures) {
				statusMessage = "Status: The Smoke Test Execution has failed. For more details, please refer to the reports.\n";
			} else {
				statusMessage = "Status: All the Environments have been tested Successfully.";
			}

			// String reportPath =
			// "C:\\Users\\hemanth.konduru\\eclipse-workspace\\SmokeTest\\extentReport.html";
			String reportPath = System.getProperty("user.dir") + File.separator + "test-output" + File.separator
					+ "extentReport.html";
			attachment.setPath(reportPath);
			attachment.setDisposition(EmailAttachment.ATTACHMENT);
			attachment.setURL(new File(reportPath).toURI().toURL());
			attachment.setDescription("SmokeTest Execution Report");
			attachment.setName("emailable-report.html");

			// Compose the email
			MultiPartEmail email = new MultiPartEmail();
			email.setHostName("smtp.office365.com");
			email.setSmtpPort(587);
			email.setAuthenticator(new DefaultAuthenticator("Jeevan.M@Wallero.com", "xpyfbvhqctjpnmfg"));
			email.setStartTLSEnabled(true);
			email.setFrom("Jeevan.M@Wallero.com");

			// Get the current date, time, and time zone
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z");
			dateFormat.setTimeZone(TimeZone.getDefault()); // Set to system's default time zone
			String currentDateTime = dateFormat.format(new Date());

			// Set the subject with the current date, time, and time zone
			email.setSubject("Smoke Execution Report - " + currentDateTime); // Adding date in dd-MM-yyyy format

			// Construct the email message
			StringBuilder message = new StringBuilder();
			message.append("Hello Team,\n\n");
			message.append(
					"The smoke test execution has been completed for the day. Please find the status below:\n\n");
			message.append(statusMessage).append("\n");
			// If there are failures, list the failed environments
			if (hasFailures) {
				message.append("Failed Environments:\n");
				for (int i = 0; i < exceptions.length(); i++) {
					JSONObject exception = exceptions.getJSONObject(i);
					message.append(exception.getString("environment")).append(", Status: ")
							.append(exception.getString("status")).append("\n");
				}
			}
			// Add a note and closing remarks
			/*
			 * message.append(
			 * "\nNOTE: This is an automated email. For any questions or further assistance, please contact the appropriate distribution list (DL).\n\n"
			 * );
			 */

			message.append(
					"\nNOTE :This is an autogenerated email. For any questions or further assistance, please reach out to the OfferX Technical Team.\n\n");
			message.append("Thanks & Regards,\n");
			message.append("OfferX Engineering Team.");
			email.setMsg(message.toString());
			email.addTo("Hemanth.Konduru@wallero.com");

			/*
			 * email.addTo("Santosh.Boosem@wallero.com");
			 * email.addTo("Divya.Nekkanti@wallero.com");
			 * email.addTo("Rohan.Mandal@wallero.com");
			 * email.addTo("Pavan.Gutty@wallero.com");
			 * email.addTo("Harsha.Gandi@wallero.com");
			 */

			email.attach(attachment);// Attach the report file
			email.send();
			System.out.println("Email sent successfully with attachment.");

		} catch (EmailException e) {
			e.printStackTrace();
			System.out.println("Failed to send email.");
		}
	}
}
