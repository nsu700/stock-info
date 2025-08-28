package home.kdkd.stock.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConf {
    @Value("${conf.finnhub.host}")
    private String host;

    @Value("${conf.protocol}")
    private String protocol; 

    @Value("${conf.key}")
    private String key;

    @Value("${conf.finnhub.endpoints.quote}")
    private String quoteEndpoint;

    @Value("${conf.finnhub.endpoints.profile}")
    private String profileEndpoint;
}