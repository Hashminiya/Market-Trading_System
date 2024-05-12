package DomainLayer.Market.Store;

import DomainLayer.Market.DataItem;
import DomainLayer.Market.IRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Store implements DataItem {
    private final String ROOT_CATEGORY_NAME = "All products";
    private final Long id;
    private String name;
    private final List<Long> owners;
    private final List<Long> managers;
    private IProduct rootCategory;
    private Map<Long,IProduct> productMap;/*Allows quick access by id to item or category*/
    private IRepository<Item.Discount> discounts;
    public Store(Long id, String name, IRepository<Item.Discount> discounts, IRepository<IProduct> products){
        this.id = id;
        this.name = name;
        this.discounts = discounts;
        this.rootCategory = new Category(genrateId(),ROOT_CATEGORY_NAME,products);
        owners = new ArrayList<>();
        managers = new ArrayList<>();

    }

    private Long genrateId() {
        return null;
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
    public void assignOwner(Long newOwnerId){
        owners.add(newOwnerId);
    }
    public void assignManager(Long newManagerId){
        managers.add(newManagerId);
    }
    public List<Long> getOwners(){
        return owners;
    }
    public List<Long> getManagers(){
        return managers;
    }
    public void addProduct(IProduct product,Long categoryId){
        rootCategory.addProduct(product,categoryId);
        productMap.put(product.getId(),product);
    }
    public void addDiscount(IProduct product ,Item.Discount discount){
        /*A method for adding regular discount, un conditional, assign to an item or a category.*/
        productMap.get(product.getId()).setDiscount(discount);
    }
    public void addDiscount(Item.Discount discount){
        /*A method for adding store discount, can be conditional.*/
        discounts.save(discount);
    }
    public void changeDiscountPolicy(){
        ///TODO
        throw new UnsupportedOperationException("changeDiscountPolicy method is not implemented yet");
    }
}
