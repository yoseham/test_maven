package cn.edu.szu.bigdata.rsp_platform.system.model;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.io.Serializable;

/**
 * <p>
 * 数据目录
 * </p>
 *
 * @author longhao
 * @since 2019-08-09
 */
@ApiModel
@TableName("sys_rsp_document")
public class RspDocument implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(hidden = true)
    @TableId(type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 数据库名
     */
    @ApiModelProperty(value = "数据集目录名",required = true)
    private String name;

    /**
     * 容量
     */
    @ApiModelProperty(value = "大小",required = true)
    private String volume;

    /**
     * hdfs路径
     */
    @ApiModelProperty(value = "hdfs路径",required = true)
    private String hdfsLocation;

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

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getHdfsLocation() {
        return hdfsLocation;
    }

    public void setHdfsLocation(String hdfsLocation) {
        this.hdfsLocation = hdfsLocation;
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

    @Override
    public String toString() {
        return "RspDocument{" +
        "id=" + id +
        ", name=" + name +
        ", volume=" + volume +
        ", hdfsLocation=" + hdfsLocation +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", status=" + status +
        "}";
    }
}
