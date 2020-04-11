package mrgreen.community.dto;

import lombok.Data;
import mrgreen.community.model.User;

/**
 * @author: mrgreen
 * @date: 2020/3/24
 * @description:
 */

@Data
public class QuestionDTO {
    private Long id;
    private String title;
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private Long creator;
    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;
    private User user;
}
