package mrgreen.community.controller;

import mrgreen.community.dto.QuestionDTO;
import mrgreen.community.mapper.QuestionMapper;
import mrgreen.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author: mrgreen
 * @date: 2020/3/26
 * @description:
 */

@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Integer id, Model model) {

        QuestionDTO questionDTO = questionService.getById(id);
        model.addAttribute("questionDTO", questionDTO);
        return "question";
    }

}
