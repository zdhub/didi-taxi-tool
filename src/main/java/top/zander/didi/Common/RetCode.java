package top.zander.didi.Common;


/**
 * Created by cuiwenming on 16/5/23.
 */
public enum  RetCode {

    SUC(1, "成功"),
    FAILED(-1, "失败"),
    UNKNOWN(-2,"未知错误"),
    EMPTY(-3,"数据为空"),
    NOSUCHTEMPLATE(-4, "没有该模板"),
    USERNOTEXIST(-5, "用户不存在"),
    PICTURE_NOT_UPLOAD(-6, "图片未上传"),
    PICTURE_PAGE_INVALID(-7, "该页面已过期，请查看最新上传的图片"),
    PICTURE_PAGE_APPROVE_FAILED(-8, "该图片已被确认不合格，页面已无效"),
    NO_LOGIN(302, "用户未登录");

    public Integer code;
    public String value;

    RetCode(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static RetCode codeof(Integer code) throws EnumException {
        for (RetCode retCode : RetCode.values()) {
            if (retCode.code.equals(code)) {
                return retCode;
            }
        }
        throw new EnumException("RetCode " + code + " is not exist");
    }
    @Override
    public String toString() {
        return "RetCode{" +
                "code=" + code +
                ", value='" + value + '\'' +
                '}';
    }
}
