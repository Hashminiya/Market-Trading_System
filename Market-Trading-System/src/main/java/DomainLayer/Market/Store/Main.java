package DomainLayer.Market.Store;

import DomainLayer.Market.InMemoryRepository;

public class Main {
    public static void main(String[] args) {
        Store s = new Store(123L,"My store", new InMemoryRepository<>(),new InMemoryRepositoryStore());
        s.addItem("gun",1234,3, "very good weapon, army",new String[]{"weapon", "fun"});
        for (Item i : s.search("weapon")) {
            System.out.println(i.getName()
            );
        }
    }
}
