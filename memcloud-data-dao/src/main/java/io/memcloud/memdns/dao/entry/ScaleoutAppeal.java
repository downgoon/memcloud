package io.memcloud.memdns.dao.entry;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.github.downgoon.jresty.data.dao.IEntity;

/** 用户扩容申请，并等待管理员的审核  */
@Entity
@Table(name = "scaleout_appeal")
public class ScaleoutAppeal implements IEntity<Long>, Serializable {

	private static final long serialVersionUID = 5726092984278405634L;

	private Long id;
	
	private Long appId;
	
	private String appName;
	
	private Long userId;
	
	private Integer shardNum;
	
	private Integer passedShard;
	
	private Integer memSize;
	
	private Integer passedMem;
	
	private Long createTime;
	
	private Long passedTime;
	
	private Short status;
	
	/** 审核通过*/
	public static final short STATUS_PASSED = 1;
	/** 审核被拒绝 */
	public static final short STATUS_REJECT = -1;
	/** 等待审核 */
	public static final short STATUS_WAITING = 0;
	
	
	
	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "recid")
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id =  id;
	}

	@Column(name = "app_id")
	public Long getAppId() {
		return appId;
	}

	@Column(name = "user_id")
	public Long getUserId() {
		return userId;
	}

	@Column(name = "shard_num")
	public Integer getShardNum() {
		return shardNum;
	}

	@Column(name = "passed_shard")
	public Integer getPassedShard() {
		return passedShard;
	}

	@Column(name = "mem_size")
	public Integer getMemSize() {
		return memSize;
	}

	@Column(name = "passed_mem")
	public Integer getPassedMem() {
		return passedMem;
	}

	@Column(name = "create_time")
	public Long getCreateTime() {
		return createTime;
	}

	@Column(name = "passed_time")
	public Long getPassedTime() {
		return passedTime;
	}

	@Column(name = "status")
	public Short getStatus() {
		return status;
	}

	@Column(name = "app_name")
	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void setShardNum(Integer shardNum) {
		this.shardNum = shardNum;
	}

	public void setPassedShard(Integer passedShard) {
		this.passedShard = passedShard;
	}

	public void setMemSize(Integer memSize) {
		this.memSize = memSize;
	}

	public void setPassedMem(Integer passedMem) {
		this.passedMem = passedMem;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public void setPassedTime(Long passedTime) {
		this.passedTime = passedTime;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "ScaleoutAppeal [id=" + id + ", appId=" + appId + ", appName=" + appName + ", userId=" + userId
				+ ", shardNum=" + shardNum + ", passedShard=" + passedShard + ", memSize=" + memSize + ", passedMem="
				+ passedMem + ", createTime=" + createTime + ", passedTime=" + passedTime + ", status=" + status + "]";
	}

	
}
