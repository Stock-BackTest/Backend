package com.stockbacktest.backend.common.web;

import com.stockbacktest.backend.common.logging.LoggingFilterProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties({
    LoggingFilterProperties.class
})
@Configuration
public class WebConfig {

}
