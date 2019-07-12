package cn.aijiamuyingfang.vo.message;

import lombok.Data;

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
@Data
public class TemplateMsgKeyValue {
  private String name;

  private String value;

  private String color;

  public TemplateMsgKeyValue() {
  }
  
  public TemplateMsgKeyValue(String name, String value) {
    this.name = name;
    this.value = value;
  }
}