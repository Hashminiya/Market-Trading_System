package DomainLayer.Market.Store;


import DomainLayer.Market.Util.DataItem;
import DomainLayer.Market.Util.IRepository;
import DomainLayer.Market.Util.IdGenerator;
import DomainLayer.Market.Util.InMemoryRepository;

import java.util.ArrayList;
import java.util.List;

public class Store implements DataItem<Long> {
    private final Long id;
    private Long founderId;
    private String name;
    private String description;
    private final List<String> owners;
    private final List<String> managers;
    private final InMemoryRepositoryStore products;
    private IRepository<Long , Discount> discounts;
    public Store(Long id, Long founderId, String name, String description, IRepository<Long, Discount> discounts){
        this.id = id;
        this.founderId = founderId;
        this.name = name;
        this.description = description;
        this.discounts = discounts;
        this.products = new InMemoryRepositoryStore();
        owners = new ArrayList<>();
        managers = new ArrayList<>();
    }

    private long genrateId() {
        return IdGenerator.generateId();
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

    public void addItem(Long itemId,String name, double price, int quantity, String description, List<String> categories){
        Item newItem = new Item(itemId, name, description ,new InMemoryRepository<Long,Discount>(), categories);
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
}
