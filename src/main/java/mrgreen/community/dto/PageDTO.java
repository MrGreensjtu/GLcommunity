package mrgreen.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: mrgreen
 * @date: 2020/3/25
 * @description:
 */

@Data
public class PageDTO<T> {
    private List<T> data;
    private Boolean showPrevious;
    private Boolean showFirstPage;
    private Boolean showNext;
    private Boolean showEndPage;
    private Integer currentPage;
    private Integer totalPage;
    private  List<Integer> pages = new ArrayList<>();

    public void setPagesInfo(Integer totalCount, Integer offset, Integer limit) {
        this.currentPage = offset;
        //计算全部页码数
        Integer totalPage = 0;
        if (totalCount % limit == 0) {
            totalPage = totalCount / limit;
        }else {
            totalPage = totalCount / limit + 1;
        }
        this.totalPage = totalPage;

        //计算需要展示的页码数，default=5
        pages.add(offset);
        for (int i= 1; i<= 3; i++){
            if (offset - i > 0){
                pages.add(0,offset-i);
            }
            if (offset + i <= totalPage){
                pages.add(offset+i);
            }
        }

        //计算是否展示前一页按钮
        if (offset == 1) {
            showPrevious = false;
        }else {
            showPrevious = true;
        }

        //计算是否展示下一页按钮
        if (offset == totalPage) {
            showNext = false;
        }else {
            showNext = true;
        }

        //计算是否展示首页按钮
        if (pages.contains(1)) {
            showFirstPage = false;
        }else{
            showFirstPage = true;
        }

        //计算是否展示尾页按钮
        if (pages.contains(totalPage)) {
            showEndPage = false;
        }else{
            showEndPage = true;
        }
    }
}
