package com.example.demo;

import com.baomidou.mybatisplus.core.toolkit.SerializationUtils;
import com.example.demo.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    private StringRedisTemplate redisTemplate;

    public static AtomicInteger num = new AtomicInteger(0);

    @Test
    void contextLoads() {

        User user = new User();
        user.setUsername("lyh");
        User user1 = SerializationUtils.clone(user);
        user1.setUsername("whb");
        System.out.println(user.getUsername());
        System.out.println(user1.getUsername());


//        Runnable runnable = () -> {
//            for (long i = 0L; i < 1000000000; i++) {
//                num.getAndAdd(1);
//            }
//            System.out.println(Thread.currentThread().getName() + "执行结束!");
//        };
//
//        Thread t1 = new Thread(runnable);
//        Thread t2 = new Thread(runnable);
//        t1.start();
//        t2.start();
//        Thread.sleep(1000);
//        System.out.println("num=" + num);

//        redisTemplate.opsForValue().set("test", "6666");
//        String test = redisTemplate.opsForValue().get("test");
//        System.out.println(test);
    }

}
