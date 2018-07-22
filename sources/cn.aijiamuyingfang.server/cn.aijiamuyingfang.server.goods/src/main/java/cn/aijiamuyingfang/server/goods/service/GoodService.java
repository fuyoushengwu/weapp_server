package cn.aijiamuyingfang.server.goods.service;

import cn.aijiamuyingfang.commons.controller.bean.ResponseCode;
import cn.aijiamuyingfang.commons.domain.exception.GoodsException;
import cn.aijiamuyingfang.commons.domain.goods.Good;
import cn.aijiamuyingfang.server.domain.goods.db.GoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * [描述]:
 * <p>
 * 商品服务Service
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-26 23:50:44
 */
@Service
public class GoodService {
  @Autowired
  private GoodRepository goodRepository;

  /**
   * 添加商品
   * 
   * @param good
   */
  public Good createGood(Good good) {
    if (null == good) {
      return good;
    }
    return goodRepository.saveAndFlush(good);
  }

  /**
   * 
   * 获得具体商品信息
   * 
   * @param goodid
   * @return
   */
  public Good getGood(String goodid) {
    return goodRepository.findOne(goodid);
  }

  /**
   * 更新Good信息
   * 
   * @param goodid
   * @param updateGood
   * @return
   */
  public Good updateGood(String goodid, Good updateGood) {
    Good good = goodRepository.findOne(goodid);
    if (null == good) {
      throw new GoodsException(ResponseCode.GOOD_NOT_EXIST, goodid);
    }
    if (null == updateGood) {
      return good;
    }
    good.update(updateGood);
    goodRepository.saveAndFlush(good);
    return good;
  }

  /**
   * 废弃商品
   * 
   * @param goodid
   */
  public void deprecateGood(String goodid) {
    Good good = goodRepository.findOne(goodid);
    if (good != null) {
      good.setDeprecated(true);
      goodRepository.saveAndFlush(good);
    }
  }
}
