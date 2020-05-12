package mrgreen.community.interceptor;

import mrgreen.community.enums.NotificationStatusEnum;
import mrgreen.community.mapper.NotificationMapper;
import mrgreen.community.mapper.UserMapper;
import mrgreen.community.model.NotificationExample;
import mrgreen.community.model.User;
import mrgreen.community.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author: mrgreen
 * @date: 2020/3/26
 * @description:
 */
@Service
public class SessionInterceptor implements HandlerInterceptor {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private NotificationMapper notificationMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    UserExample userExample = new UserExample();
                    userExample.createCriteria()
                            .andTokenEqualTo(token);
                    List<User> users = userMapper.selectByExample(userExample);
                    if (users.size() != 0) {
                        request.getSession().setAttribute("user", users.get(0));
                        NotificationExample notificationExample = new NotificationExample();
                        notificationExample.createCriteria()
                                .andReceiverEqualTo(users.get(0).getId()).andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
                        long unreadNotificationCount = notificationMapper.countByExample(notificationExample);
                        request.getSession().setAttribute("unreadNotificationCount", unreadNotificationCount);
                    }
                    break;
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
