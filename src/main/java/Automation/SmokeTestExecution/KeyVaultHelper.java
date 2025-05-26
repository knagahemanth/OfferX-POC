package Automation.SmokeTestExecution;

import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;

public class KeyVaultHelper {
	public static String getSecret(String keyVaultName, String secretName) {
		String keyVaultUrl = "https://" + keyVaultName + ".vault.azure.net";
		SecretClient secretClient = new SecretClientBuilder().vaultUrl(keyVaultUrl)
				.credential(new DefaultAzureCredentialBuilder().build()).buildClient();

		return secretClient.getSecret(secretName).getValue();
	}
}
