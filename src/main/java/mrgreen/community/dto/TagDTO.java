package mrgreen.community.dto;

import lombok.Data;

import java.util.List;

/**
 * @author: mrgreen
 * @date: 2020/4/11
 * @description:
 */
@Data
public class TagDTO {
    private String categoryName;
    private List<String> tags;
}
