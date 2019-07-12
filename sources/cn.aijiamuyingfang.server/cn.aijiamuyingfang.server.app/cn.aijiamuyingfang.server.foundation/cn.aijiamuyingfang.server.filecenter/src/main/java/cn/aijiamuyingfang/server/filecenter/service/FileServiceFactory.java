package cn.aijiamuyingfang.server.filecenter.service;

import java.util.EnumMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.aijiamuyingfang.server.filecenter.db.FileInfoRepository;
import cn.aijiamuyingfang.server.filecenter.dto.FileInfoDTO;
import cn.aijiamuyingfang.server.filecenter.utils.ConvertUtils;
import cn.aijiamuyingfang.vo.filecenter.FileSource;

/**
 * FileService工厂<br>
 * 将各个实现类放入map
 * 
 * @author
 *
 */
@Service
public class FileServiceFactory {

  private EnumMap<FileSource, FileService> map = new EnumMap<>(FileSource.class);

  @Autowired
  private FileInfoRepository fileInfoRepository;

  @Autowired
  private FileService localFileServiceImpl;

  @PostConstruct
  public void init() {
    map.put(FileSource.LOCAL, localFileServiceImpl);
  }

  /**
   * 获取默认的FileService实现
   * 
   * @return
   */
  public FileService getDefaultFileService() {
    return localFileServiceImpl;
  }

  /**
   * 根据文件来源,获取对应的FileService实现
   * 
   * @param fileSource
   * @return
   */
  public FileService getFileServiceBySource(FileSource fileSource) {
    if (null == fileSource) {
      fileSource = FileSource.LOCAL;
    }
    FileService fileService = map.get(fileSource);
    if (null == fileService) {
      fileService = localFileServiceImpl;
    }
    return fileService;
  }

  /**
   * 根据FileInfo的Id获得对应的FileService实现
   * 
   * @param id
   * @return
   */
  public FileService getFileServiceById(String id) {
    FileInfoDTO fileInfoDTO = fileInfoRepository.findOne(id);
    if (fileInfoDTO != null) {
      return getFileServiceBySource(ConvertUtils.convertFileSourceDTO(fileInfoDTO.getSource()));
    }
    return localFileServiceImpl;
  }
}