package cn.aijiamuyingfang.server.goods.dto;

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

import cn.aijiamuyingfang.server.goods.utils.ConvertUtils;
import cn.aijiamuyingfang.vo.classify.Classify;
import cn.aijiamuyingfang.vo.utils.StringUtils;
import lombok.Data;

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
@Entity(name = "classify")
@Data
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
  private List<ClassifyDTO> subClassifyList = new ArrayList<>();

  @ManyToMany
  private List<GoodDTO> goodList = new ArrayList<>();

  /**
   * 根据提供的Classify更新本条目的信息
   * 
   * @param updateClassify
   *          要更新的条目信息
   */
  public void update(Classify updateClassify) {
    if (null == updateClassify) {
      return;
    }
    if (StringUtils.hasContent(updateClassify.getName())) {
      this.setName(updateClassify.getName());
    }
    if (updateClassify.getLevel() != 0) {
      this.setLevel(updateClassify.getLevel());
    }
    if (updateClassify.getCoverImg() != null) {
      this.setCoverImg(ConvertUtils.convertImageSource(updateClassify.getCoverImg()));
    }
  }

  /**
   * 添加子条目
   * 
   * @param subclassifyDTO
   */
  public void addSubClassify(ClassifyDTO subclassifyDTO) {
    synchronized (this) {
      if (null == this.subClassifyList) {
        this.subClassifyList = new ArrayList<>();
      }
    }
    if (subclassifyDTO != null) {
      this.subClassifyList.add(subclassifyDTO);
    }
  }

  /**
   * 条目下添加商品
   * 
   * @param goodDTO
   *          商品
   */
  public void addGood(GoodDTO goodDTO) {
    if (goodDTO != null) {
      this.goodList.add(goodDTO);
    }
  }
}
