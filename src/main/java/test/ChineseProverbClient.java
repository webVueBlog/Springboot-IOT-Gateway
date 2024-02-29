package test;



import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.net.InetSocketAddress;
public class ChineseProverbClient { //作用是向服务端发送消息

    public void run(int port) throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();//创建一个事件循环组
		try {
		    Bootstrap b = new Bootstrap();//创建一个Bootstrap对象
			// Bootstrap是客户端启动类
			// NioDatagramChannel是客户端的通道实现类
			// option是客户端连接参数设置方法
			// ChannelOption.SO_BROADCAST
		    // 表示客户端的channel为广播类型
		    // handler是客户端事件处理器
		    // 这里我们用的是匿名内部类，也可以new一个 ChineseProverbClientHandler
		    b.group(group).channel(NioDatagramChannel.class)
			    .option(ChannelOption.SO_BROADCAST, true)
			    .handler(new ChineseProverbClientHandler());
		    Channel ch = b.bind(0).sync().channel();// 绑定一个本地端口
		    for(int i = 0 ; i< 10000 ; i++){//发送10000个消息
				// DatagramPacket
				// 是UDP协议的包，包含发送人的地址信息
				// ByteBufUtil.decodeHexDump表示
				// 消息内容，是一个16进制的字符串
		    	ch.writeAndFlush(//发送消息
		    		    new DatagramPacket(Unpooled.wrappedBuffer(ByteBufUtil.decodeHexDump("40402300010102090E0C0C129FB00100000000000000000004000219010101DA2323")), new InetSocketAddress(
		    			    "127.0.0.1", port))).sync().await();//发送消息并等待发送完成
		    	System.out.println("发送消息完成!");
		    }
		    Thread.sleep(Integer.MAX_VALUE);//等待接收完成
			// closeFuture
			// 表示服务端关闭的监听器
		    // await 表示等待接收完成
		    if (!ch.closeFuture().await(15000)) {//等待15秒接收完成
		    	System.out.println("查询超时!");
		    }
		    
		} finally {
		    group.shutdownGracefully();//关闭线程组
		}
    }

    public static void main(String[] args) throws Exception {// 入口方法
		int port = 9811;// 端口号
		if (args.length > 0) {// 如果有参数
		    try {
			port = Integer.parseInt(args[0]);// 转换成整数
		    } catch (NumberFormatException e) {
			e.printStackTrace();// 输出异常信息
		    }
		}
		new ChineseProverbClient().run(port);// 运行客户端
    }
}
