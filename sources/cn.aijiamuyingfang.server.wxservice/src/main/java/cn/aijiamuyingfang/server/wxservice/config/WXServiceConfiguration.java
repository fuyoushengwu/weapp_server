package cn.aijiamuyingfang.server.wxservice.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.binarywang.wx.miniapp.api.WxMaMsgService;
import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.WxMaUserService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.WxMaConfig;
import cn.binarywang.wx.miniapp.config.WxMaInMemoryConfig;

/**
 * [描述]:
 * <p>
 * WXService的配置类
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-25 04:15:59
 */
@Configuration
@ConditionalOnClass(WxMaService.class)
@EnableConfigurationProperties(MiniAppProperties.class)
public class WXServiceConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public WxMaConfig maConfig(MiniAppProperties properties) {
		WxMaInMemoryConfig config = new WxMaInMemoryConfig();
		config.setAppid(properties.getAppid());
		config.setSecret(properties.getSecret());
		config.setMsgDataFormat(properties.getMsgDataFormat());
		return config;
	}

	@Bean
	@ConditionalOnMissingBean
	public WxMaService wxMaService(WxMaConfig maConfig) {
		WxMaService service = new WxMaServiceImpl();
		service.setWxMaConfig(maConfig);
		return service;
	}

	@Bean
	@ConditionalOnMissingBean
	public WxMaMsgService wxMaMsgService(WxMaService wxMaService) {
		return wxMaService.getMsgService();
	}

	@Bean
	@ConditionalOnMissingBean
	public WxMaUserService wxMaUserService(WxMaService wxMaService) {
		return wxMaService.getUserService();
	}
}