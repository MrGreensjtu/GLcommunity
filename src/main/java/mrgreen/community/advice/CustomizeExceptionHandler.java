package mrgreen.community.advice;

import mrgreen.community.dto.ResultDTO;
import mrgreen.community.exception.CustomizeErrorCode;
import mrgreen.community.exception.CustomizeException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: mrgreen
 * @date: 2020/4/2
 * @description:
 */

@ControllerAdvice
public class CustomizeExceptionHandler {
    @ExceptionHandler(Exception.class)
    Object handle(HttpServletRequest request, Throwable e, Model model) {
        String contentType = request.getContentType();
        if("application/json".equals(contentType)){
            //返回JSON
            if(e instanceof CustomizeException){
                return ResultDTO.errorOf((CustomizeException) e);
            }else{
                model.addAttribute(CustomizeErrorCode.SYSTEM_ERROR);
            }
            return null;
        }else{
            //返回页面跳转
            if(e instanceof CustomizeException){
                model.addAttribute("message", e.getMessage());
            }else{
                model.addAttribute("message", CustomizeErrorCode.SYSTEM_ERROR.getMessage());
            }
            return new ModelAndView("error");
        }

    }
}
