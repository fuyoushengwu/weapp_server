package cn.aijiamuyingfang.server.property;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;

public class CustomEventListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

  @Override
  public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
    event.getEnvironment().getPropertySources().addLast(new RandomServerPortPropertySource());
  }
}