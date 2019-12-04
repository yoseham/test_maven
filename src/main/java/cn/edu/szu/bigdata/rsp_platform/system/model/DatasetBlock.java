package cn.edu.szu.bigdata.rsp_platform.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * <p>
 * 数据集与数据块表
 * </p>
 *
 * @author longhao
 * @since 2019-08-09
 */
@ApiModel
@TableName("sys_dataset_block")
public class DatasetBlock implements Serializable {

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

    @Override
    public String toString() {
        return "DatasetBlock{" +
        "id=" + id +
        ", datasetId=" + datasetId +
        ", blockId=" + blockId +
        "}";
    }
}
