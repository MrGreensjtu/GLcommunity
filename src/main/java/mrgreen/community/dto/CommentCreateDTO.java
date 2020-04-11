package mrgreen.community.dto;

import lombok.Data;

/**
 * @author: mrgreen
 * @date: 2020/4/3
 * @description:
 */

@Data
public class CommentCreateDTO {
    private Long parentId;
    private String content;
    private Integer type;
}
