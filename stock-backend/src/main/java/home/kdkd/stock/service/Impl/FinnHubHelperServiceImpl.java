package home.kdkd.stock.service.Impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import home.kdkd.stock.dto.FinnhubProfileDTO;
import home.kdkd.stock.dto.FinnhubQuoteDTO;
import home.kdkd.stock.service.FinnHubHelperService;

@Service
public class FinnHubHelperServiceImpl implements FinnHubHelperService{

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

    final private String quoteQueryParam = "symbol";
    final private String tokenQueryParam = "token";
    
    @Override
    public FinnhubProfileDTO getStockProfile(String symbol) {
        String url = UriComponentsBuilder.newInstance()
            .scheme(protocol)
            .host(host)
            .path(profileEndpoint)
            .queryParam(quoteQueryParam, symbol)
            .queryParam(tokenQueryParam, key)
            .build()
            .toString();
        FinnhubProfileDTO profileDTO = new RestTemplate().getForObject(url, FinnhubProfileDTO.class);
        return profileDTO;
    }

    @Override
    public FinnhubQuoteDTO getStockQuote(String symbol) {
        String url = UriComponentsBuilder.newInstance()
            .scheme(protocol)
            .host(host)
            .path(quoteEndpoint)
            .queryParam(quoteQueryParam, symbol)
            .queryParam(tokenQueryParam, key)
            .build()
            .toString();
        FinnhubQuoteDTO quoteDTO = new RestTemplate().getForObject(url, FinnhubQuoteDTO.class);
        return quoteDTO;
    }
}
