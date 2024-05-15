package DomainLayer.Market.Purchase;

import DAL.ItemDTO;

import java.util.Date;
import java.util.List;

public class Receipt {
    private String userName;
    private List<ItemDTO> itemstoQuantity ;
    private Date purchseDateAndTime;
    private int totalSum;
}
