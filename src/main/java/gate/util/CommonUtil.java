package gate.util;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.EventLoopGroup;
import io.netty.util.ReferenceCountUtil;

public class CommonUtil {
	/**
	 * 全量ip地址与Data分隔符
	 */
	public static final String ipDataSplit = "$"; 
	/**
	 * 网关编号
	 */
	public static int gateNum ;
	
	/**
	 * 使用池化的直接内存ByteBuf
	 */
	private static PooledByteBufAllocator allocator;
	/**
	 * 计数
	 */
	public static AtomicInteger recieveCount ;
	
	public static final String OS_NAME = System.getProperty("os.name");

    private static boolean isLinuxPlatform = false;
    private static boolean isWindowsPlatform = false;
	
	static{
		recieveCount = new AtomicInteger(0);
		allocator = PooledByteBufAllocator.DEFAULT;
		if (OS_NAME != null && OS_NAME.toLowerCase().indexOf("linux") >= 0) {
            isLinuxPlatform = true;
        }
        if (OS_NAME != null && OS_NAME.toLowerCase().indexOf("windows") >= 0) {
            isWindowsPlatform = true;
        }
	}
	
	public static boolean isLinuxPlatform() {
        return isLinuxPlatform;
    }


    public static boolean isWindowsPlatform() {
        return isWindowsPlatform;
    }
	
	/**
	 * 从直接内存池中获取ByteBuf
	 * @return
	 */
	public static ByteBuf getDirectByteBuf(){
		return allocator.buffer();
	}
	
	/**
	 * 释放ByteBuf
	 * @param buf
	 */
	public static void releaseByteBuf(ByteBuf buf){
		if(buf != null ){
			ReferenceCountUtil.release(buf);
		}
		
	}
	
	/**
	 * 关闭EventLoopGroup
	 * @param group
	 */
	public static void closeEventLoop(EventLoopGroup...  group ){
		for (EventLoopGroup eventLoopGroup : group) {
			eventLoopGroup.shutdownGracefully();
		}
	}
	
	
	public static Options buildCommandlineOptions(final Options options) {
		
		Option opt = new Option("n", true, "gate num");
        opt.setRequired(true);
        options.addOption(opt);
		
        opt = new Option("f", true, "cache file url: eg win:'D:\\iotGate.conf'  ; linux: '/gate/iotGate.conf'");
        opt.setRequired(true);
        options.addOption(opt);
        
        opt = new Option("h", false, "help info");
        opt.setRequired(false);
        options.addOption(opt);
        

        return options;
    }
	/**
	 * Reference from RocketMQ
	 * @param appName
	 * @param args
	 * @param options
	 * @param parser
	 * @return
	 */
	 public static CommandLine parseCmdLine(final String appName, String[] args, Options options,
	            CommandLineParser parser) {
	        HelpFormatter hf = new HelpFormatter();
	        hf.setWidth(110);
	        CommandLine commandLine = null;
	        try {
	            commandLine = parser.parse(options, args);
	            if (commandLine.hasOption('h')) {
	                hf.printHelp(appName, options, true);
	                System.out.println("-h参数以及成功被捕获");
	                return null;
	            }
	        }
	        catch (ParseException e) {
	            hf.printHelp(appName, options, true);
	        }

	        return commandLine;
	    }
}
