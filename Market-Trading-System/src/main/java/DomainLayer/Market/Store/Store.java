package DomainLayer.Market.Store;


import DomainLayer.Market.ShoppingBasket;
import DomainLayer.Market.Store.Discount.*;
import DomainLayer.Market.Store.StorePurchasePolicy.*;
import DomainLayer.Market.Util.DataItem;
import DomainLayer.Market.Util.IRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Store implements DataItem<Long> {
    private final Long id;
    private String founderId;
    private String name;
    private String description;
    private final List<String> owners;
    private final List<String> managers;
    private final InMemoryRepositoryStore products;
    private IRepository<Long , IDiscount> discounts;
    private IRepository<Long, PurchasePolicy> purchasePolicies;
    private PurchasePolicyFactory policyFactory;

    public Store(Long id, String founderId, String name, String description,
                 IRepository<Long, IDiscount> discounts,
                 IRepository<Long, PurchasePolicy> purchasePolicies){
        this.id = id;
        this.founderId = founderId;
        this.name = name;
        this.description = description;
        this.discounts = discounts;
        this.purchasePolicies = purchasePolicies;
        this.products = new InMemoryRepositoryStore();
        owners = new ArrayList<>();
        managers = new ArrayList<>();
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public List<String> getOwners(){
        return owners;
    }

    public List<String> getManagers(){
        return managers;
    }

    public void assignOwner(String newOwnerId){
        owners.add(newOwnerId);
    }

    public void assignManager(String newManagerId){
        managers.add(newManagerId);
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public List<Item> viewInventory(){
        return products.findAll();
    }

    public void addItem(Long itemId, String name, double price, int quantity, String description, List<String> categories){
        Item newItem = new Item(itemId, name, description, categories);
        newItem.setPrice(price);
        newItem.setQuantity(quantity);
        products.save(newItem);
    }


    public void updateItem(long itemId, String newName, double newPrice, int quantity){
        Item toEdit = products.findById(itemId);
        toEdit.setName(newName);
        toEdit.setPrice(newPrice);
        toEdit.setQuantity(quantity);
        products.update(toEdit);
    }
    public void deleteItem(long itemId){
        products.delete(itemId);
    }

    public void changeDiscountPolicy(){
        ///TODO
        throw new UnsupportedOperationException("changeDiscountPolicy method is not implemented yet");
    }
    public List<Item> search(String keyWord){
        return products.search(null ,keyWord,false);
    }

    public List<Item> searchKeyWordWithCategory(String category,String keyWord){
        return products.search(category ,keyWord,true);
    }
    public List<Item> searchByCategory(String category){
        /* Empty string for fetching all result with the relevant category */
        return products.search(category,"",true);
    }
    public List<String> getAllCategories(){
        return products.getAllCategoryValues();
    }

    public boolean isAvailable(long itemId, int amount){
        return products.findById(itemId).getQuantity() >= amount;
    }

    public void updateAmount(long itemId, int toDecrease){
        products.findById(itemId).decrease(toDecrease);
    }

    public Item getById(long itemId) {
        return products.findById(itemId);
    }

    public String getDescription() {
        return description;
    }

    public void calculateBasketPrice(ShoppingBasket basket, String code) throws Exception{
        Map<Item,Integer> itemsCount = new HashMap<>();
        for(Long itemId: basket.getItems().keySet())
            itemsCount.put(products.findById(itemId), basket.getItems().get(itemId));
        Map<Item, Double> itemsPrice = getItemsPrices(itemsCount.keySet().stream().toList());
        double price = 0;
        for(IDiscount discount: discounts.findAll()){
            if(discount.isValid(itemsCount, code)) {
                itemsPrice = discount.calculatePrice(itemsPrice, itemsCount, code);
            }
        }
        Map<Long, Double> itemsIdPrice = new HashMap<>();
        for(Item item: itemsPrice.keySet()) {
            price += (itemsCount.get(item) * itemsPrice.get(item));
            itemsIdPrice.put(item.getId(), itemsPrice.get(item));
        }
        basket.setItemsPrice(itemsIdPrice);
        basket.setBasketTotalPrice(price);
    }

    private Map<Item, Double> getItemsPrices(List<Item> items){
        Map<Item, Double> itemPrice = new HashMap<>();
        for(Item item: items){
            itemPrice.put(item, item.getPrice());
        }
        return itemPrice;
    }

    public void setPolicyFactory(PurchasePolicyFactory policyFactory) {
        this.policyFactory = policyFactory;
    }

    public IRepository<Long, Item> getProductRepo() {
        return products;
    }
    public void addDiscount(String discountDetails) throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerSubtypes(new NamedType(RegularDiscount.class, "RegularDiscount"));
        objectMapper.registerSubtypes(new NamedType(HiddenDiscount.class, "HiddenDiscount"));
        objectMapper.registerSubtypes(new NamedType(LogicalDiscountComposite.class, "LogicalDiscountComposite"));
        objectMapper.registerSubtypes(new NamedType(NumericDiscountComposite.class, "NumericDiscountComposite"));
        objectMapper.registerSubtypes(new NamedType(Condition.class, "Condition"));
        objectMapper.registerSubtypes(new NamedType(ConditionComposite.class, "ConditionComposite"));

        try {
            IDiscount discount = objectMapper.readValue(discountDetails, IDiscount.class);
            discounts.save(discount);
        }
        catch (Exception e){
            throw new Exception("Error while creating discount\n" + e.getMessage());
        }

    }
    public boolean checkValidBasket(ShoppingBasket basket, String userDetails){
        HashMap<Item, Integer> itemsInBasket = new HashMap<>();
        for (Map.Entry<Long, Integer> pair: basket.getItems().entrySet()) {
            itemsInBasket.put(products.findById(pair.getKey()), pair.getValue());
        }
        for (PurchasePolicy policy:purchasePolicies.findAll()){
            if(!policy.isValid(itemsInBasket, userDetails))
                return false;
        }
        return true;
    }

    public void addPolicy(String policyDetails) throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerSubtypes(new NamedType(AgeRestrictedPurchasePolicy.class, "AgeRestrictedPurchasePolicy"));
        objectMapper.registerSubtypes(new NamedType(MaximumQuantityPurchasePolicy.class, "PurchasePolicyComposite"));
        objectMapper.registerSubtypes(new NamedType(PurchasePolicyComposite.class, "PurchasePolicyComposite"));

        try {
            PurchasePolicy purchsePoilcy = objectMapper.readValue(policyDetails, PurchasePolicy.class);
            policyFactory.createPolicy(purchsePoilcy);
            purchasePolicies.save(purchsePoilcy);
        }
        catch (Exception e){
            throw new Exception("Error while creating policy\n" + e.getMessage());
        }

    }
}
