package cn.edu.szu.bigdata.rsp_platform.system.model;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author wangfan
 * @since 2019-02-11
 */
@TableName("sys_role")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色id
     */
    @TableId(value = "role_id", type = IdType.ID_WORKER_STR)
    private String roleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 备注
     */
    private String comments;

    @TableLogic
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(fill=FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField(fill=FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    public Role() {
    }

    public Role(String roleId, String roleName, String comments) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.comments = comments;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Role{" +
                ", roleId=" + roleId +
                ", roleName=" + roleName +
                ", comments=" + comments +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                "}";
    }
}
