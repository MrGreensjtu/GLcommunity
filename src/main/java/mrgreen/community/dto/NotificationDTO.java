package mrgreen.community.dto;

import lombok.Data;


/**
 * @author: mrgreen
 * @date: 2020/5/7
 * @description:
 */

@Data
public class NotificationDTO {
    private Long id;
    private Long gmtCreate;
    private Integer status;
    private Long notifier;
    private String notifierName;
    private Long outer;
    private String outerTitle;
    private Integer type;
    private String typeName;
}
