package DomainLayer.Market.Store;


import DomainLayer.Market.IRepository;

import java.util.List;


public class Category implements IProduct {
    private Long id;
    private String name;
    private final IRepository<IProduct> products;
    public Category(Long id, String name, IRepository<IProduct> productRepository){
        this.id = id;
        this.name = name;
        products = productRepository;
    }
    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void addProduct(IProduct newProduct, Long categoryId){
        if(this.id.longValue() == categoryId.longValue())
            products.save(newProduct);
        else {
            IProduct category = findCategory(newProduct, categoryId);
            category.addProduct(newProduct, categoryId);
        }
    }

    private IProduct findCategory(IProduct newProduct, Long categoryId) {
        ///TODO implement a way to find out which category to go to.
        throw new UnsupportedOperationException("findCategory method is not implemented yet");
    }

    public void setDiscount(Item.Discount discount){
        List<IProduct> nodes = products.findAll();
        for (IProduct p:nodes) {
            p.setDiscount(discount);
        }
    }
}
