package cn.csdb.portal.service;

import cn.csdb.portal.model.User;
import cn.csdb.portal.repository.UserDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {
    @Resource
    UserDao userDao;

    @Transactional
    public int addUser(User user)
    {
        int addedUserCnt = userDao.addUser(user);

        return addedUserCnt;
    }

    public List<User> queryUser(String loginId, String userName, String groups, int curUserPageNum, int pageSize)
    {

        return userDao.queryUser(loginId, userName, groups, curUserPageNum, pageSize);
    }

    public long getTotalUsers(String loginId, String userName, String groups)
    {
        return userDao.getTotalUsers(loginId, userName, groups);
    }

    /*@Transactional
    public int updateGroups(String loginId, String group) {
        return userDao.updateGroups(loginId, group);
    }*/


    @Transactional
    public int deleteUser(String id)
    {
        return userDao.deleteUser(id);
    }

    public User getUserById(String id)
    {
        return userDao.getUserById(id);

    }

    @Transactional
    public int updateUser(User user)
    {
        return userDao.updateUser(user);
    }

    /**
     * Function Description: 获取所有的用户
     *
     * @param:
     * @return:
     * @auther: xiajl
     * @date:   2018/11/5 15:16
     */
    @Transactional(readOnly = true)
    public List<User> getAll(){
        return userDao.getAll();
    }


    /**
     * Function Description: 获取指定角色的所有的用户
     *
     * @param:
     * @return:
     * @auther: xiajl
     * @date:   2018/11/26 14:09
     */
    @Transactional(readOnly = true)
    public List<User> getAllByRole(String role){
        return userDao.getAllbyRole(role);
    }

    public long queryLoginId(String loginId)
    {
        return userDao.queryLoginId(loginId);
    }

    public int validateLogin(String userName, String password) {
        int loginStatus = 0;
        loginStatus = userDao.validateLogin(userName, password);

        return loginStatus;
    }

    public User selectUserBySubjectCode(String subjectCode){
       return userDao.selectUserBySubjectCode(subjectCode);
    }
}
