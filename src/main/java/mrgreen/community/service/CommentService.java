package mrgreen.community.service;

import mrgreen.community.dto.CommentDTO;
import mrgreen.community.enums.CommentTypeEnum;
import mrgreen.community.enums.NotificationStatusEnum;
import mrgreen.community.enums.NotificationTypeEnum;
import mrgreen.community.exception.CustomizeErrorCode;
import mrgreen.community.exception.CustomizeException;
import mrgreen.community.mapper.*;
import mrgreen.community.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: mrgreen
 * @date: 2020/4/3
 * @description:
 */

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionExtMapper questionExtMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CommentExtMapper commentExtMapper;
    @Autowired
    private NotificationMapper notificationMapper;

    @Transactional
    public void insert(Comment comment, User commentator) {
        if (comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }

        if (comment.getType() == null || !CommentTypeEnum.isExitst(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }


        if (comment.getType() == CommentTypeEnum.COMMENT.getType()) {
            //回复评论
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (dbComment == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }

            Question question = questionMapper.selectByPrimaryKey(dbComment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }

            commentMapper.insert(comment);

            //增加子评论数
            dbComment.setSubCommentCount(1);
            commentExtMapper.incSubComment(dbComment);

            //插入通知内容
            createNotify(comment, dbComment.getCommentator(), question.getTitle(), commentator.getName(),
                    NotificationTypeEnum.REPLY_COMMENT, question.getId());

        } else {
            //回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            //新回复初始化子评论数为0
            comment.setSubCommentCount(0);
            commentMapper.insert(comment);
            Question record = new Question();
            record.setId(question.getId());
            record.setCommentCount(1);
            questionExtMapper.incComment(record);

            //插入通知内容
            createNotify(comment, question.getCreator(), question.getTitle(), commentator.getName(),
                    NotificationTypeEnum.REPLY_QUESTION, question.getId());
        }
    }

    private void createNotify(Comment comment, Long receiver, String outerTitle, String notifierName, NotificationTypeEnum notificationType, Long outer) {
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setType(notificationType.getType());
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setOuter(outer);
        notification.setNotifier(comment.getCommentator());
        notification.setReceiver(receiver);
        notification.setNotifierName(notifierName);
        notification.setOuterTitle(outerTitle);
        notificationMapper.insert(notification);
    }

    public List<CommentDTO> listByTargetId(Long id, CommentTypeEnum type) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andParentIdEqualTo(id)
                .andTypeEqualTo(type.getType());
        commentExample.setOrderByClause("gmt_create desc");
        List<Comment> commentList = commentMapper.selectByExample(commentExample);
        if (commentList.size() == 0) {
            return new ArrayList<>();
        } else {
            /*使用JDk8新特性Stream获取去重评论人信息*/
            List<Long> commentator =
                    commentList.stream().map(comment -> comment.getCommentator()).distinct().collect(Collectors.toList());
            UserExample userExample = new UserExample();
            userExample.createCriteria()
                    .andIdIn(commentator);
            List<User> userList = userMapper.selectByExample(userExample);
            Map<Long, User> userMap = userList.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));
            List<CommentDTO> commentDTOS = commentList.stream().map(comment -> {
                CommentDTO commentDTO = new CommentDTO();
                BeanUtils.copyProperties(comment, commentDTO);
                commentDTO.setUser(userMap.get(comment.getCommentator()));
                return commentDTO;
            }).collect(Collectors.toList());
            return commentDTOS;
        }
    }
}
