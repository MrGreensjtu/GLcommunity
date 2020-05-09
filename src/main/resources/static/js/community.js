function postComment() {
    var questionId = $('#questionId').val();
    var commentContent = $('#commentContent').val();
    comment2target(questionId, 1, commentContent);
}

function postSubComment(e) {
    var commentId = e.getAttribute("data-id");
    var subCommentContent = $("#subComment-"+commentId).val();
    comment2target(commentId, 2, subCommentContent);
}

function comment2target(targetId, type, content) {
    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: "application/json",
        data: JSON.stringify({
            "parentId": targetId,
            "content": content,
            "type": type
        }),
        success: function (response) {
            if (response.code == 200) {
                window.location.reload();
                $("#commentWindow").hide();
            } else {
                if (response.code == 2003) {
                    var isAccepted = confirm(response.message);
                    if(isAccepted){
                        window.open("https://github.com/login/oauth/authorize?client_id=0cfb60ecd997ee61a5e6&redirect_uri=http://localhost:8080/callback&scope=user&state=1");
                        window.localStorage.setItem("closable", "true");
                    }
                } else {
                    alert(response.message);
                }
            }
        },
        dataType: "json"
    });
}

/**
 *展开二级评论
 */
function collapseComment(e) {
    var id = e.getAttribute("data-id");
    var comment = $("#comment-"+id);

    //获取二级评论列表展开状态
    var collapse_state = e.getAttribute("data-collapse");
    if(collapse_state){
        comment.removeClass("in");
        e.removeAttribute("data-collapse")
        e.classList.remove("active");
    }else{
        var subCommentContainer = $("#comment-"+id);
        if(subCommentContainer.children().length != 1){
            //展开二级评论列表
            comment.addClass("in");
            //标记二级评论列表展开状态
            e.setAttribute("data-collapse", "in");
            e.classList.add("active");
        }else{
            $.getJSON( "/comment/"+id, function( data ) {
                $.each( data.data.reverse(), function (index,comment) {
                    var mediaLeftElement = $("<div/>",{
                        "class":"media-left",
                    }).append($("<img/>",{
                        "class":"media-object img-rounded",
                        "src":comment.user.avatarUrl
                    }));

                    var mediaBodyElement = $("<div/>",{
                        "class":"media-body",
                    }).append($("<h5/>",{
                        "class":"media-heading",
                        "html":comment.user.name
                    })).append($("<div/>",{
                        "html":comment.content
                    })).append($("<div/>",{
                        "class":"menu"
                    }).append($("<span/>",{
                        "class":"pull-right",
                        "html":moment(comment.gmtCreate).format("YYYY-MM-DD")
                    })));

                    var mediaElement = $("<div/>",{
                        "class":"media"
                    }).append(mediaLeftElement).append(mediaBodyElement);

                    var commentElement = $("<div/>",{
                        "class":"col-lg-12 col-md-12 col-sm-12 col-xs-12"
                    }).append(mediaElement).append($("<hr>",{
                        "class":"col-lg-12 col-md-12 col-sm-12 col-xs-12 comment-ssp"
                    }));

                    subCommentContainer.prepend(commentElement);
                });
                //展开二级评论列表
                comment.addClass("in");
                //标记二级评论列表展开状态
                e.setAttribute("data-collapse", "in");
                e.classList.add("active");
            });
        }
    }
}

function selectTag(e) {
    var tag = e.getAttribute("data-tag");
    var previous = $("#tag").val();
    if(previous){
        if(previous.indexOf(tag) == -1){
            $("#tag").val(previous+','+tag);
        }
    }else{
        $("#tag").val(tag);
    }
}

function showSelectTag() {
    $("#select-tag").show();
}