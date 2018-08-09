package cn.aijiamuyingfang.server.goods.controller;

import cn.aijiamuyingfang.commons.domain.exception.GoodsException;
import cn.aijiamuyingfang.commons.domain.goods.Classify;
import cn.aijiamuyingfang.commons.domain.response.ResponseCode;
import cn.aijiamuyingfang.commons.utils.StringUtils;
import cn.aijiamuyingfang.server.goods.service.ClassifyService;
import cn.aijiamuyingfang.server.goods.service.ImageService;
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

/**
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
  public List<Classify> getTopClassifyList() {
    return classifyService.getTopClassifyList();
  }

  /**
   * 获取某个条目
   * 
   * @param classifyid
   * @return
   */
  @PreAuthorize(value = "permitAll()")
  @GetMapping(value = "/classify/{classifyid}")
  public Classify getClassify(@PathVariable(value = "classifyid") String classifyid) {
    Classify classify = classifyService.getClassify(classifyid);
    if (null == classify) {
      throw new GoodsException(ResponseCode.CLASSIFY_NOT_EXIST, classifyid);
    }
    return classify;
  }

  /**
   * 废弃条目
   * 
   * @param classifyid
   */
  @PreAuthorize(value = "hasAuthority('admin')")
  @DeleteMapping(value = "/classify/{classifyid}")
  public void deleteClassify(@PathVariable(value = "classifyid") String classifyid) {
    classifyService.deleteClassify(classifyid);
  }

  /**
   * 创建顶层条目
   * 
   * @param request
   * @return
   */
  @PreAuthorize(value = "hasAuthority('admin')")
  @PostMapping(value = "/classify")
  public Classify createTopClassify(@RequestBody Classify request) {
    if (null == request) {
      throw new GoodsException("400", "classify request body is null");
    }
    if (StringUtils.isEmpty(request.getName())) {
      throw new GoodsException("400", "classify name is empty");
    }
    return classifyService.createTopClassify(request);
  }

  /**
   * 获得条目下的所有子条目
   * 
   * @param classifyid
   * @return
   */
  @PreAuthorize(value = "permitAll()")
  @GetMapping(value = "/classify/{classifyid}/subclassify")
  public List<Classify> getSubClassifyList(@PathVariable(value = "classifyid") String classifyid) {
    return classifyService.getSubClassifyList(classifyid);
  }

  /**
   * 创建子条目
   * 
   * @param classifyid
   * @param classifyRequest
   * @param request
   * @return
   */
  @PreAuthorize(value = "hasAuthority('admin')")
  @PostMapping(value = "/classify/{classifyid}/subclassify")
  public Classify createSubClassify(@PathVariable(value = "classifyid") String classifyid,
      @RequestParam(value = "coverImage", required = false) MultipartFile coverImage, Classify classifyRequest,
      HttpServletRequest request) {
    if (null == classifyRequest) {
      throw new GoodsException("400", "classify request is null");
    }
    if (StringUtils.isEmpty(classifyRequest.getName())) {
      throw new GoodsException("400", "classify name is empty");
    }
    Classify subClassify = classifyService.createSubClassify(classifyid, classifyRequest);

    imageService.clearLogo(subClassify.getCoverImg());
    String coverImgUrl = imageService.saveClassifyLogo(subClassify.getId(), coverImage);
    if (StringUtils.hasContent(coverImgUrl)) {
      coverImgUrl = String.format(ImageService.IMAGE_URL_PATTERN, request.getServerName(), coverImgUrl);
      subClassify.setCoverImg(coverImgUrl);
    }
    return classifyService.updateClassify(subClassify.getId(), subClassify);
  }

  /**
   * 条目下添加商品
   * 
   * @param classifyid
   * @param goodid
   */
  @PreAuthorize(value = "hasAuthority('admin')")
  @PutMapping(value = "/classify/{classifyid}/good/{goodid}")
  public void addClassifyGood(@PathVariable(value = "classifyid") String classifyid,
      @PathVariable(value = "goodid") String goodid) {
    classifyService.addGood(classifyid, goodid);
  }
}
