package com.abit.domain.service.impl;

import com.abit.domain.entity.User;
import com.abit.domain.mapper.UserMapper;
import com.abit.domain.service.IUserService;
import com.abit.expand.MyLambdaQueryWrapper;
import com.abit.expand.MyPage;
import com.abit.frame.annotation.RedisTranStartPoint;
import com.abit.redis.impl.ServiceWithRedisImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl extends ServiceWithRedisImpl<UserMapper, User> implements IUserService {

    @RedisTranStartPoint
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void testTran(long id){


        User byId = getById(id,true);

        User update = new User();
        update.setId(byId.getId());
        update.setName("15007582652");
        updateById(update);

        byId = getById(id,true);
        System.out.println(byId);

        removeById(id);

        byId = getById(id,true);
        System.out.println(byId);

        MyLambdaQueryWrapper<User> wrapper = new MyLambdaQueryWrapper<>();
        wrapper.eq(User::getName,"15007582652");
        MyPage<User> myPage = page(wrapper, 1, 10, true);
        System.out.println(myPage.getRecords());

    }
}

