package mrgreen.community.mapper;

import mrgreen.community.model.Comment;
import mrgreen.community.model.Question;

public interface CommentExtMapper {
    int incSubComment(Comment comment);
}