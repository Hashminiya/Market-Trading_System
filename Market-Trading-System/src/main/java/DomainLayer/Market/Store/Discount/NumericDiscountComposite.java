package DomainLayer.Market.Store.Discount;

import DomainLayer.Market.Util.IRepository;
import DomainLayer.Market.Util.NumericRule;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class NumericDiscountComposite extends DiscountComposite{

    private NumericRule numericRule;


    @JsonCreator
    public NumericDiscountComposite(@JsonProperty("id") Long id,
                                    @JsonProperty("percent") double percent,
                                    @JsonProperty("expirationDate") Date expirationDate,
                                    @JsonProperty("storeId") long storeId,
                                    @JsonProperty("items") List<Long> items,
                                    @JsonProperty("categories") List<String> categories,
                                    @JsonProperty("isStore") boolean isStore,
                                    @JsonProperty("discounts") List<IDiscount> discounts,
                                    @JsonProperty("numericRule") String rule) {
        super(id, percent, expirationDate, storeId, items, categories, isStore, discounts);
        this.numericRule = NumericRule.valueOf(rule);
    }


    @Override
    public Map<Long, Double> calculatePrice(Map<Long, Double> itemsPrices, Map<Long, Integer> itemsCount, String code) {
        switch (numericRule){
            case MAX:
            //TODO
        }
        return null;
    }

    private Map<Long, Double> maxCalculatePrice(Map<Long, Double> itemsPrices, Map<Long, Integer> itemsCount, String code) {
        Map<Long, Double>  newPrices = new HashMap<>();
        //TODO
        return null;

    }

    private Map<Long, Double> addCalculatePrice(Map<Long, Double> itemsPrices, Map<Long, Integer> itemsCount, String code) {
        Map<Long, Double>  newPrices = new HashMap<>();
        //TODO
        return null;

    }
}
