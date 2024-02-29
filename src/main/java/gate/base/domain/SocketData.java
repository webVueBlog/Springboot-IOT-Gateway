package gate.base.domain;



import io.netty.buffer.ByteBuf;

/**
 * 报文实体
 */
public class SocketData {
	
	private ByteBuf byteBuf;//报文缓存
	private int pId;//报文ID
	//报文头
	private byte header;
	//报文长度
	private byte[] lenArea;
	//报文体
	private byte[] content;
	private byte end;//报文尾
	
	
	public SocketData(ByteBuf byteBuf) {//构造函数
		super();
		this.byteBuf = byteBuf;//报文缓存
	}
	public SocketData(byte header, byte[] lenArea, byte[] content, byte end) {
		super();
		this.header = header;
		this.lenArea = lenArea;
		this.content = content;
		this.end = end;
	}
	public byte getHeader() {
		return header;
	}
	public void setHeader(byte header) {
		this.header = header;
	}
	public byte[] getLenArea() {
		return lenArea;
	}
	public void setLenArea(byte[] lenArea) {
		this.lenArea = lenArea;
	}
	public byte[] getContent() {
		return content;
	}
	public void setContent(byte[] content) {
		this.content = content;
	}
	public byte getEnd() {
		return end;
	}
	public void setEnd(byte end) {
		this.end = end;
	}
	public ByteBuf getByteBuf() {//报文缓存
		return byteBuf;
	}
	public void setByteBuf(ByteBuf byteBuf) {
		this.byteBuf = byteBuf;
	}
	public int getpId() {
		return pId;
	}
	public void setpId(int pId) {//报文ID
		this.pId = pId;
	}
	
	
	
	
	

}
