package cn.edu.szu.bigdata.rsp_platform.system.model;

import com.baomidou.mybatisplus.annotation.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Date;

@TableName("sys_authorities")
public class Authorities implements GrantedAuthority {
    private static final long serialVersionUID = -6058060376656180793L;

    @TableId(type = IdType.ID_WORKER_STR)
    private String authority;

    private String authorityName;

    private String parentName;

    private Integer sort;

    @TableField(fill= FieldFill.INSERT)
    private Date createTime;

    @TableField(exist = false)
    private int checked;  // 回显选中状态

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getAuthorityName() {
        return authorityName;
    }

    public void setAuthorityName(String authorityName) {
        this.authorityName = authorityName;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String getAuthority() {
        return this.authority;
    }

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }
}
