package cn.edu.szu.bigdata.rsp_platform.system.model;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.io.Serializable;

/**
 * <p>
 * 训练模式表
 * </p>
 *
 * @author longhao
 * @since 2019-08-23
 */
@ApiModel
@TableName("sys_rsp_train_mode")
public class RspTrainMode implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(hidden = true)
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 训练模式名
     */
    @ApiModelProperty(value = "训练模式名",required = true)
    private String modeName;

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

    public String getModeName() {
        return modeName;
    }

    public void setModeName(String modeName) {
        this.modeName = modeName;
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
        return "RspTrainMode{" +
        "id=" + id +
        ", modeName=" + modeName +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", status=" + status +
        "}";
    }
}
