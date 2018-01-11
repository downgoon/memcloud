package io.memcloud.memdns.dao.entry;

import java.io.Serializable;

public class MemInstancePeer implements Serializable {

	private static final long serialVersionUID = -3147464859326618051L;
	
	private Integer repcPort;
	
	private String mhost;
	
	private Integer mport;
	
	private Long mid;
	
	private String shost;
	
	private Integer sport;
	
	private Long sid;
	
	private Integer mem;

	
	public MemInstancePeer(Integer repcPort, String mhost, Integer mport,
			Long mid, String shost, Integer sport, Long sid, Integer mem) {
		super();
		this.repcPort = repcPort;
		this.mhost = mhost;
		this.mport = mport;
		this.mid = mid;
		this.shost = shost;
		this.sport = sport;
		this.sid = sid;
		this.mem = mem;
	}
	
	public MemInstancePeer() {
		
	}

	public Integer getRepcPort() {
		return repcPort;
	}

	public void setRepcPort(Integer repcPort) {
		this.repcPort = repcPort;
	}

	public String getMhost() {
		return mhost;
	}

	public void setMhost(String mhost) {
		this.mhost = mhost;
	}

	public Integer getMport() {
		return mport;
	}

	public void setMport(Integer mport) {
		this.mport = mport;
	}

	public Long getMid() {
		return mid;
	}

	public void setMid(Long mid) {
		this.mid = mid;
	}

	public String getShost() {
		return shost;
	}

	public void setShost(String shost) {
		this.shost = shost;
	}

	public Integer getSport() {
		return sport;
	}

	public void setSport(Integer sport) {
		this.sport = sport;
	}

	public Long getSid() {
		return sid;
	}

	public void setSid(Long sid) {
		this.sid = sid;
	}

	public Integer getMem() {
		return mem;
	}

	public void setMem(Integer mem) {
		this.mem = mem;
	}

	@Override
	public String toString() {
		return "MemInstancePeer [repcPort=" + repcPort + ", mhost=" + mhost + ", mport=" + mport + ", mid=" + mid
				+ ", shost=" + shost + ", sport=" + sport + ", sid=" + sid + ", mem=" + mem + "]";
	}

	
	
}
