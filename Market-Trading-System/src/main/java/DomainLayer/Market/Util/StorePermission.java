package DomainLayer.Market.Util;

//TODO add to the UML
public enum StorePermission implements StoreEnum {
    VIEW_INVENTORY,
    VIEW_STORE_MANAGEMENT_INFO,
    VIEW_PURCHASE_HISTORY,
    ASSIGN_OWNER,
    ASSIGN_MANAGER,
    ADD_ITEM,
    UPDATE_ITEM,
    DELETE_ITEM,
    CHANGE_POLICY,
    CHANGE_DISCOUNT_TYPE,
    REMOVE_STORE;
}
