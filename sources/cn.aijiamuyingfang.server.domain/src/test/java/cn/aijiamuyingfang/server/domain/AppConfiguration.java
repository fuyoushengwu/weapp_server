package cn.aijiamuyingfang.server.domain;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = { "cn.aijiamuyingfang.client", "cn.aijiamuyingfang.server" })
@EnableJpaRepositories(basePackages = { "cn.aijiamuyingfang.server.domain" })
@EntityScan(basePackages = { "cn.aijiamuyingfang.commons", "cn.aijiamuyingfang.server" })
public class AppConfiguration {
}