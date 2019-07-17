package cn.aijiamuyingfang.server.goods.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.aijiamuyingfang.server.feign.ShopOrderClient;
import cn.aijiamuyingfang.server.goods.db.GoodDetailRepository;
import cn.aijiamuyingfang.server.goods.db.GoodRepository;
import cn.aijiamuyingfang.server.goods.dto.GoodDTO;
import cn.aijiamuyingfang.server.goods.dto.GoodDetailDTO;
import cn.aijiamuyingfang.server.goods.utils.ConvertService;
import cn.aijiamuyingfang.vo.exception.GoodsException;
import cn.aijiamuyingfang.vo.goods.Good;
import cn.aijiamuyingfang.vo.goods.SaleGood;
import cn.aijiamuyingfang.vo.response.ResponseCode;
import cn.aijiamuyingfang.vo.utils.StringUtils;

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
  private ShopOrderClient shoporderClient;

  @Autowired
  private GoodRepository goodRepository;

  @Autowired
  private GoodDetailRepository goodDetailRepository;

  @Autowired
  private ConvertService convertService;

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
      GoodDTO oriGoodDTO = goodRepository.findOne(good.getId());
      if (oriGoodDTO != null) {
        int newGoodCount = good.getCount();
        int oriGoodCount = oriGoodDTO.getCount();
        oriGoodDTO.update(good);
        Good result = convertService.convertGoodDTO(goodRepository.saveAndFlush(oriGoodDTO));
        if (newGoodCount != 0 && newGoodCount != oriGoodCount) {
          shoporderClient.updatePreOrder(good.getId());
        }
        return result;
      }
    }
    return convertService.convertGoodDTO(goodRepository.saveAndFlush(convertService.convertGood(good)));
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
    return convertService.convertGoodDTO(goodRepository.findOne(goodId));
  }

  /**
   * 获取商品信息
   * 
   * @param goodIdList
   * @return
   */
  public List<Good> getGoodList(List<String> goodIdList) {
    return convertService.convertGoodDTOList(goodRepository.findAll(goodIdList));
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
    GoodDTO goodDTO = goodRepository.findOne(goodId);
    if (null == goodDTO) {
      throw new GoodsException(ResponseCode.GOOD_NOT_EXIST, goodId);
    }
    if (null == updateGood) {
      return convertService.convertGoodDTO(goodDTO);
    }
    goodDTO.update(updateGood);
    return convertService.convertGoodDTO(goodRepository.saveAndFlush(goodDTO));
  }

  /**
   * 废弃商品
   * 
   * @param goodId
   *          商品id
   */
  public void deprecateGood(String goodId) {
    GoodDetailDTO goodDetailDTO = goodDetailRepository.findOne(goodId);
    if (goodDetailDTO != null) {
      goodDetailRepository.delete(goodDetailDTO);
    }
    GoodDTO goodDTO = goodRepository.findOne(goodId);
    if (goodDTO != null) {
      goodDTO.setDeprecated(true);
      goodRepository.saveAndFlush(goodDTO);
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
    GoodDTO goodDTO = goodRepository.findOne(goodId);
    if (null == goodDTO) {
      throw new IllegalArgumentException("good[" + goodId + "] not exist");
    }
    int saleCount = saleGood.getSalecount();
    int goodCount = goodDTO.getCount();

    if (saleCount > goodCount) {
      throw new IllegalArgumentException("sale count > good count");
    }
    goodDTO.setCount(goodCount - saleCount);
    goodDTO.setSalecount(saleCount + goodDTO.getSalecount());
    goodRepository.saveAndFlush(goodDTO);
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
