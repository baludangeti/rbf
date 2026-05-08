package com.rbf.product.console.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final WebAuthInterceptor webAuthInterceptor;
    private final PermissionModelInterceptor permissionModelInterceptor;

    public WebMvcConfig(WebAuthInterceptor webAuthInterceptor, PermissionModelInterceptor permissionModelInterceptor) {
        this.webAuthInterceptor = webAuthInterceptor;
        this.permissionModelInterceptor = permissionModelInterceptor;
    }

    @Bean
    public ViewResolver jspViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        resolver.setOrder(1);
        return resolver;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/assets/**")
                .addResourceLocations("/assets/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(webAuthInterceptor)
                .addPathPatterns("/console/**", "/billing/**", "/product/**", "/inventory/**",
                        "/reports/**", "/customer/**", "/supplier/**", "/purchase/**", "/gateway/**");
        registry.addInterceptor(permissionModelInterceptor)
                .addPathPatterns("/console/**", "/billing/**", "/product/**", "/inventory/**",
                        "/reports/**", "/customer/**", "/supplier/**", "/purchase/**");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "/organization/register");
    }
}
