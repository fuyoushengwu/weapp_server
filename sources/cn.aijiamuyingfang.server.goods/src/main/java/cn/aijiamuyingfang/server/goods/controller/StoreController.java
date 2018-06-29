package cn.aijiamuyingfang.server.goods.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.aijiamuyingfang.server.commons.utils.StringUtils;
import cn.aijiamuyingfang.server.domain.goods.Store;
import cn.aijiamuyingfang.server.goods.controller.bean.GetInUseStoreResponse;
import cn.aijiamuyingfang.server.goods.service.ImageService;
import cn.aijiamuyingfang.server.goods.service.StoreService;
import cn.aijiamuyingfang.server.rest.controller.bean.ResponseCode;
import cn.aijiamuyingfang.server.rest.exception.GoodsException;

/**
 * [描述]:
 * <p>
 * 门店服务-控制层
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-26 23:43:33
 */
@RestController
public class StoreController {
	@Autowired
	private StoreService storeService;

	@Autowired
	private ImageService imageService;

	/**
	 * 分页获取在使用中的Store
	 * 
	 * @param currentpage
	 *            当前页 默认值:1 (currentpage必须>=1,否则重置为1)
	 * @param pagesize
	 *            每页大小 默认值:10(pagesize必须>0,否则重置为1)
	 * @return
	 */
	@GetMapping(value = "/stores")
	public GetInUseStoreResponse getInUseStores(
			@RequestParam(value = "currentpage", defaultValue = "1") int currentpage,
			@RequestParam(value = "pagesize", defaultValue = "10") int pagesize) {
		return storeService.getInUseStores(currentpage, pagesize);
	}

	/**
	 * 创建门店
	 * 
	 * @param coverImg
	 * @param imgs
	 * @param store
	 * @param request
	 */
	@PostMapping(value = "/stores")
	public Store createStore(@RequestParam(name = "coverImage", required = false) MultipartFile coverImg,
			@RequestParam(name = "detailImages", required = false) List<MultipartFile> imgs, Store store,
			HttpServletRequest request) {
		storeService.createStore(store);
		String coverImgUrl = imageService.saveStoreLogo(store.getId(), coverImg);
		if (StringUtils.hasContent(coverImgUrl)) {
			coverImgUrl = String.format("http://%s:%s/%s", request.getServerName(), request.getServerPort(),
					coverImgUrl);
			store.setCoverImg(coverImgUrl);
		}

		imageService.clearStoreDetailImgs(store.getId());
		List<String> detailImgList = new ArrayList<>();
		for (MultipartFile img : imgs) {
			String detailImgUrl = imageService.saveStoreDetailImg(store.getId(), img);
			if (StringUtils.hasContent(detailImgUrl)) {
				detailImgUrl = String.format("http://%s:%s/%s", request.getServerName(), request.getServerPort(),
						detailImgUrl);
				detailImgList.add(detailImgUrl);
			}
		}
		store.setDetailImgList(detailImgList);

		storeService.updateStore(store.getId(), store);
		return store;

	}

	/**
	 * 获取门店信息
	 * 
	 * @param storeid
	 */
	@GetMapping(value = "/stores/{storeid}")
	public Store getStore(@PathVariable("storeid") long storeid) {
		Store store = storeService.getStore(storeid);
		if (null == store) {
			throw new GoodsException(ResponseCode.STORE_NOT_EXIST, storeid);
		}
		return store;
	}

	/**
	 * 更新门店信息
	 * 
	 * @param storeid
	 */
	@PutMapping(value = "/stores/{storeid}")
	public Store updateStore(@PathVariable("storeid") long storeid, @RequestBody(required = false) Store store) {
		if (store != null) {
			return storeService.updateStore(storeid, store);
		}
		return null;
	}

	/**
	 * 废弃门店
	 * 
	 * @param storeid
	 */
	@DeleteMapping(value = "/stores/{storeid}")
	public void deprecateStore(@PathVariable("storeid") long storeid) {
		storeService.deprecateStore(storeid);
		imageService.deleteStoreImg(storeid);
	}

	/**
	 * 获取默认门店Id
	 * 
	 * @return
	 */
	@GetMapping(value = "/stores/defaultid")
	public long getDefaultStoreId() {
		GetInUseStoreResponse response = storeService.getInUseStores(1, 1);
		List<Store> storeList = response.getDataList();
		if (storeList != null && storeList.size() == 1) {
			return storeList.get(0).getId();
		}
		return -1L;
	}

	/**
	 * 获取在哪些城市有门店分布
	 * 
	 * @return
	 */
	@GetMapping(value = "/stores/city")
	public Set<String> getStoresCity() {
		return storeService.getStoresCity();
	}
}
