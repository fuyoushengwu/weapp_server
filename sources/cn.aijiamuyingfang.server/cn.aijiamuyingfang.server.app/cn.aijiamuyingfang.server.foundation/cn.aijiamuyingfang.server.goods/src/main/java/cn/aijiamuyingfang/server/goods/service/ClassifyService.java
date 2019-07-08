package cn.aijiamuyingfang.server.goods.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.aijiamuyingfang.commons.utils.StringUtils;
import cn.aijiamuyingfang.server.domain.response.ResponseCode;
import cn.aijiamuyingfang.server.exception.GoodsException;
import cn.aijiamuyingfang.server.goods.db.ClassifyRepository;
import cn.aijiamuyingfang.server.goods.db.GoodRepository;
import cn.aijiamuyingfang.server.goods.dto.ClassifyDTO;
import cn.aijiamuyingfang.server.goods.dto.GoodDTO;

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

  /**
   * 获取条目信息
   * 
   * @param classifyId
   *          条目Id
   * @return
   */
  public ClassifyDTO getClassify(String classifyId) {
    return classifyRepository.findOne(classifyId);
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
  public ClassifyDTO createORUpdateTopClassify(ClassifyDTO classify) {
    if (null == classify) {
      return null;
    }
    classify.setLevel(1);
    if (StringUtils.hasContent(classify.getId())) {
      ClassifyDTO oriClassify = classifyRepository.findOne(classify.getId());
      if (oriClassify != null) {
        oriClassify.update(classify);
        return classifyRepository.saveAndFlush(oriClassify);
      }
    }
    return classifyRepository.saveAndFlush(classify);
  }

  /**
   * 添加子条目
   * 
   * @param topclassifyid
   * @param subclassify
   * @return
   */
  public ClassifyDTO createORUpdateSubClassify(String topclassifyid, ClassifyDTO subclassify) {
    if (null == subclassify) {
      return null;
    }
    subclassify.setLevel(2);

    if (StringUtils.hasContent(subclassify.getId())) {
      ClassifyDTO oriSubClassify = classifyRepository.findOne(subclassify.getId());
      if (oriSubClassify != null) {
        oriSubClassify.update(subclassify);
        return classifyRepository.saveAndFlush(oriSubClassify);
      }
    }
    ClassifyDTO topclassify = classifyRepository.findOne(topclassifyid);
    if (null == topclassify) {
      throw new GoodsException(ResponseCode.CLASSIFY_NOT_EXIST, topclassifyid);
    }
    topclassify.addSubClassify(subclassify);
    classifyRepository.saveAndFlush(topclassify);
    return topclassify.getSubClassifyList().get(topclassify.getSubClassifyList().size() - 1);
  }

  /**
   * 获取子条目
   * 
   * @param classifyId
   *          父条目Id
   * @return
   */
  public List<ClassifyDTO> getSubClassifyList(String classifyId) {
    ClassifyDTO classify = classifyRepository.findOne(classifyId);
    if (null == classify) {
      throw new GoodsException(ResponseCode.CLASSIFY_NOT_EXIST, classifyId);
    }
    return classify.getSubClassifyList();
  }

  /**
   * 在条目下添加商品
   * 
   * @param classifyId
   * @param goodId
   *          商品id
   */
  public void addGood(String classifyId, String goodId) {
    GoodDTO good = goodRepository.findOne(goodId);
    if (null == good) {
      throw new GoodsException(ResponseCode.GOOD_NOT_EXIST, goodId);
    }
    ClassifyDTO classify = classifyRepository.findOne(classifyId);
    if (null == classify) {
      throw new GoodsException(ResponseCode.CLASSIFY_NOT_EXIST, classifyId);
    }
    classify.addGood(good);
    classifyRepository.saveAndFlush(classify);
  }

  /**
   * 更新条目信息
   * 
   * @param classifyId
   * @param updateClassify
   * @return
   */
  public ClassifyDTO updateClassify(String classifyId, ClassifyDTO updateClassify) {
    ClassifyDTO classify = classifyRepository.findOne(classifyId);
    if (null == classify) {
      throw new GoodsException(ResponseCode.CLASSIFY_NOT_EXIST, classifyId);
    }
    if (null == updateClassify) {
      return classify;
    }
    classify.update(updateClassify);
    classifyRepository.saveAndFlush(classify);
    return classify;
  }

  /**
   * 获取所有顶层条目
   * 
   * @return
   */
  public List<ClassifyDTO> getTopClassifyList() {
    return classifyRepository.findByLevel(1);
  }

}
