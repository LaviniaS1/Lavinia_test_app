package my.first.httpprogram;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Properties;

public class ApiEndpointTest {

    private CloseableHttpClient client;
    private Properties config;

    // Initialize resources before any tests are run
    @BeforeSuite
    public void setUp() throws IOException {
        // Create HTTP client instance
        client = HttpClients.createDefault();

        // Load configuration from properties file
        config = new Properties();
        config.load(getClass().getClassLoader().getResourceAsStream("config.properties"));
    }

    // Cleanup resources after tests are completed
    @AfterSuite
    public void cleanUp() throws IOException {
        if (client != null) {
            client.close(); // Close the HTTP client after tests
        }
    }

    // Provide test data (GitHub API endpoints) for the test method
    @DataProvider(name = "apiEndpoints")
    public Object[][] endpointsProvider() {
        return new Object[][]{
                {config.getProperty("github.api.url") + config.getProperty("github.api.users.endpoint")},
                {config.getProperty("github.api.url") + config.getProperty("github.api.repos.endpoint")}
        };
    }

    // Test method that validates the HTTP status code for each endpoint
    @Test(dataProvider = "apiEndpoints")
    public void verifyStatusCode(String apiUrl) throws IOException {
        // Create and send GET request to the provided endpoint
        HttpGet request = new HttpGet(apiUrl);
        CloseableHttpResponse response = client.execute(request);

        // Extract and assert the HTTP status code
        int actualStatusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals(actualStatusCode, 200, "Unexpected status code for " + apiUrl);

        // Close the response
        response.close();
    }
}
