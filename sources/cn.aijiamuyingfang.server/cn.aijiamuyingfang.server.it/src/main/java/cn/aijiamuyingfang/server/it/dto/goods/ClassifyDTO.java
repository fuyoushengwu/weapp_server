package cn.aijiamuyingfang.server.it.dto.goods;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import cn.aijiamuyingfang.commons.utils.CollectionUtils;
import cn.aijiamuyingfang.commons.utils.StringUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * [描述]:
 * <p>
 * 条目
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-27 00:12:32
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class ClassifyDTO {
  /**
   * 条目Id
   */
  @Id
  @GeneratedValue(generator = "strategy_uuid")
  @GenericGenerator(name = "strategy_uuid", strategy = "uuid")
  private String id;

  /**
   * 条目名
   */
  private String name;

  /**
   * 条目层级(最顶层的条目为1,顶层条目下的子条目为2)
   */
  private int level;

  /**
   * 条目封面
   */
  @ManyToOne
  private ImageSourceDTO coverImg;

  /**
   * 子条目
   */
  @OneToMany(cascade = CascadeType.ALL)
  @JsonIgnore
  private List<ClassifyDTO> subClassifyList = new ArrayList<>();

  @ManyToMany
  @JsonIgnore
  private List<GoodDTO> goodList = new ArrayList<>();

  /**
   * 添加子条目
   * 
   * @param subclassify
   *          子条目
   */
  public void addSubClassify(ClassifyDTO subclassify) {
    synchronized (this) {
      if (null == this.subClassifyList) {
        this.subClassifyList = new ArrayList<>();
      }
    }
    if (subclassify != null) {
      this.subClassifyList.add(subclassify);
    }
  }

  /**
   * 条目下添加商品
   * 
   * @param good
   *          商品
   */
  public void addGood(GoodDTO good) {
    if (good != null) {
      this.goodList.add(good);
    }
  }

  /**
   * 根据提供的Classify更新本条目的信息
   * 
   * @param updateClassify
   *          要更新的条目信息
   */
  public void update(ClassifyDTO updateClassify) {
    if (null == updateClassify) {
      return;
    }
    if (StringUtils.hasContent(updateClassify.name)) {
      this.name = updateClassify.name;
    }
    if (updateClassify.level != 0) {
      this.level = updateClassify.level;
    }
    if (updateClassify.coverImg != null) {
      this.coverImg.update(updateClassify.coverImg);
    }
    if (!CollectionUtils.isEmpty(updateClassify.subClassifyList)) {
      this.subClassifyList = updateClassify.subClassifyList;
    }
    if (!CollectionUtils.isEmpty(updateClassify.goodList)) {
      this.goodList = updateClassify.goodList;
    }
  }

}
