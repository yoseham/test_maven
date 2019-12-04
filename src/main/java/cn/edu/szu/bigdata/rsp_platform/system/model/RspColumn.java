package cn.edu.szu.bigdata.rsp_platform.system.model;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.io.Serializable;

/**
 * <p>
 * 数据集的属性表
 * </p>
 *
 * @author longhao
 * @since 2019-08-09
 */
@ApiModel
@TableName("sys_rsp_column")
public class RspColumn implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(hidden = true)
    @TableId(type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 列名称
     */
    @ApiModelProperty(value = "列名称",required = true)
    private String name;

    /**
     * 数据类型
     */
    @ApiModelProperty(value = "数据类型",required = true)
    private String type;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述",required = true)
    private String comment;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    @TableField(fill=FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(hidden = true)
    @TableField(fill=FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 状态，0正常，1删除
     */
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    @TableLogic
    private Integer status;

    /**
     * 能否数据探索，0正常，1删除
     */
    @ApiModelProperty(hidden = true)
    private Integer dataExploration;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDataExploration() {
        return dataExploration;
    }

    public void setDataExploration(Integer dataExploration) {
        this.dataExploration = dataExploration;
    }

    @Override
    public String toString() {
        return "RspColumn{" +
        "id=" + id +
        ", name=" + name +
        ", type=" + type +
        ", comment=" + comment +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", status=" + status +
        ", dataExploration=" + dataExploration +
        "}";
    }
}
