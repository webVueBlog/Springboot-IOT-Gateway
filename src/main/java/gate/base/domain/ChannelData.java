package gate.base.domain;

import java.io.Serializable;
/**
 * 一个客户端请求所对应的所有有用数据对象
 */
public class ChannelData implements Serializable{
	private static final long serialVersionUID = -7164069554228806843L;
	/**
	 * 通道的远程ip地址
	 */
	private String ipAddress;
	private SocketData socketData;// SocketData是SocketChannel的封装
	/**
	 *根据SocketData创建对象 
	 */
	public ChannelData(SocketData socketData) {
		
		this(null,socketData);// 默认ip地址为null
	}
	
	public ChannelData(String ipAddress, SocketData socketData) {// 构造方法
		super();
		this.ipAddress = ipAddress;// 通道的远程ip地址
		this.socketData = socketData;// SocketData是SocketChannel的封装
	}
	public String getIpAddress() {
		return ipAddress;// 通道的远程ip地址
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public SocketData getSocketData() {
		return socketData;
	}
	public void setSocketData(SocketData socketData) {
		this.socketData = socketData;
	}
	
}
