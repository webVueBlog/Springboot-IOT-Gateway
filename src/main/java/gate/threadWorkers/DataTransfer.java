package gate.threadWorkers;

/**
 * 数据传输接口
 */
public interface DataTransfer extends Runnable {
	void start()  throws Exception ;
}
