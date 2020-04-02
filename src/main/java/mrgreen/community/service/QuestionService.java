package mrgreen.community.service;

import mrgreen.community.dto.PageDTO;
import mrgreen.community.dto.QuestionDTO;
import mrgreen.community.exception.CustomizeErrorCode;
import mrgreen.community.exception.CustomizeException;
import mrgreen.community.mapper.QuestionMapper;
import mrgreen.community.mapper.UserMapper;
import mrgreen.community.model.Question;
import mrgreen.community.model.QuestionExample;
import mrgreen.community.model.User;
import org.apache.ibatis.session.RowBounds;
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
        Integer totalCount = (int) questionMapper.countByExample(new QuestionExample());
        pageDTO.setPagesInfo(totalCount, offset, limit);
        if (offset < 1){
            offset = 1;
        }
        if (offset >= pageDTO.getTotalPage()){
            offset = pageDTO.getTotalPage();
        }
        Integer page = limit*(offset-1);
        List<Question> questions = questionMapper.selectByExampleWithBLOBsWithRowbounds(new QuestionExample(), new RowBounds(page, limit));
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question: questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
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
        QuestionExample example = new QuestionExample();
        example.createCriteria()
                .andCreatorEqualTo(userId);
        Integer totalCount = (int) questionMapper.countByExample(example);
        pageDTO.setPagesInfo(totalCount, offset, limit);
        if (offset < 1){
            offset = 1;
        }
        if (offset >= pageDTO.getTotalPage()){
            offset = pageDTO.getTotalPage();
        }
        Integer page = limit*(offset-1);
        QuestionExample example1 = new QuestionExample();
        example1.createCriteria()
                .andCreatorEqualTo(userId);
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(example1, new RowBounds(page, limit));

        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question: questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        pageDTO.setQuestionDTOList(questionDTOList);
        return pageDTO;
    }

    public QuestionDTO getById(Integer id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        if (question == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if(question.getId() == null){
            //创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.insert(question);
        }else{
            //更新
            question.setGmtModified(System.currentTimeMillis());
            QuestionExample example = new QuestionExample();
            example.createCriteria()
                    .andIdEqualTo(question.getId());
            int updateStatus = questionMapper.updateByExampleSelective(question, example);
            if(updateStatus != 1){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }
}
