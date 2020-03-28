package mrgreen.community.service;

import mrgreen.community.dto.PageDTO;
import mrgreen.community.dto.QuestionDTO;
import mrgreen.community.mapper.QuestionMapper;
import mrgreen.community.mapper.UserMapper;
import mrgreen.community.model.Question;
import mrgreen.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: mrgreen
 * @date: 2020/3/24
 * @description:
 */

@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;

    public PageDTO list(Integer offset, Integer limit) {
        PageDTO pageDTO = new PageDTO();
        Integer totalCount = questionMapper.count();
        pageDTO.setPagesInfo(totalCount, offset, limit);
        if (offset < 1){
            offset = 1;
        }
        if (offset >= pageDTO.getTotalPage()){
            offset = pageDTO.getTotalPage();
        }
        Integer page = limit*(offset-1);
        List<Question> questions = questionMapper.list(page, limit);
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question: questions) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        pageDTO.setQuestionDTOList(questionDTOList);
        return pageDTO;
    }

    public PageDTO list(Integer userId, Integer offset, Integer limit) {
        PageDTO pageDTO = new PageDTO();
        Integer totalCount = questionMapper.countByUserId(userId);
        pageDTO.setPagesInfo(totalCount, offset, limit);
        if (offset < 1){
            offset = 1;
        }
        if (offset >= pageDTO.getTotalPage()){
            offset = pageDTO.getTotalPage();
        }
        Integer page = limit*(offset-1);
        List<Question> questions = questionMapper.listByUserId(userId, page, limit);
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question: questions) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        pageDTO.setQuestionDTOList(questionDTOList);
        return pageDTO;
    }

    public QuestionDTO getById(Integer id) {
        Question question = questionMapper.getById(id);
        User user = userMapper.findById(question.getCreator());
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);
        questionDTO.setUser(user);
        return questionDTO;
    }
}
