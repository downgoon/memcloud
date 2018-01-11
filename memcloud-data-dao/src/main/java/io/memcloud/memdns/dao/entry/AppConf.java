package io.memcloud.memdns.dao.entry;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.github.downgoon.jresty.data.dao.IEntity;

@Entity
@Table(name = "app_conf")
public class AppConf implements IEntity<Long>, Serializable {

	private static final long serialVersionUID = 7218664733731725364L;

	private Long appId;
	
	private Integer shardNum;
	
	private String groupText;
	
	private Integer version;
	
	@Override
	@Id
	@Column(name = "appid")
	public Long getId() {
		return appId;
	}

	@Override
	public void setId(Long appId) {
		this.appId = appId;
	}

	@Transient
	public Long getAppId() {
		return getId();
	}

	public void setAppId(Long appId) {
		setId(appId);
	}

	@Column(name = "version")
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
	@Column(name = "shard_num")
	public Integer getShardNum() {
		return shardNum;
	}

	public void setShardNum(Integer shardNum) {
		this.shardNum = shardNum;
	}

	@Column(name = "group_text")
	public String getGroupText() {
		return groupText;
	}

	public void setGroupText(String groupText) {
		this.groupText = groupText;
	}

	@Override
	public String toString() {
		return "AppConf [appId=" + appId + ", shardNum=" + shardNum + ", groupText=" + groupText + ", version="
				+ version + "]";
	}
	
}
