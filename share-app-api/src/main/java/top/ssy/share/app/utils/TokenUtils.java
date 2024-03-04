package top.ssy.share.app.utils;

import cn.hutool.core.lang.UUID;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ycshang
 */
public class TokenUtils {

    private static final Logger logger = LoggerFactory.getLogger(TokenUtils.class);
     
    /**
     * 生成 AccessToken
     */
    public static String generator() {
        return UUID.fastUUID().toString(true);
    }

    /**
     * 获取 AccessToken
     */
    public static String getAccessToken(HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization");
        if (StringUtils.isBlank(accessToken)) {
            accessToken = request.getParameter("accessToken");
        }
        logger.info("accessToken: {}", accessToken);
        return accessToken;
    }
}
