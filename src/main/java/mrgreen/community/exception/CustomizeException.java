package mrgreen.community.exception;

import javax.xml.bind.PrintConversionEvent;

/**
 * @author: mrgreen
 * @date: 2020/4/2
 * @description:
 */

public class CustomizeException extends RuntimeException {
    private String message;

    public CustomizeException(ICustomizeErrorCode errorCode){
        this.message = errorCode.getMessage();
    }

    public CustomizeException(String message){
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
