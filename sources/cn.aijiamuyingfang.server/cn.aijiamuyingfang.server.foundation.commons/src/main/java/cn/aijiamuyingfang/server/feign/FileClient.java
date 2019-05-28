package cn.aijiamuyingfang.server.feign;

import java.util.Map;
import java.util.concurrent.Future;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import cn.aijiamuyingfang.server.domain.response.ResponseBean;
import cn.aijiamuyingfang.server.feign.domain.filecenter.FileInfo;
import cn.aijiamuyingfang.server.feign.domain.filecenter.GetFileInfoListResponse;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;

@FeignClient(value = "filecenter-service", configuration = FileClient.MultipartSupportConfig.class)
public interface FileClient {
  /**
   * 上传文件
   * 
   * @param filePart
   *          文件实体
   * @param source
   *          文件来源
   * @return
   */
  @PostMapping(value = "/files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  ResponseBean<FileInfo> upload(@RequestPart(value = "file") MultipartFile filePart,
      @RequestParam("source") String source);

  /**
   * 删除文件
   * 
   * @param id
   *          文件Id
   * @return
   */
  @DeleteMapping("/files/{id}")
  Future<ResponseBean<Void>> delete(@PathVariable("id") String id);

  /**
   * 分页查询文件信息
   * 
   * @param params
   *          查询参数
   * @return
   */
  @GetMapping("files")
  ResponseBean<GetFileInfoListResponse> getFileInfoList(@RequestParam Map<String, String> params);

  class MultipartSupportConfig {
    @Bean
    public Encoder feignFormEncoder() {
      return new SpringFormEncoder();
    }
  }

}
