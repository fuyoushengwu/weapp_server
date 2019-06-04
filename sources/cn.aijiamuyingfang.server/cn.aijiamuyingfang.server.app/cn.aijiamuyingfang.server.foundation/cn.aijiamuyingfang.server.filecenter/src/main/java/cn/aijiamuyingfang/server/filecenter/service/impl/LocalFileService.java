
package cn.aijiamuyingfang.server.filecenter.service.impl;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletContext;

import org.apache.commons.lang.math.NumberUtils;
import org.hibernate.SessionFactory;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import cn.aijiamuyingfang.commons.utils.JsonUtils;
import cn.aijiamuyingfang.server.domain.FileSource;
import cn.aijiamuyingfang.server.filecenter.db.FileInfoRepository;
import cn.aijiamuyingfang.server.filecenter.domain.FileInfo;
import cn.aijiamuyingfang.server.filecenter.domain.response.GetFileInfoListResponse;
import cn.aijiamuyingfang.server.filecenter.service.FileService;
import cn.aijiamuyingfang.server.filecenter.utils.WebFileUtils;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LocalFileService implements FileService, InitializingBean {
  /**
   * 文件后缀名分隔符
   */
  private static final String FILE_SUFFIX_SEPARATOR = ".";

  @Autowired
  private FileInfoRepository fileInfoRepository;

  @Value("${weapp.file-repo.access-url.prefix}")
  private String urlPrefix;

  /**
   * 上传文件存储在本地的根路径
   */
  @Value("${weapp.file-repo.localfs.path}")
  private String localFilePath;

  @Autowired
  private ServletContext servletContext;

  @Autowired
  private EntityManagerFactory entityManagerFactory;

  private List<String> fileInfoColumnNameList = new ArrayList<>();

  @Override
  public void afterPropertiesSet() throws Exception {
    SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
    AbstractEntityPersister persister = ((AbstractEntityPersister) sessionFactory.getClassMetadata(FileInfo.class));
    for (Field field : FileInfo.class.getDeclaredFields()) {
      if (Modifier.isStatic(field.getModifiers())) {
        continue;
      }
      String columnName = null;
      if (field.isAnnotationPresent(Column.class)) {
        columnName = field.getAnnotation(Column.class).name();
      } else {
        String[] columnNames = persister.getPropertyColumnNames(field.getName());
        if (columnNames.length > 0) {
          columnName = columnNames[0];
        }
      }
      fileInfoColumnNameList.add(columnName);
    }
  }

  @Override
  public FileInfo upload(MultipartFile file, FileSource fileSource) {
    FileInfo fileInfo = WebFileUtils.extractFileInfo(file);
    FileInfo oriFileInfo = fileInfoRepository.findOne(fileInfo.getId());
    if (oriFileInfo != null) {
      return oriFileInfo;
    }
    if (!fileInfo.getName().contains(FILE_SUFFIX_SEPARATOR)) {
      throw new IllegalArgumentException("file not exist suffix name");
    }

    fileInfo.setSource(FileSource.LOCAL);

    int index = fileInfo.getName().lastIndexOf(FILE_SUFFIX_SEPARATOR);
    // 文件扩展名
    String fileSuffix = fileInfo.getName().substring(index);
    String suffix = File.separator + LocalDate.now().toString().replace("-", File.separator) + File.separator
        + fileInfo.getId() + fileSuffix;

    String url = urlPrefix + suffix;
    fileInfo.setUrl(url);

    String path = localFilePath + suffix;
    File tmp = new File(path);
    if (!tmp.isAbsolute()) {
      File location = (File) servletContext.getAttribute(ServletContext.TEMPDIR);
      path = location.getAbsolutePath() + path;
    }

    fileInfo.setPath(path);

    WebFileUtils.saveFile(file, path);
    fileInfoRepository.saveAndFlush(fileInfo);

    log.info("上传文件到本地：{}", fileInfo);
    return fileInfo;
  }

  @Override
  public void delete(String id) {
    FileInfo fileInfo = fileInfoRepository.findOne(id);
    if (fileInfo != null) {
      WebFileUtils.deleteFile(fileInfo.getPath());
      fileInfoRepository.delete(fileInfo.getId());
      log.info("删除本地文件：{}", fileInfo);
    }
  }

  @Override
  public GetFileInfoListResponse getFileInfoList(Map<String, String> params) {
    int currentPage = NumberUtils.toInt(params.remove("current_page"), 1);
    int pageSize = NumberUtils.toInt(params.remove("page_size"), 10);
    PageRequest pageRequest = new PageRequest(currentPage - 1, pageSize);
    FileInfo fileInfo = JsonUtils.fromJson(JsonUtils.toJson(params), FileInfo.class);
    Page<FileInfo> fileInfoPage = fileInfoRepository.findAll(Example.of(fileInfo), pageRequest);
    GetFileInfoListResponse response = new GetFileInfoListResponse();
    response.setCurrentPage(fileInfoPage.getNumber() + 1);
    response.setDataList(fileInfoPage.getContent());
    response.setTotalpage(fileInfoPage.getTotalPages());
    return response;
  }
}
