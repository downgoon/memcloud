package io.memcloud.client;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

public class MemDNSLookup {

	private Integer version;
	
	private List<MemShard> memGroup;

	private int TTLSecond;
	
	private long timestamp;
	

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public int getTTLSecond() {
		return TTLSecond;
	}

	public void setTTLSecond(int tTLSecond) {
		TTLSecond = tTLSecond;
	}
	
	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public List<MemShard> getMemGroup() {
		return memGroup;
	}

	public void setMemGroup(List<MemShard> memGroup) {
		this.memGroup = memGroup;
	}
	
	public List<MemShard> diffRemoved(MemDNSLookup previous) {
		return diff(previous, true);
	}
	
	public List<MemShard> diffAppended(MemDNSLookup previous) {
		return diff(previous, false);
	}
	
	@SuppressWarnings("unchecked")
	private List<MemShard> diff(MemDNSLookup previous, boolean is4Removed) {
		if (previous == null) {
			return is4Removed ? null : this.memGroup; 
		}
		
		if (previous.version == this.version) {
			return null;
		}
		if (previous.version > this.version) {
			throw new IllegalStateException("Cond: previous MemDNSLookup version <= current");
		}
		if (is4Removed) {
			return (List<MemShard>) CollectionUtils.subtract(previous.memGroup, this.memGroup);
		} else {
			return (List<MemShard>) CollectionUtils.subtract(this.memGroup, previous.memGroup);
		}
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("v=").append(version).append(";");
		sb.append("ttl=").append(TTLSecond).append(";");
		sb.append("group=").append(MemShard.group2conf(memGroup));
		return sb.toString();
	}
	
	
}
