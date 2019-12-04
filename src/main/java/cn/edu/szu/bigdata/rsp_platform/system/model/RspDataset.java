package cn.edu.szu.bigdata.rsp_platform.system.model;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.io.Serializable;

/**
 * <p>
 * 数据集
 * </p>
 *
 * @author longhao
 * @since 2019-08-09
 */
@ApiModel
@TableName("sys_rsp_dataset")
public class RspDataset implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(hidden = true)
    @TableId(type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 表名
     */
    @ApiModelProperty(value = "数据集名",required = true)
    private String name;

    /**
     * 容量
     */
    @ApiModelProperty(value = "容量",required = true)
    private String volume;

    /**
     * 包含块数目
     */
    @ApiModelProperty(value = "块数目",required = true)
    private String blockNumber;

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

    /**
     * 分隔符
     */
    @ApiModelProperty(value = "分隔符",hidden = true)
    private String separatorFlag;

    /**
     * 数据集简介
     */
    @ApiModelProperty(value = "数据集简介")
    private String introduction;

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

    public String getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(String blockNumber) {
        this.blockNumber = blockNumber;
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

    public String getIntroduction() { return introduction; }

    public void setIntroduction(String introduction) { this.introduction = introduction; }

    public String getSeparatorFlag() {
        return separatorFlag;
    }

    public void setSeparatorFlag(String separatorFlag) {
        this.separatorFlag = separatorFlag;
    }

    @Override
    public String toString() {
        return "RspDataset{" +
        "id=" + id +
        ", name=" + name +
        ", volume=" + volume +
        ", blockNumber=" + blockNumber +
        ", hdfsLocation=" + hdfsLocation +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", status=" + status +
                "separatorFlag="+separatorFlag+
                ", introduction=" + introduction +
        "}";
    }
}
