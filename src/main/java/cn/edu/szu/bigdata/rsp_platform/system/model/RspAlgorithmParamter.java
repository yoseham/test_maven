package cn.edu.szu.bigdata.rsp_platform.system.model;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.io.Serializable;

/**
 * <p>
 * 算法参数表
 * </p>
 *
 * @author longhao
 * @since 2019-08-23
 */
@ApiModel
@TableName("sys_rsp_algorithm_paramter")
public class RspAlgorithmParamter implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 参数id
     */
    @ApiModelProperty(hidden = true)
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 算法id
     */
    @ApiModelProperty(value = "算法id",required = true)
    private String algorithmId;


    @ApiModelProperty(value = "算法名称",required = true)
    @TableField(exist = false)
    private String algorithmName;

    /**
     * 参数类型
     */
    @ApiModelProperty(value = "参数类型",required = true)
    private String paramterType;

    /**
     * 参数名称
     */
    @ApiModelProperty(value = "参数名称",required = true)
    private String paramterName;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(hidden = true)
    @TableField(value = "update_time",fill=FieldFill.INSERT_UPDATE)
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

    public String getAlgorithmId() {
        return algorithmId;
    }

    public void setAlgorithmId(String algorithmId) {
        this.algorithmId = algorithmId;
    }

    public String getParamterType() {
        return paramterType;
    }

    public void setParamterType(String paramterType) {
        this.paramterType = paramterType;
    }

    public String getParamterName() {
        return paramterName;
    }

    public void setParamterName(String paramterName) {
        this.paramterName = paramterName;
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

    public String getAlgorithmName() {
        return algorithmName;
    }

    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    @Override
    public String toString() {
        return "RspAlgorithmParamter{" +
        "id=" + id +
        ", algorithmId=" + algorithmId +
        ", paramterType=" + paramterType +
        ", paramterName=" + paramterName +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", status=" + status +
        "}";
    }
}
