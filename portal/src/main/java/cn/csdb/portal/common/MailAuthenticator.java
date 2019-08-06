package cn.csdb.portal.common;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * Created by shibaoping on 2019/5/7.
 */
public class MailAuthenticator extends Authenticator {
    public static String USERNAME = "vdbcloud@cnic.cn";//zhuanxb@radi.ac.cn
    public static String PASSWORD = "sdc123456";//Radi2018

    public MailAuthenticator() {
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(USERNAME, PASSWORD);
    }
}
