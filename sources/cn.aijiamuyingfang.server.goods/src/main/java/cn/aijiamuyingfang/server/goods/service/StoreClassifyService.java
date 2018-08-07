package cn.aijiamuyingfang.server.goods.service;

import cn.aijiamuyingfang.server.domain.goods.db.StoreRepository;
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

  /**
   * 删除门店的条目
   * 
   * @param classifyid
   */
  public void removeStoreClassify(String classifyid) {
    storeRepository.removeStoreClassify(classifyid);
  }

}
