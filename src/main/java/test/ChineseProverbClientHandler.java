package test;


import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

public class ChineseProverbClientHandler extends
	SimpleChannelInboundHandler<DatagramPacket> {//处理收到的消息

    @Override
    public void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg)
	    throws Exception {
		String response = ByteBufUtil.hexDump(msg.content());//将收到的数据转成16进制
		
	    System.out.println("收到响应数据"+response+";index = " + CountHelper.clientRecieveCount.addAndGet(1));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
	    throws Exception {//异常处理
		cause.printStackTrace();//打印异常信息
		ctx.close();//发生异常，关闭连接
    }
}
