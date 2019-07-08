package cn.aijiamuyingfang.server.goods.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import cn.aijiamuyingfang.commons.utils.CollectionUtils;
import cn.aijiamuyingfang.commons.utils.StringUtils;
import cn.aijiamuyingfang.server.domain.response.ResponseCode;
import cn.aijiamuyingfang.server.exception.GoodsException;
import cn.aijiamuyingfang.server.goods.db.ClassifyRepository;
import cn.aijiamuyingfang.server.goods.db.GoodRepository;
import cn.aijiamuyingfang.server.goods.domain.response.PagableGoodList;
import cn.aijiamuyingfang.server.goods.dto.Classify;
import cn.aijiamuyingfang.server.goods.dto.Good;

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
  public PagableGoodList getClassifyGoodList(String classifyId, List<String> packFilter,
      List<String> levelFilter, String orderType, String orderValue, int currentPage, int pageSize) {
    Classify classify = classifyRepository.findOne(classifyId);
    if (null == classify) {
      throw new GoodsException(ResponseCode.CLASSIFY_NOT_EXIST, classifyId);
    }
    // PageRequest的Page参数是基于0的,但是currentPage是基于1的,所有将currentPage作为参数传递给PgeRequest时需要'-1'
    PageRequest pageRequest;
    if (StringUtils.hasContent(orderType)) {
      pageRequest = new PageRequest(currentPage - 1, pageSize, Sort.Direction.fromString(orderValue), orderType);
    } else {
      pageRequest = new PageRequest(currentPage - 1, pageSize);
    }
    Page<Good> goodPage;
    if (CollectionUtils.isEmpty(packFilter) && CollectionUtils.isEmpty(levelFilter)) {
      goodPage = goodRepository.findClassifyGood(classifyId, pageRequest);
    } else if (CollectionUtils.isEmpty(packFilter)) {
      goodPage = goodRepository.findClassifyGoodByLevelIn(classifyId, levelFilter, pageRequest);
    } else if (CollectionUtils.isEmpty(levelFilter)) {
      goodPage = goodRepository.findClassifyGoodByPackIn(classifyId, packFilter, pageRequest);
    } else {
      goodPage = goodRepository.findClassifyGoodByPackInAndLevelIn(classifyId, packFilter, levelFilter, pageRequest);
    }
    PagableGoodList response = new PagableGoodList();
    response.setCurrentPage(goodPage.getNumber() + 1);
    response.setDataList(goodPage.getContent());
    response.setTotalpage(goodPage.getTotalPages());
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
