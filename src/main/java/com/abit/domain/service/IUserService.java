package com.abit.domain.service;

import com.abit.expand.MyLambdaQueryWrapper;
import com.abit.expand.MyPage;
import com.abit.domain.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;


public interface IUserService extends IService<User> {

    User getById(Serializable id, boolean isUseCache);

    MyPage<User> page(User user, Integer page, Integer size, boolean isUseCache);

    MyPage<User> page(User user, Integer page, Integer size, boolean isUseCache, String dimension);

    MyPage<User> page(MyLambdaQueryWrapper<User> wrapper, Integer page, Integer size, boolean isUseCache);

    MyPage<User> page(MyLambdaQueryWrapper<User> wrapper, Integer page, Integer size, boolean isUseCache, String dimension);

    void delAllPageCache();

    void delDimensionPageCache(String dimension);

    void testTran(long id);
}
