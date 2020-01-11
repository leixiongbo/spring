package com.geekcattle.model.blog;

import javax.persistence.Table;

@Table(name = "blog_show")
public class LifeShare {
  private   String id;
    private   String title;
    private   String content;
    private   String creattime;
    private   String zan;
    private   String updatastatu;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreattime() {
        return creattime;
    }

    public void setCreattime(String creattime) {
        this.creattime = creattime;
    }

    public String getZan() {
        return zan;
    }

    public void setZan(String zan) {
        this.zan = zan;
    }

    public String getUpdatastatu() {
        return updatastatu;
    }

    public void setUpdatastatu(String updatastatu) {
        this.updatastatu = updatastatu;
    }


}
