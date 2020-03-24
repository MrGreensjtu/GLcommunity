package mrgreen.community.dto;

import lombok.Data;

/**
 * @author: mrgreen
 * @date: 2020/3/20
 * @description:
 */

@Data
public class GithubUserDTO {
    private String name;
    private Long id;
    private String bio;
    private String avatar_url;
}
