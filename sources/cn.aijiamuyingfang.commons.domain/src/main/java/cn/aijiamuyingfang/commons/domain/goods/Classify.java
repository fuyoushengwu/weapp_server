package cn.aijiamuyingfang.commons.domain.goods;

import cn.aijiamuyingfang.commons.utils.CollectionUtils;
import cn.aijiamuyingfang.commons.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import org.hibernate.annotations.GenericGenerator;

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
public class Classify extends ClassifyRequest {
  /**
   * 条目Id
   */
  @Id
  @GeneratedValue(generator = "strategy_uuid")
  @GenericGenerator(name = "strategy_uuid", strategy = "uuid")
  private String id;

  /**
   * 条目层级(最顶层的条目为1,顶层条目下的子条目为2)
   */
  private int level;

  /**
   * 条目封面
   */
  private String coverImg;

  /**
   * 子条目
   */
  @OneToMany(cascade = CascadeType.ALL)
  @JsonIgnore
  private List<Classify> subClassifyList = new ArrayList<>();

  @ManyToMany
  @JsonIgnore
  private List<Good> goodList = new ArrayList<>();

  /**
   * 添加子条目
   * 
   * @param subclassify
   */
  public void addSubClassify(Classify subclassify) {
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
   */
  public void addGood(Good good) {
    if (good != null) {
      this.goodList.add(good);
    }
  }

  /**
   * 根据提供的Classify更新本条目的信息
   * 
   * @param updateClassify
   */
  public void update(Classify updateClassify) {
    if (null == updateClassify) {
      return;
    }
    if (StringUtils.hasContent(updateClassify.name)) {
      this.name = updateClassify.name;
    }
    if (updateClassify.level != 0) {
      this.level = updateClassify.level;
    }
    if (StringUtils.hasContent(updateClassify.coverImg)) {
      this.coverImg = updateClassify.coverImg;
    }
    if (!CollectionUtils.isEmpty(updateClassify.subClassifyList)) {
      this.subClassifyList = updateClassify.subClassifyList;
    }
    if (!CollectionUtils.isEmpty(updateClassify.goodList)) {
      this.goodList = updateClassify.goodList;
    }
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = level;
  }

  public String getCoverImg() {
    return coverImg;
  }

  public void setCoverImg(String coverImg) {
    this.coverImg = coverImg;
  }

  public List<Classify> getSubClassifyList() {
    return subClassifyList;
  }

  public void setSubClassifyList(List<Classify> subClassifyList) {
    this.subClassifyList = subClassifyList;
  }

  public List<Good> getGoodList() {
    return goodList;
  }

  public void setGoodList(List<Good> goodList) {
    this.goodList = goodList;
  }

}
