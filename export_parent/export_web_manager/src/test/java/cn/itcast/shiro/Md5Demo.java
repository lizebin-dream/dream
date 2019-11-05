package cn.itcast.shiro;

import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * 演示Md5加盐加密
 */
public class Md5Demo {

    public static void main(String[] args) {
        String password = "123"; // 源密码
        String salt = "xiaocang@itcast.cn"; // 盐

        //1.创建Md5Hash对象
        /**
         * 参数一：源密码
         * 参数二：盐（可选的）
         */
        Md5Hash md5Hash = new Md5Hash(password,salt);

        //2.生成新密码
        String encodePwd = md5Hash.toString();

        System.out.println(encodePwd);

    }


}
