/**   
 * @Description:(用一句话描述该类做什么)
 * @author Daxian.jiang
 * @Email  Daxian.jiang@vipshop.com
 * @Date 2015年9月16日 下午5:15:02
 * @Version V1.0   
 */
package com.maoshen.component.base.dto;

import com.maoshen.component.base.errorcode.BaseErrorCode;

public class ResponseResultDto<T> {
    private int    code    = 200;

    private String message = "";

    private T      data;

    public ResponseResultDto(T t) {
        data = t;
    }

    public ResponseResultDto() {

    }
    
    public ResponseResultDto(BaseErrorCode errorCode) {
        code = errorCode.getCode();
        message = errorCode.getMsg();
    }
    
    public ResponseResultDto(T t, BaseErrorCode errorCode) {
        code = errorCode.getCode();
        message = errorCode.getMsg();
        data = t;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}