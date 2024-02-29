package gate.codec;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import gate.base.domain.ChannelData;
import gate.base.domain.SocketData;
import gate.util.CommonUtil;
import gate.util.StringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.internal.RecyclableArrayList;
/**
 * 解码器
 */
public class Gate2ClientDecoderMulti  extends ByteToMessageDecoder{
	private int pId;//协议ID
	private boolean isBigEndian ;//是否大端
	private int beginHexVal;//起始值
	private int lengthFieldOffset;//长度域偏移量
	private int lengthFieldLength;//值为Data得长度
	private boolean isDataLenthIncludeLenthFieldLenth ;//长度域长度值是否包含长度域本身长度
	private int exceptDataLenth;//除了Data得长度
	private int initialBytesToStrip;//默认为0
	
	/**
	 * 全参构造
	 * @param pId
	 * @param isBigEndian
	 * @param beginHexVal
	 * @param lengthFieldOffset
	 * @param lengthFieldLength
	 * @param isDataLenthIncludeLenthFieldLenth
	 * @param exceptDataLenth
	 * @param initialBytesToStrip
	 */
	public Gate2ClientDecoderMulti( int pId, boolean isBigEndian, int beginHexVal, int lengthFieldOffset, int lengthFieldLength,
			boolean isDataLenthIncludeLenthFieldLenth, int exceptDataLenth, int initialBytesToStrip) {
		super();
		this.pId = pId;
		this.isBigEndian = isBigEndian;
		this.beginHexVal = beginHexVal;
		this.lengthFieldOffset = lengthFieldOffset;
		this.lengthFieldLength = lengthFieldLength;
		this.isDataLenthIncludeLenthFieldLenth = isDataLenthIncludeLenthFieldLenth;
		this.exceptDataLenth = exceptDataLenth;
		this.initialBytesToStrip = initialBytesToStrip;
	}
	
	/**
	 * 默认起始偏移量为0
	 * @param pId
	 * @param isBigEndian
	 * @param beginHexVal
	 * @param lengthFieldOffset
	 * @param lengthFieldLength
	 * @param isDataLenthIncludeLenthFieldLenth
	 * @param exceptDataLenth
	 */
	public Gate2ClientDecoderMulti(int pId, boolean isBigEndian, int beginHexVal, int lengthFieldOffset, int lengthFieldLength,
			boolean isDataLenthIncludeLenthFieldLenth, int exceptDataLenth) {
		this(pId, isBigEndian, beginHexVal, lengthFieldOffset, lengthFieldLength,
				isDataLenthIncludeLenthFieldLenth, exceptDataLenth, 0);
	}


	/**
	 *
	 * 解码
	 * @param ctx
	 * @param in
	 * @param out
	 * @throws Exception
	 */
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		int baseLen = lengthFieldOffset + lengthFieldLength + exceptDataLenth + initialBytesToStrip;//总长度
		if(in.readableBytes()>= baseLen){//如果可读字节数大于等于总长度
			int beginReader;//起始读取位置
			RecyclableArrayList arrayList = RecyclableArrayList.newInstance();//创建一个可回收的数组列表
			while (true) {
				if(in.readableBytes()>= baseLen){
					beginReader = in.readerIndex();
					if(beginHexVal > 0){
						int startHexVal = in.readByte() & 0xFF;
						if(startHexVal != beginHexVal){
							dataTransfer(arrayList,out);
							continue;
						}
						in.readerIndex(beginReader);
					}
					
					ByteBuf byteBuf = CommonUtil.getDirectByteBuf();
					
					if(initialBytesToStrip == 0){
						byteBuf.writeBytes(in.readBytes(lengthFieldOffset));
						
						ByteBuf lenAre = in.readBytes(lengthFieldLength);
						byteBuf.writeBytes(lenAre);
						lenAre.readerIndex(0);
						int lenVal = 0;
						switch (lengthFieldLength) {
						case 1:
								lenVal = lenAre.readByte() & 0xFF;
							break;
						case 2:
							if(isBigEndian){
								lenVal = lenAre.readShort() & 0xFFFF;
							}else{
								lenVal = lenAre.readShortLE() & 0xFFFF ;
							}
							break;
						case 4:
							if(isBigEndian){
								lenVal = lenAre.readInt();
							}else{
								lenVal = lenAre.readIntLE();
							}
							break;
						default:
							CommonUtil.releaseByteBuf(byteBuf);
							break;
						}
						if(isDataLenthIncludeLenthFieldLenth){
							lenVal = lenVal - lengthFieldLength;
						}
						
						if(in.readableBytes() >= (lenVal+exceptDataLenth)  && lenVal>0){
							byteBuf.writeBytes(in.readBytes(lenVal+exceptDataLenth));
//							in.markReaderIndex();
							Channel channel = ctx.channel();
							InetSocketAddress insocket = (InetSocketAddress)channel.remoteAddress();
							String ipAddress = StringUtils.formatIpAddress(insocket.getHostName(), String.valueOf(insocket.getPort()));
							String clientIpAddress = ipAddress;
							SocketData data = new SocketData(byteBuf);
							data.setpId(pId);
							ChannelData channelData =  new ChannelData(clientIpAddress, data);
							arrayList.add(channelData);
							continue;
						}else{
							if(beginHexVal < 0 ){
								in.readerIndex(beginReader+1);
							}else{
								in.readerIndex(beginReader);
							}
							dataTransfer(arrayList,out);
							break;
						}
						
					}else{
						byteBuf.writeBytes(in.readBytes(initialBytesToStrip));
						byteBuf.writeBytes(in.readBytes(lengthFieldOffset));
						ByteBuf lenAre = in.readBytes(lengthFieldLength);
						byteBuf.writeBytes(lenAre);
						lenAre.readerIndex(0);
						int lenVal = 0;
						switch (lengthFieldLength) {
						case 1:
								lenVal = lenAre.readByte() & 0xFF;//读取小端序
							break;
						case 2:
							if(isBigEndian){
								lenVal = lenAre.readShort() & 0xFFFF;//读取大端序
							}else{
								lenVal = lenAre.readShortLE() & 0xFFFF;//读取小端序
							}
							break;
						case 4:
							if(isBigEndian){//读取大端序
								lenVal = lenAre.readInt();//读取大端序
							}else{
								lenVal = lenAre.readIntLE();//读取小端序
							}
							break;
						default:
							CommonUtil.releaseByteBuf(byteBuf);//释放byteBuf
							break;
						}
						if(isDataLenthIncludeLenthFieldLenth){//数据长度是否包含长度字段的长度
							lenVal = lenVal - lengthFieldLength;//数据长度
						}
						
						if(in.readableBytes() >= (lenVal+exceptDataLenth) && lenVal>0){
							byteBuf.writeBytes(in.readBytes(lenVal+exceptDataLenth));//读取数据
//							in.markReaderIndex();
							Channel channel = ctx.channel();//获取channel
							InetSocketAddress insocket = (InetSocketAddress)channel.remoteAddress();//客户端地址
							String ipAddress = StringUtils.formatIpAddress(insocket.getHostName(), String.valueOf(insocket.getPort()));//客户端IP地址
							String clientIpAddress = ipAddress;//客户端IP地址
							SocketData data = new SocketData(byteBuf);//封装数据
							data.setpId(pId);//设置协议ID
							ChannelData channelData =  new ChannelData(clientIpAddress, data);//封装数据
							arrayList.add(channelData);//数据到达，加入队列
							continue;
						}else{
							if(beginHexVal < 0 ){//数据不完整，等待下次数据到达后继续处理
								in.readerIndex(beginReader+1);//恢复读指针
							}else{
								in.readerIndex(beginReader);//恢复读指针
							}
							dataTransfer(arrayList,out);//数据传输
							break;
						}
					}
				}else{
					dataTransfer(arrayList,out);//数据传输
					break;
				}
			}
		}
	}
	private void dataTransfer(RecyclableArrayList arrayList,List<Object> out){//数据传输
		if(!arrayList.isEmpty()){//数据不为空
			int size = arrayList.size();//数据个数
			ArrayList<Object> arrayList2 = new ArrayList<>(size);//数据容器
			for (int i = 0; i < size; i++) {//遍历数据
				arrayList2.add(arrayList.get(i));//添加数据
			}
			out.add(arrayList2);//添加数据
			arrayList.recycle();//回收数据
		}
	}
}
