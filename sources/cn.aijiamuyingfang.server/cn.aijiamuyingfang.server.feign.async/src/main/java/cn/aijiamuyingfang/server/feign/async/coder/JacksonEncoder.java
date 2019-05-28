package cn.aijiamuyingfang.server.feign.async.coder;

import java.lang.reflect.Type;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import feign.RequestTemplate;
import feign.codec.EncodeException;
import feign.codec.Encoder;

/**
 * [描述]:
 * <p>
 * 定制Feign Encoder
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2019-05-20 11:13:20
 */
public class JacksonEncoder implements Encoder {
  private final ObjectMapper mapper;

  public JacksonEncoder(ObjectMapper mapper) {
    this.mapper = mapper;
  }

  @Override
  public void encode(Object object, Type bodyType, RequestTemplate template) {
    try {
      JavaType e = this.mapper.getTypeFactory().constructType(bodyType);
      template.body(this.mapper.writerFor(e).writeValueAsString(object));
    } catch (JsonProcessingException var5) {
      throw new EncodeException(var5.getMessage(), var5);
    }
  }

}
