package mrgreen.community.controller;

import mrgreen.community.dto.AccessTokenDTO;
import mrgreen.community.dto.GithubUserDTO;
import mrgreen.community.mapper.UserMapper;
import mrgreen.community.model.User;
import mrgreen.community.provider.GithubProvider;
import mrgreen.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    private UserService userService;

    @GetMapping("/callback")
    public String callback(@RequestParam(name= "code") String code, @RequestParam(name = "state") String state,
                           HttpServletResponse response) {
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
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUserDTO.getName());
            user.setAccountId(String.valueOf(githubUserDTO.getId()));
            user.setAvatarUrl(githubUserDTO.getAvatarUrl());
            userService.createOrUpdate(user);
            response.addCookie(new Cookie("token", token));
            //登录成功
            return "redirect:/";
        } else {
            //登录失败
            return "redirect:/";
        }

    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().removeAttribute("user");
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
