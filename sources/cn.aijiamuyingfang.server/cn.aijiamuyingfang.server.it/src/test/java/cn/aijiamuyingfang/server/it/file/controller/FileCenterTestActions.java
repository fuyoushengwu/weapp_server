package cn.aijiamuyingfang.server.it.file.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.aijiamuyingfang.client.rest.api.impl.FileControllerClient;
import cn.aijiamuyingfang.server.it.AbstractTestAction;
import cn.aijiamuyingfang.server.it.annotation.TargetDataSource;
import cn.aijiamuyingfang.server.it.filecenter.db.FileInfoRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileCenterTestActions extends AbstractTestAction {
  @Autowired
  private FileInfoRepository fileRepository;

  @Autowired
  protected FileControllerClient fileControllerClient;

  @TargetDataSource(name = "weapp-filecenter")
  public void clearFileInfo() {
    fileRepository.findAll().forEach(fileInfo -> {
      try {
        fileControllerClient.delete(fileInfo.getId(), getAdminAccessToken(), false);
      } catch (IOException e) {
        log.error("delete failed", e);
      }
    });
  }

}
