package cn.edu.szu.bigdata.rsp_platform.system.model;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.io.Serializable;

/**
 * <p>
 * 算法库表
 * </p>
 *
 * @author Sun Haotong
 * @since 2019-08-23
 */
@ApiModel
@TableName("sys_rsp_algorithm")
public class RspAlgorithm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 算法id
     */
    @ApiModelProperty(hidden = true)
    @TableId(type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 算法类型
     */
    @ApiModelProperty(value = "算法类型")
    private String algorithmType;

    /**
     * 算法名
     */
    @ApiModelProperty(value = "算法名称")
    private String algorithmName;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;


    /**
     * 更新时间
     */
    @ApiModelProperty(hidden = true)
    @TableField(fill = FieldFill.INSERT)
    private Date updateTime;

    /**
     * 状态，0未完成，1完成
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

    public String getAlgorithmType() {
        return algorithmType;
    }

    public void setAlgorithmType(String algorithmType) {
        this.algorithmType = algorithmType;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
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
        return "RspAlgorithm{" +
        "id=" + id +
        ", algorithmType=" + algorithmType +
        ", algorithmName=" + algorithmName +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", status=" + status +
        "}";
    }
}
