package mrgreen.community.cache;

import mrgreen.community.dto.TagDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: mrgreen
 * @date: 2020/4/11
 * @description:
 */

public class TagCache {
    public static List<TagDTO> getTags(){
        List<TagDTO> tagDTOList = new ArrayList<>();
        TagDTO programLanguage = new TagDTO();
        programLanguage.setCategoryName("开发语言");
        programLanguage.setTags(Arrays.asList("Java","Javascript","Html","CSS","Go","Python","C","C++","Shell","MySQL"
                ,"Node","Swift","Java","Javascript","Html","CSS","Go","Python","C","C++","Shell","MySQL"
                ,"Node","Swift"));
        tagDTOList.add(programLanguage);
        TagDTO framework = new TagDTO();
        framework.setCategoryName("平台框架");
        framework.setTags(Arrays.asList("Spring","SpringBoot","Django","Tornado","jQuery"));
        tagDTOList.add(framework);
        return tagDTOList;
    }

    public static String filterInvalid(String tags){
        String[] allTag = tags.split(",");
        List<TagDTO> tagDTOList = getTags();
        List<String> tagList = tagDTOList.stream().flatMap(tag -> tag.getTags().stream()).collect(Collectors.toList());
        String invalidTags = Arrays.stream(allTag).filter(tag -> !tagList.contains(tag)).collect(Collectors.joining(","));
        return invalidTags;
    }
}
