package com.geekcattle.controller.boke;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/boke")
public class BokeController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value="/index")
    public String Index(){

        return "boke/index";
    }
}
