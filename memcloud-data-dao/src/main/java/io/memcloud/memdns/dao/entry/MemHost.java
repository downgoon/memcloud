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
@Table(name = "mem_host")
public class MemHost implements  IEntity<Long>, Serializable {

	private static final long serialVersionUID = -5664912598228980773L;

	/** 主键ID就是IP地址的整数化编码 */
	private Long id;
	
	private String ip;
	
	private String sshUser;
	
	private String sshPwd;

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

	@Column(name = "ip")
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Column(name = "ssh_user")
	public String getSshUser() {
		return sshUser;
	}

	public void setSshUser(String sshUser) {
		this.sshUser = sshUser;
	}

	@Column(name = "ssh_pwd")
	public String getSshPwd() {
		return sshPwd;
	}

	public void setSshPwd(String sshPwd) {
		this.sshPwd = sshPwd;
	}

	@Override
	public String toString() {
		return "MemHost [id=" + id + ", ip=" + ip + ", sshUser=" + sshUser + ", sshPwd=" + sshPwd + "]";
	}

}
