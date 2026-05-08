package com.rbf.product.credit;

import com.rbf.product.common.web.OrgFilterConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(OrgFilterConfig.class)
public class CustomerCreditServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerCreditServiceApplication.class, args);
    }
}
