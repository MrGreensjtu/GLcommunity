package mrgreen.community.dto;

import lombok.Data;

/**
 * @author: mrgreen
 * @date: 2020/3/20
 * @description:
 */

@Data
public class AccessTokenDTO {
    private String client_id;
    private String client_secret;
    private String code;
    private String redirect_url;
    private String state;
}
