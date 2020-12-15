package com.ding.log.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 系统日志表(SysLog)实体类
 *
 * @author ding
 * @since 2020-09-25 14:32:14
 */

public class SysLog {
    /**
     * 主键id
     */

    private String id;
    /**
     * 操作人账号
     */

    private String username;
    /**
     * 操作
     */

    private String operationContent;
    /**
     * 日志操作时间
     */

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date operationTime;
    /**
     * 1成功,0失败
     */

    private Integer isSuccess;
    /**
     * 耗时
     */

    private Long costTime;
    /**
     * 请求路径
     */

    private String requestUrl;
    /**
     * 请求java方法
     */

    private String controllerMethod;
    /**
     * 请求方式
     */

    private String methodType;
    /**
     * 请求参数
     */

    private String requestParam;
    /**
     * ip
     */

    private String ip;
    /**
     * 请求来源地址
     */

    private String reference;
    /**
     * 客户端信息
     */

    private String userAgent;
    /**
     * 错误信息
     */

    private String errorMsg;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOperationContent() {
        return operationContent;
    }

    public void setOperationContent(String operationContent) {
        this.operationContent = operationContent;
    }

    public Date getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(Date operationTime) {
        this.operationTime = operationTime;
    }

    public Integer getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Integer isSuccess) {
        this.isSuccess = isSuccess;
    }

    public Long getCostTime() {
        return costTime;
    }

    public void setCostTime(Long costTime) {
        this.costTime = costTime;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getControllerMethod() {
        return controllerMethod;
    }

    public void setControllerMethod(String controllerMethod) {
        this.controllerMethod = controllerMethod;
    }

    public String getMethodType() {
        return methodType;
    }

    public void setMethodType(String methodType) {
        this.methodType = methodType;
    }

    public String getRequestParam() {
        return requestParam;
    }

    public void setRequestParam(String requestParam) {
        this.requestParam = requestParam;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}