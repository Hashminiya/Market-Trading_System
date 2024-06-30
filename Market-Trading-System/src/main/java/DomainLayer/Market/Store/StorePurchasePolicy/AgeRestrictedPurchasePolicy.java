package DomainLayer.Market.Store.StorePurchasePolicy;

import DomainLayer.Market.Store.Item;
import DomainLayer.Market.User.IUserFacade;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;

import java.util.HashMap;
import java.util.List;

@Entity
@DiscriminatorValue("AGE_RESTRICTED")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class AgeRestrictedPurchasePolicy extends PurchasePolicy {
    private int minAge;

    @Transient
    IUserFacade userFacade;
    @JsonCreator
    public AgeRestrictedPurchasePolicy(@JsonProperty("name") String name,
                                       @JsonProperty("id") Long id,
                                       @JsonProperty("minAge") int minAge,
                                       @JsonProperty("items") List<Long> items,
                                       @JsonProperty("categories") List<String> categories,
                                       @JsonProperty("isStore") boolean isStore){
        super(id, name, items,categories, isStore);
        this.minAge = minAge;

    }

    public AgeRestrictedPurchasePolicy() {

    }

    @Override
    public boolean isValid(HashMap<Item,Integer> itemsInBasket, String userName) {
        if(userFacade.getUserAge(userName) < minAge) {
            for (Item itemToCheck : itemsInBasket.keySet()) {
                if (isIncluded(itemToCheck))
                    return false;
            }
        }
        return true;
    }

    public void setUserFacade(IUserFacade userFacade) {
        this.userFacade = userFacade;
    }
}
