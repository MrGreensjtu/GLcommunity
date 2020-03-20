package mrgreen.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author: mrgreen
 * @date: 2020/3/19
 * @description:
 */

@Controller
public class IndexController {
    @GetMapping("/")
    public String index() {
        return "index";

    }
}
