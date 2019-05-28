package cn.aijiamuyingfang.client.domain.classify;

import cn.aijiamuyingfang.client.domain.ImageSource;
import lombok.Data;

/**
 * [描述]:
 * <p>
 * 条目
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-27 00:12:32
 */
@Data
public class Classify {
  /**
   * 条目Id
   */
  private String id;

  /**
   * 条目名
   */
  private String name;

  /**
   * 条目层级(最顶层的条目为1,顶层条目下的子条目为2)
   */
  private int level;

  /**
   * 条目封面
   */
  private ImageSource coverImg;
}
