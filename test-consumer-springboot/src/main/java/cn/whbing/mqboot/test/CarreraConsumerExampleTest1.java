package cn.whbing.mqboot.test;

import com.xiaojukeji.carrera.consumer.thrift.Context;
import com.xiaojukeji.carrera.consumer.thrift.Message;
import com.xiaojukeji.carrera.consumer.thrift.client.CarreraConfig;
import com.xiaojukeji.carrera.consumer.thrift.client.CarreraConsumer;
import com.xiaojukeji.carrera.consumer.thrift.client.MessageProcessor;
import com.xiaojukeji.carrera.sd.Env;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

@SpringBootConfiguration
public class CarreraConsumerExampleTest1 {
    private static final Logger LOGGER = LoggerFactory.getLogger(CarreraConsumerExampleTest1.class);

    private  static String groupName = "cg_whb_test";
    private  String topic1 = "whb_test_topic1";
    private  String topic2 = "whb_test_topic2";


    @Bean
    public CarreraConsumer getCarreraConsumer() {
        CarreraConfig config = getSDConfig();

        //指定iplist方式,服务发现方式不满足时，再用此方式
        //CarreraConfig config = getNoSDConfig("cg_capricornus_client", "10.95.121.43:9713;10.95.137.66:9713");
        CarreraConsumer carreraConsumer = new CarreraConsumer(config);

        //指定某topic，消费进程
        Map<String/*Topic*/, Integer/*该topic额外的线程数*/> extraThreads = new HashMap<>();
        extraThreads.put(topic1, 1);

        //该配置下有6个消费线程，有4个消费其他topic， 另外2个线程只消费jms_dpp_task_rerun_apply.
        carreraConsumer.startConsume(new MessageProcessor() {
            @Override
            public Result process(Message message, Context context) {
                LOGGER.info("测试1收到如下消息：{}",message.value); //收到如下消息：java.nio.HeapByteBuffer[pos=83 lim=109 cap=139]
                LOGGER.info("process key:{}, value.length:{}, offset:{}, context:{}", message.getKey(),
                        message.getValue().length, message.getOffset(), context);
                return Result.SUCCESS;
            }
        }, 0,extraThreads); // 每台server有2个线程，额外的一个随机分配。

        return carreraConsumer;
    }

    @Bean
    public CarreraConsumer getCarreraConsumer2() {
        CarreraConfig config = getSDConfig();

        //指定iplist方式,服务发现方式不满足时，再用此方式
        //CarreraConfig config = getNoSDConfig("cg_capricornus_client", "10.95.121.43:9713;10.95.137.66:9713");
        CarreraConsumer carreraConsumer = new CarreraConsumer(config);

        //指定某topic，消费进程
        Map<String/*Topic*/, Integer/*该topic额外的线程数*/> extraThreads = new HashMap<>();
        extraThreads.put(topic2, 1);

        //该配置下有6个消费线程，有4个消费其他topic， 另外2个线程只消费jms_dpp_task_rerun_apply.
        carreraConsumer.startConsume(new MessageProcessor() {
            @Override
            public Result process(Message message, Context context) {
                LOGGER.info("测试2收到如下消息：{}",message.value); //收到如下消息：java.nio.HeapByteBuffer[pos=83 lim=109 cap=139]
                LOGGER.info("process key:{}, value.length:{}, offset:{}, context:{}", message.getKey(),
                        message.getValue().length, message.getOffset(), context);
                return Result.SUCCESS;
            }
        }, 0,extraThreads); // 每台server有2个线程，额外的一个随机分配。

        return carreraConsumer;
    }

    @Bean
    public String test(){
        System.out.println("测试测试测试");
        return "返回结果";
    }

    // 使用服务发现
    private static CarreraConfig getSDConfig() {
        CarreraConfig config = new CarreraConfig(groupName, Env.valueOf("Test"));
        config.setRetryInterval(500); // 拉取消息重试的间隔时间，单位ms。
        config.setMaxBatchSize(8);  //一次拉取的消息数量。
        return config;
    }
}