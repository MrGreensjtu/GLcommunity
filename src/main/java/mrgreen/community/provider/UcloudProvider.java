package mrgreen.community.provider;

import cn.ucloud.ufile.UfileClient;
import cn.ucloud.ufile.api.object.ObjectConfig;
import cn.ucloud.ufile.auth.ObjectAuthorization;
import cn.ucloud.ufile.auth.UfileObjectLocalAuthorization;
import cn.ucloud.ufile.bean.PutObjectResultBean;
import cn.ucloud.ufile.exception.UfileClientException;
import cn.ucloud.ufile.exception.UfileServerException;
import cn.ucloud.ufile.http.OnProgressListener;
import mrgreen.community.exception.CustomizeErrorCode;
import mrgreen.community.exception.CustomizeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.UUID;

/**
 * @author: mrgreen
 * @date: 2020/5/12
 * @description:
 */

@Service
public class UcloudProvider {
    @Value("${ucloud.ufile.public-key}")
    private String ucloudPublicKey;

    @Value("${ucloud.ufile.private-key}")
    private String ucloudPrivateKey;

    @Value("${ucloud.ufile.bucket-name}")
    private String bucketName;

    @Value("${ucloud.ufile.region}")
    private String region;

    @Value("${ucloud.ufile.proxy-suffix}")
    private String proxySuffix;

    @Value("${ucloud.ufile.expires-duration}")
    private Integer expiresDuration;


    public String upload(InputStream fileStream, String mimeType, String rawFileName){
        String fileName;
        String[] filePath = rawFileName.split("\\.");
        if(filePath.length > 1){
            fileName = UUID.randomUUID().toString() + "." + filePath[filePath.length-1];
        }else {
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        }
        try {
            ObjectAuthorization objectAuthorization = new UfileObjectLocalAuthorization(ucloudPublicKey, ucloudPrivateKey);
            ObjectConfig config = new ObjectConfig(region, proxySuffix);
            PutObjectResultBean response = UfileClient.object(objectAuthorization, config)
                    .putObject(fileStream, mimeType)
                    .nameAs(fileName)
                    .toBucket(bucketName)
                    .setOnProgressListener((bytesWritten, contentLength) -> { })
                    .execute();
            if(response != null && response.getRetCode() == 0){
                String url = UfileClient.object(objectAuthorization, config)
                        .getDownloadUrlFromPrivateBucket(fileName, bucketName, expiresDuration)
                        /**
                         * 使用Content-Disposition: attachment，并且默认文件名为KeyName
                         */
//                    .withAttachment()
                        /**
                         * 使用Content-Disposition: attachment，并且配置文件名
                         */
//                    .withAttachment("filename")
                        .createUrl();
                return url;
            }else{
                throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
            }

        } catch (UfileClientException e) {
            e.printStackTrace();
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        } catch (UfileServerException e) {
            e.printStackTrace();
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        }
    }
}
