package my.first.httpprogram.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import my.first.httpprogram.dto.Valute;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@XStreamAlias("ValCurs")
public class ValCurs {

    @XStreamAlias("Date")
    @XStreamAsAttribute
    private String date;

    @XStreamAlias("Name")  // Assuming the XML uses "Name" and not "name"
    @XStreamAsAttribute
    private String name;

    private List<Valute> valutes = new ArrayList<>();

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Valute> getValutes() {
        return valutes;
    }

    public void setValutes(List<Valute> valutes) {
        this.valutes = valutes;
    }
   //Task 1
    public void validateNominalValues() throws Exception {
        for (Valute valute : valutes) {
            if (valute.getNominal() != 1) {
                // Exception
                throw new Exception("Error: Valute " + valute.getCharCode() + " has a Nominal value of " + valute.getNominal());
            }
        }
        System.out.println("All Valute objects have a Nominal value of 1.");
    }
    //Task 2
    public String findExchangeRateByCharCode(String charCode) {
        for (Valute valute : valutes) {
            if (valute.getCharCode().equalsIgnoreCase(charCode)) {
                return "Exchange rate for " + charCode + ": " + valute.getValue();
            }
        }
        return "Error: Currency not found.";
    }
    // Task 3
    public void calculateAndPrintAverageExchangeRate() {
        if (valutes.isEmpty()) {
            System.out.println("No exchange rates available to calculate.");
            return;
        }

        double sum = 0.0;
        for (Valute valute : valutes) {
            sum += valute.getValue();
        }

        double average = sum / valutes.size();
        System.out.printf("The average exchange rate is: %.4f\n", average);
    }
    // Task 4
    public void sortValutesByExchangeRate() {
        valutes.sort(Comparator.comparing(Valute::getValue).reversed());  // Sorting by Value field in descending order
    }

    // Task 4: Print sorted Valutes
    public void printSortedValutes() {
        System.out.println("Sorted Valutes by Exchange Rate (Descending):");
        for (Valute valute : valutes) {
            System.out.println(valute.getCharCode() + ": " + valute.getValue());
        }
    }
    // Task 5: Convert all rates to a different base currency
    public void convertToBaseCurrency(String baseCurrencyCharCode) {
        Valute baseCurrency = null;

        for (Valute valute : valutes) {
            if (valute.getCharCode().equalsIgnoreCase(baseCurrencyCharCode)) {
                baseCurrency = valute;
                break;
            }
        }

        if (baseCurrency == null) {
            System.out.println("Base currency " + baseCurrencyCharCode + " not found.");
            return;
        }

        double baseValue = baseCurrency.getValue();

        for (Valute valute : valutes) {
            double newValue = valute.getValue() / baseValue;
            valute.setValue(newValue);
        }

        System.out.println("All rates converted relative to base currency: " + baseCurrencyCharCode);
    }
}
