package com.example.project.studentsystem.enums;



public enum ErrorCodeEnum {

    ASF_NETWORK_BASE_CONNECT_TIMEOUT(0,"网络连接超时"),
    UNKNOWN(1,"未知错误"),
    INVALID_PARAM(2,"无效参数"),
    UNSUPPORTED(3,"引擎不支持"),
    NO_MEMORY(4,"内存不足"),
    BAD_STATE(5,"状态错误"),
    USER_CANCEL(6,"用户取消相关操作"),
    EXPIRED(7,"操作时间过期"),
    USER_PAUSE(8,"用户暂停操作"),
    BUFFER_OVERFLOW(9,"缓冲上溢"),
    BUFFER_UNDERFLOW(10,"缓冲下溢"),
    NO_DISKSPACE(11,"存贮空间不足"),
    COMPONENT_NOT_EXIST(12,"组件不存在"),
    GLOBAL_DATA_NOT_EXIST(13,"全局数据不存在"),
    ASF_NETWORK_BASE_COULDNT_CONNECT_SERVER(14,"无法连接服务器"),
    ASF_NETWORK_BASE_UNKNOWN_ERROR(15,"未知错误"),
    USERNAME_POSSWORD_ERROE(16,"用户名密码错误");


    private Integer code;
    private String description;

     ErrorCodeEnum(Integer code, String description){
        this.code=code;
        this.description=description;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static ErrorCodeEnum getDescriptionByCode(Integer code){
        for(ErrorCodeEnum errorCodeEnum : ErrorCodeEnum.values()){
            if(code.equals(errorCodeEnum.getCode())){
                return errorCodeEnum;
            }
        }
        return  ErrorCodeEnum.UNKNOWN;
    }

}
