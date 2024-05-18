package DomainLayer.Market.User;

public class SystemManager extends User{

    private SystemManager systemManager;

    private SystemManager(){
        super();
    }

    public synchronized SystemManager getInstance(){
        if(systemManager == null){
            systemManager = new SystemManager();
        }
        return systemManager;
    }

    public void InitializeTradingSystem(){

    }

    public void AddConnectionWithExternalServices(){

    }

    public void ChangeConnectionWithExternalServices(){

    }
}
