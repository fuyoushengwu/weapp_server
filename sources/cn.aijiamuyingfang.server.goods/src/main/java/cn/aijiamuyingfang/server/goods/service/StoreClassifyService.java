package cn.aijiamuyingfang.server.goods.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import cn.aijiamuyingfang.server.domain.goods.Classify;
import cn.aijiamuyingfang.server.domain.goods.Store;
import cn.aijiamuyingfang.server.domain.goods.db.StoreRepository;
import cn.aijiamuyingfang.server.rest.controller.bean.ResponseCode;
import cn.aijiamuyingfang.server.rest.exception.GoodsException;

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
public class StoreClassifyService {
	@Autowired
	private StoreRepository storeRepository;

	/**
	 * 获得门店下的所有条目
	 * 
	 * @param storeid
	 * @return
	 */
	public List<Classify> getStoreClassifyList(long storeid) {
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
	 * @param classify
	 */
	public void addClassify(long storeid, Classify classify) {
		if (null == classify) {
			return;
		}
		Store store = storeRepository.findOne(storeid);
		if (null == store) {
			throw new GoodsException(ResponseCode.STORE_NOT_EXIST, storeid);
		}
		store.addClassify(classify);
		storeRepository.save(store);
	}
}
