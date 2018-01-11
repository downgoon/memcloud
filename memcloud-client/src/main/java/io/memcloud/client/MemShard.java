package io.memcloud.client;

import java.util.ArrayList;
import java.util.List;

public class MemShard {
	private String master;
	private String slave;
	
	public MemShard() {
		
	}
	
	public MemShard(String master, String slave) {
		super();
		this.master = master;
		this.slave = slave;
	}
	public String getMaster() {
		return master;
	}
	public void setMaster(String master) {
		this.master = master;
	}
	public String getSlave() {
		return slave;
	}
	public void setSlave(String slave) {
		this.slave = slave;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((master == null) ? 0 : master.hashCode());
		result = prime * result + ((slave == null) ? 0 : slave.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MemShard other = (MemShard) obj;
		if (master == null) {
			if (other.master != null)
				return false;
		} else if (!master.equals(other.master))
			return false;
		if (slave == null) {
			if (other.slave != null)
				return false;
		} else if (!slave.equals(other.slave))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "["+master+","+slave+"]";
	}
	
	public static String shard2txt(MemShard shard) {
		return shard.master+","+shard.slave;
	}
	
	public static MemShard txt2shard(String confTxt) {
		String[] items = confTxt.split(",");
		if(items==null || items.length < 2) {
			throw new IllegalArgumentException("memshard format: host1:port1,host2:port2");
		}
		return new MemShard(items[0], items[1]);
	}

	public static String group2conf(List<MemShard> group) {
		StringBuilder sb = new StringBuilder();
		for (MemShard ms : group) {
			sb.append(shard2txt(ms)).append(" ");
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}

	public static List<MemShard> conf2group(String conf) {
		if ( conf==null || conf.length() == 0) {
			return null;
		}
		String[] shardTxts = conf.split(" ");
		if ( shardTxts==null || shardTxts.length == 0 ) {
			return null;
		}
		List<MemShard> group = new ArrayList<MemShard>();
		for (String txt : shardTxts) {
			MemShard s = txt2shard(txt);
			if (s!=null) {
				group.add(s);
			}
		}
		return group;
	}
	
}
