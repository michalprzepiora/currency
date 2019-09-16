package pl.com.przepiora.currency.frontend;

import com.fasterxml.jackson.core.type.TypeReference;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.web.client.RestTemplate;
import pl.com.przepiora.currency.repository.CurrencyRepository;

import java.util.ArrayList;

@Route
public class MainView extends VerticalLayout {
private Button button;

  public MainView() {
    button = new Button("Click");
    add(button);

    button.addClickListener(buttonClickEvent -> {
      System.out.println("click");

      RestTemplate restTemplate = new RestTemplate();
      CurrencyRepository[] currencyRepositories = restTemplate.getForObject("http://api.nbp.pl/api/exchangerates/tables/a",CurrencyRepository[].class);
      System.out.println(currencyRepositories[0].toString());
      currencyRepositories[0].getRates().forEach(e-> System.out.println(e.getCurrency()));


//      ArrayList currencyRepositoryList = new ArrayList<>();
//      currencyRepositoryList = restTemplate.getForObject("http://api.nbp.pl/api/exchangerates/tables/a",ArrayList.class);
//      CurrencyRepository currencyRepository = (CurrencyRepository) currencyRepositoryList.get(0);
//      System.out.println(currencyRepositoryList.toString());
//      System.out.println(currencyRepository.toString());

    });
  }
}
