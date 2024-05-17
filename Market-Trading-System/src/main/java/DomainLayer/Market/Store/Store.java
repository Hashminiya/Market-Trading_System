package DomainLayer.Market.Store;

import DomainLayer.Market.Util.DataItem;
import DomainLayer.Market.Util.IRepository;
import DomainLayer.Market.Util.IdGenerator;
import DomainLayer.Market.Util.InMemoryRepository;

import java.util.ArrayList;
import java.util.List;

public class Store implements DataItem {
    private final Long id;
    private String name;
    private String description;
    private final List<Long> owners;
    private final List<Long> managers;
    private final IRepository<Long,IProduct> rootCategories;
    private final IRepository<Long,IProduct> productsMap;/*Allows quick access by id to item or category*/
    private IRepository<Long ,Discount> discounts;
    public Store(Long id, String name, IRepository<Long, Discount> discounts,
                 IRepository<Long, IProduct> rootCategories, IRepository<Long,IProduct> productsMap){
        this.id = id;
        this.name = name;
        this.discounts = discounts;
        this.rootCategories = rootCategories;
        this.productsMap = productsMap;
        owners = new ArrayList<>();
        managers = new ArrayList<>();

    }

    private long genrateId() {
        return IdGenerator.generateId();
        //TODO implement this
    }

    @Override
    public Long getId() {
        return id;
    }
    @Override
    public String getName() {
        return name;
    }
    public List<Long> getOwners(){
        return owners;
    }
    public List<Long> getManagers(){
        return managers;
    }
    public void assignOwner(Long newOwnerId){
        owners.add(newOwnerId);
    }
    public void assignManager(Long newManagerId){
        managers.add(newManagerId);
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public void addProduct(IProduct product,Long categoryId){
        productsMap.save(product);
        IProduct category = productsMap.findById(categoryId);
        category.addProduct(product);
    }
    public void addItem(String name, double price, int quantity, long categoryId){
        long itemId = genrateId();
        Item newItem = new Item(itemId, name, new InMemoryRepository<Long,Discount>());
        productsMap.findById(categoryId).addProduct(newItem);

    }
    public void addDiscount(IProduct product, Discount discount){
        /*A method for adding regular discount, un conditional, assign to an item or a category.*/
        productsMap.findById(product.getId()).setDiscount(discount);
    }
    public void addDiscount(Discount discount){
        /*A method for adding store discount, can be conditional.*/
        discounts.save(discount);
    }
    void updateItem(long itemId, String newName, double newPrice, int quantity){
//        productsMap.update(new Item(itemId,newName,newPrice, quantity));
    }
    void deleteItem(long itemId){
        productsMap.delete(itemId);
    }
    public void changeDiscountPolicy(){
        ///TODO
        throw new UnsupportedOperationException("changeDiscountPolicy method is not implemented yet");
    }


}
