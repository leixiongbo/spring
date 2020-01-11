package com.geekcattle.model.blog;

import javax.persistence.Table;
import javax.xml.crypto.Data;
import java.io.Serializable;
@Table(name = "pic_show")
public class PicShow  implements Serializable {
    private String id;
    private String type;
    private String src;
    private String resource;
    private String species;
    private String remark;
    private String pictime;
    private String userid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }



    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }



    public String getPictime() {
        return pictime;
    }

    public void setPictime(String pictime) {
        this.pictime = pictime;
    }


    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

}
