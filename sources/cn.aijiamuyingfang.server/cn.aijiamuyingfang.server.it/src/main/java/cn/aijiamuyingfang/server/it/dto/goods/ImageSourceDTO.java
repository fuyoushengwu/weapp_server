package cn.aijiamuyingfang.server.it.dto.goods;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

/**
 * [描述]:
 * <p>
 * 图片资源
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2019-05-17 18:28:31
 */
@Entity(name = "image_source")
@Data
public class ImageSourceDTO {
  @Id
  private String id;

  private String url;
}
