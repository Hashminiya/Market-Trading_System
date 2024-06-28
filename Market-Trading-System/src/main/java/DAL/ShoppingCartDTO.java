package DAL;

import DomainLayer.Market.ShoppingBasket;
import DomainLayer.Market.Store.IStoreFacade;
import DomainLayer.Market.Store.Item;
import DomainLayer.Market.User.ShoppingCart;
//import ItemDTO;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;


public class ShoppingCartDTO {
    private List<ShoppingBasketDTO> basketDTOS;
    private double totalPrice;

    public ShoppingCartDTO() {
        this.basketDTOS = new ArrayList<>();
        this.totalPrice = 0;
    }

    public ShoppingCartDTO(List<ShoppingBasketDTO> basketDTOS, double totalPrice) {
        this.basketDTOS = basketDTOS;
        this.totalPrice = totalPrice;
    }

    // Constructor to initialize from a ShoppingCart entity with storeFacade
    public ShoppingCartDTO(ShoppingCart shoppingCart, IStoreFacade storeFacade) {
        this.basketDTOS = new ArrayList<>();
        this.totalPrice = shoppingCart.getShoppingCartPrice();
        for (ShoppingBasket shoppingBasket : shoppingCart.getBaskets()) {
            Map<Long,Integer> itemsIdAndQuantity = shoppingBasket.getItems();
            Map<ItemDTO,Integer> itemsAndQuantity = new HashMap<>();
            // Convert the map of item ids and quantities to a map of items and quantities using storeFacade.getItem
            for (Map.Entry<Long, Integer> entry : itemsIdAndQuantity.entrySet()) {
                Item item = storeFacade.getItem(entry.getKey());
                ItemDTO itemDTO = new ItemDTO(item, shoppingBasket.getStoreId());
                itemsAndQuantity.put(itemDTO, entry.getValue());
            }
            ShoppingBasketDTO shoppingBasketDTO = new ShoppingBasketDTO(shoppingBasket.getId(), itemsAndQuantity, shoppingBasket.getBasketTotalPrice(), shoppingBasket.getStoreId());
            this.basketDTOS.add(shoppingBasketDTO);
        }

    }
}
