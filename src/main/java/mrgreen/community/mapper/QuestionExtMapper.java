package mrgreen.community.mapper;

import mrgreen.community.dto.QuestionQueryDTO;
import mrgreen.community.model.Question;
import mrgreen.community.model.QuestionExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface QuestionExtMapper {
    void incView(Question question);
    void incComment(Question question);

    List<Question> selectRelatedWithBLOBs(Question question);

    Integer countBySearch(QuestionQueryDTO questionQueryDTO);

    List<Question> selectBySearchWithBLOBs(QuestionQueryDTO questionQueryDTO);
}