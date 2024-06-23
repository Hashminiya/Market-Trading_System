package DomainLayer.Market.Store;


import DomainLayer.Market.ShoppingBasket;
import DomainLayer.Market.Store.Discount.*;
import DomainLayer.Market.Store.StorePurchasePolicy.*;
import DomainLayer.Market.Util.DataItem;
import DomainLayer.Market.Util.IRepository;
import DomainLayer.Repositories.DiscountRepository;
import DomainLayer.Repositories.ItemRepository;
import DomainLayer.Repositories.ItemSpecifications;
import DomainLayer.Repositories.PurchasePolicyRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;

public class Store implements DataItem<Long> {
    private final Long id;
    private String founderId;
    private String name;
    private String description;
    private final List<String> owners;
    private final List<String> managers;
    private ItemRepository products;
    private DiscountRepository discounts;
    private PurchasePolicyRepository purchasePolicies;
    private PurchasePolicyFactory policyFactory;

    public Store(Long id, String founderId, String name, String description, ItemRepository products,
                 DiscountRepository discounts,
                 PurchasePolicyRepository purchasePolicies){
        this.id = id;
        this.founderId = founderId;
        this.name = name;
        this.description = description;
        this.products = products;
        this.discounts = discounts;
        this.purchasePolicies = purchasePolicies;
        owners = new ArrayList<>();
        managers = new ArrayList<>();
        assignOwner(founderId);
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

    public void addItem(Long itemId, String name, double price, int quantity, String description, List<String> categories)throws InterruptedException{
        Item newItem = new Item(itemId, name, description, categories);
        newItem.setPrice(price);
        newItem.setQuantity(quantity);
        products.save(newItem);
    }


    public void updateItem(long itemId, String newName, double newPrice, int quantity)throws InterruptedException{
        Item toEdit = getItem(itemId);
        toEdit.setName(newName);
        toEdit.setPrice(newPrice);
        toEdit.setQuantity(quantity);
        products.save(toEdit);
    }
    public void deleteItem(long itemId){
        products.deleteById(itemId);
    }

    public void changeDiscountPolicy(){
        ///TODO
        throw new UnsupportedOperationException("changeDiscountPolicy method is not implemented yet");
    }
    public List<Item> search(String keyWord){
        //return products.search(null ,keyWord,false);
        return products.findAll(Specification.where(ItemSpecifications.nameContains(keyWord)));
    }

    public List<Item> searchKeyWordWithCategory(String category,String keyWord){
        //return products.search(category ,keyWord,true);
        List<String> categories = new ArrayList<>();
        categories.add(category);
        return products.findAll(Specification.where(ItemSpecifications.nameContains(keyWord))
                .and(ItemSpecifications.categoriesIn(categories)));
    }
    public List<Item> searchByCategory(String category){
        /* Empty string for fetching all result with the relevant category */
        //return products.search(category,"",true);
        List<String> categories = new ArrayList<>();
        categories.add(category);
        return products.findAll(Specification.where(ItemSpecifications.categoriesIn(categories)));
    }
    public List<String> getAllCategories(){
        //return products.getAllCategoryValues();
        return products.findAllCategories();
    }

    public boolean isAvailable(long itemId, int amount){
        return getItem(itemId).getQuantity() >= amount;
    }

    public void updateAmount(long itemId, int toDecrease)throws InterruptedException{
        getItem(itemId).decrease(toDecrease);
    }

    public Item getById(long itemId) {
        return getItem(itemId);
    }

    public String getDescription() {
        return description;
    }

    public void calculateBasketPrice(ShoppingBasket basket, String code) throws Exception{
        Map<Item,Integer> itemsCount = new HashMap<>();
        for(Long itemId: basket.getItems().keySet())
            itemsCount.put(getItem(itemId), basket.getItems().get(itemId));
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

    public ItemRepository getProductRepo() {
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
    public boolean checkValidBasket(ShoppingBasket basket, String userDetails) throws InterruptedException{
        HashMap<Item, Integer> itemsInBasket = new HashMap<>();
        for (Map.Entry<Long, Integer> pair: basket.getItems().entrySet()) {
            getItem(pair.getKey()).lock(); //sync
            itemsInBasket.put(getItem(pair.getKey()), pair.getValue());
            if(getItem(pair.getKey()).getQuantity() < pair.getValue()) {
                getItem(pair.getKey()).unlock(); //sync
                return false;
            }
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
        objectMapper.registerSubtypes(new NamedType(MaximumQuantityPurchasePolicy.class, "MaximumQuantityPurchasePolicy"));
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

    public void releaseLocks(long itemId) {
        getItem(itemId).unlock(); //sync
    }

    private Item getItem(long itemId){
        Optional<Item> item = products.findById(itemId);
        if(item.isEmpty())
            throw new NoSuchElementException("Item not found");
        return item.get();
    }
}
