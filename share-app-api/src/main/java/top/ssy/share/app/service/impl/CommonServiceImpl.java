package top.ssy.share.app.service.impl;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.cloopen.rest.sdk.BodyType;
import com.cloopen.rest.sdk.CCPRestSmsSDK;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.ssy.share.app.common.cache.RedisCache;
import top.ssy.share.app.common.cache.RedisKeys;
import top.ssy.share.app.common.config.CloopenConfig;
import top.ssy.share.app.common.exception.ErrorCode;
import top.ssy.share.app.common.exception.ServerException;
import top.ssy.share.app.enums.SmsTemplateEnum;
import top.ssy.share.app.service.CommonService;
import top.ssy.share.app.utils.CommonUtils;
import top.ssy.share.app.vo.FileUrlVO;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

/**
 * @author : Flobby
 * @program : share-app-api
 * @description : impl
 * @create : 2024-03-04 12:31
 **/

@Slf4j
@Service
public class CommonServiceImpl implements CommonService {
    @Resource
    private CloopenConfig cloopenConfig;
    @Resource
    private RedisCache redisCache;

    private static final String[] IMAGE_TYPE = new String[]{".bmp", ".jpg",
            ".jpeg", ".gif", ".png"};

    @Resource
    private OSSClient ossClient;

    @Value("${aliyun.oss.bucketName}")
    private String bucketName;

    @Override
    public String sendSms(String phone) {
        if (!CommonUtils.checkPhone(phone)) {
            throw new ServerException(ErrorCode.PARAMS_ERROR);
        }
        int code = CommonUtils.generateCode();
        redisCache.set(RedisKeys.getSmsKey(phone), code, 60);
        // boolean result = cloopenSendSms(phone, code, SmsTemplateEnum.LOGIN_CODE_TEMPLATE);
        // if (result) {
        //     log.info(" ============= 短信发送成功 ============= ");
        // }
        return String.valueOf(code);
    }

    @Override
    public FileUrlVO upload(MultipartFile uploadFile) {

        String returnImgeUrl = "";

        // 校验图片格式
        boolean isLegal = false;
        for (String type : IMAGE_TYPE) {
            if (StringUtils.endsWithIgnoreCase(uploadFile.getOriginalFilename(), type)) {
                isLegal = true;
                break;
            }
        }
        if (!isLegal) {// 如果图片格式不合法
            throw new ServerException("图片格式不正确");
        }

        // 获取文件原名称
        String originalFilename = uploadFile.getOriginalFilename();
        // 获取文件类型
        String fileType = originalFilename.substring(originalFilename.lastIndexOf("."));
        // 新文件名称
        String newFileName = UUID.randomUUID().toString() + fileType;
        // 构建日期路径, 例如：OSS目标文件夹/2020/10/31/文件名
        String filePath = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
        // 文件上传的路径地址
        String uploadImgeUrl = filePath + "/" + newFileName;

        // 获取文件输入流
        InputStream inputStream = null;
        try {
            inputStream = uploadFile.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /**
         * 现在阿里云OSS 默认图片上传ContentType是image/jpeg
         * 也就是说，获取图片链接后，图片是下载链接，而并非在线浏览链接，
         * 因此，这里在上传的时候要解决ContentType的问题，将其改为image/jpg
         */
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentType("image/jpg");

        //文件上传至阿里云OSS
        ossClient.putObject(bucketName, uploadImgeUrl, inputStream, meta);
        // 获取文件上传后的图片返回地址
        returnImgeUrl = "https://" + bucketName + "." + ossClient.getEndpoint().getHost() + "/" + uploadImgeUrl;



        return new FileUrlVO(returnImgeUrl);


    }



    /**
     * cloopen 发送短信
     *
     * @param phone 电话
     * @param code  验证码
     * @return boolean
     */
    private boolean cloopenSendSms(String phone, int code, SmsTemplateEnum templateEnum) {
        try {
            log.info(" ============= 创建短信发送通道中 ============= \nphone is {},code is {}", phone, code);
            String serverIp = cloopenConfig.getServerIp();
            // 请求端口
            String serverPort = cloopenConfig.getPort();
            // 主账号,登陆云通讯网站后,可在控制台首页看到开发者主账号ACCOUNT SID和主账号令牌AUTH TOKEN
            String accountSId = cloopenConfig.getAccountSId();
            String accountToken = cloopenConfig.getAccountToken();
            // 请使用管理控制台中已创建应用的APPID
            String appId = cloopenConfig.getAppId();
            CCPRestSmsSDK sdk = new CCPRestSmsSDK();
            sdk.init(serverIp, serverPort);
            sdk.setAccount(accountSId, accountToken);
            sdk.setAppId(appId);
            sdk.setBodyType(BodyType.Type_JSON);
            // TODO 测试号，只能给这个手机发短信
            String to = "15729611641";
            String templateId = templateEnum.getTemplateId();
            String[] datas = {String.valueOf(code), "1"};
            HashMap<String, Object> result = sdk.sendTemplateSMS(to, templateId, datas, "1234", UUID.randomUUID().toString());
            if ("000000".equals(result.get("statusCode"))) {
                // 正常返回输出data包体信息（map）
                HashMap<String, Object> data = (HashMap<String, Object>) result.get("data");
                Set<String> keySet = data.keySet();
                for (String key : keySet) {
                    Object object = data.get(key);
                    log.info(key + " = " + object);
                }
                log.info(" ============= 短信已经发送成功 ============= ");
            } else {
                // 异常返回输出错误码和错误信息
                log.error("错误码=" + result.get("statusCode") + " 错误信息= " + result.get("statusMsg"));
                throw new ServerException(ErrorCode.CODE_SEND_FAIL);
            }
        } catch (Exception e) {
            throw new ServerException(ErrorCode.CODE_SEND_FAIL);
        }
        return true;
    }

}
