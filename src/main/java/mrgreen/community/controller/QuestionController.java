package mrgreen.community.controller;

import mrgreen.community.dto.CommentDTO;
import mrgreen.community.dto.QuestionDTO;
import mrgreen.community.enums.CommentTypeEnum;
import mrgreen.community.service.CommentService;
import mrgreen.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author: mrgreen
 * @date: 2020/3/26
 * @description:
 */

@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;
    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Long id, Model model) {

        QuestionDTO questionDTO = questionService.getById(id);
        List<CommentDTO> commentDTOList = commentService.listByTargetId(id, CommentTypeEnum.QUESTION);
        List<QuestionDTO> relatedQuestionList = questionService.selectRelated(questionDTO);
        questionService.incView(id);
        model.addAttribute("questionDTO", questionDTO);
        model.addAttribute("commentDTO", commentDTOList);
        model.addAttribute("relatedQuestionDTO", relatedQuestionList);
        return "question";
    }

}
