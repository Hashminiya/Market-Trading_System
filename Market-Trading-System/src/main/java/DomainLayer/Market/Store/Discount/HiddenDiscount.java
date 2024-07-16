package DomainLayer.Market.Store.Discount;

import DomainLayer.Market.Store.Item;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

import java.security.cert.CertificateExpiredException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Entity
@NoArgsConstructor
@DiscriminatorValue("HiddenDiscount")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class HiddenDiscount extends Discount {

    private String code;

    @JsonCreator
    public HiddenDiscount(@JsonProperty("id") Long id,
                          @JsonProperty("name") String name,
                          @JsonProperty("percent") double percent,
                          @JsonProperty("expirationDate") Date expirationDate,
                          @JsonProperty("storeId") long storeId,
                          @JsonProperty("items") List<Long> items,
                          @JsonProperty("categories") List<String> categories,
                          @JsonProperty("isStore") boolean isStore,
                          @JsonProperty("code") String code){
        super(id,name, percent, expirationDate, storeId, items, categories, isStore);
        this.code = code;
    }

    @Override
    public boolean isValid(Map<Item, Integer> items, String code){
        Date now = new Date();
        return (expirationDate.after(now) && this.code.equals(code));
    }

}