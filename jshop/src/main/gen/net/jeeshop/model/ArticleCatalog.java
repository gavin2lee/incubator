package net.jeeshop.model;

import java.io.Serializable;

public class ArticleCatalog implements Serializable {
    /** ID */
    private Integer id;

    /** 分类名称 */
    private String name;

    /** 父级ID */
    private Integer pid;

    /** 顺序 */
    private Integer order;

    /** 类型 */
    private String type;

    /** 分类编码 */
    private String code;

    private static final long serialVersionUID = 1L;

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

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }
}