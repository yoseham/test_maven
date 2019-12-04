package cn.edu.szu.bigdata.rsp_platform.system.dto;

import cn.edu.szu.bigdata.rsp_platform.system.model.RspColumn;

import java.util.List;

/**
 * Created by longhao on 2019/9/29
 */
public class RspJobDto {
    private String sourcePath;
    private String separatorFlag;
    private String documentName;
    private String datasetName;
    private String reduceNumber;
    private String introduction;
//    private List<RspColumn> columns;

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public String getSeparatorFlag() {
        return separatorFlag;
    }

    public void setSeparatorFlag(String separatorFlag) {
        this.separatorFlag = separatorFlag;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    public String getReduceNumber() {
        return reduceNumber;
    }

    public void setReduceNumber(String reduceNumber) {
        this.reduceNumber = reduceNumber;
    }

//    public List<RspColumn> getColumns() {
//        return columns;
//    }
//
//    public void setColumns(List<RspColumn> columns) {
//        this.columns = columns;
//    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
}
