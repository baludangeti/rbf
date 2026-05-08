package com.rbf.product.common.web;

import com.rbf.product.common.tenant.OrgContextResolver;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

public class OrgFilterConfig {

    @Bean
    public FilterRegistrationBean<OrgContextFilter> orgContextFilter() {
        FilterRegistrationBean<OrgContextFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new OrgContextFilter());
        registration.addUrlPatterns("/api/*");
        registration.setOrder(1);
        return registration;
    }

    @Bean
    public OrgContextResolver orgContextResolver() {
        return new OrgContextResolver();
    }
}
