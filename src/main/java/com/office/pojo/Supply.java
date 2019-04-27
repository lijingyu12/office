package com.office.pojo;

import java.util.Date;

public class Supply {
    private Integer id;

    private String category;

    private String name;

    private Integer stock;

    private String image;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    public Supply(Integer id, String category, String name, Integer stock, String image, Integer status, Date createTime, Date updateTime) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.stock = stock;
        this.image = image;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Supply() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category == null ? null : category.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}