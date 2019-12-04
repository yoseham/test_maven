package cn.edu.szu.bigdata.rsp_platform.system.model;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.io.Serializable;

/**
 * <p>
 * 前端绘图表
 * </p>
 *
 * @author longhao
 * @since 2019-08-09
 */
@ApiModel
@TableName("sys_block_view")
public class BlockView implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(hidden = true)
    @TableId(type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 表id
     */
    @ApiModelProperty(value = "数据集id",required = true)
    private String datasetId;

    /**
     * 块id
     */
    @ApiModelProperty(value = "块id",required = true)
    private String blockId;

    /**
     * 列id
     */
    @ApiModelProperty(value = "列id",required = true)
    private String columnId;

    /**
     * 行数
     */
    @ApiModelProperty(value = "行数",required = true)
    private String rowCount;

    /**
     * x轴数据
     */
    @ApiModelProperty(value = "x轴数据",required = true)
    private String xAxisData;

    /**
     * y轴数据
     */
    @ApiModelProperty(value = "y轴数据",required = true)
    private String yAxisData;

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

    public String getColumnId() {
        return columnId;
    }

    public void setColumnId(String columnId) {
        this.columnId = columnId;
    }

    public String getRowCount() {
        return rowCount;
    }

    public void setRowCount(String rowCount) {
        this.rowCount = rowCount;
    }

    public String getxAxisData() {
        return xAxisData;
    }

    public void setxAxisData(String xAxisData) {
        this.xAxisData = xAxisData;
    }

    public String getyAxisData() {
        return yAxisData;
    }

    public void setyAxisData(String yAxisData) {
        this.yAxisData = yAxisData;
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
        return "BlockView{" +
        "id=" + id +
        ", datasetId=" + datasetId +
        ", blockId=" + blockId +
        ", columnId=" + columnId +
        ", rowCount=" + rowCount +
        ", xAxisData=" + xAxisData +
        ", yAxisData=" + yAxisData +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", status=" + status +
        "}";
    }
}
