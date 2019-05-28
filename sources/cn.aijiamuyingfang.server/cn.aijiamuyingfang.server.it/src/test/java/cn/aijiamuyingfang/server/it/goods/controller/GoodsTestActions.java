package cn.aijiamuyingfang.server.it.goods.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.aijiamuyingfang.client.domain.classify.Classify;
import cn.aijiamuyingfang.client.domain.goods.Good;
import cn.aijiamuyingfang.client.domain.store.Store;
import cn.aijiamuyingfang.client.rest.api.impl.ClassifyControllerClient;
import cn.aijiamuyingfang.client.rest.api.impl.StoreControllerClient;
import cn.aijiamuyingfang.server.it.AbstractTestAction;

/**
 * [描述]:
 * <p>
 * 测试动作集
 * </p>
 *
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-12 02:15:05
 */
@Service
public class GoodsTestActions extends AbstractTestAction {
  @Autowired
  private StoreControllerClient storeControllerClient;

  @Autowired
  private ClassifyControllerClient classifyControllerClient;

  public Store getStoreOne() throws IOException {
    if (null == storeOne) {
      Store storeRequest = new Store();
      storeRequest.setName("store1");
      this.storeOne = storeControllerClient.createStore(null, null, storeRequest, getAdminAccessToken());
    }
    return storeOne;
  }

  public void deprecatedStoreOne() throws IOException {
    if (storeOne != null) {
      storeControllerClient.deprecateStore(storeOne.getId(), getAdminAccessToken(), false);
      storeOne = null;
    }
  }

  public Store getStoreTwo() throws IOException {
    if (null == storeTwo) {
      Store storeRequest = new Store();
      storeRequest.setName("store2");
      this.storeTwo = storeControllerClient.createStore(null, null, storeRequest, getAdminAccessToken());
    }
    return storeTwo;
  }

  public Classify getClassifyOne() throws IOException {
    if (null == classifyOne) {
      Classify classifyRequest = new Classify();
      classifyRequest.setName("classify1");
      this.classifyOne = classifyControllerClient.createTopClassify(classifyRequest, getAdminAccessToken());
    }
    return classifyOne;
  }

  public void addGoodOneForSubClassifyOne() throws IOException {
    Classify subClassifyOne = getSubClassifyOneForClassifyOne();
    Good goodOne = getGoodOne();
    classifyControllerClient.addClassifyGood(subClassifyOne.getId(), goodOne.getId(), super.getAdminAccessToken(),
        false);
  }

  public void deleteClassifyOne() throws IOException {
    if (classifyOne != null) {
      classifyControllerClient.deleteClassify(classifyOne.getId(), getAdminAccessToken(), false);
      classifyOne = null;
    }
  }

  public void deleteGoodOne() throws IOException {
    if (goodOne != null) {
      goodControllerClient.deprecateGood(goodOne.getId(), getAdminAccessToken(), false);
      goodOne = null;
    }
  }

  public Classify getSubClassifyOneForClassifyOne() throws IOException {
    if (null == subClassifyOne) {
      Classify classifyOne = getClassifyOne();
      Classify classifyRequest = new Classify();
      classifyRequest.setName("subclassifyoneforclassifyone");
      this.subClassifyOne = classifyControllerClient.createSubClassify(classifyOne.getId(), null, classifyRequest,
          getAdminAccessToken());
    }
    return subClassifyOne;

  }

}
