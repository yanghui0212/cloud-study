package com.study.filter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

	@Bean
	public AccessTokenFilter accessTokenFilter() {
		return new AccessTokenFilter();
	}

//    @Bean
    public IpFilter ipFilter() {
        return new IpFilter();
    }

	/*@Bean
	public RestTemplateFilter restTemplateFilter(RestTemplate restTemplate) {
		return new RestTemplateFilter(restTemplate); // 注入RestTemplate
	}



	@LoadBalanced
	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}*/
}
