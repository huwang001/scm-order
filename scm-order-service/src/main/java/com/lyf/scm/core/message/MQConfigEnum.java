/**
 * Filename MQConfigEnum.java
 * Company 上海来伊份科技有限公司。
 * @author xly
 * @version 
 */
package com.lyf.scm.core.message;

/**
 * @author xly
 * @since 2019年6月5日 下午4:50:15
 */
public enum MQConfigEnum {
	
	/**
//	 * 库存模型发送消息队列更新数据库
	 */
//	CORE_STOCK_MESSAGE_QUEUE("stock", 3000L, 0, "库存模型发送消息队列更新数据库"),
	;
	
	
	MQConfigEnum(String tag, long timeout, int delayLevel, String desc){
		this.tag = tag;
		this.timeout = timeout;
		this.delayLevel = delayLevel;
		this.desc = desc;
	}
	/**
	 * tag值
	 */
	private final String tag;
	
	/**
	 * 超时时间,单位毫秒
	 * 通常是发送超时
	 */
	private final long timeout;
	
	/**
     * 描述
     */
    private final String desc;

	/**
	 * 延时等级
	 * RcoketMQ的延时等级为：1s，5s，10s，30s，1m，2m，3m，4m，5m，6m，7m，8m，9m，10m，20m，30m，1h，2h。
	 * level=0，表示不延时。level=1，表示 1 级延时，对应延时 1s。level=2 表示 2 级延时，对应5s，以此类推
	 */
	private final int delayLevel;

	/**
	 * tag值
	 * @return the tag
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * 超时时间,单位毫秒
	 * @return the timeout
	 */
	public long getTimeout() {
		return timeout;
	}

	/**
	 * 描述
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * 延时等级
	 * @return the delayLevel
	 */
	public int getDelayLevel() {
		return delayLevel;
	}
	
	
	
	
	
	
}
