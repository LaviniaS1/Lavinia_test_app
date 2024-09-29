package my.first.httpprogram;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.thoughtworks.xstream.XStream;
import my.first.httpprogram.dto.ValCurs;
import my.first.httpprogram.dto.Valute;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.methods.HttpGet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {

        // Create an HTTP client and execute the GET request
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet("https://www.bnm.md/en/official_exchange_rates?get_xml=1&date=13.07.2021");
        HttpResponse response = client.execute(request);

        // Get the response content
        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent())
        );

        StringBuilder fullResponse = new StringBuilder();
        String line;

        while ((line = rd.readLine()) != null) {
            fullResponse.append(line).append("\r\n");
        }

        // Create an instance of XStream
        XStream xstream = new XStream();

        // Configure security settings for XStream
        XStream.setupDefaultSecurity(xstream);
        xstream.allowTypes(new Class[]{ValCurs.class, Valute.class});

        // Allow DTO package classes (using wildcard)
        xstream.allowTypesByWildcard(new String[]{
                "my.first.httpprogram.dto.**"
        });

        // Register the annotated classes with XStream
        xstream.processAnnotations(ValCurs.class);
        xstream.processAnnotations(Valute.class);

        // Add implicit collection for 'valutes' field in ValCurs class
        xstream.addImplicitCollection(ValCurs.class, "valutes", Valute.class);

        // Deserialize the XML to ValCurs object
        ValCurs valCurs = (ValCurs) xstream.fromXML(String.valueOf(fullResponse));

        // Validate nominal values
        try {
            valCurs.validateNominalValues();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        // Prompt the user to input a currency CharCode
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the currency CharCode (e.g., EUR, USD): ");
        String charCode = scanner.nextLine();

        // Find and print the exchange rate for the given CharCode
        String result = valCurs.findExchangeRateByCharCode(charCode);
        System.out.println(result);

        // Calculate and print the average exchange rate
        valCurs.calculateAndPrintAverageExchangeRate();

        //Sorting Based on Exchange Rates
        valCurs.sortValutesByExchangeRate();
        valCurs.printSortedValutes();

        //Convert all exchange rates to a different base currency (e.g., USD)
        valCurs.convertToBaseCurrency("EUR");

        // Optionally, print JSON representation of the ValCurs object
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        System.out.println(objectMapper.writeValueAsString(valCurs));
    }
}
