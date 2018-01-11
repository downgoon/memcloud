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
@Table(name = "app_conf_audit")
public class AppConfAudit implements IEntity<Long>, Serializable {

	private static final long serialVersionUID = 7218664733731725364L;

	private Long id;
	
	private Long appId;
	
	private Integer shardNum;
	
	private String groupText;
	
	private Integer version;
	
	private Long operUid;
	
	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long appId) {
		this.id = appId;
	}
	
	@Column(name = "appid")
	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	@Column(name = "version")
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@Column(name = "group_text")
	public String getGroupText() {
		return groupText;
	}

	public void setGroupText(String groupText) {
		this.groupText = groupText;
	}

	@Column(name = "oper_uid")
	public Long getOperUid() {
		return operUid;
	}

	public void setOperUid(Long operUid) {
		this.operUid = operUid;
	}
	
	@Column(name = "shard_num")
	public Integer getShardNum() {
		return shardNum;
	}

	public void setShardNum(Integer shardNum) {
		this.shardNum = shardNum;
	}

	@Override
	public String toString() {
		return "AppConfAudit [id=" + id + ", appId=" + appId + ", shardNum=" + shardNum + ", groupText=" + groupText
				+ ", version=" + version + ", operUid=" + operUid + "]";
	}
	
}
