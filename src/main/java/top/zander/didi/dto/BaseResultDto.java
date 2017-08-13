package top.zander.didi.dto;

import top.zander.didi.Common.RetCode;

/**
 * Created by cuiwenming on 16/5/23.
 */
public class BaseResultDto<T> {

    private Integer code = 1;
    private String msg;
    private T data;


    public BaseResultDto(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public BaseResultDto(RetCode retCode, T data) {
        this.code = retCode.getCode();
        this.msg = retCode.getValue();
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
