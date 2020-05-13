package mrgreen.community.dto;

import lombok.Data;

/**
 * @author: mrgreen
 * @date: 2020/5/13
 * @description:
 */

@Data
public class QuestionQueryDTO {
    private Integer page;
    private Integer limit;
    private String search;
}
