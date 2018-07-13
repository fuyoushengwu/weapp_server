package cn.aijiamuyingfang.server.goods.controller;

import cn.aijiamuyingfang.server.client.api.impl.GoodControllerClient;
import cn.aijiamuyingfang.server.client.itapi.impl.ClassifyControllerClient;
import cn.aijiamuyingfang.server.client.itapi.impl.StoreControllerClient;
import cn.aijiamuyingfang.server.domain.goods.Classify;
import cn.aijiamuyingfang.server.domain.goods.ClassifyRequest;
import cn.aijiamuyingfang.server.domain.goods.Good;
import cn.aijiamuyingfang.server.domain.goods.GoodRequest;
import cn.aijiamuyingfang.server.domain.goods.Store;
import cn.aijiamuyingfang.server.domain.goods.StoreRequest;
import cn.aijiamuyingfang.server.domain.goods.db.ClassifyRepository;
import cn.aijiamuyingfang.server.domain.goods.db.GoodRepository;
import cn.aijiamuyingfang.server.domain.goods.db.StoreRepository;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
public class GoodsTestActions {
  /**
   * Admin用户的JWT
   */
  public static String ADMIN_USER_TOKEN = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI1YzZhMTMyYS04MjlmLTE"
      + "xZTgtOTZmYy0wMGNmZTA0MzBlMmEiLCJjcmVhdGVkIjoxNTMxMTQ1NTY2NTcyLCJl"
      + "eHAiOjE1MzIwMDk1NjZ9.CVWIQTMg37WCzc5GUQwu-oblPKUTzZ60sWKLJrom7tZLY"
      + "hs1vMq3TSWNR-YQJWWfs-yD7y7uxbUgW9cPQMplsg";

  /**
   * Admin用户的Id
   */
  public static final String ADMIN_USER_ID = "5c6a132a-829f-11e8-96fc-00cfe0430e2a";

  @Autowired
  public StoreRepository storeRepository;

  @Autowired
  public ClassifyRepository classifyRepository;

  @Autowired
  private GoodRepository goodRepository;

  @Autowired
  public StoreControllerClient storeControllerClient;

  @Autowired
  public ClassifyControllerClient classifyControllerClient;

  @Autowired
  public GoodControllerClient goodControllerClient;

  public void clearData() {
    storeRepository.deleteAll();
    List<Classify> classifyList = classifyRepository.findByLevel(1);
    classifyRepository.delete(classifyList);
    goodRepository.deleteAll();
    storeoneId = null;
    storetwoId = null;
    classifyoneId = null;
    subclassifyoneId = null;
    goodoneId = null;
  }

  public String storeoneId;

  public Store createStoreOne() throws IOException {
    StoreRequest storeRequest = new StoreRequest();
    storeRequest.setName("store1");
    Store store = storeControllerClient.createStore(ADMIN_USER_TOKEN, null, null, storeRequest);
    if (store != null) {
      storeoneId = store.getId();
    }
    return store;
  }

  public void deprecatedStoreOne() throws IOException {
    storeControllerClient.deprecateStore(ADMIN_USER_TOKEN, storeoneId, false);
    storeoneId = null;
  }

  public String storetwoId;

  public Store createStoreTwo() throws IOException {
    StoreRequest storeRequest = new StoreRequest();
    storeRequest.setName("store2");
    Store store = storeControllerClient.createStore(ADMIN_USER_TOKEN, null, null, storeRequest);
    if (store != null) {
      storetwoId = store.getId();
    }
    return store;
  }

  public String classifyoneId;

  public Classify createClassifyOne() throws IOException {
    ClassifyRequest classifyRequest = new ClassifyRequest();
    classifyRequest.setName("classify1");
    Classify classify = classifyControllerClient.createTopClassify(ADMIN_USER_TOKEN, classifyRequest);
    if (classify != null) {
      classifyoneId = classify.getId();
    }
    return classify;
  }

  public void applyClassifyOneForStoreOne() throws IOException {
    storeControllerClient.addStoreClassify(ADMIN_USER_TOKEN, storeoneId, classifyoneId, false);
  }

  public String subclassifyoneId;

  public void createSubClassifyOneForClassifyOne() throws IOException {
    ClassifyRequest classifyRequest = new ClassifyRequest();
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

  public String goodoneId;

  public Good createGoodOne() throws IOException {
    GoodRequest goodRequest = new GoodRequest();
    goodRequest.setName("good 1");
    goodRequest.setCount(100);
    goodRequest.setPrice(100);
    goodRequest.setBarcode("111111111111");
    Good good = goodControllerClient.createGood(ADMIN_USER_TOKEN, null, null, goodRequest);
    if (good != null) {
      goodoneId = good.getId();
    }
    return good;
  }

  public void applyGoodOneForSubClassifyOne() throws IOException {
    classifyControllerClient.addClassifyGood(ADMIN_USER_TOKEN, subclassifyoneId, goodoneId, false);
  }

  public void deprecateGoodOne() throws IOException {
    goodControllerClient.deprecateGood(ADMIN_USER_TOKEN, goodoneId, false);
  }

}
