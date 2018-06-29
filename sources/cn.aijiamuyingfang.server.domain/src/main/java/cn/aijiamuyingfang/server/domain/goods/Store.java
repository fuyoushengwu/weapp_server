package cn.aijiamuyingfang.server.domain.goods;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import cn.aijiamuyingfang.server.commons.utils.StringUtils;
import cn.aijiamuyingfang.server.domain.address.StoreAddress;

/**
 * [描述]:
 * <p>
 * 店铺
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-27 00:12:21
 */
@Entity
public class Store {

	/**
	 * 门店是否废弃(该字段用于删除门店:当需要删除门店时,设置该字段为true)
	 */
	private boolean deprecated;

	/**
	 * 门店Id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/**
	 * 门店名
	 */
	private String name;

	/**
	 * 营业时间
	 */
	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "start", column = @Column(name = "start_worktime")),
			@AttributeOverride(name = "end", column = @Column(name = "end_worktime")) })
	private WorkTime workTime;

	/**
	 * 封面图片地址
	 */
	private String coverImg;

	/**
	 * 详细图片地址
	 */
	@ElementCollection
	private List<String> detailImgList;

	/**
	 * 门店地址
	 */
	@OneToOne(cascade = CascadeType.ALL)
	private StoreAddress storeAddress;

	@ManyToMany
	@JsonIgnore
	private List<Classify> classifyList;

	/**
	 * 根据提供的Store更新本门店的信息
	 * 
	 * @param store
	 */
	public void update(Store store) {
		if (null == store) {
			return;
		}
		this.deprecated = store.deprecated;
		if (StringUtils.hasContent(store.name)) {
			this.name = store.name;
		}
		if (store.workTime != null) {
			this.workTime.update(store.workTime);
		}
		if (StringUtils.hasContent(store.coverImg)) {
			this.coverImg = store.coverImg;
		}
		if (store.detailImgList != null && !store.detailImgList.isEmpty()) {
			this.detailImgList = store.detailImgList;
		}
		if (store.storeAddress != null) {
			this.storeAddress = store.storeAddress;
		}
		if (store.classifyList != null && !store.classifyList.isEmpty()) {
			this.classifyList = store.classifyList;
		}
	}

	/**
	 * 为门店添加顶层条目
	 * 
	 * @param classify
	 */
	public void addClassify(Classify classify) {
		if (null == classify) {
			return;
		}
		synchronized (this) {
			if (null == this.classifyList) {
				this.classifyList = new ArrayList<>();
			}
		}
		this.classifyList.add(classify);
	}

	public boolean isDeprecated() {
		return deprecated;
	}

	public void setDeprecated(boolean deprecated) {
		this.deprecated = deprecated;
	}

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

	public WorkTime getWorkTime() {
		return workTime;
	}

	public void setWorkTime(WorkTime workTime) {
		this.workTime = workTime;
	}

	public String getCoverImg() {
		return coverImg;
	}

	public void setCoverImg(String coverImg) {
		this.coverImg = coverImg;
	}

	public List<String> getDetailImgList() {
		return detailImgList;
	}

	public void setDetailImgList(List<String> detailImgList) {
		this.detailImgList = detailImgList;
	}

	public StoreAddress getStoreAddress() {
		return storeAddress;
	}

	public void setStoreAddress(StoreAddress storeAddress) {
		this.storeAddress = storeAddress;
	}

	public List<Classify> getClassifyList() {
		return classifyList;
	}

	public void setClassifyList(List<Classify> classifyList) {
		this.classifyList = classifyList;
	}

	/**
	 * 营业时间
	 */
	public static class WorkTime {
		/**
		 * 开始时间
		 */
		private String start;

		/**
		 * 结束时间
		 */
		private String end;

		public void update(WorkTime workTime) {
			if (null == workTime) {
				return;
			}
			if (StringUtils.hasContent(workTime.start)) {
				this.start = workTime.start;
			}
			if (StringUtils.hasContent(workTime.end)) {
				this.end = workTime.end;
			}
		}

		public String getStart() {
			return start;
		}

		public void setStart(String start) {
			this.start = start;
		}

		public String getEnd() {
			return end;
		}

		public void setEnd(String end) {
			this.end = end;
		}

	}

}
