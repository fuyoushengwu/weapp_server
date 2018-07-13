package cn.aijiamuyingfang.server.goods.controller;

import cn.aijiamuyingfang.server.client.itapi.impl.StoreControllerClient;
import cn.aijiamuyingfang.server.commons.annotation.TestDescription;
import cn.aijiamuyingfang.server.commons.utils.StringUtils;
import cn.aijiamuyingfang.server.domain.address.City;
import cn.aijiamuyingfang.server.domain.address.County;
import cn.aijiamuyingfang.server.domain.address.Province;
import cn.aijiamuyingfang.server.domain.address.StoreAddressRequest;
import cn.aijiamuyingfang.server.domain.exception.GoodsException;
import cn.aijiamuyingfang.server.domain.goods.GetDefaultStoreIdResponse;
import cn.aijiamuyingfang.server.domain.goods.GetInUseStoreListResponse;
import cn.aijiamuyingfang.server.domain.goods.Store;
import cn.aijiamuyingfang.server.domain.goods.StoreRequest;
import cn.aijiamuyingfang.server.goods.GoodsApplication;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * [描述]:
 * <p>
 * StoreController的集成测试类
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-28 16:19:35
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT, classes = GoodsApplication.class)
public class StoreControllerTest {

  @Autowired
  private StoreControllerClient storeControllerClient;

  @Autowired
  private GoodsTestActions goodsTestActions;

  @Before
  public void before() throws IOException {
    goodsTestActions.clearData();
  }

  @After
  public void after() {
    goodsTestActions.clearData();
  }

  @Test
  @TestDescription(description = "分页查询门店数据", condition = "数据库中没有分页数据")
  public void testGetInUseStores_001() throws JsonParseException, JsonMappingException, IOException {
    GetInUseStoreListResponse getInUseStoreResponse = storeControllerClient
        .getInUseStoreList(GoodsTestActions.ADMIN_USER_TOKEN, 1, 2);
    Assert.assertEquals(0, getInUseStoreResponse.getTotalpage());
    Assert.assertEquals(1, getInUseStoreResponse.getCurrentpage());
    Assert.assertEquals(0, getInUseStoreResponse.getDataList().size());
  }

  @Test
  @TestDescription(description = "分页查询门店数据", condition = "数据库中存在门店数据")
  public void testGetInUseStores_002() throws JsonParseException, JsonMappingException, IOException {
    for (int i = 0; i < 4; i++) {
      StoreRequest storeRequest = new StoreRequest();
      storeRequest.setName("store " + i);
      goodsTestActions.storeControllerClient.createStore(GoodsTestActions.ADMIN_USER_TOKEN, null, null, storeRequest);
    }

    // {currentpage,pageisze}
    int[][] parameterArr = { { 0, 0 }, { 0, 1 }, { 0, 2 }, { 0, 3 }, { 1, 0 }, { 1, 1 }, { 1, 2 }, { 1, 3 }, { 2, 0 },
        { 2, 1 }, { 2, 2 }, { 2, 3 }, { 3, 0 }, { 3, 1 }, { 3, 2 }, { 3, 3 }, };
    // {totalpage,currentpage,datasize}
    int[][] expectedArr = { { 4, 1, 1 }, { 4, 1, 1 }, { 2, 1, 2 }, { 2, 1, 3 }, { 4, 1, 1 }, { 4, 1, 1 }, { 2, 1, 2 },
        { 2, 1, 3 }, { 4, 2, 1 }, { 4, 2, 1 }, { 2, 2, 2 }, { 2, 2, 1 }, { 4, 3, 1 }, { 4, 3, 1 }, { 2, 3, 0 },
        { 2, 3, 0 }, };
    for (int i = 0; i < parameterArr.length; i++) {
      int[] parameter = parameterArr[i];

      GetInUseStoreListResponse getInUseStoreResponse = storeControllerClient
          .getInUseStoreList(GoodsTestActions.ADMIN_USER_TOKEN, parameter[0], parameter[1]);

      int[] expected = expectedArr[i];
      Assert.assertEquals(expected[0], getInUseStoreResponse.getTotalpage());
      Assert.assertEquals(expected[1], getInUseStoreResponse.getCurrentpage());
      Assert.assertEquals(expected[2], getInUseStoreResponse.getDataList().size());
    }
  }

  @Test
  @TestDescription(description = "创建门店", condition = "没有门店图片和详情图片")
  public void testCreateStore_001() throws URISyntaxException, IOException {
    StoreRequest request = new StoreRequest();
    request.setName("store1");
    Store store = goodsTestActions.storeControllerClient.createStore(GoodsTestActions.ADMIN_USER_TOKEN, null, null,
        request);
    Assert.assertTrue(StringUtils.isEmpty(store.getCoverImg()));
    Assert.assertEquals(0, store.getDetailImgList().size());
  }

  @Test
  @TestDescription(description = "创建门店", condition = "有门店图片和详情图片")
  public void testCreateStore_002() throws URISyntaxException, IOException {
    StoreRequest request = new StoreRequest();
    request.setName("store1");
    File coverImg = new File(this.getClass().getClassLoader().getResource("testdata/store_logo.jpg").getPath());

    List<File> detailImgs = new ArrayList<>();
    detailImgs.add(new File(this.getClass().getClassLoader().getResource("testdata/store_detail.jpg").getPath()));
    detailImgs.add(new File(this.getClass().getClassLoader().getResource("testdata/store_detail.jpg").getPath()));

    Store store = goodsTestActions.storeControllerClient.createStore(GoodsTestActions.ADMIN_USER_TOKEN, coverImg,
        detailImgs, request);
    Assert.assertTrue(StringUtils.hasContent(store.getId()));
    Assert.assertEquals(2, store.getDetailImgList().size());
    Assert.assertTrue(StringUtils.hasContent(store.getCoverImg()));
  }

  @Test
  @TestDescription(description = "创建门店", condition = "有门店图片")
  public void testCreateStore_003() throws URISyntaxException, IOException {
    StoreRequest request = new StoreRequest();
    request.setName("store1");
    File coverImg = new File(this.getClass().getClassLoader().getResource("testdata/store_logo.jpg").getPath());
    Store store = goodsTestActions.storeControllerClient.createStore(GoodsTestActions.ADMIN_USER_TOKEN, coverImg, null,
        request);
    Assert.assertTrue(StringUtils.hasContent(store.getId()));

  }

  @Test(expected = GoodsException.class)
  @TestDescription(description = "获取门店信息", condition = "门店不存在")
  public void testGetStore_001() throws IOException {
    storeControllerClient.getStore(GoodsTestActions.ADMIN_USER_TOKEN, "notexist");
    Assert.fail();
  }

  @Test

  @TestDescription(description = "获取门店信息", condition = "门店存在")
  public void testGetStore_002() throws IOException {
    goodsTestActions.createStoreOne();
    Store actualStore = storeControllerClient.getStore(GoodsTestActions.ADMIN_USER_TOKEN, goodsTestActions.storeoneId);
    Assert.assertEquals(goodsTestActions.storeoneId, actualStore.getId());
  }

  @Test
  @TestDescription(description = "更新门店名称信息")
  public void testUpdateStore_001() throws URISyntaxException, IOException {
    goodsTestActions.createStoreOne();
    StoreRequest storeRequest = new StoreRequest();
    storeRequest.setName("store one rename");
    Store actualStore = storeControllerClient.updateStore(GoodsTestActions.ADMIN_USER_TOKEN,
        goodsTestActions.storeoneId, storeRequest);
    Assert.assertEquals("store one rename", actualStore.getName());
  }

  @Test(expected = GoodsException.class)
  @TestDescription(description = "更新不存在的门店")
  public void testUpdateStore_003() throws URISyntaxException, IOException {
    StoreRequest storeRequest = new StoreRequest();
    storeRequest.setName("store one rename");
    storeControllerClient.updateStore(GoodsTestActions.ADMIN_USER_TOKEN, "not_exit_storeid", storeRequest);
    Assert.fail();
  }

  @Test
  @TestDescription(description = "废弃已存在的门店")
  public void testDeprecateStore_001() throws IOException, URISyntaxException {
    goodsTestActions.createStoreOne();
    goodsTestActions.createStoreTwo();

    GetInUseStoreListResponse getInUseStoreResponse = storeControllerClient
        .getInUseStoreList(GoodsTestActions.ADMIN_USER_TOKEN, 1, 10);
    Assert.assertEquals(1, getInUseStoreResponse.getTotalpage());
    Assert.assertEquals(1, getInUseStoreResponse.getCurrentpage());
    Assert.assertEquals(2, getInUseStoreResponse.getDataList().size());

    goodsTestActions.deprecatedStoreOne();

    getInUseStoreResponse = storeControllerClient.getInUseStoreList(GoodsTestActions.ADMIN_USER_TOKEN, 1, 10);
    Assert.assertEquals(1, getInUseStoreResponse.getTotalpage());
    Assert.assertEquals(1, getInUseStoreResponse.getCurrentpage());
    Assert.assertEquals(1, getInUseStoreResponse.getDataList().size());
    Assert.assertEquals(goodsTestActions.storetwoId, getInUseStoreResponse.getDataList().get(0).getId());
  }

  @Test(expected = GoodsException.class)
  @TestDescription(description = "废弃不存在的门店")
  public void testDeprecateStore_002() throws IOException, URISyntaxException {
    goodsTestActions.createStoreOne();
    goodsTestActions.createStoreTwo();
    storeControllerClient.deprecateStore(GoodsTestActions.ADMIN_USER_TOKEN, "notexist", false);
    Assert.fail();
  }

  @Test
  @TestDescription(description = "没有门店存在的情况下获取默认门店Id")
  public void testGetDefaultStoreId_001() throws IOException {
    GetDefaultStoreIdResponse response = storeControllerClient.getDefaultStoreId(GoodsTestActions.ADMIN_USER_TOKEN);
    Assert.assertNotNull(response);
    Assert.assertNull(response.getDefaultId());

  }

  @Test
  @TestDescription(description = "存在多门店的情况下获取默认门店ID")
  public void testGetDefaultStoreId_002() throws IOException {
    goodsTestActions.createStoreOne();
    goodsTestActions.createStoreTwo();

    GetDefaultStoreIdResponse response = storeControllerClient.getDefaultStoreId(GoodsTestActions.ADMIN_USER_TOKEN);
    Assert.assertNotNull(response);
    Assert.assertEquals(goodsTestActions.storeoneId, response.getDefaultId());
  }

  @Test
  @TestDescription(description = "不存在门店的情况下获取门店分布的城市")
  public void testGetStoresCity_001() throws IOException {
    List<String> cities = storeControllerClient.getStoresCity(GoodsTestActions.ADMIN_USER_TOKEN);
    Assert.assertEquals(0, cities.size());
  }

  @Test
  @TestDescription(description = "一个城市有多门店情况下获取门店分布的城市")
  public void testGetStoresCity_002() throws IOException {

    StoreAddressRequest storeaddressRequest1 = new StoreAddressRequest();
    Province storeAddress1Province = new Province();
    storeAddress1Province.setName("Province");
    storeaddressRequest1.setProvince(storeAddress1Province);
    City storeAddress1City = new City();
    storeAddress1City.setName("City1");
    storeaddressRequest1.setCity(storeAddress1City);
    County storeAddress1County = new County();
    storeAddress1County.setName("County1");
    storeaddressRequest1.setCounty(storeAddress1County);

    StoreRequest storeRequest1 = new StoreRequest();
    storeRequest1.setName("store 1");
    storeRequest1.setStoreaddressRequest(storeaddressRequest1);
    goodsTestActions.storeControllerClient.createStore(GoodsTestActions.ADMIN_USER_TOKEN, null, null, storeRequest1);

    StoreAddressRequest storeAddressRequest2 = new StoreAddressRequest();
    Province storeAddress2Province = new Province();
    storeAddress2Province.setName("Province");
    storeAddressRequest2.setProvince(storeAddress2Province);
    City storeAddress2City = new City();
    storeAddress2City.setName("City1");
    storeAddressRequest2.setCity(storeAddress2City);
    County storeAddress2County = new County();
    storeAddress2County.setName("County2");
    storeAddressRequest2.setCounty(storeAddress2County);
    StoreRequest storeRequest2 = new StoreRequest();
    storeRequest2.setName("store 2");
    storeRequest2.setStoreaddressRequest(storeAddressRequest2);
    goodsTestActions.storeControllerClient.createStore(GoodsTestActions.ADMIN_USER_TOKEN, null, null, storeRequest2);

    StoreAddressRequest storeAddressRequest3 = new StoreAddressRequest();
    Province storeAddress3Province = new Province();
    storeAddress3Province.setName("Province");
    storeAddressRequest3.setProvince(storeAddress3Province);
    City storeAddress3City = new City();
    storeAddress3City.setName("City2");
    storeAddressRequest3.setCity(storeAddress3City);
    County storeAddress3County = new County();
    storeAddress3County.setName("County3");
    storeAddressRequest3.setCounty(storeAddress3County);
    StoreRequest storeRequest3 = new StoreRequest();
    storeRequest3.setName("store 3");
    storeRequest3.setStoreaddressRequest(storeAddressRequest3);
    goodsTestActions.storeControllerClient.createStore(GoodsTestActions.ADMIN_USER_TOKEN, null, null, storeRequest3);

    List<String> cities = storeControllerClient.getStoresCity(GoodsTestActions.ADMIN_USER_TOKEN);
    Assert.assertEquals(2, cities.size());
  }
}
