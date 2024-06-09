package DomainLayer.Market.Store;


import DomainLayer.Market.ShoppingBasket;
import DomainLayer.Market.Store.Discount.Discount;
import DomainLayer.Market.Store.Discount.IDiscount;
import DomainLayer.Market.Util.DataItem;
import DomainLayer.Market.Util.IRepository;
import DomainLayer.Market.Util.InMemoryRepository;

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

    public Store(Long id, String founderId, String name, String description, IRepository<Long, IDiscount> discounts){
        this.id = id;
        this.founderId = founderId;
        this.name = name;
        this.description = description;
        this.discounts = discounts;
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

    public void addDiscount(List<Long> items, Discount discount){
        /*A method for adding regular discount, un conditional, assign to an item or a category.*/
        for(Long item : items){
            products.findById(item).setDiscount(discount);
        }
    }

    public void addDiscount(Discount discount){
        /*A method for adding store discount, can be conditional.*/
        discounts.save(discount);
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
        Map<Long,Integer> items = basket.getItems();
        Map<Long, Double> itemsPrice = new HashMap<>();
        double price = 0;
        for(IDiscount discount: discounts.findAll()){
            if(discount.isValid(items, code)) {
                if (discount.isByCategory()) {
                    List<Long> updatedItems = new ArrayList<>();
                    for (String category : discount.getCategories()) {
                        List<Item> discountItems = searchByCategory(category);
                        for (Item currItem : discountItems) {
                            if (!updatedItems.contains(currItem.getId()))
                                updatedItems.add(currItem.getId());
                        }
                    }
                    discount.setItems(updatedItems);
                }
                itemsPrice = getItemsPrices(items.keySet().stream().toList());
                itemsPrice = discount.calculatePrice(itemsPrice, items, code);
            }
        }
        basket.setItemsPrice(itemsPrice);
        for(Long itemId: itemsPrice.keySet()){
            price += (items.get(itemId) * itemsPrice.get(itemId));
        }
        basket.setBasketTotalPrice(price);
    }

    private Map<Long, Double> getItemsPrices(List<Long> items){
        Map<Long, Double> itemPrice = new HashMap<>();
        for(Long itemId: items){
            itemPrice.put(itemId, products.findById(itemId).getPrice());
        }
        return itemPrice;
    }
}
