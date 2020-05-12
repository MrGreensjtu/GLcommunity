package mrgreen.community.service;

import mrgreen.community.dto.NotificationDTO;
import mrgreen.community.dto.PageDTO;
import mrgreen.community.enums.NotificationStatusEnum;
import mrgreen.community.enums.NotificationTypeEnum;
import mrgreen.community.exception.CustomizeErrorCode;
import mrgreen.community.exception.CustomizeException;
import mrgreen.community.mapper.NotificationMapper;
import mrgreen.community.mapper.UserMapper;
import mrgreen.community.model.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: mrgreen
 * @date: 2020/5/8
 * @description:
 */

@Service
public class NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private UserMapper userMapper;

    public PageDTO<NotificationDTO> list(Long userId, Integer offset, Integer limit) {
        PageDTO<NotificationDTO> pageDTO = new PageDTO<>();
        NotificationExample example = new NotificationExample();
        example.createCriteria()
                .andReceiverEqualTo(userId);
        Integer totalCount = (int) notificationMapper.countByExample(example);
        pageDTO.setPagesInfo(totalCount, offset, limit);
        if (offset < 1){
            offset = 1;
        }
        if (offset >= pageDTO.getTotalPage()){
            offset = pageDTO.getTotalPage();
        }
        Integer page = limit*(offset-1);
        NotificationExample example1 = new NotificationExample();
        example1.createCriteria()
                .andReceiverEqualTo(userId);
        example1.setOrderByClause("gmt_create desc");
        List<Notification> notifications = notificationMapper.selectByExampleWithRowbounds(example1, new RowBounds(page, limit));

        if(notifications.size() == 0){
            return  pageDTO;
        }

        List<NotificationDTO> notificationDTOList = new ArrayList<>();
        for (Notification notification : notifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification, notificationDTO);
            notificationDTO.setTypeName(NotificationTypeEnum.nameOf(notification.getType()));
            notificationDTOList.add(notificationDTO);
        }

//        List<Long> userIds = notifications.stream().map(notifier -> notifier.getNotifier()).distinct().collect(Collectors.toList());
//        UserExample userExample = new UserExample();
//        userExample.createCriteria()
//                .andIdIn(userIds);
//        List<User> users = userMapper.selectByExample(userExample);
//        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));

        pageDTO.setData(notificationDTOList);
        return pageDTO;
    }


    public NotificationDTO read(Long notificationId, User user) {
        Notification notification = notificationMapper.selectByPrimaryKey(notificationId);
        if(notification == null){
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        if(!notification.getReceiver().equals(user.getId())){
            throw new CustomizeException(CustomizeErrorCode.READ_OTHERS_NOTIFICATION);
        }
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKey(notification);
        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification, notificationDTO);
        notificationDTO.setTypeName(NotificationTypeEnum.nameOf(notification.getType()));
        return notificationDTO;
    }
}
