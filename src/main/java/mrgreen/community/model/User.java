package mrgreen.community.model;

import lombok.Data;

/**
 * @author: mrgreen
 * @date: 2020/3/22
 * @description:
 */

@Data
public class User {
    private Integer id;
    private String name;
    private String accountId;
    private String token;
    private Long gmtCreate;
    private Long gmtModified;
    private String avatar_url;
}
