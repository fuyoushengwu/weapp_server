package cn.aijiamuyingfang.server.feign.async.contract;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.Future;

import feign.Contract;
import feign.MethodMetadata;
import feign.Util;

/**
 * [描述]:
 * <p>
 * 支持Future返回类型的Contract
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2019-05-20 11:20:03
 */
public class FutureContract implements Contract {
  private final Contract delegate;

  public FutureContract(Contract delegate) {
    this.delegate = delegate;
  }

  @Override
  public List<MethodMetadata> parseAndValidatateMetadata(Class<?> targetType) {
    List<MethodMetadata> metadataList = this.delegate.parseAndValidatateMetadata(targetType);
    for (MethodMetadata metadata : metadataList) {
      Type type = metadata.returnType();
      if (type instanceof ParameterizedType) {
        ParameterizedType parameterizedType = ParameterizedType.class.cast(type);
        Class<?> rawType = (Class<?>) parameterizedType.getRawType();
        if (Future.class.isAssignableFrom(rawType)) {
          metadata.returnType(Util.resolveLastTypeParameter(type, rawType));
        }
      }
    }
    return metadataList;
  }

}
