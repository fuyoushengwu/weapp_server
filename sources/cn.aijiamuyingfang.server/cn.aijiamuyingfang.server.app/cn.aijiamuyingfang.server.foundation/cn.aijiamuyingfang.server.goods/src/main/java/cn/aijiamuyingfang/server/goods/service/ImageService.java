package cn.aijiamuyingfang.server.goods.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import cn.aijiamuyingfang.server.feign.FileClient;
import cn.aijiamuyingfang.server.goods.db.ImageSourceRepository;
import cn.aijiamuyingfang.server.goods.dto.ImageSourceDTO;
import cn.aijiamuyingfang.server.goods.utils.ConvertUtils;
import cn.aijiamuyingfang.vo.ImageSource;
import cn.aijiamuyingfang.vo.filecenter.FileInfo;
import cn.aijiamuyingfang.vo.response.ResponseBean;
import cn.aijiamuyingfang.vo.utils.CollectionUtils;
import cn.aijiamuyingfang.vo.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;

/***
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
@Slf4j
public class ImageService {

  public static final String IMAGE_URL_PATTERN = "http://%s:8000/%s";

  @Autowired
  private ImageSourceRepository imageSourceRepository;

  @Autowired
  private FileClient fileClient;

  /**
   * 保存图片
   * 
   * @param imagePart
   * @return
   */
  public ImageSource saveImage(MultipartFile imagePart) {
    if (null == imagePart) {
      return null;
    }
    String md5 = "";
    try {
      md5 = DigestUtils.md5Hex(imagePart.getInputStream());
    } catch (IOException e) {
      log.error("md5 upload file failed", e);
      return null;
    }
    ImageSourceDTO imageSourceDTO = imageSourceRepository.findOne(md5);
    if (null == imageSourceDTO) {

      ResponseBean<FileInfo> responseBean = fileClient.upload(new CustomMultipartFile("file", imagePart), null);
      FileInfo fileInfo = responseBean.getData();
      if (null == fileInfo) {
        return null;
      }
      imageSourceDTO = new ImageSourceDTO();
      imageSourceDTO.setId(fileInfo.getId());
      imageSourceDTO.setUrl(fileInfo.getUrl());
      imageSourceRepository.saveAndFlush(imageSourceDTO);
    }
    return ConvertUtils.convertImageSourceDTO(imageSourceDTO);
  }

  /**
   * 删除图片
   * 
   * @param imageSource
   */
  public void deleteImage(ImageSource imageSource) {
    if (null == imageSource || StringUtils.isEmpty(imageSource.getId())) {
      return;
    }
    fileClient.delete(imageSource.getId());
  }

  /**
   * 删除图片
   * 
   * @param imageSourceList
   */
  public void deleteImage(List<ImageSource> imageSourceList) {
    if (CollectionUtils.hasContent(imageSourceList)) {
      for (ImageSource imageSource : imageSourceList) {
        deleteImage(imageSource);
      }
    }
  }

  /**
   * [描述]:
   * <p>
   * 定制MultipartFile的name
   * </p>
   * 
   * @version 1.0.0
   * @author ShiWei
   * @email shiweideyouxiang@sina.cn
   * @date 2019-05-18 05:54:22
   */
  private static class CustomMultipartFile implements MultipartFile {
    private MultipartFile delegate;

    private String partname;

    public CustomMultipartFile(String partname, MultipartFile delegate) {
      Assert.notNull(delegate, "MultipartFile cannot be null");
      Assert.isTrue(StringUtils.hasContent(partname), "partname cannot be null");
      this.partname = partname;
      this.delegate = delegate;
    }

    @Override
    public String getName() {
      return partname;
    }

    @Override
    public String getOriginalFilename() {
      return delegate.getOriginalFilename();
    }

    @Override
    public String getContentType() {
      return delegate.getContentType();
    }

    @Override
    public boolean isEmpty() {
      return delegate.isEmpty();
    }

    @Override
    public long getSize() {
      return delegate.getSize();
    }

    @Override
    public byte[] getBytes() throws IOException {
      return delegate.getBytes();
    }

    @Override
    public InputStream getInputStream() throws IOException {
      return delegate.getInputStream();
    }

    @Override
    public void transferTo(File dest) throws IOException {
      delegate.transferTo(dest);
    }

  }
}
