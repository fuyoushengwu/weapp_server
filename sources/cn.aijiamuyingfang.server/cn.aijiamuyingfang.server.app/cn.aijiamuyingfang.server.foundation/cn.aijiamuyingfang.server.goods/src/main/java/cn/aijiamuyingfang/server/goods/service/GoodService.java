package cn.aijiamuyingfang.server.goods.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.aijiamuyingfang.commons.utils.StringUtils;
import cn.aijiamuyingfang.server.domain.response.ResponseCode;
import cn.aijiamuyingfang.server.exception.GoodsException;
import cn.aijiamuyingfang.server.goods.db.GoodDetailRepository;
import cn.aijiamuyingfang.server.goods.db.GoodRepository;
import cn.aijiamuyingfang.server.goods.domain.Good;
import cn.aijiamuyingfang.server.goods.domain.GoodDetail;
import cn.aijiamuyingfang.server.goods.domain.request.SaleGood;

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

  @Autowired
  private GoodDetailRepository goodDetailRepository;

  /**
   * 添加商品
   * 
   * @param good
   */
  public Good createORUpdateGood(Good good) {
    if (null == good) {
      return good;
    }
    if (StringUtils.hasContent(good.getId())) {
      Good oriGood = goodRepository.findOne(good.getId());
      if (oriGood != null) {
        oriGood.update(good);
        return goodRepository.saveAndFlush(oriGood);
      }
    }
    return goodRepository.saveAndFlush(good);
  }

  /**
   * 
   * 获得具体商品信息
   * 
   * @param goodId
   *          商品id
   * @return
   */
  public Good getGood(String goodId) {
    return goodRepository.findOne(goodId);
  }

  /**
   * 获取商品信息
   * 
   * @param goodIdList
   * @return
   */
  public List<Good> getGoodList(List<String> goodIdList) {
    return goodRepository.findAll(goodIdList);
  }

  /**
   * 更新Good信息
   * 
   * @param goodId
   *          商品id
   * @param updateGood
   * @return
   */
  public Good updateGood(String goodId, Good updateGood) {
    Good good = goodRepository.findOne(goodId);
    if (null == good) {
      throw new GoodsException(ResponseCode.GOOD_NOT_EXIST, goodId);
    }
    if (null == updateGood) {
      return good;
    }
    good.update(updateGood);
    return goodRepository.saveAndFlush(good);
  }

  /**
   * 废弃商品
   * 
   * @param goodId
   *          商品id
   */
  public void deprecateGood(String goodId) {
    GoodDetail goodDetail = goodDetailRepository.findOne(goodId);
    if (goodDetail != null) {
      goodDetailRepository.delete(goodDetail);
    }
    Good good = goodRepository.findOne(goodId);
    if (good != null) {
      good.setDeprecated(true);
      goodRepository.saveAndFlush(good);
    }
  }

  /**
   * 售卖商品
   * 
   * @param goodId
   * @param saleGood
   */
  public void saleGood(String goodId, SaleGood saleGood) {
    if (StringUtils.isEmpty(goodId) || null == saleGood) {
      return;
    }
    Good good = goodRepository.findOne(goodId);
    if (null == good) {
      throw new IllegalArgumentException("good[" + goodId + "] not exist");
    }
    int saleCount = saleGood.getSalecount();
    int goodCount = good.getCount();

    if (saleCount > goodCount) {
      throw new IllegalArgumentException("sale count > good count");
    }
    good.setCount(goodCount - saleCount);
    good.setSalecount(saleCount + good.getSalecount());
    goodRepository.saveAndFlush(good);
  }

  /**
   * 售卖商品
   * 
   * @param saleGoodList
   */
  public void saleGoodList(List<SaleGood> saleGoodList) {
    for (SaleGood saleGood : saleGoodList) {
      if (saleGood != null) {
        saleGood(saleGood.getGoodId(), saleGood);
      }
    }
  }

  /**
   * 废弃商品兑换券
   * 
   * @param goodvoucherId
   */
  public void deprecateGoodVoucher(String goodvoucherId) {
    goodRepository.deprecateGoodVoucher(goodvoucherId);
  }
}
