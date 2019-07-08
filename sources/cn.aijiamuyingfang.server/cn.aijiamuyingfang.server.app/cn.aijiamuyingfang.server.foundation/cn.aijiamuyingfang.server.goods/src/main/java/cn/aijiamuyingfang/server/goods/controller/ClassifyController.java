package cn.aijiamuyingfang.server.goods.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.aijiamuyingfang.commons.utils.StringUtils;
import cn.aijiamuyingfang.server.domain.response.ResponseCode;
import cn.aijiamuyingfang.server.exception.GoodsException;
import cn.aijiamuyingfang.server.goods.dto.ClassifyDTO;
import cn.aijiamuyingfang.server.goods.dto.ImageSourceDTO;
import cn.aijiamuyingfang.server.goods.service.ClassifyService;
import cn.aijiamuyingfang.server.goods.service.ImageService;

/***
 * [描述]:
 * <p>
 * 条目服务-控制层
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-26 23:43:44
 */
@RestController
public class ClassifyController {

  @Autowired
  private ClassifyService classifyService;

  @Autowired
  private ImageService imageService;

  /**
   * 获取所有顶层条目
   *
   * @return
   */
  @PreAuthorize(value = "permitAll()")
  @GetMapping(value = "/classify")
  public List<ClassifyDTO> getTopClassifyList() {
    return classifyService.getTopClassifyList();
  }

  /**
   * 获取某个条目
   *
   * @param classifyId
   * @return
   */
  @PreAuthorize(value = "permitAll()")
  @GetMapping(value = "/classify/{classify_id}")
  public ClassifyDTO getClassify(@PathVariable(value = "classify_id") String classifyId) {
    ClassifyDTO classify = classifyService.getClassify(classifyId);
    if (null == classify) {
      throw new GoodsException(ResponseCode.CLASSIFY_NOT_EXIST, classifyId);
    }
    return classify;
  }

  /**
   * 废弃条目
   *
   * @param classifyId
   */
  @PreAuthorize("hasAuthority('permission:manager:*')")
  @DeleteMapping(value = "/classify/{classify_id}")
  public void deleteClassify(@PathVariable(value = "classify_id") String classifyId) {
    classifyService.deleteClassify(classifyId);
  }

  /**
   * 创建顶层条目
   *
   * @param request
   * @return
   */
  @PreAuthorize("hasAuthority('permission:manager:*')")
  @PostMapping(value = "/classify")
  public ClassifyDTO createTopClassify(@RequestBody ClassifyDTO request) {
    if (null == request) {
      throw new IllegalArgumentException("classify request body is null");
    }
    if (StringUtils.isEmpty(request.getName())) {
      throw new IllegalArgumentException("classify name is empty");
    }
    return classifyService.createORUpdateTopClassify(request);
  }

  /**
   * 获得条目下的所有子条目
   *
   * @param classifyId
   * @return
   */
  @PreAuthorize(value = "permitAll()")
  @GetMapping(value = "/classify/{classify_id}/subclassify")
  public List<ClassifyDTO> getSubClassifyList(@PathVariable(value = "classify_id") String classifyId) {
    return classifyService.getSubClassifyList(classifyId);
  }

  /**
   * 创建子条目
   *
   * @param classifyId
   * @param classifyRequest
   * @param request
   * @return
   */
  @PreAuthorize("hasAuthority('permission:manager:*')")
  @PostMapping(value = "/classify/{classify_id}/subclassify")
  public ClassifyDTO createSubClassify(@PathVariable(value = "classify_id") String classifyId,
      @RequestParam(value = "coverImage", required = false) MultipartFile coverImagePart, ClassifyDTO classifyRequest,
      HttpServletRequest request) {
    if (null == classifyRequest) {
      throw new IllegalArgumentException("classify request is null");
    }
    if (StringUtils.isEmpty(classifyRequest.getName())) {
      throw new IllegalArgumentException("classify name is empty");
    }
    ClassifyDTO subClassify = classifyService.createORUpdateSubClassify(classifyId, classifyRequest);

    ImageSourceDTO coverImageSource = imageService.saveImage(coverImagePart);
    if (coverImageSource != null) {
      subClassify.setCoverImg(coverImageSource);
    }
    return classifyService.updateClassify(subClassify.getId(), subClassify);
  }

  /**
   * 条目下添加商品
   *
   * @param classifyId
   * @param goodId
   *          商品id
   */
  @PreAuthorize("hasAuthority('permission:manager:*')")
  @PutMapping(value = "/classify/{classify_id}/good/{good_id}")
  public void addClassifyGood(@PathVariable(value = "classify_id") String classifyId,
      @PathVariable(value = "good_id") String goodId) {
    classifyService.addGood(classifyId, goodId);
  }
}
