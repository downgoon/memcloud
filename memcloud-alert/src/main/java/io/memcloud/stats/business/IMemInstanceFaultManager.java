package io.memcloud.stats.business;


public interface IMemInstanceFaultManager {

	/** 发送故障消息 */
	public void sendFaultMessage(String host, Integer port);
	
	/** 确认故障已受理 */
	public void receiveFaultMessage(Long id);
	
	/** 故障自动恢复了 */
	public void recoverFault(String host, Integer port);
}
