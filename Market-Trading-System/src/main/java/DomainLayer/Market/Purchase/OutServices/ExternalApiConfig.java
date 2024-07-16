package DomainLayer.Market.Purchase.OutServices;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;
import DomainLayer.Market.Purchase.ExternalApiUtil;

@Configuration
public class ExternalApiConfig {

    @Value("${api.external_system.url}")
    private String apiUrl;

    @PostConstruct
    public void initializeExternalApiUtil() {
        ExternalApiUtil.setApiUrl(apiUrl);
    }
}