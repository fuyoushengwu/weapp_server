package cn.aijiamuyingfang.server.goods.controller;

import cn.aijiamuyingfang.commons.domain.goods.Classify;
import cn.aijiamuyingfang.commons.domain.goods.Store;
import cn.aijiamuyingfang.server.client.AbstractTestAction;
import java.io.IOException;
import org.springframework.stereotype.Service;

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

  public String storeoneId;

  public String storetwoId;

  public Store createStoreOne() throws IOException {
    Store storeRequest = new Store();
    storeRequest.setName("store1");
    Store store = storeControllerClient.createStore(ADMIN_USER_TOKEN, null, null, storeRequest);
    if (store != null) {
      storeoneId = store.getId();
    }
    return store;
  }

  public Store createStoreTwo() throws IOException {
    Store storeRequest = new Store();
    storeRequest.setName("store2");
    Store store = storeControllerClient.createStore(ADMIN_USER_TOKEN, null, null, storeRequest);
    if (store != null) {
      storetwoId = store.getId();
    }
    return store;
  }

  public void deprecatedStoreOne() throws IOException {
    storeControllerClient.deprecateStore(ADMIN_USER_TOKEN, storeoneId, false);
    storeoneId = null;
  }

  public String classifyoneId;

  public Classify createClassifyOne() throws IOException {
    Classify classifyRequest = new Classify();
    classifyRequest.setName("classify1");
    Classify classify = classifyControllerClient.createTopClassify(ADMIN_USER_TOKEN, classifyRequest);
    if (classify != null) {
      classifyoneId = classify.getId();
    }
    return classify;
  }

  public String subclassifyoneId;

  public void createSubClassifyOneForClassifyOne() throws IOException {
    Classify classifyRequest = new Classify();
    classifyRequest.setName("subclassifyoneforclassifyone");
    Classify classify = classifyControllerClient.createSubClassify(ADMIN_USER_TOKEN, classifyoneId, null,
        classifyRequest);
    if (classify != null) {
      this.subclassifyoneId = classify.getId();
    }
  }

  public void deleteClassifyOne() throws IOException {
    classifyControllerClient.deleteClassify(ADMIN_USER_TOKEN, classifyoneId, false);
    classifyoneId = null;
  }

  public void applyGoodOneForSubClassifyOne() throws IOException {
    classifyControllerClient.addClassifyGood(ADMIN_USER_TOKEN, subclassifyoneId, goodoneId, false);
  }

  public void deprecateGoodOne() throws IOException {
    goodControllerClient.deprecateGood(ADMIN_USER_TOKEN, goodoneId, false);
  }

  @Override
  public void clearData() {
    super.clearData();
    storeoneId = null;
    storetwoId = null;
    classifyoneId = null;
    subclassifyoneId = null;
  }

}
