package com.example.demo.service.impl;

import com.example.demo.service.SecKillService;
import org.apache.ibatis.transaction.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SerKillServiceImpl implements SecKillService {

    @Autowired
    private StringRedisTemplate redisTemplate;


    @Override
    public boolean doSecKill(String uid, String pid) {
        // 1 非空判断
        if(uid == null || pid == null){
            return false;
        }
        // 2 连接redis

        // 3 拼接key
        // 3.1 库存key
        String kcKey = "sk:"+pid+":qt";
        // 3.2 秒杀成功用户key
        String userKey = "sk:"+pid+":user";

//        // 开启redis事务支持
//        redisTemplate.setEnableTransactionSupport(true);
//
//        // 监视库存
//        redisTemplate.watch(kcKey);
        // 4 获取库存 如果库存null 秒杀还没开始
//        String kc = redisTemplate.opsForValue().get(kcKey);
//        if(kc == null){
//            System.out.println("秒杀还没有开始");
//            return false;
//        }
//
//
//        // 5 判断用户是否重复秒杀
//        if(Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(userKey, uid))){
//            System.out.println("已经秒杀成功了，不能重复秒杀");
//            return false;
//        }
//
//        // 6 判断如果商品数量，库存数量小于1，秒杀结束
//        if(Integer.parseInt(kc) <= 0){
//            System.out.println("秒杀已经结束");
//            return false;
//        }
//
//
//        // 7 秒杀过程
//        redisTemplate.multi();
//        // 7.1 秒杀库存-1
//        redisTemplate.opsForValue().decrement(kcKey);
//
//        // 7.2 秒杀成功用户添加到清单
//        redisTemplate.opsForSet().add(userKey,uid);
//
//        List<Object> results = redisTemplate.exec();

        List<Object> results = redisTemplate.execute(new SessionCallback<List<Object>>() {
            // 乐观锁，淘汰用户 存在的问题：库存遗留问题
            public List<Object> execute(RedisOperations operations) throws DataAccessException {
                operations.watch(kcKey);
                // 4 获取库存 如果库存null 秒杀还没开始
                String kc = (String) operations.opsForValue().get(kcKey);
                if(kc == null){
//                    System.out.println("秒杀还没有开始");
                    return null;
                }
                // 5 判断用户是否重复秒杀
                if(Boolean.TRUE.equals(operations.opsForSet().isMember(userKey, uid))){
//                    System.out.println("已经秒杀成功了，不能重复秒杀");
                    return null;
                }

                // 6 判断如果商品数量，库存数量小于1，秒杀结束
                if(Integer.parseInt(kc) <= 0){
//                    System.out.println("秒杀已经结束");
                    return null;
                }
                // 7 秒杀过程
                operations.multi();
                // 7.1 秒杀库存-1
                operations.opsForValue().decrement(kcKey);
                // 7.2 秒杀成功用户添加到清单
                operations.opsForSet().add(userKey,uid);
                return operations.exec();
            }
        });



        if(results == null || results.size() == 0){
//            System.out.println("秒杀失败");
            return false;
        }

//        System.out.println("秒杀成功！");


        return true;
    }
}
