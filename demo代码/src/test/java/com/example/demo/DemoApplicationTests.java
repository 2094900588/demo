package com.example.demo;

import com.example.demo.entity.Person;
import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    private StringRedisTemplate redisTemplate;

    public static AtomicInteger num = new AtomicInteger(0);

    @Autowired
    MongoTemplate mongoTemplate;

    @Test
    void contextLoads() {

//        int total = 10000000;
//        BloomFilter<CharSequence> bf = BloomFilter.create(Funnels.stringFunnel(Charsets.UTF_8),total);
//        for(int i=0;i<total;i++){
//            bf.put(""+i);
//        }
//
//        int count = 0;
//        for(int i=total;i<total*2;i++){
//            if(bf.mightContain(""+i)){
//                count++;
//            }
//        }
//        System.out.println(count);

//        Criteria criteria = Criteria.where("age").lt(30).and("person_name").is("张三");
//
//        Query query = new Query(criteria);
//        List<Person> list = mongoTemplate.find(query,Person.class);

        Person person = new Person();
        person.setName("张三");
        person.setAge(18);
        person.setAddress("666");
        mongoTemplate.save(person);

//        Runnable runnable = () -> {
//            for (long i = 0L; i < 1000000000; i++) {
//                num.getAndAdd(1);
//            }
//            System.out.println(Thread.currentThread().getName() + "执行结束!");
//        };


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
