package vloving.site.crawler.config;

import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author yzw
 * @date 2021/3/10 17:06
 */
@Configuration
public class RestTemplateConfig {

    private static final Logger logger = LoggerFactory.getLogger(RestTemplateConfig.class);

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate(httpRequestFactory());
        List<HttpMessageConverter<?>> converterList=restTemplate.getMessageConverters();
        HttpMessageConverter<?> converterRemove = null;
        for (HttpMessageConverter<?> item:converterList){
            if(item.getClass()== StringHttpMessageConverter.class){
                converterRemove=item;
                break;
            }
        }
        if (converterRemove != null) {
            converterList.remove(converterRemove);
        }
        HttpMessageConverter<?> converter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        converterList.add(1,converter);
        logger.info("-----restTemplate-----初始化完成");
        return restTemplate;
    }

    @Bean
    public ClientHttpRequestFactory httpRequestFactory() {
        return new HttpComponentsClientHttpRequestFactory(httpClient());
    }

    @Bean
    public HttpClient httpClient() {
        PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager(30, TimeUnit.SECONDS);
        manager.setMaxTotal(50);
        manager.setDefaultMaxPerRoute(50);
        RequestConfig config = RequestConfig.custom()
                .setSocketTimeout(8000)
                .setConnectTimeout(5000)
                .setConnectionRequestTimeout(500)
                .build();
        List<Header> headers = new ArrayList<>();
        headers.add(new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.16 Safari/537.36"));
        headers.add(new BasicHeader("Accept-Encoding", "gzip,deflate"));
        headers.add(new BasicHeader("Accept-Language", "zh-CN"));
        headers.add(new BasicHeader("Connection", "Keep-Alive"));
        headers.add(new BasicHeader("Content-type", "application/json;charset=UTF-8"));
        return HttpClientBuilder.create()
                .setDefaultRequestConfig(config)
                .setConnectionManager(manager)
                .setDefaultHeaders(headers)
                .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
                .setRetryHandler(new DefaultHttpRequestRetryHandler(2, true))
                .build();
    }
}
