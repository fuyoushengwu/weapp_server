package cn.aijiamuyingfang.server.feign.domain.message;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * [描述]:
 * <p>
 * 模板消息
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-25 15:51:38
 */
@Data
public class TemplateMsg {
  /**
   * 表单提交场景下，为 submit 事件带上的 formId；支付场景下，为本次支付的 prepay_id
   */
  private String formid;

  /**
   * 点击模板卡片后的跳转页面，仅限本小程序内的页面。支持带参数,（示例index?foo=bar）。该字段不填则模板无跳转。
   */
  private String page;

  /**
   * 模板内容，不填则下发空模板
   */
  private List<TemplateMsgKeyValue> data = new ArrayList<>();
}
