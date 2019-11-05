package cn.itcast.web.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * 自定义凭证匹配器
 */
public class CustomCredentialsMatcher extends SimpleCredentialsMatcher{

    /**
     * 编写凭证匹配逻辑
     * @param token  用户输入的登录信息
     * @param info Realm返回的封装数据库的信息
     * @return true: 密码一致
     *         false  密码不一致
     */
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {

        //1.从token获取用户输入用户名（盐）和密码（源密码）
        UsernamePasswordToken userToken = (UsernamePasswordToken)token;
        String email = userToken.getUsername();//用户名（盐）
        String password = new String(userToken.getPassword());//密码

        //2.使用Md5Hash对源密码进行加盐加密，得到新密码
        Md5Hash md5Hash = new Md5Hash(password,email);
        String encodePwd = md5Hash.toString();

        //3.从info取出数据库的密码（加密的）
        //getCredentials: 取出AuthRealm的SimpleAuthenticationInfo对象的第二个参数的内容
        String dbPwd = (String)info.getCredentials();

        //4.使用第二步新密码和第三步的数据库密码比较，如果相等，返回true；不相等则返回false
        return encodePwd.equals(dbPwd);
    }
}
