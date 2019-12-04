package cn.edu.szu.bigdata.rsp_platform.system.model;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;
import java.io.Serializable;

/**
 * <p>
 * 创建模型任务
 * </p>
 *
 * @author Haotong Sun
 * @since 2019-08-22
 */
@TableName("sys_rsp_create_model_task")
public class RspCreateModelTask implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(hidden = true)
    @TableId(type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 模型名称
     */
    @ApiModelProperty(value = "模型名",required = true)
    private String modelName;

    /**
     * 数据集目录名id
     */
    @ApiModelProperty(value = "数据集目录名id", required = true)
    private String documentId;

    /**
     * 数据集名id
     */
    @ApiModelProperty(value = "数据集名id", required = true)
    private String datasetId;

    /**
     * 训练方式id
     */
    @ApiModelProperty(value = "训练方式id", required = true)
    private String trainMethodId;

    /**
     * 特征(逗号分隔)
     */
    @ApiModelProperty(value = "特征(逗号分隔)", required = true)
    private String feature;

    /**
     * 标签(逗号分隔)
     */
    @ApiModelProperty(value = "标签(逗号分隔)", required = true)
    private String label;

    /**
     * 算法id
     */
    @ApiModelProperty(value = "算法id", required = true)
    private String algorithmId;

    /**
     * 算法参数
     */
    @ApiModelProperty(value = "算法参数", required = true)
    private String param;

    /**
     * 运行状态，0停止，1活动
     */
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private Integer runningStatus;

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

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(String datasetId) {
        this.datasetId = datasetId;
    }


    public String getTrainMethodId() {
        return trainMethodId;
    }

    public void setTrainMethodId(String trainMethodId) {
        this.trainMethodId = trainMethodId;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getAlgorithmId() {
        return algorithmId;
    }

    public void setAlgorithmId(String algorithmId) {
        this.algorithmId = algorithmId;
    }

    public Integer getRunningStatus() {
        return runningStatus;
    }

    public void setRunningStatus(Integer runningStatus) {
        this.runningStatus = runningStatus;
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

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    @Override
    public String toString() {
        return "RspCreateModelTask{" +
                "id=" + id +
                ", modelName=" + modelName +
                ", documentId=" + documentId +
                ", datasetId=" + datasetId +
                ", trainMethodId=" + trainMethodId +
                ", feature=" + feature +
                ", label=" + label +
                ", algorithmId=" + algorithmId +
                ", runningStatus=" + runningStatus +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", status=" + status +
                "}";
    }
}
