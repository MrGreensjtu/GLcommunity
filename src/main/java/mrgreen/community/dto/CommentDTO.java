package mrgreen.community.dto;

import lombok.Data;
import mrgreen.community.model.User;

import javax.swing.*;

/**
 * @author: mrgreen
 * @date: 2020/4/7
 * @description:
 */
@Data
public class CommentDTO {
    private Long id;
    private Long parentId;
    private String content;
    private Integer type;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer likeCount;
    private Integer subCommentCount;
    private Long commentator;
    private User user;
}
