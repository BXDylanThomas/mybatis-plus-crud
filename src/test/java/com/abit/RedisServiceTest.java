package com.abit;

import com.abit.redis.impl.RedisBaseService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
public class RedisServiceTest extends BaseServiceTest {

    @Autowired
    private RedisBaseService redisBaseService;

    @Test
    public void setNx(){
        IntStream.rangeClosed(1,1000000).forEach( id -> {
            if (redisBaseService.setNx("key","val",10)){
                System.out.println("用户(" + id + ")获取锁, 时间 :" + new Date().toLocaleString());
            }
        });
    }

    @Test
    public void set(){
        boolean result = redisBaseService.set("key", Arrays.asList(1, 2, 3, 4, 5), 150, false);
        System.out.println(result);
        List<Integer> list  = (List<Integer>)redisBaseService.get("key");
        System.out.println(list);
    }


}