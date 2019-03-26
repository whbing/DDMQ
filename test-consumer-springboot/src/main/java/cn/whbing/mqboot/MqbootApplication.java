package cn.whbing.mqboot;

import cn.whbing.mqboot.test.CarreraConsumerExampleTest1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MqbootApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(CarreraConsumerExampleTest1.class);

    public static void main(String[] args) {
        SpringApplication.run(MqbootApplication.class, args);
        LOGGER.info("启动测试成功");
    }

}
