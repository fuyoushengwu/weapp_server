package cn.aijiamuyingfang.server.goods.controller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.RequestEntity.BodyBuilder;
import org.springframework.http.RequestEntity.HeadersBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import cn.aijiamuyingfang.server.commons.annotation.TestDescription;
import cn.aijiamuyingfang.server.commons.utils.JsonUtils;
import cn.aijiamuyingfang.server.commons.utils.StringUtils;
import cn.aijiamuyingfang.server.domain.address.City;
import cn.aijiamuyingfang.server.domain.address.County;
import cn.aijiamuyingfang.server.domain.address.Province;
import cn.aijiamuyingfang.server.domain.address.StoreAddress;
import cn.aijiamuyingfang.server.domain.goods.Store;
import cn.aijiamuyingfang.server.domain.goods.db.StoreRepository;
import cn.aijiamuyingfang.server.goods.controller.bean.GetInUseStoreResponse;
import cn.aijiamuyingfang.server.rest.controller.bean.ResponseBean;

/**
 * [描述]:
 * <p>
 * TODO
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-28 16:19:35
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class StoreControllerTest {
	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private StoreRepository storeRepository;

	@Before
	public void before() throws IOException {
		storeRepository.deleteAll();
	}

	@After
	public void after() {
		storeRepository.deleteAll();
	}

	@Test
	@SuppressWarnings({ "unchecked" })
	@TestDescription(description = "分页查询门店数据", condition = "数据库中没有分页数据")
	public void testGetInUseStores_001() throws JsonParseException, JsonMappingException, IOException {
		ResponseBean<Map<String, ?>> responseBean = restTemplate.getForObject("/stores?currentpage={1}&pagesize={2}",
				ResponseBean.class, 1, 2);
		Assert.assertEquals(HttpStatus.OK.value() + "", responseBean.getCode());
		GetInUseStoreResponse getInUseStoreResponse = JsonUtils.json2Bean(JsonUtils.map2Json(responseBean.getData()),
				GetInUseStoreResponse.class);
		Assert.assertEquals(0, getInUseStoreResponse.getTotalpage());
		Assert.assertEquals(1, getInUseStoreResponse.getCurrentpage());
		Assert.assertEquals(0, getInUseStoreResponse.getDataList().size());
	}

	@Test
	@SuppressWarnings({ "unchecked" })
	@TestDescription(description = "分页查询门店数据", condition = "数据库中存在门店数据")
	public void testGetInUseStores_002() throws JsonParseException, JsonMappingException, IOException {
		for (int i = 0; i < 4; i++) {
			Store store = new Store();
			store.setName("store " + i);
			storeRepository.save(store);
		}

		String getURI = "/stores?currentpage={1}&pagesize={2}";
		// {currentpage,pageisze}
		int[][] parameterArr = { { 0, 0 }, { 0, 1 }, { 0, 2 }, { 0, 3 }, { 1, 0 }, { 1, 1 }, { 1, 2 }, { 1, 3 },
				{ 2, 0 }, { 2, 1 }, { 2, 2 }, { 2, 3 }, { 3, 0 }, { 3, 1 }, { 3, 2 }, { 3, 3 }, };
		// {totalpage,currentpage,datasize}
		int[][] expectedArr = { { 4, 1, 1 }, { 4, 1, 1 }, { 2, 1, 2 }, { 2, 1, 3 }, { 4, 1, 1 }, { 4, 1, 1 },
				{ 2, 1, 2 }, { 2, 1, 3 }, { 4, 2, 1 }, { 4, 2, 1 }, { 2, 2, 2 }, { 2, 2, 1 }, { 4, 3, 1 }, { 4, 3, 1 },
				{ 2, 3, 0 }, { 2, 3, 0 }, };
		for (int i = 0; i < parameterArr.length; i++) {
			int[] parameter = parameterArr[i];
			ResponseBean<Map<String, ?>> responseBean = restTemplate.getForObject(getURI, ResponseBean.class,
					parameter[0], parameter[1]);
			GetInUseStoreResponse getInUseStoreResponse = JsonUtils
					.json2Bean(JsonUtils.map2Json(responseBean.getData()), GetInUseStoreResponse.class);

			int[] expected = expectedArr[i];
			Assert.assertEquals(expected[0], getInUseStoreResponse.getTotalpage());
			Assert.assertEquals(expected[1], getInUseStoreResponse.getCurrentpage());
			Assert.assertEquals(expected[2], getInUseStoreResponse.getDataList().size());
		}
	}

	@Test
	@SuppressWarnings("unchecked")
	@TestDescription(description = "创建门店", condition = "没有门店图片和详情图片")
	public void testCreateStore_001() throws URISyntaxException, IOException {

		MultiValueMap<String, Object> multiPartBody = new LinkedMultiValueMap<>();
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.MULTIPART_FORM_DATA);

		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(multiPartBody, header);
		ResponseBean<Map<String, ?>> responseBean = restTemplate.postForObject("/stores", requestEntity,
				ResponseBean.class);
		Assert.assertEquals(HttpStatus.OK.value() + "", responseBean.getCode());
		Store store = JsonUtils.json2Bean(JsonUtils.map2Json(responseBean.getData()), Store.class);
		Assert.assertTrue(StringUtils.isEmpty(store.getCoverImg()));
		Assert.assertEquals(0, store.getDetailImgList().size());
	}

	@Test
	@SuppressWarnings("unchecked")
	@TestDescription(description = "创建门店", condition = "有门店图片和详情图片")
	public void testCreateStore_002() throws URISyntaxException, IOException {

		MultiValueMap<String, Object> multiPartBody = new LinkedMultiValueMap<>();

		FileSystemResource coverResource = new FileSystemResource(
				this.getClass().getClassLoader().getResource("testdata/store_logo.jpg").getPath());
		multiPartBody.add("coverImage", coverResource);

		FileSystemResource detailResource = new FileSystemResource(
				this.getClass().getClassLoader().getResource("testdata/store_detail.jpg").getPath());
		multiPartBody.add("detailImages", detailResource);
		multiPartBody.add("detailImages", detailResource);

		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.MULTIPART_FORM_DATA);

		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(multiPartBody, header);
		ResponseBean<Map<String, ?>> responseBean = restTemplate.postForObject("/stores", requestEntity,
				ResponseBean.class);
		Assert.assertEquals(HttpStatus.OK.value() + "", responseBean.getCode());
		Store store = JsonUtils.json2Bean(JsonUtils.map2Json(responseBean.getData()), Store.class);
		Assert.assertTrue(store.getId() > 0);
		Assert.assertEquals(2, store.getDetailImgList().size());
		Assert.assertTrue(StringUtils.hasContent(store.getCoverImg()));
	}

	@Test
	@SuppressWarnings("unchecked")
	@TestDescription(description = "创建门店", condition = "有门店图片")
	public void testCreateStore_003() throws URISyntaxException, IOException {

		MultiValueMap<String, Object> multiPartBody = new LinkedMultiValueMap<>();

		FileSystemResource coverResource = new FileSystemResource(
				this.getClass().getClassLoader().getResource("testdata/store_logo.jpg").getPath());
		multiPartBody.add("coverImage", coverResource);

		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.MULTIPART_FORM_DATA);

		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(multiPartBody, header);
		ResponseBean<Map<String, ?>> responseBean = restTemplate.postForObject("/stores", requestEntity,
				ResponseBean.class);
		Assert.assertEquals(HttpStatus.OK.value() + "", responseBean.getCode());
		Store store = JsonUtils.json2Bean(JsonUtils.map2Json(responseBean.getData()), Store.class);
		Assert.assertTrue(store.getId() > 0);
	}

	@Test
	@SuppressWarnings("unchecked")
	@TestDescription(description = "获取门店信息", condition = "门店不存在")
	public void testGetStore_001() throws IOException {

		ResponseBean<Map<String, ?>> responseBean = restTemplate.getForObject("/stores/{1}", ResponseBean.class, 1);
		Assert.assertEquals(HttpStatus.NOT_FOUND.value() + "", responseBean.getCode());
		Store actualStore = JsonUtils.json2Bean(JsonUtils.map2Json(responseBean.getData()), Store.class);
		Assert.assertNull(actualStore);
	}

	@Test
	@SuppressWarnings("unchecked")
	@TestDescription(description = "获取门店信息", condition = "门店存在")
	public void testGetStore_002() throws IOException {
		Store store = new Store();
		storeRepository.save(store);

		ResponseBean<Map<String, ?>> responseBean = restTemplate.getForObject("/stores/{1}", ResponseBean.class,
				store.getId());
		Assert.assertEquals(HttpStatus.OK.value() + "", responseBean.getCode());
		Store actualStore = JsonUtils.json2Bean(JsonUtils.map2Json(responseBean.getData()), Store.class);
		Assert.assertEquals(store.getId(), actualStore.getId());
	}

	@Test
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@TestDescription(description = "使用null更新门店信息")
	public void testUpdateStore_001() throws URISyntaxException, IOException {
		Store store = new Store();
		storeRepository.save(store);

		BodyBuilder bodyBuilder = RequestEntity.put(new URI("/stores/" + store.getId()));
		ResponseEntity<ResponseBean> responseEntity = restTemplate.exchange("/stores/{1}", HttpMethod.PUT,
				bodyBuilder.body(null), ResponseBean.class, store.getId());
		ResponseBean<Map<String, ?>> responseBean = responseEntity.getBody();
		Assert.assertEquals(HttpStatus.OK.value() + "", responseBean.getCode());
		Store actualStore = JsonUtils.json2Bean(JsonUtils.map2Json(responseBean.getData()), Store.class);
		Assert.assertNull(actualStore);
	}

	@Test
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@TestDescription(description = "更新门店信息")
	public void testUpdateStore_002() throws URISyntaxException, IOException {
		Store store = new Store();
		storeRepository.save(store);

		Store updateStore = new Store();
		updateStore.setName("update Store");
		updateStore.setId(-1);

		BodyBuilder bodyBuilder = RequestEntity.put(new URI("/stores/" + store.getId()));
		ResponseEntity<ResponseBean> responseEntity = restTemplate.exchange("/stores/{1}", HttpMethod.PUT,
				bodyBuilder.body(updateStore), ResponseBean.class, store.getId());
		ResponseBean<Map<String, ?>> responseBean = responseEntity.getBody();
		Assert.assertEquals(HttpStatus.OK.value() + "", responseBean.getCode());
		Store actualStore = JsonUtils.json2Bean(JsonUtils.map2Json(responseBean.getData()), Store.class);
		Assert.assertEquals(store.getId(), actualStore.getId());
		Assert.assertEquals("update Store", actualStore.getName());
	}

	@Test
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@TestDescription(description = "更新不存在的门店")
	public void testUpdateStore_003() throws URISyntaxException, IOException {
		Store updateStore = new Store();
		updateStore.setName("update Store");
		updateStore.setId(-1);

		BodyBuilder bodyBuilder = RequestEntity.put(new URI("/stores/-1"));
		ResponseEntity<ResponseBean> responseEntity = restTemplate.exchange("/stores/{1}", HttpMethod.PUT,
				bodyBuilder.body(updateStore), ResponseBean.class, -1);
		ResponseBean<Map<String, ?>> responseBean = responseEntity.getBody();
		Assert.assertEquals(HttpStatus.NOT_FOUND.value() + "", responseBean.getCode());
	}

	@Test
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@TestDescription(description = "废弃已存在的门店")
	public void testDeprecateStore_001() throws IOException, URISyntaxException {
		Store store1 = new Store();
		store1 = storeRepository.save(store1);

		Store store2 = new Store();
		store2 = storeRepository.save(store2);

		ResponseBean<Map<String, ?>> responseBean = restTemplate.getForObject("/stores?currentpage={1}&pagesize={2}",
				ResponseBean.class, 1, 10);
		Assert.assertEquals(HttpStatus.OK.value() + "", responseBean.getCode());
		GetInUseStoreResponse getInUseStoreResponse = JsonUtils.json2Bean(JsonUtils.map2Json(responseBean.getData()),
				GetInUseStoreResponse.class);
		Assert.assertEquals(1, getInUseStoreResponse.getTotalpage());
		Assert.assertEquals(1, getInUseStoreResponse.getCurrentpage());
		Assert.assertEquals(2, getInUseStoreResponse.getDataList().size());

		HeadersBuilder headersBuilder = RequestEntity.delete(new URI("/stores/" + store1.getId()));
		restTemplate.exchange("/stores/{1}", HttpMethod.DELETE, headersBuilder.build(), ResponseBean.class,
				store1.getId());

		responseBean = restTemplate.getForObject("/stores?currentpage={1}&pagesize={2}", ResponseBean.class, 1, 10);
		Assert.assertEquals(HttpStatus.OK.value() + "", responseBean.getCode());
		getInUseStoreResponse = JsonUtils.json2Bean(JsonUtils.map2Json(responseBean.getData()),
				GetInUseStoreResponse.class);
		Assert.assertEquals(1, getInUseStoreResponse.getTotalpage());
		Assert.assertEquals(1, getInUseStoreResponse.getCurrentpage());
		Assert.assertEquals(1, getInUseStoreResponse.getDataList().size());
		Assert.assertEquals(store2.getId(), getInUseStoreResponse.getDataList().get(0).getId());
	}

	@Test
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@TestDescription(description = "废弃不存在的门店")
	public void testDeprecateStore_002() throws IOException, URISyntaxException {
		Store store1 = new Store();
		store1 = storeRepository.save(store1);

		Store store2 = new Store();
		store2 = storeRepository.save(store2);

		HeadersBuilder headersBuilder = RequestEntity.delete(new URI("/stores/" + store1.getId()));
		ResponseEntity<ResponseBean> responseEntity = restTemplate.exchange("/stores/-1", HttpMethod.DELETE,
				headersBuilder.build(), ResponseBean.class);

		ResponseBean<Map<String, ?>> responseBean = responseEntity.getBody();
		Assert.assertEquals(HttpStatus.NOT_FOUND.value() + "", responseBean.getCode());
	}

	@Test
	@SuppressWarnings("unchecked")
	@TestDescription(description = "没有门店存在的情况下获取默认门店Id")
	public void testGetDefaultStoreId_001() {
		ResponseBean<Long> responseBean = restTemplate.getForObject("/stores/defaultid", ResponseBean.class);
		Assert.assertEquals(HttpStatus.OK.value() + "", responseBean.getCode());
		Assert.assertEquals("-1", responseBean.getData() + "");
	}

	@Test
	@SuppressWarnings("unchecked")
	@TestDescription(description = "存在多门店的情况下获取默认门店ID")
	public void testGetDefaultStoreId_002() {
		Store store1 = new Store();
		store1 = storeRepository.save(store1);

		Store store2 = new Store();
		store2 = storeRepository.save(store2);

		ResponseBean<Long> responseBean = restTemplate.getForObject("/stores/defaultid", ResponseBean.class);
		Assert.assertEquals(store1.getId() + "", responseBean.getData() + "");
	}

	@Test
	@SuppressWarnings("unchecked")
	@TestDescription(description = "不存在门店的情况下获取门店分布的城市")
	public void testGetStoresCity_001() {
		ResponseBean<List<String>> responseBean = restTemplate.getForObject("/stores/city", ResponseBean.class);
		Assert.assertEquals(HttpStatus.OK.value() + "", responseBean.getCode());
		Assert.assertEquals(0, responseBean.getData().size());
	}

	@Test
	@SuppressWarnings("unchecked")
	@TestDescription(description = "一个城市有多门店情况下获取门店分布的城市")
	public void testGetStoresCity_002() {
		Store store1 = new Store();
		StoreAddress storeAddress1 = new StoreAddress();
		Province storeAddress1Province = new Province();
		storeAddress1Province.setName("Province");
		storeAddress1.setProvince(storeAddress1Province);
		City storeAddress1City = new City();
		storeAddress1City.setName("City1");
		storeAddress1.setCity(storeAddress1City);
		County storeAddress1County = new County();
		storeAddress1County.setName("County1");
		storeAddress1.setCounty(storeAddress1County);
		store1.setStoreAddress(storeAddress1);
		store1 = storeRepository.save(store1);

		Store store2 = new Store();
		StoreAddress storeAddress2 = new StoreAddress();
		Province storeAddress2Province = new Province();
		storeAddress2Province.setName("Province");
		storeAddress2.setProvince(storeAddress2Province);
		City storeAddress2City = new City();
		storeAddress2City.setName("City1");
		storeAddress2.setCity(storeAddress2City);
		County storeAddress2County = new County();
		storeAddress2County.setName("County2");
		storeAddress2.setCounty(storeAddress2County);
		store2.setStoreAddress(storeAddress2);
		store2 = storeRepository.save(store2);

		Store store3 = new Store();
		StoreAddress storeAddress3 = new StoreAddress();
		Province storeAddress3Province = new Province();
		storeAddress3Province.setName("Province");
		storeAddress3.setProvince(storeAddress3Province);
		City storeAddress3City = new City();
		storeAddress3City.setName("City2");
		storeAddress3.setCity(storeAddress3City);
		County storeAddress3County = new County();
		storeAddress3County.setName("County3");
		storeAddress3.setCounty(storeAddress3County);
		store3.setStoreAddress(storeAddress3);
		store3 = storeRepository.save(store3);

		ResponseBean<List<String>> responseBean = restTemplate.getForObject("/stores/city", ResponseBean.class);
		Assert.assertEquals(HttpStatus.OK.value() + "", responseBean.getCode());
		Assert.assertEquals(2, responseBean.getData().size());
	}
}
