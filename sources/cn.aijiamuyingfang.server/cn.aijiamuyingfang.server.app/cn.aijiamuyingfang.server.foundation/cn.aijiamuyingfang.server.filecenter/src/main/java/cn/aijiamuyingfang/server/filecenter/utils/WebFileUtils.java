package cn.aijiamuyingfang.server.filecenter.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import cn.aijiamuyingfang.server.exception.FileCenterException;
import cn.aijiamuyingfang.server.filecenter.domain.FileInfo;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * [描述]:
 * <p>
 * Web文件工具类
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2019-04-19 22:20:00
 */
@UtilityClass
@Slf4j
public class WebFileUtils {
  /**
   * 获取上传的文件信息
   * 
   * @param file
   * @return
   */
  public static FileInfo extractFileInfo(MultipartFile file) {
    FileInfo fileInfo = new FileInfo();
    try {
      fileInfo.setId(md5(file.getInputStream()));
    } catch (IOException e) {
      log.error("extract fileinfo failed", e);
      throw new FileCenterException("500", "extract fileinfo failed");
    }
    fileInfo.setName(file.getOriginalFilename());
    fileInfo.setContentType(file.getContentType());
    fileInfo.setIsImg(fileInfo.getContentType().startsWith("image/"));
    fileInfo.setSize(file.getSize());
    fileInfo.setCreateTime(new Date());
    return fileInfo;
  }

  /**
   * 获取上传文件的MD5摘要
   * 
   * @param inputStream
   * @return
   */
  private static String md5(InputStream inputStream) {
    try {
      return DigestUtils.md5Hex(inputStream);
    } catch (IOException e) {
      log.error("md5 upload file failed", e);
      throw new FileCenterException("500", "md5 upload file failed");
    }
  }

  /**
   * 保存上传的文件到指定的位置
   * 
   * @param file
   * @param path
   * @return
   */
  public static String saveFile(MultipartFile file, String path) {
    try {
      File targetFile = new File(path);

      if (!targetFile.getParentFile().exists()) {
        targetFile.getParentFile().mkdirs();
      }
      if (!targetFile.exists()) {
        file.transferTo(targetFile);
      }
    } catch (IOException e) {
      log.error("save web file failed", e);
      throw new FileCenterException("500", "save web file failed");
    }
    return path;
  }

  /**
   * 删除指定位置的文件
   * 
   * @param path
   * @return
   */
  public static boolean deleteFile(String path) {
    try {
      Files.delete(Paths.get(path));
      return true;
    } catch (IOException e) {
      log.error("delete file：{} failed,because:{}", path, e.getMessage());
      return false;
    }
  }

}
