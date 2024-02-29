package gate.server;



import gate.base.cache.ProtocalStrategyCache;
import gate.server.handler.SocketInHandler;
import gate.util.CommonUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

/**
 * 网关获取终端报文
 */
public class Server4Terminal {
	/**
	 * 规约编号作为规约服务以及规约策略的唯一标识
	 */
	private  String  pId;
	private  String  serverPort;
	private  EventLoopGroup  boss;
	
	public Server4Terminal (String pId,String serverPort){
		this.pId = pId;
		this.serverPort = serverPort;
		this.boss = new NioEventLoopGroup();
	}
	
	
	/**
	 * 通过引导配置参数
	 * @return
	 */
	public  Bootstrap config(int pId, boolean isBigEndian, int beginHexVal, int lengthFieldOffset, int lengthFieldLength,
			boolean isDataLenthIncludeLenthFieldLenth, int exceptDataLenth){
		 Bootstrap serverBootstrap = new Bootstrap();
		 serverBootstrap
		 .group(boss)/* 绑定线程池 */
		 .channel(NioDatagramChannel.class)/* 指定使用NIO进行网络通讯 */
		 .option(ChannelOption.SO_BROADCAST, true)/* 支持广播 */
		 .option(ChannelOption.ALLOCATOR, UnpooledByteBufAllocator.DEFAULT)/* 指定ByteBuf分配器 */
		 .handler(new SocketInHandler());/* 绑定IO事件和处理逻辑 */
		 
		return serverBootstrap;// 返回引导配置参数
	}

	
	
	/**
	 * 绑定服务到指定端口
	 * @param serverBootstrap
	 */
	public  void bindAddress(Bootstrap serverBootstrap){
		ChannelFuture channelFuture;
		try {
			ProtocalStrategyCache.protocalServerCache.put(pId, this);
			System.out.println("网关服务端已启动！！");
			channelFuture = serverBootstrap.bind(Integer.parseInt(serverPort)).sync().channel().closeFuture().await();
			
			
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}finally{
			boss.shutdownGracefully();
		}
	}
	/**
	 * 关闭服务
	 */
	public void close(){
		CommonUtil.closeEventLoop(boss);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		ProtocalStrategyCache.protocalServerCache.remove(pId);
	}
	
	
}
