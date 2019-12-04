package cn.edu.szu.bigdata.rsp_platform.system.model;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author longhao
 * @since 2019-09-30
 */
@TableName("sys_wait_task")
public class WaitTask implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务id
     */
    @ApiModelProperty(hidden = true)
    @TableId(type = IdType.ID_WORKER_STR)
    private String taskId;

    /**
     * 任务参数
     */
    private String parameter;

    /**
     * 子任务id
     */
    private String childTaskId;

    /**
     * 父任务id
     */
    private String parentTaskId;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    @TableField(fill= FieldFill.INSERT_UPDATE)
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(hidden = true)
    @TableField(fill=FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 任务类型
     */
    private Integer taskType;


    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getChildTaskId() {
        return childTaskId;
    }

    public void setChildTaskId(String childTaskId) {
        this.childTaskId = childTaskId;
    }

    public String getParentTaskId() {
        return parentTaskId;
    }

    public void setParentTaskId(String parentTaskId) {
        this.parentTaskId = parentTaskId;
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

    public Integer getTaskType() {
        return taskType;
    }

    public void setTaskType(Integer taskType) {
        this.taskType = taskType;
    }

    @Override
    public String toString() {
        return "WaitTask{" +
        "taskId=" + taskId +
        ", parameter=" + parameter +
        ", childTaskId=" + childTaskId +
        ", parentTaskId=" + parentTaskId +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", taskType=" + taskType +
        "}";
    }
}
