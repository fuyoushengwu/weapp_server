package cn.aijiamuyingfang.server.goods.service;

import cn.aijiamuyingfang.commons.domain.exception.GoodsException;
import cn.aijiamuyingfang.commons.domain.goods.Classify;
import cn.aijiamuyingfang.commons.domain.goods.Good;
import cn.aijiamuyingfang.commons.domain.response.ResponseCode;
import cn.aijiamuyingfang.commons.utils.StringUtils;
import cn.aijiamuyingfang.server.domain.goods.db.ClassifyRepository;
import cn.aijiamuyingfang.server.domain.goods.db.GoodRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
   * @param classifyid
   *          条目Id
   * @return
   */
  public Classify getClassify(String classifyid) {
    return classifyRepository.findOne(classifyid);
  }

  /**
   * 废弃条目
   * 
   * @param classifyid
   */
  public void deleteClassify(String classifyid) {
    classifyRepository.delete(classifyid);
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
      Classify oriClassify = classifyRepository.findOne(classify.getId());
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
  public Classify createORUpdateSubClassify(String topclassifyid, Classify subclassify) {
    if (null == subclassify) {
      return null;
    }
    subclassify.setLevel(2);

    if (StringUtils.hasContent(subclassify.getId())) {
      Classify oriSubClassify = classifyRepository.findOne(subclassify.getId());
      if (oriSubClassify != null) {
        oriSubClassify.update(subclassify);
        return classifyRepository.saveAndFlush(oriSubClassify);
      }
    }
    Classify topclassify = classifyRepository.findOne(topclassifyid);
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
   * @param classifyid
   *          父条目Id
   * @return
   */
  public List<Classify> getSubClassifyList(String classifyid) {
    Classify classify = classifyRepository.findOne(classifyid);
    if (null == classify) {
      throw new GoodsException(ResponseCode.CLASSIFY_NOT_EXIST, classifyid);
    }
    return classify.getSubClassifyList();
  }

  /**
   * 在条目下添加商品
   * 
   * @param classifyid
   * @param goodid
   */
  public void addGood(String classifyid, String goodid) {
    Good good = goodRepository.findOne(goodid);
    if (null == good) {
      throw new GoodsException(ResponseCode.GOOD_NOT_EXIST, goodid);
    }
    Classify classify = classifyRepository.findOne(classifyid);
    if (null == classify) {
      throw new GoodsException(ResponseCode.CLASSIFY_NOT_EXIST, classifyid);
    }
    classify.addGood(good);
    classifyRepository.saveAndFlush(classify);
  }

  /**
   * 更新条目信息
   * 
   * @param classifyid
   * @param updateClassify
   * @return
   */
  public Classify updateClassify(String classifyid, Classify updateClassify) {
    Classify classify = classifyRepository.findOne(classifyid);
    if (null == classify) {
      throw new GoodsException(ResponseCode.CLASSIFY_NOT_EXIST, classifyid);
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
  public List<Classify> getTopClassifyList() {
    return classifyRepository.findByLevel(1);
  }

}
