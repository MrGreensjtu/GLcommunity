package mrgreen.community.controller;

import mrgreen.community.dto.PageDTO;
import mrgreen.community.model.Notification;
import mrgreen.community.model.User;
import mrgreen.community.service.NotificationService;
import mrgreen.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: mrgreen
 * @date: 2020/3/25
 * @description:
 */

@Controller
public class ProfileController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name="action") String action, Model model, HttpServletRequest request,
                          HttpServletResponse response,
                          @RequestParam(name="offset", defaultValue = "1") Integer offset,
                          @RequestParam(name="limit", defaultValue = "8") Integer limit) {

        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            return "redirect:/";
        }

        if("questions".equals(action)){
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的提问");
            PageDTO pageDTOProfileQuestionList = questionService.list(user.getId(), offset, limit);
            model.addAttribute("pageProfileList", pageDTOProfileQuestionList);

        }else if("replies".equals(action)){
            model.addAttribute("section", "replies");
            model.addAttribute("sectionName", "最新回复");

            PageDTO pageDTOProfileNotificationList = notificationService.list(user.getId(), offset, limit);
            Long unreadCount = notificationService.unreadCount(user.getId());
            model.addAttribute("unreadCount",unreadCount);
            model.addAttribute("pageProfileList", pageDTOProfileNotificationList);

        }

        return "profile";
    }
}
