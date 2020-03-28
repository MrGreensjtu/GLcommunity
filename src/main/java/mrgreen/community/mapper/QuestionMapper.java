package mrgreen.community.mapper;

import mrgreen.community.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: mrgreen
 * @date: 2020/3/23
 * @description:
 */

@Mapper
public interface QuestionMapper {
    @Select("select * from question limit #{page}, #{limit}")
    List<Question> list(@Param(value = "page") Integer page, @Param(value = "limit") Integer limit);

    @Insert("insert into question (title, description, gmt_create, gmt_modified, creator, tag) values (#{title}, #{description}, #{gmtCreate}, #{gmtModified}, #{creator}, #{tag})")
    void create(Question question);

    @Select("select count(1) from question")
    Integer count();

    @Select("select * from question where creator = #{userId} limit #{page}, #{limit}")
    List<Question> listByUserId(@Param(value = "userId") Integer userId, @Param(value = "page") Integer page,
                        @Param(value = "limit") Integer limit);

    @Select("select count(1) from question where creator = #{userId}")
    Integer countByUserId(@Param(value = "userId") Integer userId);

    @Select("select * from question where id = #{id}")
    Question getById(@Param(value = "id") Integer id);
}
