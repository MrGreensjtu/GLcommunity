package mrgreen.community.controller;

import mrgreen.community.dto.PageDTO;
import mrgreen.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author: mrgreen
 * @date: 2020/3/19
 * @description:
 */

@Controller
public class IndexController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(name="offset", defaultValue = "1") Integer offset,
                        @RequestParam(name="limit", defaultValue = "5") Integer limit
                        ) {
        PageDTO pageDTOList = questionService.list(offset, limit);
        model.addAttribute("pageList", pageDTOList);
        return "index";

    }
}
