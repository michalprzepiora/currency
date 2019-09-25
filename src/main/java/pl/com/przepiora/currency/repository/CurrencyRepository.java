package pl.com.przepiora.currency.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import pl.com.przepiora.currency.model.CurrencyNbpApi;
import pl.com.przepiora.currency.model.Rate;

import java.util.List;

@Repository
public class CurrencyRepository {

  private List<Rate> currencyRateList;
  private String date;
  final static String URL_API_NBP = "http://api.nbp.pl/api/exchangerates/tables/a";

  public CurrencyRepository() {
    RestTemplate restTemplate = new RestTemplate();
    CurrencyNbpApi[] currencyNbpApi = restTemplate
        .getForObject(URL_API_NBP, CurrencyNbpApi[].class);
    currencyRateList = currencyNbpApi[0].getRates();
    date = currencyNbpApi[0].getEffectiveDate();
    Rate rate = new Rate();
    rate.setCurrency("polski złoty");
    rate.setCode("PLN");
    rate.setMid(1D);
    currencyRateList.add(rate);

  }

  public List<Rate> getCurrencyRateList() {
    return currencyRateList;
  }

  public Rate getCurrencyByCode(String code){
    for (Rate rate : currencyRateList) {
      if (rate.getCode().equals(code)){
        return rate;
      }
    }
    throw new IllegalArgumentException("Code not found.");
  }

  public String getDate(){
    return date;
  }
}
