package com.xiaojukeji.carrera.consumer.examples;

import com.xiaojukeji.carrera.consumer.thrift.Context;
import com.xiaojukeji.carrera.consumer.thrift.Message;
import com.xiaojukeji.carrera.consumer.thrift.client.CarreraConfig;
import com.xiaojukeji.carrera.consumer.thrift.client.CarreraConsumer;
import com.xiaojukeji.carrera.consumer.thrift.client.MessageProcessor;
import com.xiaojukeji.carrera.sd.Env;
import org.apache.logging.log4j.LogManager;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CarreraConsumerExampleTest1 {
    private static final Logger LOGGER = LoggerFactory.getLogger(CarreraConsumerExampleTest1.class);

    private static final String groupName = "cg_whb_test";

    public static void simpleExample(long consumeTime) throws InterruptedException {
        //CarreraConfig config = getConfig("cg_your_groupName", "127.0.0.1:9713");
        CarreraConfig config = getSDConfig();

        final CarreraConsumer consumer = new CarreraConsumer(config);

        consumer.startConsume(new MessageProcessor() {
            @Override
            public Result process(Message message, Context context) {
                LOGGER.info("收到如下消息：{}",message.value); //收到如下消息：java.nio.HeapByteBuffer[pos=83 lim=109 cap=139]
                LOGGER.info("process key:{}, value.length:{}, offset:{}, context:{}", message.getKey(),
                        message.getValue().length, message.getOffset(), context);
                return Result.SUCCESS;
            }
        }, 5); // 每台server有2个线程，额外的一个随机分配。

        Thread.sleep(consumeTime); // consume for 10 seconds
        consumer.stop(); //1分钟便于测试，可以注释掉
    }

    public static void main(String[] args) throws TTransportException, InterruptedException {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                LogManager.shutdown(); //shutdown log4j2.
            }
        }));

        LOGGER.info("start simpleExample...");
        simpleExample(60 * 1000);

    }

    // 不使用服务发现
    public static CarreraConfig getConfig(String groupName, String ipList) {
        CarreraConfig config = new CarreraConfig(groupName, ipList);
        config.setRetryInterval(500); // 拉取消息重试的间隔时间，单位ms。
        config.setMaxBatchSize(8);  //一次拉取的消息数量。
        return config;
    }

    // 使用服务发现
    private static CarreraConfig getSDConfig() {
        CarreraConfig config = new CarreraConfig(groupName, Env.valueOf("Test"));
        config.setRetryInterval(500); // 拉取消息重试的间隔时间，单位ms。
        config.setMaxBatchSize(8);  //一次拉取的消息数量。
        return config;
    }
}