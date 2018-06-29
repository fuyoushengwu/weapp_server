package cn.aijiamuyingfang.server.domain.goods;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
public class Classify {
	/**
	 * 条目Id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/**
	 * 条目名
	 */
	private String name;

	/**
	 * 条目是否废弃(该字段用于删除条目:当需要删除条目时,设置该字段为true)
	 */
	private boolean deprecated;

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
	@ManyToMany
	@JsonIgnore
	private List<Classify> subClassifyList;

	@ManyToMany
	@JsonIgnore
	private List<Good> goodList;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isDeprecated() {
		return deprecated;
	}

	public void setDeprecated(boolean deprecated) {
		this.deprecated = deprecated;
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
