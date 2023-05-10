package com.example.demo.Controller;


import com.example.demo.common.Constants;
import com.example.demo.common.Result;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.locks.ReentrantLock;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final ReentrantLock lock = new ReentrantLock();
    @Autowired
    UserMapper userMapper;

    @GetMapping("/{id}/{money}")
    public Result buyprod(@PathVariable Integer id,@PathVariable Integer money) {

            User user = userMapper.selectById(id);
            if(user.getMoney() - money >= 0){
                lock.lock();
                try {
                    user.setMoney(user.getMoney() - money);
                    user.setTimes(user.getTimes()+1);
                    userMapper.updateById(user);
                    System.out.println("买！");
                } finally {
                    lock.unlock();
                }
                return Result.success();
            } else {
                return Result.error(Constants.CODE_600,"余额不足");
            }


    }
}

/*
* 4827次，一次买77 可以买次
*
* */