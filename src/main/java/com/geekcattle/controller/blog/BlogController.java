package com.geekcattle.controller.blog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/blog")
public class BlogController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //博客主页
    @RequestMapping(value="/index")
    public String Index(){

        return "blog/index";
    }
    //后台登录
    @RequestMapping(value="/login")
    public String login(){
        return "blog/login";
    }

    //后台登录-照片展示
    @RequestMapping(value="/picshow")
    public String picShow(){
        return "blog/picshow";
    }

    //后台登录-给力博文
    @RequestMapping(value="/lifeshare")
    public String lifeShare(){
        return "blog/da";
    }

    //todo 博文发布还需要插入数据  和获取数据动态渲染页面 ---------------------------------

    //后台登录-wangeditor富文本编辑器
    @RequestMapping(value="/wangeditor")
    public String wangEditor(){
        return "blog/wangeditor";
    }

}
