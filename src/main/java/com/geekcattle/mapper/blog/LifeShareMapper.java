package com.geekcattle.mapper.blog;

import com.geekcattle.model.blog.LifeShare;
import com.geekcattle.model.blog.PicShow;
import com.geekcattle.util.CustomerMapper;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface LifeShareMapper extends CustomerMapper<LifeShare> {
    List<LifeShare> selectAll();

}
