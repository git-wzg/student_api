package com.fh.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.util.Date;
import java.util.Random;

public class AliyunOssUtils {
    /*日志*/
    public static final Logger logger = LoggerFactory.getLogger(AliyunOssUtils.class);

    private String endpoint = "oss-cn-beijing.aliyuncs.com";
    /**
     * accessKey
     */
    private String accessKeyId = "LTAI4FuZbopfvSqWmurFGDN1";
    /**
     * accessKeySecret
     */
    private String accessKeySecret = "FHro5BoLeEbYB6wEU42WNs4A9JHrzI";
    /**
     * 空间
     */
    private String bucketName = "oss-ssm-shop";
    /**
     * 文件存储目录
     */
    private String filedir = "file/";

    private OSS ossClient;
    /**
     * 设置文件的大小
     */
    private static final Integer MAX_SIZE = 1024 * 1024 * 10;

    public AliyunOssUtils(String endpoint, String accessKeyId, String accessKeySecret, String bucketName, String filedir) {
        ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        this.bucketName = bucketName;
        this.filedir = filedir;

    }

    public AliyunOssUtils() {
        ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

    }

    /**
     * 初始化
     */
    public void init() {
        ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }

    /**
     * 销毁
     */
    public void destory() {
        ossClient.shutdown();
    }

    /**
     * 上传图片
     *
     * @param url
     */
    public void uploadImg2Oss(String url) throws Exception {
        File fileOnServer = new File(url);
        FileInputStream fin;
        try {
            fin = new FileInputStream(fileOnServer);
            String[] split = url.split("/");
            this.uploadFile2OSS(fin, split[split.length - 1]);
        } catch (FileNotFoundException e) {
            throw new Exception("图片上传失败01");
        }
    }

    /**
     * 指定文件名为Id
     */
    public String uploadImg2Oss(MultipartFile file, Long id) {

        if (file.getSize() > MAX_SIZE) {
            return "";
        }
        //获取文件名
        String originalFilename = file.getOriginalFilename();
        //获取文件后缀名
        String substring = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        //判断图片格式是否正确
        if (!substring.equals(".jpg") && !substring.equals(".jpeg") && !substring.equals(".png")) {
            return "";
        }
        //设置文件名
        String name = id + substring;
        try {
            InputStream inputStream = file.getInputStream();
            //上传文件
            this.uploadFile2OSS(inputStream, name);
            return name;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 手动拼接访问路径
     *
     * @param name
     * @return
     */
    public String getImageUrl(String name) {
        String s = "https://" + bucketName + "." + endpoint + "/" + filedir + name;
        return s;
    }

    public String uploadImg2Oss(MultipartFile file) throws Exception {
        if (file.getSize() > 1024 * 1024 * 10) {
            throw new Exception("上传图片大小不能超过10M！");
        }
        String originalFilename = file.getOriginalFilename();
        String substring = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        Random random = new Random();
        String name = random.nextInt(10000) + System.currentTimeMillis() + substring;
        try {
            InputStream inputStream = file.getInputStream();
            this.uploadFile2OSS(inputStream, name);
            return name;
        } catch (Exception e) {
            throw new Exception("图片上传失败02");
        }
    }

    /**
     * 获得图片路径
     *
     * @param fileUrl
     * @return
     */
    public String getImgUrl(String fileUrl) {
        if (!StringUtils.isEmpty(fileUrl)) {
            String[] split = fileUrl.split("/");
            return this.getUrl(this.filedir + split[split.length - 1]);
        }
        return null;
    }

    /**
     * 上传到OSS服务器  如果同名文件会覆盖服务器上的
     *
     * @param instream 文件流
     * @param fileName 文件名称 包括后缀名
     * @return 出错返回"" ,唯一MD5数字签名
     */
    public String uploadFile2OSS(InputStream instream, String fileName) {
        String ret = "";
        try {
            //创建上传Object的Metadata
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(instream.available());
            objectMetadata.setCacheControl("no-cache");
            objectMetadata.setHeader("Pragma", "no-cache");
            objectMetadata.setContentType(getContentType(fileName.substring(fileName.lastIndexOf("."))));
            objectMetadata.setContentDisposition("inline;filename=" + fileName);
            //上传文件

            boolean b = ossClient.doesObjectExist(bucketName, filedir);
            if (b == false) {

            }
            PutObjectResult putResult = ossClient.putObject(bucketName, filedir + fileName, instream, objectMetadata);
            ret = putResult.getETag();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            try {
                if (instream != null) {
                    instream.close();
                }
            } catch (IOException e) {
                return "";
            }
        }
        return ret;
    }

    /**
     * Description: 判断OSS服务文件上传时文件的contentType
     *
     * @param FilenameExtension 文件后缀
     * @return String
     */
    public static String getContentType(String FilenameExtension) {
        if (FilenameExtension.equalsIgnoreCase(".bmp")) {
            return "image/bmp";
        }
        if (FilenameExtension.equalsIgnoreCase(".gif")) {
            return "image/gif";
        }
        if (FilenameExtension.equalsIgnoreCase(".jpeg") ||
                FilenameExtension.equalsIgnoreCase(".jpg") ||
                FilenameExtension.equalsIgnoreCase(".png")) {
            return "image/jpeg";
        }
        if (FilenameExtension.equalsIgnoreCase(".html")) {
            return "text/html";
        }
        if (FilenameExtension.equalsIgnoreCase(".txt")) {
            return "text/plain";
        }
        if (FilenameExtension.equalsIgnoreCase(".vsd")) {
            return "application/vnd.visio";
        }
        if (FilenameExtension.equalsIgnoreCase(".pptx") ||
                FilenameExtension.equalsIgnoreCase(".ppt")) {
            return "application/vnd.ms-powerpoint";
        }
        if (FilenameExtension.equalsIgnoreCase(".docx") ||
                FilenameExtension.equalsIgnoreCase(".doc")) {
            return "application/msword";
        }
        if (FilenameExtension.equalsIgnoreCase(".xml")) {
            return "text/xml";
        }
        return "image/jpeg";
    }

    /**
     * 获得url链接
     *
     * @param key
     * @return
     */
    public String getUrl(String key) {
        // 设置URL过期时间为10年  3600l* 1000*24*365*10
        Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 10);
        // 生成URL
        URL url = ossClient.generatePresignedUrl(bucketName, key, expiration);
        if (url != null) {
            return url.toString();
        }
        return null;
    }



}
