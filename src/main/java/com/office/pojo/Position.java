package com.office.pojo;

public class Position {
    private Integer id;

    private String name;

    private Integer departmentId;

    public Position(Integer id, String name, Integer departmentId) {
        this.id = id;
        this.name = name;
        this.departmentId = departmentId;
    }

    public Position() {
        super();
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
        this.name = name == null ? null : name.trim();
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }
}