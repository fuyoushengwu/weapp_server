package cn.aijiamuyingfang.server.feign.async.coder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.RuntimeJsonMappingException;

import feign.Response;
import feign.Util;
import feign.codec.Decoder;

/**
 * [描述]:
 * <p>
 * 定制Feign Decoder
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2019-05-20 11:12:41
 */
public class JacksonDecoder implements Decoder {
  private final ObjectMapper mapper;

  public JacksonDecoder(ObjectMapper mapper) {
    this.mapper = mapper;
  }

  @Override
  public Object decode(Response response, Type type) throws IOException {
    if (response.status() == 404) {
      return Util.emptyValueOf(type);
    } else if (response.body() == null) {
      return null;
    } else {
      Object reader = response.body().asReader();
      if (!((Reader) reader).markSupported()) {
        reader = new BufferedReader((Reader) reader, 1);
      }
      try {
        ((Reader) reader).mark(1);
        if (((Reader) reader).read() == -1) {
          return null;
        } else {
          ((Reader) reader).reset();
          return this.mapper.readValue((Reader) reader, this.mapper.constructType(type));
        }
      } catch (RuntimeJsonMappingException var5) {
        if (var5.getCause() != null && var5.getCause() instanceof IOException) {
          throw (IOException) IOException.class.cast(var5.getCause());
        } else {
          throw var5;
        }
      }
    }
  }

}
