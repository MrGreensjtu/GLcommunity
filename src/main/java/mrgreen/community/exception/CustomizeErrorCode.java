package mrgreen.community.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode{

    QUESTION_NOT_FOUND(2001,"你找的问题不在啦，要不换个试试？"),
    TARGET_PARAM_NOT_FOUND(2002,"未选中任何问题或评论！"),
    NO_LOGIN(2003, "当前操作需要登录，请先登录！"),
    SYSTEM_ERROR(2004, "服务器冒烟啦，要不稍后再试？"),
    TYPE_PARAM_WRONG(2005, "评论类型错误或不存在！"),
    COMMENT_NOT_FOUND(2006, "回复的评论不存在，要不换个试试？"),
    COMMENT_CONTENT_EMPTY(2007, "评论内容不能为空！"),
    NOTIFICATION_NOT_FOUND(2008, "该通知消息不存在！"),
    READ_OTHERS_NOTIFICATION(2009, "兄弟这是别人的信息！"),
    FILE_UPLOAD_FAIL(2010, "图片上传失败！");


    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    private String message;
    private Integer code;

    CustomizeErrorCode(Integer code, String message) {
        this.message = message;
        this.code = code;
    }

}
