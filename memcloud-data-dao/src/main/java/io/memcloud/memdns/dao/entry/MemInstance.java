package io.memcloud.memdns.dao.entry;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.github.downgoon.jresty.data.dao.IEntity;

@Entity
@Table(name = "mem_instance")
public class MemInstance implements  IEntity<Long>, Serializable {

	private static final long serialVersionUID = -1367828584341731090L;

	private Long id;
	
	/** 外键 */
	private Long hostid;
	
	/** 外键HostID的冗余数据 */
	private String hostIp;
	
	private Integer port;
	
	/** 双向复制对等机的服务IP*/
	private String peerIp;
	
	/** 同步端口 (peerIp+repcPort 索引到本实例的孪生实例 )*/
	private Integer repcPort;
	
	private Integer status;
	public static final int STATUS_UNUSE = 0;
	public static final int STATUS_USED = 1;
	
	private Integer argMem;
	
	private Integer argConn;
	
	/** 双向复制对等实例是否也已经运行着 
	 * 0 表示对等体没有安装，没有兄弟；
	 * -1 表示对等体是哥哥，自己是弟弟。
	 * 1 表示对等体是弟弟，自己是哥哥。
	 * */
	private Short roleInPeer;
	/** -1 表示对等体是哥哥，自己是弟弟 */
	public static final short YOUNG_IN_PEER = -1;
	/** 0表示对等体还没有安装 */
	public static final short NONE_IN_PEER = 0;
	/** 1 表示对等体是弟弟，自己是哥哥 */
	public static final short ELDER_IN_PEER = 1;
	
	private String memCmd;
	
	

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

	@Column(name = "host_id")
	public Long getHostid() {
		return hostid;
	}

	public void setHostid(Long hostid) {
		this.hostid = hostid;
	}

	@Column(name = "host_ip")
	public String getHostIp() {
		return hostIp;
	}

	public void setHostIp(String hostip) {
		this.hostIp = hostip;
	}

	@Column(name = "port")
	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	@Column(name = "arg_mem")
	public Integer getArgMem() {
		return argMem;
	}

	public void setArgMem(Integer argMem) {
		this.argMem = argMem;
	}

	@Column(name = "arg_conn")
	public Integer getArgConn() {
		return argConn;
	}

	public void setArgConn(Integer argConn) {
		this.argConn = argConn;
	}

	@Column(name = "mem_cmd")
	public String getMemCmd() {
		return memCmd;
	}

	public void setMemCmd(String memCmd) {
		this.memCmd = memCmd;
	}

	@Column(name = "status")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "peer_ip")
	public String getPeerIp() {
		return peerIp;
	}

	public void setPeerIp(String peerIp) {
		this.peerIp = peerIp;
	}

	@Column(name = "repc_port")
	public Integer getRepcPort() {
		return repcPort;
	}

	public void setRepcPort(Integer repcPort) {
		this.repcPort = repcPort;
	}

	@Column(name = "role_in_peer")
	public Short getRoleInPeer() {
		return roleInPeer;
	}
	
	@Transient
	public boolean isPeerInstalled() {
		return (NONE_IN_PEER+0) != roleInPeer;
	}

	public void setRoleInPeer(Short roleInPeer) {
		this.roleInPeer = roleInPeer;
	}

	@Override
	public String toString() {
		return "MemInstance [id=" + id + ", hostid=" + hostid + ", hostIp=" + hostIp + ", port=" + port + ", peerIp="
				+ peerIp + ", repcPort=" + repcPort + ", status=" + status + ", argMem=" + argMem + ", argConn="
				+ argConn + ", roleInPeer=" + roleInPeer + ", memCmd=" + memCmd + "]";
	}

	
}
