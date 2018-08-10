package cn.aijiamuyingfang.server.goods.service;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import cn.aijiamuyingfang.commons.utils.FileUtils;
import cn.aijiamuyingfang.commons.utils.StringUtils;

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

  public static final String IMAGE_URL_PATTERN = "http://%s:8000/%s";

  private static final Logger LOGGER = LogManager.getLogger(ImageService.class);

  @Value("${weapp.images.dir}")
  private String imagesPath;

  /**
   * 保存门店的Logo
   * 
   * @param storeId
   *          门店Id
   * @param img
   *          门店Logo
   * @return
   */
  public String saveStoreLogo(String storeId, MultipartFile img) {
    String logouri = String.format("stores/%s/%s.jpg", storeId, UUID.randomUUID().toString());
    File logoFile = new File(imagesPath, logouri);
    if (saveFile(logoFile, img)) {
      return logouri;
    }
    return null;
  }

  /**
   * 删除store logo
   * 
   * @param logourl
   */
  public void clearLogo(String logourl) {
    if (StringUtils.isEmpty(logourl)) {
      return;
    }
    logourl = logourl.replace("http://", "");
    String logoFilePath = logourl.substring(logourl.indexOf('/'));
    File logoFile = new File(imagesPath, logoFilePath);
    try {
      FileUtils.forceDelete(logoFile);
    } catch (IOException e) {
      LOGGER.error(e);
    }
  }

  /**
   * 保存门店对应的详细图片
   * 
   * @param storeId
   *          门店Id
   * @param img
   *          详情图片
   * @return
   */
  public String saveStoreDetailImg(String storeId, MultipartFile img) {
    String detailuri = String.format("stores/%s/store_details/%s.jpg", storeId, UUID.randomUUID().toString());
    File detailFile = new File(imagesPath, detailuri);
    if (saveFile(detailFile, img)) {
      return detailuri;
    }
    return null;
  }

  /**
   * 清理该门店之前的详细图片
   * 
   * @param storeId
   *          门店Id
   */
  public void clearStoreDetailImgs(String storeId) {
    File detailDir = new File(imagesPath, "stores/" + storeId + "/store_details");
    try {
      FileUtils.cleanDirectory(detailDir);
    } catch (IOException e) {
      LOGGER.error(e);
    }
  }

  /**
   * 删除门店的图片
   * 
   * @param storeId
   *          门店Id
   */
  public void deleteStoreImg(String storeId) {
    // Restrict the storeId to uuid only
    if (!storeId.matches("[0-9a-f]{8}-?[0-9a-f]{4}-?[0-9a-f]{4}-?[0-9a-f]{4}-?[0-9a-f]{12}")) {
      throw new IllegalArgumentException("storeId is not UUID");
    }
    File storeDir = new File(imagesPath, "stores/" + storeId);
    try {
      FileUtils.deleteDirectory(storeDir);
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
  public String saveClassifyLogo(String classifyId, MultipartFile file) {
    String logouri = String.format("classifies/%s/%s.jpg", classifyId, UUID.randomUUID().toString());
    File logoFile = new File(imagesPath, logouri);
    if (saveFile(logoFile, file)) {
      return logouri;
    }
    return null;
  }

  /**
   * 保存商品对应的图标
   * 
   * @param goodid
   * @param file
   */
  public String saveGoodLogo(String goodid, MultipartFile file) {
    String logouri = String.format("goods/%s/%s.jpg", goodid, UUID.randomUUID().toString());
    File logoFile = new File(imagesPath, logouri);
    if (saveFile(logoFile, file)) {
      return logouri;
    }
    return null;
  }

  /**
   * 保存商品对应的详细图片
   * 
   * @param goodid
   * @param file
   * @return
   */
  public String saveGoodDetailImg(String goodid, MultipartFile file) {
    String detailuri = String.format("goods/%s/good_details/%s.jpg", goodid, UUID.randomUUID().toString());
    File detailFile = new File(imagesPath, detailuri);
    if (saveFile(detailFile, file)) {
      return detailuri;
    }
    return null;
  }

  /**
   * 清理该商品之前的详细图片
   * 
   * @param goodid
   */
  public void clearGoodDetailImgs(String goodid) {
    String detailDirPath = String.format("goods/%s/good_details", goodid);
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
