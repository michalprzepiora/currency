package pl.com.przepiora.currency.frontend;

import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.com.przepiora.currency.model.Rate;
import pl.com.przepiora.currency.repository.CurrencyRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;


@Route
@UIScope
@Component
public class MainView extends VerticalLayout {

  private TextField amount;
  private ComboBox<Rate> from;
  private ComboBox<Rate> to;
  private Button count;
  private H1 result;
  private final CurrencyRepository currencyRepository;

  @Autowired
  public MainView(CurrencyRepository currencyRepository) {
    this.currencyRepository = currencyRepository;
    this.setAlignItems(Alignment.CENTER);
    VerticalLayout main = new VerticalLayout();
    main.setWidth("40%");
    amount = new TextField("Kwota");
    amount.setWidth("100%");

    ItemLabelGenerator<Rate> items = (ItemLabelGenerator<Rate>) rate -> rate.getCode() + " - "
        + rate.getCurrency();

    from = new ComboBox<>("Przelicz z");
    from.setWidth("100%");
    from.setItems(currencyRepository.getCurrencyRateList());
    from.setItemLabelGenerator(items);
    from.setValue(currencyRepository.getCurrencyByCode("PLN"));

    to = new ComboBox<>("Przelicz na");
    to.setWidth("100%");
    to.setItems(currencyRepository.getCurrencyRateList());
    to.setItemLabelGenerator(items);
    to.setValue(currencyRepository.getCurrencyByCode("PLN"));

    Div div = new Div();
    div.setHeight("30px");

    count = new Button("Przelicz");
    count.setWidth("100%");
    count.addClickListener(buttonClickEvent -> {

      getFinalPrice();
      String message = amount.getValue() + " " + from.getValue().getCode()+ " = "+getFinalPrice().toString() + " "+ to.getValue().getCode();
      result.setText(message);

    });

    VerticalLayout resultView = new VerticalLayout();
    result = new H1("xxx");
    resultView.add(result);


    main.add(amount, from, to, div, count, resultView);
    add(main);
  }

  private BigDecimal getFinalPrice() {
    Rate fromRate = from.getValue();
    Rate toRate = to.getValue();
    BigDecimal fromRatePrice = BigDecimal.valueOf(fromRate.getMid());
    BigDecimal toRatePrice = BigDecimal.valueOf(toRate.getMid());
    BigDecimal ammountPrice = BigDecimal.valueOf(Double.parseDouble(amount.getValue()));
    BigDecimal fromInPln = ammountPrice.multiply(fromRatePrice);
    return fromInPln.divide(toRatePrice,2, RoundingMode.HALF_UP);
  }
}
