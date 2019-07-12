package cn.aijiamuyingfang.server.goods.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.aijiamuyingfang.server.goods.db.GoodDetailRepository;
import cn.aijiamuyingfang.server.goods.dto.GoodDetailDTO;
import cn.aijiamuyingfang.server.goods.utils.ConvertUtils;
import cn.aijiamuyingfang.vo.goods.GoodDetail;
import cn.aijiamuyingfang.vo.utils.StringUtils;

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
  public GoodDetail createORUpdateGoodDetail(GoodDetail gooddetail) {
    if (null == gooddetail) {
      return null;
    }
    if (StringUtils.hasContent(gooddetail.getId())) {
      GoodDetailDTO oriGoodDetailDTO = goodDetailRepository.findOne(gooddetail.getId());
      if (oriGoodDetailDTO != null) {
        oriGoodDetailDTO.update(gooddetail);
        return ConvertUtils.convertGoodDetailDTO(goodDetailRepository.saveAndFlush(oriGoodDetailDTO));
      }
    }
    return ConvertUtils
        .convertGoodDetailDTO(goodDetailRepository.saveAndFlush(ConvertUtils.convertGoodDetail(gooddetail)));
  }

  /**
   * 获得商品详细信息
   * 
   * @param gooddetailId
   * @return
   */
  public GoodDetail getGoodDetail(String gooddetailId) {
    return ConvertUtils.convertGoodDetailDTO(goodDetailRepository.findOne(gooddetailId));
  }
}
