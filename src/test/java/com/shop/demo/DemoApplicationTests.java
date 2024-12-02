package com.shop.demo;

import com.shop.demo.service.product.ProductService;
import com.shop.demo.service.discountpolicy.DiscountPolicyService;
import com.shop.demo.service.discountprocessing.DiscountProcessingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    private ProductService productService;

    @Autowired
    private DiscountPolicyService discountPolicyService;

    @Autowired
    private DiscountProcessingService discountProcessingService;

    @Test
    void contextLoads() {
        assertThat(productService).isNotNull();
        assertThat(discountPolicyService).isNotNull();
        assertThat(discountProcessingService).isNotNull();
    }

}
