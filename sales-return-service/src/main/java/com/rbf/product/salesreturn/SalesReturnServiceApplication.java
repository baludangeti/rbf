package com.rbf.product.salesreturn;

import com.rbf.product.common.web.OrgFilterConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(OrgFilterConfig.class)
public class SalesReturnServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SalesReturnServiceApplication.class, args);
    }
}
