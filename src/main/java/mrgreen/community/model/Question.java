package mrgreen.community.model;

import lombok.Data;

/**
 * @author: mrgreen
 * @date: 2020/3/23
 * @description:
 */

@Data
public class Question {
    private Integer id;
    private String title;
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer creator;
    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;
}
