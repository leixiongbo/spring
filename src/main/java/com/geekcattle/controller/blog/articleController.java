package com.geekcattle.controller.blog;

import com.geekcattle.mapper.blog.LifeShareMapper;
import com.geekcattle.model.blog.LifeShare;
import com.geekcattle.util.ReturnUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/artilce")
public class articleController {
    @Resource
    private LifeShareMapper lifeShareMapper;
    //todo 获取文章标题数据
    @RequestMapping(value = "/aitletitle", method = RequestMethod.GET)
    public ModelMap getAlitleDta(HttpServletRequest request) {
        try {
            List<LifeShare> lifeShareList = lifeShareMapper.selectAllData();
            return ReturnUtil.Success(null, lifeShareList, null);
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnUtil.Error(null, null, null);
        }
    }
    //todo 获取文章标题数据
    @RequestMapping(value = "/aitlecontent", method = RequestMethod.GET)
    public ModelMap getAitlecontent(HttpServletRequest request) {
        try {
            String id = request.getParameter("id");
            LifeShare lifeShare = lifeShareMapper.selectAllDataById(id);
            return ReturnUtil.Success(null, lifeShare.getContent(), null);
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnUtil.Error(null, null, null);
        }
    }
}
