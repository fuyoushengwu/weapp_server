package cn.aijiamuyingfang.server.commons.controller.bean.wxservice;

/**
 * [描述]:
 * <p>
 * 模板消息中键值对
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-25 15:28:49
 */
public class TemplateMsgKeyValue {
  private String name;

  private String value;

  private String color;

  public TemplateMsgKeyValue() {
  }

  public TemplateMsgKeyValue(String name, String value) {
    super();
    this.name = name;
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

}