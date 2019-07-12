package cn.aijiamuyingfang.server.it.goods.controller;

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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import cn.aijiamuyingfang.client.rest.api.impl.StoreControllerClient;
import cn.aijiamuyingfang.server.it.ITApplication;
import cn.aijiamuyingfang.server.it.UseCaseDescription;
import cn.aijiamuyingfang.vo.address.City;
import cn.aijiamuyingfang.vo.address.County;
import cn.aijiamuyingfang.vo.address.Province;
import cn.aijiamuyingfang.vo.exception.GoodsException;
import cn.aijiamuyingfang.vo.store.PagableStoreList;
import cn.aijiamuyingfang.vo.store.Store;
import cn.aijiamuyingfang.vo.store.StoreAddress;
import cn.aijiamuyingfang.vo.utils.StringUtils;

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
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT, classes = ITApplication.class)
public class StoreControllerTest {

  @Autowired
  private StoreControllerClient storeControllerClient;

  @Autowired
  private GoodsTestActions testActions;

  @Before
  public void before() throws IOException {
    testActions.clearStore();
    testActions.clearClassify();
    testActions.clearGoodDetail();
    testActions.clearGood();
    testActions.clearImageSource();
  }

  @After
  public void after() {
    testActions.clearStore();
    testActions.clearClassify();
    testActions.clearGoodDetail();
    testActions.clearGood();
    testActions.clearImageSource();
  }

  @Test
  @UseCaseDescription(description = "分页查询门店数据", condition = "数据库中没有分页数据")
  public void testGetInUseStores_001() throws JsonParseException, JsonMappingException, IOException {
    PagableStoreList getInUseStoreResponse = storeControllerClient.getInUseStoreList(1, 2);
    Assert.assertEquals(0, getInUseStoreResponse.getTotalpage());
    Assert.assertEquals(1, getInUseStoreResponse.getCurrentPage());
    Assert.assertEquals(0, getInUseStoreResponse.getDataList().size());
  }

  @Test
  @UseCaseDescription(description = "分页查询门店数据", condition = "数据库中存在门店数据")
  public void testGetInUseStores_002() throws JsonParseException, JsonMappingException, IOException {
    for (int i = 0; i < 4; i++) {
      Store storeRequest = new Store();
      storeRequest.setName("store " + i);
      storeControllerClient.createStore(null, null, storeRequest, testActions.getAdminAccessToken());
    }

    // {currentPage,pageisze}
    int[][] parameterArr = { { 0, 0 }, { 0, 1 }, { 0, 2 }, { 0, 3 }, { 1, 0 }, { 1, 1 }, { 1, 2 }, { 1, 3 }, { 2, 0 },
        { 2, 1 }, { 2, 2 }, { 2, 3 }, { 3, 0 }, { 3, 1 }, { 3, 2 }, { 3, 3 }, };
    // {totalpage,currentPage,datasize}
    int[][] expectedArr = { { 4, 1, 1 }, { 4, 1, 1 }, { 2, 1, 2 }, { 2, 1, 3 }, { 4, 1, 1 }, { 4, 1, 1 }, { 2, 1, 2 },
        { 2, 1, 3 }, { 4, 2, 1 }, { 4, 2, 1 }, { 2, 2, 2 }, { 2, 2, 1 }, { 4, 3, 1 }, { 4, 3, 1 }, { 2, 3, 0 },
        { 2, 3, 0 }, };
    for (int i = 0; i < parameterArr.length; i++) {
      int[] parameter = parameterArr[i];

      PagableStoreList getInUseStoreResponse = storeControllerClient.getInUseStoreList(parameter[0],
          parameter[1]);

      int[] expected = expectedArr[i];
      Assert.assertEquals(expected[0], getInUseStoreResponse.getTotalpage());
      Assert.assertEquals(expected[1], getInUseStoreResponse.getCurrentPage());
      Assert.assertEquals(expected[2], getInUseStoreResponse.getDataList().size());
    }
  }

  @Test
  @UseCaseDescription(description = "创建门店", condition = "没有门店图片和详情图片")
  public void testCreateStore_001() throws URISyntaxException, IOException {
    Store request = new Store();
    request.setName("store1");
    Store store = storeControllerClient.createStore(null, null, request, testActions.getAdminAccessToken());
    Assert.assertNull(store.getCoverImg());
    Assert.assertEquals(0, store.getDetailImgList().size());
  }

  @Test
  @UseCaseDescription(description = "创建门店", condition = "有门店图片和详情图片")
  public void testCreateStore_002() throws URISyntaxException, IOException {
    Store request = new Store();
    request.setName("store1");
    File coverImg = new File(this.getClass().getClassLoader().getResource("testdata/0.jpg").getPath());

    List<File> detailImgs = new ArrayList<>();
    detailImgs.add(new File(this.getClass().getClassLoader().getResource("testdata/1.jpg").getPath()));
    detailImgs.add(new File(this.getClass().getClassLoader().getResource("testdata/2.jpg").getPath()));

    Store store = storeControllerClient.createStore(coverImg, detailImgs, request, testActions.getAdminAccessToken());
    Assert.assertTrue(StringUtils.hasContent(store.getId()));
    Assert.assertEquals(2, store.getDetailImgList().size());
    Assert.assertTrue(store.getCoverImg() != null && StringUtils.hasContent(store.getCoverImg().getUrl()));
  }

  @Test
  @UseCaseDescription(description = "创建门店", condition = "有门店图片")
  public void testCreateStore_003() throws URISyntaxException, IOException {
    Store request = new Store();
    request.setName("store1");
    File coverImg = new File(this.getClass().getClassLoader().getResource("testdata/0.jpg").getPath());
    Store store = storeControllerClient.createStore(coverImg, null, request, testActions.getAdminAccessToken());
    Assert.assertTrue(StringUtils.hasContent(store.getId()));

  }

  @Test(expected = GoodsException.class)
  @UseCaseDescription(description = "获取门店信息", condition = "门店不存在")
  public void testGetStore_001() throws IOException {
    storeControllerClient.getStore("notexist");
    Assert.fail();
  }

  @Test
  @UseCaseDescription(description = "获取门店信息", condition = "门店存在")
  public void testGetStore_002() throws IOException {
    Store storeOne = testActions.getStoreOne();
    Assert.assertNotNull(storeOne);
    Store actualStore = storeControllerClient.getStore(storeOne.getId());
    Assert.assertEquals(storeOne.getName(), actualStore.getName());
  }

  @Test
  @UseCaseDescription(description = "更新门店名称信息")
  public void testUpdateStore_001() throws URISyntaxException, IOException {
    Store storeOne = testActions.getStoreOne();
    Store storeRequest = new Store();
    storeRequest.setName("store one rename");

    Store actualStore = storeControllerClient.updateStore(storeOne.getId(), storeRequest,
        testActions.getAdminAccessToken());
    Assert.assertEquals("store one rename", actualStore.getName());
  }

  @Test(expected = GoodsException.class)
  @UseCaseDescription(description = "更新不存在的门店")
  public void testUpdateStore_003() throws URISyntaxException, IOException {
    Store storeRequest = new Store();
    storeRequest.setName("store one rename");
    storeControllerClient.updateStore("NOT_EXIT_STOREID", storeRequest, testActions.getAdminAccessToken());
    Assert.fail();
  }

  @Test
  @UseCaseDescription(description = "废弃已存在的门店")
  public void testDeprecateStore_001() throws IOException, URISyntaxException {
    Store storeOne = testActions.getStoreOne();
    Assert.assertNotNull(storeOne);
    Store storeTwo = testActions.getStoreTwo();
    Assert.assertNotNull(storeTwo);

    PagableStoreList getInUseStoreResponse = storeControllerClient.getInUseStoreList(1, 10);
    Assert.assertEquals(1, getInUseStoreResponse.getTotalpage());
    Assert.assertEquals(1, getInUseStoreResponse.getCurrentPage());
    Assert.assertEquals(2, getInUseStoreResponse.getDataList().size());

    testActions.deprecatedStoreOne();
    getInUseStoreResponse = storeControllerClient.getInUseStoreList(1, 10);
    Assert.assertEquals(1, getInUseStoreResponse.getTotalpage());
    Assert.assertEquals(1, getInUseStoreResponse.getCurrentPage());
    Assert.assertEquals(1, getInUseStoreResponse.getDataList().size());
    Assert.assertEquals(storeTwo.getId(), getInUseStoreResponse.getDataList().get(0).getId());
  }

  @Test
  @UseCaseDescription(description = "废弃不存在的门店")
  public void testDeprecateStore_002() throws IOException, URISyntaxException {
    storeControllerClient.deprecateStore("notexist", testActions.getAdminAccessToken(), false);
  }

  @Test
  @UseCaseDescription(description = "没有门店存在的情况下获取默认门店Id")
  public void testGetDefaultStoreId_001() throws IOException {
    String defaultStoreId = storeControllerClient.getDefaultStoreId();
    Assert.assertNull(defaultStoreId);

  }

  @Test
  @UseCaseDescription(description = "存在多门店的情况下获取默认门店ID")
  public void testGetDefaultStoreId_002() throws IOException {
    Store storeOne = testActions.getStoreOne();
    Assert.assertNotNull(storeOne);
    Store storeTwo = testActions.getStoreTwo();
    Assert.assertNotNull(storeTwo);

    String defaultStoreId = storeControllerClient.getDefaultStoreId();
    Assert.assertEquals(storeOne.getId(), defaultStoreId);
  }

  @Test
  @UseCaseDescription(description = "不存在门店的情况下获取门店分布的城市")
  public void testGetStoresCity_001() throws IOException {
    List<String> cities = storeControllerClient.getStoresCity();
    Assert.assertEquals(0, cities.size());
  }

  @Test
  @UseCaseDescription(description = "一个城市有多门店情况下获取门店分布的城市")
  public void testGetStoresCity_002() throws IOException {

    StoreAddress storeaddressRequest1 = new StoreAddress();
    Province storeAddress1Province = new Province();
    storeAddress1Province.setName("Province");
    storeaddressRequest1.setProvince(storeAddress1Province);
    City storeAddress1City = new City();
    storeAddress1City.setName("City1");
    storeaddressRequest1.setCity(storeAddress1City);
    County storeAddress1County = new County();
    storeAddress1County.setName("County1");
    storeaddressRequest1.setCounty(storeAddress1County);

    Store storeRequest1 = new Store();
    storeRequest1.setName("store 1");
    storeRequest1.setStoreAddress(storeaddressRequest1);
    storeControllerClient.createStore(null, null, storeRequest1, testActions.getAdminAccessToken());

    StoreAddress storeAddressRequest2 = new StoreAddress();
    Province storeAddress2Province = new Province();
    storeAddress2Province.setName("Province");
    storeAddressRequest2.setProvince(storeAddress2Province);
    City storeAddress2City = new City();
    storeAddress2City.setName("City1");
    storeAddressRequest2.setCity(storeAddress2City);
    County storeAddress2County = new County();
    storeAddress2County.setName("County2");
    storeAddressRequest2.setCounty(storeAddress2County);
    Store storeRequest2 = new Store();
    storeRequest2.setName("store 2");
    storeRequest2.setStoreAddress(storeAddressRequest2);
    storeControllerClient.createStore(null, null, storeRequest2, testActions.getAdminAccessToken());

    StoreAddress storeAddressRequest3 = new StoreAddress();
    Province storeAddress3Province = new Province();
    storeAddress3Province.setName("Province");
    storeAddressRequest3.setProvince(storeAddress3Province);
    City storeAddress3City = new City();
    storeAddress3City.setName("City2");
    storeAddressRequest3.setCity(storeAddress3City);
    County storeAddress3County = new County();
    storeAddress3County.setName("County3");
    storeAddressRequest3.setCounty(storeAddress3County);
    Store storeRequest3 = new Store();
    storeRequest3.setName("store 3");
    storeRequest3.setStoreAddress(storeAddressRequest3);
    storeControllerClient.createStore(null, null, storeRequest3, testActions.getAdminAccessToken());

    List<String> cities = storeControllerClient.getStoresCity();
    Assert.assertEquals(2, cities.size());
  }
}
