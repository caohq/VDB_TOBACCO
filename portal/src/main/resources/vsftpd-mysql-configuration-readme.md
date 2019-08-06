配置vsftpd从mysql中读取虚拟用户的方法
======================================
vsftpd的用户分为三种。
第一种是匿名用户，包括anonymous用户，ftp用户，他们都没有密码。
第二种是操作系统用户，也就说操作系统用户也可以作为vsftpd服务器的用户，去直接登录vsftpd。
第三种是虚拟用户，这种用户也是vsftpd专用的用户。
这里要配置的就是虚拟用户，就是第三种用户。
配置虚拟用户其实有两种方法，一种是把虚拟用户的用户名和密码写在本地文件中，第二种是把虚拟用户存在mysql数据中。
这里要介绍第二种方式。

配置之前请先装好vsftpd软件和mysql软件。

以下所有的操作都在root用户下完成。

1、创建供虚拟用户来映射的系统用户，并且设置系统用户家目录的权限
---------------------------------------------------------
配置这个的原因是，ftp是要用操作系统本地的目录存放文件的，所以虚拟用户实际上是通过一个操作系统用户来完成文件的读写的，
虚拟用户的虚拟二字也是这个含义，就是说它不是真实的用户，光有虚拟用户也不能直接存取操作系统的文件，只能让虚拟用户通过
实际的操作系统用户来操作操作系统的文件和目录。

useradd vftpuser
chmod 700 /home/vftpuser
usermod -g root vftpuser

2、在mysql中创建存放虚拟用户的数据库和表
-----------------------------------------
set password for 'root'@'localhost' = password();
create database vu_list;
create table vuser(id int auto_increment primary key, name char(30) not null, password char(30) not null);

3、配置虚拟用户的登录认证方式
------------------------------
vsftpd的用户登录vsftpd的时候，需要一个判断用户名和密码的过程，这个叫做认证。认证是通过pam这样一个模块实现的，
现在虚拟用户存放在mysql中，要想让虚拟用户登录的时候能够认证成功，得配置pam，让pam来访问mysql。
需要安装一个pam-mysql的so文件。
yum install pam-devel
wget https://nchc.dl.sourceforge.net/project/pam-mysql/pam-mysql/0.7RC1/pam_mysql-0.7RC1.tar.gz
tar -zxvf pam_mysql-0.7RC1.tar.gz 
cd pam_mysql-0.7RC1
./configure --with-mysql=/usr --with-pam-mods-dir=/lib64/security/
make && make install

vim /etc/pam.d/vu_pam.mysql
auth required /lib64/security/pam_mysql.so user=root passwd=123456, host=localhost  db=vu_list table=vuser usercolumn=name passwdcolumn=password crypt=0
account required /lib64/security/pam_mysql.so user=root passwd=123456, host=localhost db=vu_list table=vuser usercolumn=name passwdcolumn=password crypt=0

4、修改vsftpd的主配置文件vsftpd.conf
------------------------------------
为vsftpd.conf中增加配置项
guest_enable=YES
guest_username=vftpuser
以下修改vsftpd.conf中的下列配置项
pam_service_name=vu_pam.mysql

5、重新启动vsftpd服务器
-------------------------
service vsftpd restart



