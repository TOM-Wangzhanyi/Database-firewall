package com.wzy.mybatis.pojo;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * ClassName: TestIntercepter
 * Package: com.wzy.mybatis.pojo
 * DESCRIPTION :
 *
 * @Author :WZY
 * @Create:2023/3/29 - 21:38
 * @Version: v1.0
 */
public class TestIntercepter {
    private Integer id ;
    private String name ;
    private String sex ;
    private LocalDateTime createdAt;
    private String createdBy ;

    @Override
    public String toString() {
        return "TestIntercepter{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", createdBy='" + createdBy + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public TestIntercepter(Integer id, String 你礼貌吗, String 男, LocalDateTime now, Object createdBy) {
    }

    public TestIntercepter(Integer id, String name, String sex) {
        this.id = id;
        this.name = name;
        this.sex = sex;
    }

    public TestIntercepter(Integer id, String name, String sex, LocalDateTime createdAt, String createdBy) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
    }
}
