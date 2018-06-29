package cn.aijiamuyingfang.server.goods.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import cn.aijiamuyingfang.server.domain.goods.Store;
import cn.aijiamuyingfang.server.domain.goods.db.StoreRepository;
import cn.aijiamuyingfang.server.goods.controller.bean.GetInUseStoreResponse;
import cn.aijiamuyingfang.server.rest.controller.bean.ResponseCode;
import cn.aijiamuyingfang.server.rest.exception.GoodsException;

/**
 * [描述]:
 * <p>
 * 门店服务Service
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-26 23:50:27
 */
@Service
public class StoreService {
	@Autowired
	private StoreRepository storeRepository;

	/**
	 * 分页获取在使用中的Store
	 * 
	 * @param currentpage
	 *            当前页 (currentpage必须>=1,否则重置为1)
	 * @param pagesize
	 *            每页大小 (pagesize必须>0,否则重置为1)
	 * @return
	 */
	public GetInUseStoreResponse getInUseStores(int currentpage, int pagesize) {
		// currentpage必须>=1,否则重置为1
		if (currentpage < 1) {
			currentpage = 1;
		}
		// pagesize必须>0,否则重置为1
		if (pagesize <= 0) {
			pagesize = 1;
		}

		// PageRequest的Page参数是基于0的,但是currentPage是基于1的,所有将currentPage作为参数传递给PgeRequest时需要'-1'
		PageRequest pageRequest = new PageRequest(currentpage - 1, pagesize);
		Page<Store> storePage = storeRepository.findInUseStores(pageRequest);
		GetInUseStoreResponse response = new GetInUseStoreResponse();
		response.setCurrentpage(storePage.getNumber() + 1);
		response.setDataList(storePage.getContent());
		response.setTotalpage(storePage.getTotalPages());
		return response;
	}

	/**
	 * 新增门店信息
	 * 
	 * @param store
	 */
	public void createStore(Store store) {
		if (null == store) {
			return;
		}
		storeRepository.save(store);
	}

	/**
	 * 获取某个门店信息
	 * 
	 * @param storeid
	 *            门店ID
	 * @return
	 */
	public Store getStore(long storeid) {
		return storeRepository.findOne(storeid);
	}

	/**
	 * 更新门店信息
	 * 
	 * @param storeid
	 * @param store
	 */
	public Store updateStore(long storeid, Store updateStore) {
		if (null == updateStore) {
			return null;
		}
		Store store = storeRepository.findOne(storeid);
		if (null == store) {
			throw new GoodsException(ResponseCode.STORE_NOT_EXIST, storeid);
		}
		store.update(updateStore);
		storeRepository.save(store);
		return store;
	}

	/**
	 * 废弃门店
	 * 
	 * @param storeid
	 */
	public void deprecateStore(long storeid) {
		Store store = storeRepository.findOne(storeid);
		if (null == store) {
			throw new GoodsException(ResponseCode.STORE_NOT_EXIST, storeid);
		}
		store.setDeprecated(true);
		storeRepository.save(store);
	}

	/**
	 * 获取在哪些城市有门店分布
	 * 
	 * @return
	 */
	public Set<String> getStoresCity() {
		List<Store> stores = storeRepository.findInUseStores();
		Set<String> storeCity = new HashSet<>();
		for (Store store : stores) {
			storeCity.add(store.getStoreAddress().getCity().getName());
		}
		return storeCity;
	}
}
