/**   
 * @Description:(用一句话描述该类做什么)
 * @author Daxian.jiang
 * @Email  Daxian.jiang@vipshop.com
 * @Date 2015年9月17日 上午10:18:43
 * @Version V1.0   
 */
package com.maoshen.component.base.errorcode;

public enum BaseErrorCode {
	SERVICE_EXCEPTION(500,"系统发生错误"),
	URL_NOT_FOUND(404,"找不到路径");
	
    private int    code; //错误代码

    private String msg; //错误消息

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    private BaseErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}