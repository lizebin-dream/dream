package cn.itcast.qiniu;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

/**
 * 演示七牛存储文件
 */
public class Demo {

    public static void main(String[] args) {
        //构造一个带指定 Region 对象的配置类
        //修改可用区域：Region.region2()
        Configuration cfg = new Configuration(Region.region0());
//...其他参数参考类注释

        UploadManager uploadManager = new UploadManager(cfg);
//...生成上传凭证，然后准备上传
        String accessKey = "Fp7U06205MZtAmZwwF3VdGxQNvMGzhCYTdBjwdaZ";
        String secretKey = "9vvOgetME6hmPH3S5tYadsVZKZJhDVVsz-jswdAc";
        //存储空间名称
        String bucket = "java116234545";
//如果是Windows情况下，格式是 D:\\qiniu\\test.png
        //上传图片内容
        String localFilePath = "e:/mm4.jpg";
//默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null;

        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);

        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }

    }

}
