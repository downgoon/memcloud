package io.memcloud.memdns.dao.entry;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.github.downgoon.jresty.data.dao.IEntity;

/** App 与 MemGroup 的分配关系表 */
@Entity
@Table(name = "app_mem_group")
public class AppMemGroup implements  IEntity<Long>, Serializable {

	private static final long serialVersionUID = 3627502514066075729L;

	private Long id;
	
	private Long appId;
	
	private String masterIP;
	
	private Integer masterPort;
	
	private String slaveIP;
	
	private Integer slavePort;
	
	private Integer status;
	
	
	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
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

	@Column(name = "master_ip")
	public String getMasterIP() {
		return masterIP;
	}

	public void setMasterIP(String masterIP) {
		this.masterIP = masterIP;
	}

	@Column(name = "master_port")
	public Integer getMasterPort() {
		return masterPort;
	}

	public void setMasterPort(Integer masterPort) {
		this.masterPort = masterPort;
	}

	@Column(name = "slave_ip")
	public String getSlaveIP() {
		return slaveIP;
	}

	public void setSlaveIP(String slaveIP) {
		this.slaveIP = slaveIP;
	}

	@Column(name = "slave_port")
	public Integer getSlavePort() {
		return slavePort;
	}

	public void setSlavePort(Integer slavePort) {
		this.slavePort = slavePort;
	}

	@Column(name = "status")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "AppMemGroup [id=" + id + ", appId=" + appId + ", masterIP=" + masterIP + ", masterPort=" + masterPort
				+ ", slaveIP=" + slaveIP + ", slavePort=" + slavePort + ", status=" + status + "]";
	}

	
}
