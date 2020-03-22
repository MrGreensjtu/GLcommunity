package mrgreen.community.controller;

import mrgreen.community.dto.AccessTokenDTO;
import mrgreen.community.dto.GithubUserDTO;
import mrgreen.community.mapper.UserMapper;
import mrgreen.community.model.User;
import mrgreen.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * @author: mrgreen
 * @date: 2020/3/20
 * @description:
 */

@Controller
public class OAuthController {
    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String cliendSecret;
    @Value("${github.redirect_url}")
    private String redirectUrl;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/callback")
    public String callback(@RequestParam(name= "code") String code, @RequestParam(name = "state") String state,
                           HttpServletRequest requset) {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_url(redirectUrl);
        accessTokenDTO.setState(state);
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(cliendSecret);
        String accsssToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUserDTO githubUserDTO = githubProvider.getUser(accsssToken);
        if (githubUserDTO != null){
            User user = new User();
            user.setToken(UUID.randomUUID().toString());
            user.setName(githubUserDTO.getName());
            user.setAccountId(String.valueOf(githubUserDTO.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(System.currentTimeMillis());
            userMapper.insert(user);
            //登录成功
            requset.getSession().setAttribute("user", githubUserDTO);
            return "redirect:/";
        } else {
            //登录失败
            return "redirect:/";
        }

    }
}
