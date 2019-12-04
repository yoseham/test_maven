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
@TableName("sys_running_task")
public class RunningTask implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(hidden = true)
    @TableId(type = IdType.ID_WORKER_STR)
    private String taskId;

    /**
     * 任务batchId
     */
    private Integer batchId;

    /**
     * 任务applicationId
     */
    private String applicationId;

    /**
     * 任务参数
     */
    private String parameter;

    /**
     * 运行状态
     */
    private String runningState;

    /**
     * 父任务id
     */
    private String parentTaskId;

    /**
     * 任务执行次数
     */
    private Integer count;

    /**
     * 子任务id
     */
    private String childTaskId;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    @TableField(fill=FieldFill.INSERT_UPDATE)
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(hidden = true)
    @TableField(fill= FieldFill.INSERT_UPDATE)
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

    public Integer getBatchId() {
        return batchId;
    }

    public void setBatchId(Integer batchId) {
        this.batchId = batchId;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getRunningState() {
        return runningState;
    }

    public void setRunningState(String runningState) {
        this.runningState = runningState;
    }

    public String getParentTaskId() {
        return parentTaskId;
    }

    public void setParentTaskId(String parentTaskId) {
        this.parentTaskId = parentTaskId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getChildTaskId() {
        return childTaskId;
    }

    public void setChildTaskId(String childTaskId) {
        this.childTaskId = childTaskId;
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
        return "RunningTask{" +
        "taskId=" + taskId +
        ", batchId=" + batchId +
        ", applicationId=" + applicationId +
        ", parameter=" + parameter +
        ", runningState=" + runningState +
        ", parentTaskId=" + parentTaskId +
        ", count=" + count +
        ", childTaskId=" + childTaskId +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", taskType=" + taskType +
        "}";
    }
}
