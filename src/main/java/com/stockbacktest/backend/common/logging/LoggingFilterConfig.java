package com.stockbacktest.backend.common.logging;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggingFilterConfig {

  @Bean
  public FilterRegistrationBean<LoggingFilter> LoggingFilterRegistration(
      LoggingFilterProperties loggingFilterProperties) {
    LoggingFilter loggingFilter = new LoggingFilter(loggingFilterProperties);
    FilterRegistrationBean<LoggingFilter> registrationBean = new FilterRegistrationBean<>(
        loggingFilter);
    registrationBean.setOrder(SecurityProperties.DEFAULT_FILTER_ORDER - 1);
    registrationBean.setAsyncSupported(true);
    return registrationBean;
  }
}
