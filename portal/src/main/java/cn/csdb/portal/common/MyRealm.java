package cn.csdb.portal.common;

import cn.csdb.portal.model.User;
import cn.csdb.portal.service.CheckUserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by shiba on 2018/11/6.
 */
public class MyRealm extends AuthorizingRealm {

    @Autowired
    private CheckUserService CheckUserService;

    /**
     * 授权方法
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        /**
         * 注意principals.getPrimaryPrincipal()对应
         * new SimpleAuthenticationInfo(user.getUserName(), user.getPassword(), getName())的第一个参数
         */
        //获取当前身份

        String userName = (String) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        //从数据库中查找该用户有何角色和权限
        User user = CheckUserService.getByUserName(userName);
/*
        Set<String> permissions = userService.getPermissions(userName);
*/

        //为当前用户赋予对应角色和权限
            if (user.getRole().equals("系统管理员")) {
                Set<String> roles = new HashSet<>();
                roles.add("root");
                info.setRoles(roles);
            } else if (user.getRole().equals("数据节点管理员")) {
                Set<String> roles = new HashSet<>();
                roles.add("admin");
                info.setRoles(roles);
            } else {

            }
        return info;
    }

    /**
     * 认证方法
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //获取用户名
        String LoginId = (String) token.getPrincipal();

        //从数据库中查找用户信息
        User user = CheckUserService.getByUserName(LoginId);
        if (user == null) {
            return null;
        }
        AuthenticationInfo info = new SimpleAuthenticationInfo(user.getLoginId(), user.getPassword(), getName());
        return info;
    }
}
