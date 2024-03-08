package top.ssy.share.app.service;

import org.springframework.web.multipart.MultipartFile;
import top.ssy.share.app.vo.FileUrlVO;

/**
 * @author : Flobby
 * @program : share-app-api
 * @description : sms
 * @create : 2024-03-04 12:29
 **/

public interface CommonService {

    /**
     * 发送短信
     *
     * @param phone                电话
     */
    void sendSms(String phone);

    FileUrlVO upload(MultipartFile file);
}
