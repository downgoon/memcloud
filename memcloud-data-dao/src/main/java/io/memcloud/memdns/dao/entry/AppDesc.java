package io.memcloud.memdns.dao.entry;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.github.downgoon.jresty.data.dao.IEntity;

@Entity
@Table(name = "app_desc")
public class AppDesc implements IEntity<Long>, Serializable {

	private static final long serialVersionUID = 7405115213076941366L;

	private Long appId;
	
	/**应用名称*/
	private String name;
	
	/** 应用详细介绍*/
	private String descr;
	
	/** 应用第一负责人 */
	private Long ownerUid;
	
	private Integer status;
	
	/** 应用刚刚初始化，还没有分配云资源 */
	public static final int STATUS_INITIALIZE = 0;
	
	/** 应用被分配了资源 */
	public static final int STATUS_ALLOCATED = 1;
	
	/** 应用已经发布 */
	public static final int STATUS_PUBLISHED = 2;
	
	/**申请时间*/
	private Long createTime;
	
	/**审核通过时间*/
	private Long passedTime;
	
	private String notifyEmails;
	
	private String notifyMobiles;
	
	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "appid")
	public Long getId() {
		return appId;
	}

	@Override
	public void setId(Long id) {
		this.appId = id;
	}

	@Column(name = "`descr`")
	public String getDescr() {
		return descr;
	}

	public void setDescr(String desc) {
		this.descr = desc;
	}

	@Column(name = "owner_uid")
	public Long getOwnerUid() {
		return ownerUid;
	}

	public void setOwnerUid(Long ownerUid) {
		this.ownerUid = ownerUid;
	}

	@Column(name = "status")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "notify_emails")
	public String getNotifyEmails() {
		return notifyEmails;
	}

	public void setNotifyEmails(String notifyEmails) {
		this.notifyEmails = notifyEmails;
	}

	@Column(name = "notify_mobiles")
	public String getNotifyMobiles() {
		return notifyMobiles;
	}

	public void setNotifyMobiles(String notifyMobiles) {
		this.notifyMobiles = notifyMobiles;
	}

	@Column(name = "create_time")
	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	@Column(name = "passed_time")
	public Long getPassedTime() {
		return passedTime;
	}

	public void setPassedTime(Long passedTime) {
		this.passedTime = passedTime;
	}
	
	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "AppDesc [appId=" + appId + ", name=" + name + ", descr=" + descr + ", ownerUid=" + ownerUid
				+ ", status=" + status + ", createTime=" + createTime + ", passedTime=" + passedTime + ", notifyEmails="
				+ notifyEmails + ", notifyMobiles=" + notifyMobiles + "]";
	}

	
}
