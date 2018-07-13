package cn.aijiamuyingfang.server.goods.service;

import cn.aijiamuyingfang.server.commons.controller.bean.ResponseCode;
import cn.aijiamuyingfang.server.domain.exception.GoodsException;
import cn.aijiamuyingfang.server.domain.goods.Classify;
import cn.aijiamuyingfang.server.domain.goods.Store;
import cn.aijiamuyingfang.server.domain.goods.db.ClassifyRepository;
import cn.aijiamuyingfang.server.domain.goods.db.StoreRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * [描述]:
 * <p>
 * 门店和条目的关联服务
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-29 01:18:39
 */
@Service
public class StoreClassifyService {
  @Autowired
  private StoreRepository storeRepository;

  @Autowired
  private ClassifyRepository classifyRepository;

  /**
   * 获得门店下的所有条目
   * 
   * @param storeid
   * @return
   */
  public List<Classify> getStoreClassifyList(String storeid) {
    Store store = storeRepository.findOne(storeid);
    if (null == store) {
      throw new GoodsException(ResponseCode.STORE_NOT_EXIST, storeid);
    }
    return store.getClassifyList();
  }

  /**
   * 门店下添加顶层条目
   * 
   * @param storeid
   * @param classifyid
   */
  public void addClassify(String storeid, String classifyId) {
    Classify classify = classifyRepository.findOne(classifyId);
    if (null == classify) {
      throw new GoodsException(ResponseCode.CLASSIFY_NOT_EXIST, classifyId);
    }
    Store store = storeRepository.findOne(storeid);
    if (null == store) {
      throw new GoodsException(ResponseCode.STORE_NOT_EXIST, storeid);
    }
    store.addClassify(classify);
    storeRepository.save(store);
  }

  /**
   * 删除门店的条目
   * 
   * @param classifyid
   */
  public void removeStoreClassify(String classifyid) {
    storeRepository.removeStoreClassify(classifyid);
  }

}
