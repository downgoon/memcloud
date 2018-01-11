package io.memcloud.stats.notification;

public class FaultEvent {

	/** 故障所在的机器 */
	private String host;
	
	/** 故障所在机器的端口 */
	private int port;
	
	/** 故障事件编号 */
	private Long id;
	
	public FaultEvent(String host, int port, Long id) {
		super();
		this.host = host;
		this.port = port;
		this.id = id;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	
}
