package cn.aijiamuyingfang.server.filecenter.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import cn.aijiamuyingfang.server.domain.FileSource;
import cn.aijiamuyingfang.server.filecenter.domain.response.PagableFileInfoList;
import cn.aijiamuyingfang.server.filecenter.dto.FileInfoDTO;

/**
 * [描述]:
 * <p>
 * 文件服务接口
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2019-05-15 01:38:38
 */
public interface FileService {

  /**
   * 文件上传
   * 
   * @param part
   * @param fileSource
   *          文件来源(根据fileSource选择上传方式，目前仅实现了上传到本地)
   * @return
   */
  FileInfoDTO upload(MultipartFile part, FileSource fileSource);

  /**
   * 文件删除
   * 
   * @param id
   */
  void delete(String id);

  /**
   * 文件查询
   * 
   * @param params
   * @return
   */
  PagableFileInfoList getFileInfoList(Map<String, String> params);
}
