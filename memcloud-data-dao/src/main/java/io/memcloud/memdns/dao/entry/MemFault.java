/**
 * 
 */
package io.memcloud.memdns.dao.entry;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.github.downgoon.jresty.data.dao.IEntity;

/**
 * @author ganghuawang
 * 记录memcached的故障
 */

@Entity
@Table(name = "mem_fault")
public class MemFault implements  IEntity<Long>, Serializable{

	private static final long serialVersionUID = 8141174905675892249L;

	private Long id;
	
	/** 所属APP */
	private Long appId;
	
	private Long groupId;
	
	/** IP */
	private String ip;
	
	/** 端口 */
	private Integer port;
	
	/** 故障状态 0 未处理, 1 已处理, 2 无效*/
	private Integer status;
	
	private Long createTime;

	
	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	
	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "appid")
	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}
	
	@Column(name = "group_id")
	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	@Column(name = "ip")
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Column(name = "port")
	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	@Column(name = "status")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	@Column(name = "create_time")
	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "MemFault [id=" + id + ", appId=" + appId + ", groupId=" + groupId + ", ip=" + ip + ", port=" + port
				+ ", status=" + status + ", createTime=" + createTime + "]";
	}

	
}
