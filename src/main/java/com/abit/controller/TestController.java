package com.abit.controller;

import com.abit.expand.MyLambdaQueryWrapper;
import com.abit.expand.MyPage;
import com.abit.domain.entity.User;
import com.abit.domain.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private IUserService userService;


    @GetMapping("/list")
    public List<User> getList(){
        Integer page = 0;
        Integer size = 100;
        MyLambdaQueryWrapper<User> wrapper = new MyLambdaQueryWrapper<>();
        wrapper.eq(User::getName, "李明").orderByAsc(User::getCreateTime);
        MyPage<User> userIPage = userService.page(wrapper,page,size,true,":name:李明");
        return userIPage.getRecords();
    }

}
