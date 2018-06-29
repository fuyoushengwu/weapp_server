package cn.aijiamuyingfang.server.goods.service;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import cn.aijiamuyingfang.server.commons.utils.FileUtils;

/**
 * [描述]:
 * <p>
 * 图片服务,用于Store、Classify、Good上传图片的保存
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-28 23:37:42
 */
@Service
public class ImageService {
	private static final Logger LOGGER = LogManager.getLogger(ImageService.class);

	@Value("${weapp.images.dir}")
	private String imagesPath;

	/**
	 * 保存门店的Logo
	 * 
	 * @param storeId
	 *            门店Id
	 * @param img
	 *            门店Logo
	 * @return
	 */
	public String saveStoreLogo(long storeId, MultipartFile img) {
		String logouri = String.format("stores/%s/store_logo.jpg", storeId);
		File logoFile = new File(imagesPath, logouri);
		if (saveFile(logoFile, img)) {
			return logouri;
		}
		return null;
	}

	/**
	 * 保存门店对应的详细图片
	 * 
	 * @param storeId
	 *            门店Id
	 * @param img
	 *            详情图片
	 * @return
	 */
	public String saveStoreDetailImg(long storeId, MultipartFile img) {
		String detailuri = String.format("stores/%s/store_details/%s.jpg", storeId, UUID.randomUUID().toString());
		File detailFile = new File(imagesPath, detailuri);
		if (saveFile(detailFile, img)) {
			return detailuri;
		}
		return null;
	}

	/**
	 * 删除门店的图片
	 * 
	 * @param storeId
	 *            门店Id
	 */
	public void deleteStoreImg(long storeId) {
		File storeDir = new File(imagesPath, "stores/" + storeId);
		try {
			FileUtils.deleteDirectory(storeDir);
		} catch (IOException e) {
			LOGGER.error(e);
		}
	}

	/**
	 * 清理该门店之前的详细图片
	 * 
	 * @param storeId
	 *            门店Id
	 */
	public void clearStoreDetailImgs(long storeId) {
		File detailDir = new File(imagesPath, "stores/" + storeId + "/store_details");
		try {
			FileUtils.cleanDirectory(detailDir);
		} catch (IOException e) {
			LOGGER.error(e);
		}
	}

	/**
	 * 保存条目对应的图标
	 * 
	 * @param classifyId
	 * @param file
	 * @return
	 */
	public String saveClassifyLogo(long classifyId, MultipartFile file) {
		String logouri = String.format("classifies/%s/classify_logo.jpg", classifyId);
		File logoFile = new File(imagesPath, logouri);
		if (saveFile(logoFile, file)) {
			return logouri;
		}
		return null;
	}

	/**
	 * 保存商品对应的图标
	 * 
	 * @param goodId
	 * @param file
	 */
	public String saveGoodLogo(long goodId, MultipartFile file) {
		String logouri = String.format("goods/%s/good_logo.jpg", goodId);
		File logoFile = new File(imagesPath, logouri);
		if (saveFile(logoFile, file)) {
			return logouri;
		}
		return null;
	}

	/**
	 * 保存商品对应的详细图片
	 * 
	 * @param goodId
	 * @param file
	 * @return
	 */
	public String saveGoodDetailImg(long goodId, MultipartFile file) {
		String detailuri = String.format("goods/%s/good_details/%s.jpg", goodId, UUID.randomUUID().toString());
		File detailFile = new File(imagesPath, detailuri);
		if (saveFile(detailFile, file)) {
			return detailuri;
		}
		return null;
	}

	/**
	 * 清理该商品之前的详细图片
	 * 
	 * @param goodId
	 */
	public void clearGoodDetailImgs(long goodId) {
		String detailDirPath = String.format("goods/%s/good_details", goodId);
		File detailDir = new File(imagesPath, detailDirPath);
		try {
			FileUtils.cleanDirectory(detailDir);
		} catch (IOException e) {
			LOGGER.error(e);
		}
	}

	/**
	 * 保存文件到target
	 * 
	 * @param targetFile
	 * @param file
	 * @return
	 */
	private boolean saveFile(File targetFile, MultipartFile file) {
		if (null == file || file.isEmpty()) {
			return false;
		}
		boolean parentExist = FileUtils.createDirectory(targetFile.getParentFile());
		if (parentExist) {
			try {
				file.transferTo(targetFile);
				return true;
			} catch (Exception e) {
				LOGGER.error("save logo failed", e);
				return false;
			}
		}
		return false;
	}
}
