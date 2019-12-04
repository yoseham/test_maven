package cn.edu.szu.bigdata.rsp_platform.system.model;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * @author longhao
 * @date 2019/6/24 14:04
 */
@ApiModel
@TableName("sys_rsp_task")
public class RspTask implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(hidden = true)
    @TableId(value = "rsp_task_id", type = IdType.ID_WORKER_STR)
    private String rspTaskId;

    /**
     * user_id
     */
    @TableField("user_id")
    private String userId;

    /**
     * 账户名
     */
    @TableField("username")
    private String username;

    /**
     * 参数
     */
    @TableField("param")
    private String param;

    /**
     * 参数
     */
    @TableField("job_url")
    private String jobUrl;


    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 参数
     */
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    @TableLogic
    private Integer status;

    public String getRspTaskId() {
        return rspTaskId;
    }

    public void setRspTaskId(String rspTaskId) {
        this.rspTaskId = rspTaskId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getJobUrl() {
        return jobUrl;
    }

    public void setJobUrl(String jobUrl) {
        this.jobUrl = jobUrl;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
