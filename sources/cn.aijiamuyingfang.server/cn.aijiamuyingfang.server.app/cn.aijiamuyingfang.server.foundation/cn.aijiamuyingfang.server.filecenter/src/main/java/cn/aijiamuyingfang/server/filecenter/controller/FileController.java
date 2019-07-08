package cn.aijiamuyingfang.server.filecenter.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.aijiamuyingfang.server.domain.FileSource;
import cn.aijiamuyingfang.server.filecenter.domain.response.PagableFileInfoList;
import cn.aijiamuyingfang.server.filecenter.dto.FileInfoDTO;
import cn.aijiamuyingfang.server.filecenter.service.FileService;
import cn.aijiamuyingfang.server.filecenter.service.FileServiceFactory;

@RestController
public class FileController {
  @Autowired
  private FileServiceFactory fileServiceFactory;

  /**
   * 文件上传
   * 
   * @param file
   * @param fileSource
   *          文件来源(根据fileSource选择上传方式，目前仅实现了上传到本地)
   * @return
   */
  @PreAuthorize("hasAuthority('permission:manager:*')")
  @PostMapping(value = "/files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public FileInfoDTO upload(@RequestPart("file") MultipartFile file,
      @RequestParam(value = "source", required = false) FileSource fileSource) {
    FileService fileService = fileServiceFactory.getFileServiceBySource(fileSource);
    return fileService.upload(file, fileSource);
  }

  /**
   * 文件删除
   * 
   * @param id
   */
  @PreAuthorize("isAuthenticated()")
  @DeleteMapping("/files/{id}")
  public void delete(@PathVariable("id") String id) {
    FileService fileService = fileServiceFactory.getFileServiceById(id);
    fileService.delete(id);
  }

  /**
   * 文件查询
   * 
   * @param params
   * @return
   */
  @GetMapping("/files")
  public PagableFileInfoList getFileInfoList(@RequestParam Map<String, String> params) {
    return fileServiceFactory.getDefaultFileService().getFileInfoList(params);
  }
}
