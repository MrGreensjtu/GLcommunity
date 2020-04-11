package mrgreen.community.exception;

import javax.xml.bind.PrintConversionEvent;

/**
 * @author: mrgreen
 * @date: 2020/4/2
 * @description:
 */

public class CustomizeException extends RuntimeException {
    private String message;
    private Integer code;

    public CustomizeException(ICustomizeErrorCode errorCode) {
        this.message = errorCode.getMessage();
        this.code = errorCode.getCode();
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
