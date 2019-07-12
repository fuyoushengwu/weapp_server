package cn.aijiamuyingfang.server.goods.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.aijiamuyingfang.server.goods.db.ClassifyRepository;
import cn.aijiamuyingfang.server.goods.db.GoodRepository;
import cn.aijiamuyingfang.server.goods.dto.ClassifyDTO;
import cn.aijiamuyingfang.server.goods.dto.GoodDTO;
import cn.aijiamuyingfang.server.goods.utils.ConvertService;
import cn.aijiamuyingfang.vo.classify.Classify;
import cn.aijiamuyingfang.vo.exception.GoodsException;
import cn.aijiamuyingfang.vo.response.ResponseCode;
import cn.aijiamuyingfang.vo.utils.StringUtils;

/**
 * [描述]:
 * <p>
 * 条目服务Service
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-26 23:51:11
 */
@Service
public class ClassifyService {
  @Autowired
  private ClassifyRepository classifyRepository;

  @Autowired
  private GoodRepository goodRepository;

  @Autowired
  private ConvertService convertService;

  /**
   * 获取条目信息
   * 
   * @param classifyId
   *          条目Id
   * @return
   */
  public Classify getClassify(String classifyId) {
    return convertService.convertClassifyDTO(classifyRepository.findOne(classifyId));
  }

  /**
   * 废弃条目
   * 
   * @param classifyId
   */
  public void deleteClassify(String classifyId) {
    classifyRepository.delete(classifyId);
  }

  /**
   * 创建顶层条目
   * 
   * @param classify
   * @return
   */
  public Classify createORUpdateTopClassify(Classify classify) {
    if (null == classify) {
      return null;
    }
    classify.setLevel(1);
    if (StringUtils.hasContent(classify.getId())) {
      ClassifyDTO oriClassifyDTO = classifyRepository.findOne(classify.getId());
      if (oriClassifyDTO != null) {
        oriClassifyDTO.update(classify);
        return convertService.convertClassifyDTO(classifyRepository.saveAndFlush(oriClassifyDTO));
      }
    }
    return convertService.convertClassifyDTO(classifyRepository.saveAndFlush(convertService.convertClassify(classify)));
  }

  /**
   * 添加子条目
   * 
   * @param topclassifyid
   * @param subclassify
   * @return
   */
  public Classify createORUpdateSubClassify(String topclassifyid, Classify subclassify) {
    if (null == subclassify) {
      return null;
    }
    subclassify.setLevel(2);

    if (StringUtils.hasContent(subclassify.getId())) {
      ClassifyDTO oriSubClassifyDTO = classifyRepository.findOne(subclassify.getId());
      if (oriSubClassifyDTO != null) {
        oriSubClassifyDTO.update(subclassify);
        return convertService.convertClassifyDTO(classifyRepository.saveAndFlush(oriSubClassifyDTO));
      }
    }
    ClassifyDTO topclassifyDTO = classifyRepository.findOne(topclassifyid);
    if (null == topclassifyDTO) {
      throw new GoodsException(ResponseCode.CLASSIFY_NOT_EXIST, topclassifyid);
    }
    topclassifyDTO.addSubClassify(convertService.convertClassify(subclassify));
    classifyRepository.saveAndFlush(topclassifyDTO);
    return convertService
        .convertClassifyDTO(topclassifyDTO.getSubClassifyList().get(topclassifyDTO.getSubClassifyList().size() - 1));
  }

  /**
   * 获取子条目
   * 
   * @param classifyId
   *          父条目Id
   * @return
   */
  public List<Classify> getSubClassifyList(String classifyId) {
    ClassifyDTO classifyDTO = classifyRepository.findOne(classifyId);
    if (null == classifyDTO) {
      throw new GoodsException(ResponseCode.CLASSIFY_NOT_EXIST, classifyId);
    }
    return convertService.convertClassifyDTOList(classifyDTO.getSubClassifyList());
  }

  /**
   * 在条目下添加商品
   * 
   * @param classifyId
   * @param goodId
   *          商品id
   */
  public void addGood(String classifyId, String goodId) {
    GoodDTO goodDTO = goodRepository.findOne(goodId);
    if (null == goodDTO) {
      throw new GoodsException(ResponseCode.GOOD_NOT_EXIST, goodId);
    }
    ClassifyDTO classifyDTO = classifyRepository.findOne(classifyId);
    if (null == classifyDTO) {
      throw new GoodsException(ResponseCode.CLASSIFY_NOT_EXIST, classifyId);
    }
    classifyDTO.addGood(goodDTO);
    classifyRepository.saveAndFlush(classifyDTO);
  }

  /**
   * 更新条目信息
   * 
   * @param classifyId
   * @param updateClassify
   * @return
   */
  public Classify updateClassify(String classifyId, Classify updateClassify) {
    ClassifyDTO classifyDTO = classifyRepository.findOne(classifyId);
    if (null == classifyDTO) {
      throw new GoodsException(ResponseCode.CLASSIFY_NOT_EXIST, classifyId);
    }
    if (null == updateClassify) {
      return convertService.convertClassifyDTO(classifyDTO);
    }
    classifyDTO.update(updateClassify);
    classifyRepository.saveAndFlush(classifyDTO);
    return convertService.convertClassifyDTO(classifyDTO);
  }

  /**
   * 获取所有顶层条目
   * 
   * @return
   */
  public List<Classify> getTopClassifyList() {
    return convertService.convertClassifyDTOList(classifyRepository.findByLevel(1));
  }

}
