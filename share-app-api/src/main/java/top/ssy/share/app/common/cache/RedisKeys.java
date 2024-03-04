package top.ssy.share.app.common.cache;

/**
 * @author ycshang
 */
public class RedisKeys {

    /**
     * 验证码Key
     */
    public static String getCaptchaKey(String phone) {
        return "sys:captcha:" + phone;
    }

    /**
     * accessToken Key
     */
    public static String getAccessTokenKey(String accessToken) {
        return "sys:access:" + accessToken;
    }

    public static String getManagerIdKey(Integer id) {
        return "sys:user:" + id;
    }

}
