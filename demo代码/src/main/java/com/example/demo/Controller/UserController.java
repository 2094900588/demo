package com.example.demo.Controller;

import com.example.demo.common.Constants;
import com.example.demo.common.Result;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.SecKillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final ReentrantLock lock = new ReentrantLock();
    @Autowired
    UserMapper userMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    SecKillService secKillService;

    @GetMapping("/{id}/{money}")
    public Result buyprod(@PathVariable Integer id, @PathVariable Integer money) {

        User user = userMapper.selectById(id);
        if (user.getMoney() - money >= 0) {
            lock.lock();
            try {
                user.setMoney(user.getMoney() - money);
                user.setTimes(user.getTimes() + 1);
                userMapper.updateById(user);
                System.out.println("买！");
            } finally {
                lock.unlock();
            }
            return Result.success();
        } else {
            return Result.error(Constants.CODE_600, "余额不足");
        }
    }

    @GetMapping("seckill/{prodid}")
    public Result seckill(@PathVariable String prodid) {
        String userid = new Random().nextInt(50000) + "";
        boolean isSuccess = secKillService.doSecKill(userid, prodid);
        return Result.success(isSuccess);
    }

    @GetMapping("limitFlow") // zset实现限流 缺点:会使zset越来越多
    public Result LimitFlow() {
        int intervalTime = 60000; // 单位：毫秒
        Long currentTime = new Date().getTime();
        System.out.println(currentTime);
        if (Boolean.TRUE.equals(redisTemplate.hasKey("limit"))) {
            Integer count = redisTemplate.opsForZSet().rangeByScore("limit", currentTime - intervalTime, currentTime)
                    .size();// intervalTime是限流的时间
            System.out.println(count);

            if (count != null && count > 5) {
                return Result.success("每分钟最多只能访问5次");
            }
        }
        redisTemplate.opsForZSet().add("limit", UUID.randomUUID().toString(), currentTime);

        return Result.success("访问成功");
    }



}

/*
 * 4827次，一次买77 可以买次
 *
 */