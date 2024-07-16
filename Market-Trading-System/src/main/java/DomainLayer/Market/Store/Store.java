package DomainLayer.Market.Store;
import DAL.ItemDTO;

import API.SpringContext;
import DAL.PolicyDTO;
import DomainLayer.Market.ShoppingBasket;
import DomainLayer.Market.Store.Discount.*;
import DomainLayer.Market.Store.StorePurchasePolicy.*;
import DomainLayer.Market.Util.DataItem;

import DomainLayer.Repositories.DiscountRepository;
import DomainLayer.Repositories.ItemRepository;
import DomainLayer.Repositories.ItemSpecifications;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import jakarta.persistence.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;

@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Store implements DataItem<Long> {

    @Id
    private Long id;
    private String founderId;
    private String name;
    private String description;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "store_owners", joinColumns = @JoinColumn(name = "store_id", referencedColumnName = "id"))
    @Column(name = "owner_username")
    @BatchSize(size = 25)
    private List<String> owners;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "store_managers", joinColumns = @JoinColumn(name = "store_id", referencedColumnName = "id"))
    @Column(name = "manager_username")
    @BatchSize(size = 25)
    private List<String> managers;
    @Transient
    private ItemRepository products;

    @BatchSize(size = 25)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private List<BaseDiscount> discounts;
    //@Transient
    //private PurchasePolicyRepository purchasePolicies;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    @BatchSize(size = 25)
    private List<PurchasePolicy> purchasePolicies = new ArrayList<>();

    @Transient
    private PurchasePolicyFactory policyFactory;
    @Transient
    private Map<Long, ItemsCache> itemsCache;

    public Store(Long id, String founderId, String name, String description, ItemRepository products,
                 DiscountRepository discounts){
        this.id = id;
        this.founderId = founderId;
        this.name = name;
        this.description = description;
        this.products = products;
        this.discounts = new ArrayList<>();
        //this.purchasePolicies = purchasePolicies;
        this.purchasePolicies = new ArrayList<>();
        owners = new ArrayList<>();
        managers = new ArrayList<>();
        itemsCache = new HashMap<>();
        assignOwner(founderId);
    }

    public Store() {

    }

    @PostLoad
    private void loadItems() {
        products = SpringContext.getBean(ItemRepository.class);
        //purchasePolicies = SpringContext.getBean(PurchasePolicyRepository.class);
        //purchasePolicies = new ArrayList<>();
        policyFactory = SpringContext.getBean(PurchasePolicyFactory.class);

        itemsCache = new HashMap<>();
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
        return products.findAllByStoreId(id);
    }

    public void addItem(Long itemId, String name, double price, int quantity, String description, List<String> categories)throws InterruptedException{
        Item newItem = new Item(itemId, name, description, categories, id);
        newItem.setPrice(price);
        newItem.setQuantity(quantity);
        products.save(newItem);
    }

    public void updateItem(long itemId, String newName, double newPrice, int quantity)throws InterruptedException{
        Item toEdit = getItem(itemId);
        toEdit.lock();
        toEdit.setName(newName);
        toEdit.setPrice(newPrice);
        toEdit.setQuantity(quantity);
        toEdit.unlock();

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
        return products.findAllCategoriesByStoreId(id);
    }

    public boolean isAvailable(long itemId, int amount){
        return getItem(itemId).getQuantity() >= amount;
    }

    public void decreaseAmount(long itemId, int toDecrease)throws InterruptedException{
        Item item = getItem(itemId);
        item.lock();
        item.decrease(toDecrease);
        products.save(item);
        item.unlock();
    }

    public void increaseAmount(long itemId, int toIncrease)throws InterruptedException{
        Item item = getItem(itemId);
        item.lock();
        item.increase(toIncrease);
        products.save(item);
        item.unlock();

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
        for(IDiscount discount: discounts){
            if(discount.isValid(itemsCount, code)) {
                try {
                    itemsPrice = discount.calculatePrice(itemsPrice, itemsCount, code);
                }
                catch(Exception e){
                    //restoreStock(basket.getId());
                    throw new Exception(e.getMessage());
                }
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
            BaseDiscount discount = objectMapper.readValue(discountDetails, BaseDiscount.class);
            discounts.add(discount);
        }
        catch (Exception e){
            throw new Exception("Error while creating discount\n" + e.getMessage());
        }

    }
    public boolean checkValidBasket(ShoppingBasket basket, String userDetails) throws InterruptedException{
        HashMap<Item, Integer> itemsInBasket = new HashMap<>();
        //Map<Item, Integer> decreasedItems = new HashMap<>();
        for (Map.Entry<Long, Integer> pair: basket.getItems().entrySet()) {
            Item item = getItem(pair.getKey());
            item.lock(); //sync
            itemsInBasket.put(item, pair.getValue());
            if (item.getQuantity() < pair.getValue()) {
                //restoreStock(decreasedItems);
                item.unlock(); //sync
                return false;
            }
            decreaseAmount(pair.getKey(), pair.getValue());
            item.unlock();
            itemsCache.putIfAbsent(basket.getId(), new ItemsCache(basket.getId()));
            itemsCache.get(basket.getId()).lock();
            itemsCache.get(basket.getId()).addItem(item, pair.getValue());
            itemsCache.get(basket.getId()).unlock();
        }
        for (PurchasePolicy policy:purchasePolicies){
            if(!policy.isValid(itemsInBasket, userDetails))
                //restoreStock(itemsInBasket);
                return false;
        }
        return true;
    }

    public void restoreStock(Long basketId) throws InterruptedException{
        if(!itemsCache.containsKey(basketId))
            return;
        ItemsCache cacheBasket = itemsCache.get(basketId);

        cacheBasket.lock();
        Map<Item, Integer> restoreItems = cacheBasket.getItems();
        for (Item item: restoreItems.keySet()) {
            increaseAmount(item.getId(), restoreItems.get(item));
            itemsCache.get(basketId).removeItem(item);
        }
        itemsCache.get(basketId).unlock();
    }


    public void addPolicy(String policyDetails) throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerSubtypes(new NamedType(AgeRestrictedPurchasePolicy.class, "AgeRestrictedPurchasePolicy"));
        objectMapper.registerSubtypes(new NamedType(MaximumQuantityPurchasePolicy.class, "MaximumQuantityPurchasePolicy"));
        objectMapper.registerSubtypes(new NamedType(PurchasePolicyComposite.class, "PurchasePolicyComposite"));

        try {
            PurchasePolicy purchsePoilcy = objectMapper.readValue(policyDetails, PurchasePolicy.class);
            policyFactory.createPolicy(purchsePoilcy);
            purchasePolicies.add(purchsePoilcy);
        }
        catch (Exception e){
            throw new Exception("Error while creating policy\n" + e.getMessage());
        }
    }



    public void clearCache(Long id) {
        itemsCache.remove(id);
    }

    private Item getItem(long itemId){
        Optional<Item> item = products.findById(itemId);
        if(item.isEmpty())
            throw new NoSuchElementException("Item not found");
        return item.get();
    }

    public void clearCache() {
        itemsCache = new HashMap<>();
    }

    public Map<Long, ItemsCache> getCache(){ return itemsCache; }

    public List<PolicyDTO>  getPolicies() {
        List<PolicyDTO> policyDTOS = new ArrayList<>();
        for(PurchasePolicy policy: purchasePolicies){
            String type = "";
            if (policy instanceof AgeRestrictedPurchasePolicy){
                type = "AgeRestrictedPurchasePolicy";
            }
            else if (policy instanceof MaximumQuantityPurchasePolicy){
                type = "MaximumQuantityPurchasePolicy";
            }
            else if (policy instanceof PurchasePolicyComposite){
                type = "PurchasePolicyComposite";
            }
            policyDTOS.add(new PolicyDTO(policy.getId(), policy.getName(), type));
        }
        return policyDTOS;
    }
}
