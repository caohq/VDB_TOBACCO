package cn.csdb.portal.common;


import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cas.CasRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;

public class NCasRealm extends CasRealm {

   /* @Resource
    private UserService userService;*/

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
//        String username = (String) principals.fromRealm(getName()).iterator().next();
        String username = (String) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        String roles ="admin";
        //        String roles = HTTPAccess.getRoles(username);
        /*User user = userService.getByLoginId(username);
        if (user == null) {
            return authorizationInfo;
        }
        String roles = user.getRoleName();*/
        /*Userlog userlog = new Userlog();
        userlog.setUsername(username);
        userlog.setOp("登录");
        userlog.setOptime(new Date());
        userlogService.insertUserlog(userlog);*/
        if (Strings.isNullOrEmpty(roles)) {
            return authorizationInfo;
        }
//        authorizationInfo.setRoles(Sets.newHashSet(JSON.parseArray(roles, String.class)));
        authorizationInfo.setRoles(Sets.newHashSet(roles));
        return authorizationInfo;
    }

}

