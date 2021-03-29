package com.zsr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {
    @RequestMapping("/user/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, Model model, HttpSession session) {
        //如果用户名和密码正确
        if ("admin".equals(username) && "123456".equals(password)) {
            session.setAttribute("LoginUser", username);
            return "redirect:/main.html";//重定向到main.html页面,也就是跳转到dashboard页面
        }
        //如果用户名或者密码不正确
        else {
            model.addAttribute("msg", "用户名或者密码错误");//显示错误信息
            return "index";//跳转到首页
        }
    }

    @RequestMapping("/user/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/index.html";
    }
}
