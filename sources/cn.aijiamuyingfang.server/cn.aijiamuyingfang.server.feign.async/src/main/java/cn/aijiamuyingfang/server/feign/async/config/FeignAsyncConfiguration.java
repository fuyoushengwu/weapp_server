package cn.aijiamuyingfang.server.feign.async.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.feign.AnnotatedParameterProcessor;
import org.springframework.cloud.netflix.feign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;

import cn.aijiamuyingfang.server.feign.async.contract.FutureContract;
import cn.aijiamuyingfang.server.feign.async.core.AsyncFeignBuilder;
import feign.Contract;
import feign.Feign;
import feign.Retryer;

@Configuration
public class FeignAsyncConfiguration {
  @Autowired(required = false)
  private List<AnnotatedParameterProcessor> parameterProcessors = new ArrayList<>();

  @Bean
  public Contract feignContract(ConversionService feignConversionService) {
    Contract delegate = new SpringMvcContract(this.parameterProcessors, feignConversionService);
    return new FutureContract(delegate);
  }

  @Bean
  public Retryer feignRetryer() {
    return Retryer.NEVER_RETRY;
  }

  @Bean
  public Feign.Builder feignBuilder(Retryer retryer) {
    return new AsyncFeignBuilder().retryer(retryer);
  }

}
