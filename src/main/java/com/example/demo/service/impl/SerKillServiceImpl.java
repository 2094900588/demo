package com.example.demo.service.impl;

import com.example.demo.service.SecKillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;


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

        // 4 获取库存 如果库存null 秒杀还没开始
        String kc = redisTemplate.opsForValue().get(kcKey);
        if(kc == null){
            System.out.println("秒杀还没有开始");
            return false;
        }




        // 5 判断用户是否重复秒杀

        // 6 判断如果商品数量，库存数量小于1，秒杀结束

        // 7 秒杀过程
        // 7.1 秒杀库存-1
        // 7.2 秒杀成功用户添加到清单



        return true;
    }
}
