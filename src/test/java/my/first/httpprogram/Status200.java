package my.first.httpprogram;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class Status200 {

    @Test
    public void firstTest() throws IOException {
        // Build the HTTP client
        CloseableHttpClient client = HttpClients.createDefault();

        // Create an HTTP GET request with the URL from the properties file
        HttpGet request  = new HttpGet(PropertyReader.getProperty("gitUrl"));

        // Execute the request
        CloseableHttpResponse response = client.execute(request);

        // Get the actual status code from the response
        int actualStatusCode = response.getStatusLine().getStatusCode();

        // Assert that the status code is 200 (OK)
        Assert.assertEquals(actualStatusCode, 200);

        // Clean up by closing the response and client
        response.close();
        client.close();
    }
}
