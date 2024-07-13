package DAL;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PurchaseDTO {
    private Long purchaseId;
    @JsonIgnoreProperties({"id"})
    private List<ItemDTO> purchasedItemsList;
    private Double totalAmount;
    private String userId;
    private String purchaseDate;

    // No-argument constructor
    public PurchaseDTO() {}

    // Parameterized constructor
    public PurchaseDTO(Long purchaseId, List<ItemDTO> purchasedItemsList, Double totalAmount, String userId, String purchaseDate) {
        this.purchaseId = purchaseId;
        this.purchasedItemsList = purchasedItemsList;
        this.totalAmount = totalAmount;
        this.userId = userId;
        this.purchaseDate = purchaseDate;
    }

    // Getters and Setters
    public Long getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(Long purchaseId) {
        this.purchaseId = purchaseId;
    }

    public List<ItemDTO> getPurchasedItemsList() {
        return purchasedItemsList;
    }

    public void setPurchasedItemsList(List<ItemDTO> purchasedItemsList) {
        this.purchasedItemsList = purchasedItemsList;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }
}
