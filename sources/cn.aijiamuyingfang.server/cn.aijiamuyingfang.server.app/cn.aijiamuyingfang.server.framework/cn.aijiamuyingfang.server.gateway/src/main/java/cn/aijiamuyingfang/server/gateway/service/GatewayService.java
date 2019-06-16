package cn.aijiamuyingfang.server.gateway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import cn.aijiamuyingfang.server.dnspod.api.impl.DNSPodClient;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GatewayService {
  @Value("${weapp.dnspod.token-id}")
  private String tokenId;

  @Value("${weapp.dnspod.token-value}")
  private String tokenValue;

  @Value("${weapp.dnspod.domain}")
  private String domain;

  @Autowired
  private DNSPodClient dnspodClient;

  /**
   * 半分钟执行一次更新DNSPOD的操作
   */
  @Async
  @Scheduled(fixedDelay = 1000 * 60 * 30)
  public void updateDNSPod() {
    try {
      for (String str : domain.split(",")) {
        dnspodClient.updateDNSPod(str, tokenId, tokenValue);
      }
    } catch (Exception e) {
      log.error("update dnspod failed", e);
    }
  }
}
