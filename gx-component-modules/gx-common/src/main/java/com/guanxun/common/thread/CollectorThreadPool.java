package com.guanxun.common.thread;


import org.slf4j.*;

import java.util.concurrent.*;

/**
 * 线程池类
 * @author zhangk
 *
 */
public class CollectorThreadPool{
	private static Logger log = LoggerFactory.getLogger(CollectorThreadPool.class);
	
	private static Executor executor = null;
	//线程池缓冲队列
	private static BlockingQueue<Runnable> workQueue = null;
	//当线程池中的线程数目达到corePoolSize后，就会把到达的任务放到缓存队列当中
	private static final int QUEUESIZE = 10000;
	
	private static final int COREPOOLSIZE = 1000;
	
	private static final int MAXPOOLSIZE = 1000;//
	
	private static synchronized void init(){
		log.info("初始化线程池对象");
		if(workQueue  == null){ 
			workQueue = new ArrayBlockingQueue<Runnable>(QUEUESIZE);
		}
		if(executor == null){
			executor = new ThreadPoolExecutor(COREPOOLSIZE, MAXPOOLSIZE, 60, TimeUnit.SECONDS, workQueue);
		}		
	}
	public static void execute(Runnable runnable){
		if(executor == null || workQueue == null){
			init();
		}
		if(workQueue.size() >= 4500){
			try {
				log.error("线程池队列达到90%");
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				log.error("线程池异常",e);
			}
		}
		executor.execute(runnable);
	}

	public int getWorkQueueSize(){
		if(workQueue != null) {
			return workQueue.size();
		}
		return 0;
	}
}
