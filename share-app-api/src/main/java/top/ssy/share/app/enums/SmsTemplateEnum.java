package top.ssy.share.app.enums;

import lombok.Getter;

/**
 * @author Flobby
 */

@Getter
public enum SmsTemplateEnum {

    /**
     * 登录验证码
     */
    LOGIN_CODE_TEMPLATE("1", "登录验证码"),
    ;

    final String templateId;
    final String desc;

    SmsTemplateEnum(String templateId, String desc) {
        this.templateId = templateId;
        this.desc = desc;
    }
}