package cn.edu.szu.bigdata.rsp_platform.system.model;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * @author longhao
 * @date 2019/6/23 18:26
 */
@ApiModel
@TableName("sys_operation_log")
public class OperationLog implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(hidden = true)
    @TableId(value = "operation_log_id", type = IdType.ID_WORKER_STR)
    private String operationLogId;

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
     * 昵称
     */
    @TableField("nick_name")
    private String nickName;

    /**
     * 日志类型
     */
    @TableField("oper_type")
    private Long operType;


    /**
     * 操作的url
     */
    @TableField("oper_url")
    private String operUrl;

    /**
     * 用户id
     */
    @TableField("oper_event")
    private String operEvent;

    /**
     * 请求参数
     */
    @TableField("req_param")
    private String reqParam;

    /**
     * 请求方法
     */
    @TableField("req_type")
    private String reqType;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField("client_ip")
    private String clientIp;

    /**
     * 备注
     */
    @TableField("message")
    private String message;

    /**
     * 删除标记
     */
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private Integer status;

    public String getOperationLogId() {
        return operationLogId;
    }

    public void setOperationLogId(String operationLogId) {
        this.operationLogId = operationLogId;
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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Long getOperType() {
        return operType;
    }

    public void setOperType(Long operType) {
        this.operType = operType;
    }

    public String getOperUrl() {
        return operUrl;
    }

    public void setOperUrl(String operUrl) {
        this.operUrl = operUrl;
    }

    public String getOperEvent() {
        return operEvent;
    }

    public void setOperEvent(String operEvent) {
        this.operEvent = operEvent;
    }

    public String getReqParam() {
        return reqParam;
    }

    public void setReqParam(String reqParam) {
        this.reqParam = reqParam;
    }

    public String getReqType() {
        return reqType;
    }

    public void setReqType(String reqType) {
        this.reqType = reqType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }
}
