package cn.aijiamuyingfang.server.goods.service;

import cn.aijiamuyingfang.server.domain.goods.GoodDetail;
import cn.aijiamuyingfang.server.domain.goods.db.GoodDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * [描述]:
 * <p>
 * 商品详情Service
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-01 02:43:34
 */
@Service
public class GoodDetailService {
  @Autowired
  private GoodDetailRepository goodDetailRepository;

  /**
   * 创建商品详细信息
   * 
   * @param gooddetail
   */
  public GoodDetail createGoodDetail(GoodDetail gooddetail) {
    if (null == gooddetail) {
      return null;
    }
    return goodDetailRepository.save(gooddetail);
  }

  /**
   * 获得商品详细信息
   * 
   * @param gooddetailId
   * @return
   */
  public GoodDetail getGoodDetail(String gooddetailId) {
    return goodDetailRepository.findOne(gooddetailId);
  }
}
