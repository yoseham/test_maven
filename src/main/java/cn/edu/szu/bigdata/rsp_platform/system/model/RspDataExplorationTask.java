package cn.edu.szu.bigdata.rsp_platform.system.model;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * <p>
 * 数据预览任务表
 * </p>
 *
 * @author longhao
 * @since 2019-09-28
 */
@TableName("sys_rsp_data_exploration_task")
public class RspDataExplorationTask implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(hidden = true)
    @TableId(type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 数据集id
     */
    @ApiModelProperty(value = "数据集名",required = true)
    private String datasetId;

    /**
     * 块id
     */
    @ApiModelProperty(value = "数据块名",required = true)
    private String blockId;

    /**
     * 列id（逗号分隔）
     */
    @ApiModelProperty(value = "数据列名",required = true)
    private String columnIds;

    /**
     * 状态，0未完成，1完成
     */
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    @TableLogic
    private Integer status;

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
    @TableField(fill=FieldFill.INSERT)
    private Date updateTime;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private String resultUrl;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(String datasetId) {
        this.datasetId = datasetId;
    }

    public String getBlockId() {
        return blockId;
    }

    public void setBlockId(String blockId) {
        this.blockId = blockId;
    }

    public String getColumnIds() {
        return columnIds;
    }

    public void setColumnIds(String columnIds) {
        this.columnIds = columnIds;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public String getResultUrl() {
        return resultUrl;
    }

    public void setResultUrl(String resultUrl) {
        this.resultUrl = resultUrl;
    }

    @Override
    public String toString() {
        return "RspDataExplorationTask{" +
        "id=" + id +
        ", datasetId=" + datasetId +
        ", blockId=" + blockId +
        ", columnIds=" + columnIds +
        ", status=" + status +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", resultUrl=" + resultUrl +
        "}";
    }
}
