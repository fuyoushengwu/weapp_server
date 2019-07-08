package cn.aijiamuyingfang.server.it.file.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import cn.aijiamuyingfang.client.rest.api.impl.FileControllerClient;
import cn.aijiamuyingfang.server.it.ITApplication;
import cn.aijiamuyingfang.vo.filecenter.FileInfo;
import cn.aijiamuyingfang.vo.filecenter.PagableFileInfoList;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT, classes = ITApplication.class)
public class FileControllerTest {
  @Autowired
  private FileControllerClient fileControllerClient;

  @Autowired
  private FileCenterTestActions testActions;

  @Before
  public void before() {
    testActions.clearFileInfo();
  }

  @After
  public void after() {
    testActions.clearFileInfo();
  }

  @Test
  public void test_filecenter() throws IOException {
    // Step1: 未上传文件:文件数为0
    Map<String, String> params = new HashMap<String, String>();
    params.put("access_token", testActions.getAdminAccessToken());
    PagableFileInfoList response = fileControllerClient.getFileInfoList(params, testActions.getAdminAccessToken());
    Assert.assertNotNull(response);
    Assert.assertEquals(0, response.getDataList().size());

    // Step2:上传一个文件:文件数为1
    File file = new File(FileControllerTest.class.getClassLoader().getResource("testdata/all_goods.png").getFile());
    FileInfo fileInfo = fileControllerClient.upload(file, null, testActions.getAdminAccessToken());
    Assert.assertNotNull(fileInfo);

    response = fileControllerClient.getFileInfoList(params, testActions.getAdminAccessToken());
    Assert.assertNotNull(response);
    Assert.assertEquals(1, response.getDataList().size());

    // Step3: 在上传5个文件,文件数为6
    for (int i = 0; i < 5; i++) {
      fileControllerClient.upload(
          new File(FileControllerTest.class.getClassLoader().getResource("testdata/" + i + ".jpg").getFile()), null,
          testActions.getAdminAccessToken());
    }
    response = fileControllerClient.getFileInfoList(params, testActions.getAdminAccessToken());
    Assert.assertNotNull(response);
    Assert.assertEquals(6, response.getDataList().size());

    // Step4: 查询第一个文件
    params.put("name", "all_goods.png");
    response = fileControllerClient.getFileInfoList(params, testActions.getAdminAccessToken());
    Assert.assertNotNull(response);
    Assert.assertEquals(1, response.getDataList().size());
    Assert.assertEquals("all_goods.png", response.getDataList().get(0).getName());
    Assert.assertEquals(fileInfo.getId(), response.getDataList().get(0).getId());

    // Step5: 删除第一个文件,剩余文件数为5
    fileControllerClient.delete(fileInfo.getId(), testActions.getAdminAccessToken(), false);
    params.clear();
    response = fileControllerClient.getFileInfoList(params, testActions.getAdminAccessToken());
    Assert.assertNotNull(response);
    Assert.assertEquals(5, response.getDataList().size());
  }

}
