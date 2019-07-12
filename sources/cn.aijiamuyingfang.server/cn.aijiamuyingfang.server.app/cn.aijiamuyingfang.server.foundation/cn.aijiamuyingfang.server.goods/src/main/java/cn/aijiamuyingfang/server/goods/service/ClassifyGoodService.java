package cn.aijiamuyingfang.server.goods.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import cn.aijiamuyingfang.commons.utils.CollectionUtils;
import cn.aijiamuyingfang.server.goods.db.ClassifyRepository;
import cn.aijiamuyingfang.server.goods.db.GoodRepository;
import cn.aijiamuyingfang.server.goods.dto.ClassifyDTO;
import cn.aijiamuyingfang.server.goods.dto.GoodDTO;
import cn.aijiamuyingfang.server.goods.utils.ConvertService;
import cn.aijiamuyingfang.vo.exception.GoodsException;
import cn.aijiamuyingfang.vo.goods.PagableGoodList;
import cn.aijiamuyingfang.vo.response.ResponseCode;
import cn.aijiamuyingfang.vo.utils.StringUtils;

/**
 * [描述]:
 * <p>
 * 条目服务Service
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-26 23:51:11
 */
@Service
public class ClassifyGoodService {
  @Autowired
  private ClassifyRepository classifyRepository;

  @Autowired
  private GoodRepository goodRepository;

  @Autowired
  private ConvertService convertService;

  /**
   * 分页查询条目下的商品
   * 
   * @param classifyId
   *          条目id
   * @param packFilter
   *          商品过滤条件:包装类型
   * @param levelFilter
   *          商品过滤条件:商品等级
   * @param orderType
   *          商品排序条件:排序字段
   * @param orderValue
   *          商品排序条件:DESC:降序;ASC:升序.
   * @param currentPage
   *          请求页
   * @param pageSize
   *          每页商品数
   * @return
   */
  public PagableGoodList getClassifyGoodList(String classifyId, List<String> packFilter, List<String> levelFilter,
      String orderType, String orderValue, int currentPage, int pageSize) {
    ClassifyDTO classifyDTO = classifyRepository.findOne(classifyId);
    if (null == classifyDTO) {
      throw new GoodsException(ResponseCode.CLASSIFY_NOT_EXIST, classifyId);
    }
    // PageRequest的Page参数是基于0的,但是currentPage是基于1的,所有将currentPage作为参数传递给PgeRequest时需要'-1'
    PageRequest pageRequest;
    if (StringUtils.hasContent(orderType)) {
      pageRequest = new PageRequest(currentPage - 1, pageSize, Sort.Direction.fromString(orderValue), orderType);
    } else {
      pageRequest = new PageRequest(currentPage - 1, pageSize);
    }
    Page<GoodDTO> goodDTOPage;
    if (CollectionUtils.isEmpty(packFilter) && CollectionUtils.isEmpty(levelFilter)) {
      goodDTOPage = goodRepository.findClassifyGood(classifyId, pageRequest);
    } else if (CollectionUtils.isEmpty(packFilter)) {
      goodDTOPage = goodRepository.findClassifyGoodByLevelIn(classifyId, levelFilter, pageRequest);
    } else if (CollectionUtils.isEmpty(levelFilter)) {
      goodDTOPage = goodRepository.findClassifyGoodByPackIn(classifyId, packFilter, pageRequest);
    } else {
      goodDTOPage = goodRepository.findClassifyGoodByPackInAndLevelIn(classifyId, packFilter, levelFilter, pageRequest);
    }
    PagableGoodList response = new PagableGoodList();
    response.setCurrentPage(goodDTOPage.getNumber() + 1);
    response.setDataList(convertService.convertGoodDTOList(goodDTOPage.getContent()));
    response.setTotalpage(goodDTOPage.getTotalPages());
    return response;
  }

  /**
   * 移除条目下的商品
   * 
   * @param goodId
   *          商品id
   */
  public void removeClassifyGood(String goodId) {
    classifyRepository.removeClassifyGood(goodId);
  }
}
