package DomainLayer.Market.Store.Discount;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.security.cert.CertificateExpiredException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class HiddenDiscount extends Discount {

    private String code;

    @JsonCreator
    public HiddenDiscount(@JsonProperty("id") Long id,
                          @JsonProperty("percent") double percent,
                          @JsonProperty("expirationDate") Date expirationDate,
                          @JsonProperty("storeId") long storeId,
                          @JsonProperty("items") List<Long> items,
                          @JsonProperty("categories") List<String> categories,
                          @JsonProperty("isStore") boolean isStore,
                          @JsonProperty("code") String code){
        super(id, percent, expirationDate, storeId, items, categories, isStore);
        this.code = code;
    }

    @Override
    public boolean isValid(Map<Long, Integer> items, String code){
        Date now = new Date();
        return (getExpirationDate().after(now) && this.code.equals(code));
    }

}
