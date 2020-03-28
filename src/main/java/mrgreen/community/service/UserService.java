package mrgreen.community.service;

import mrgreen.community.mapper.UserMapper;
import mrgreen.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: mrgreen
 * @date: 2020/3/28
 * @description:
 */

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public void createOrUpdate(User user) {
        User dbUser = userMapper.findByAccountId(user.getAccountId());
        if (dbUser == null) {
            //插入
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        }else{
            //更新
            dbUser.setGmtModified(System.currentTimeMillis());
            dbUser.setName(user.getName());
            dbUser.setToken(user.getToken());
            dbUser.setAvatarUrl(user.getAvatarUrl());
            userMapper.update(dbUser);
        }
    }
}
