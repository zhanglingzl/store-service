#创建数据库
CREATE DATABASE IF NOT EXISTS store DEFAULT CHARSET utf8 COLLATE utf8_general_ci;
#初始化用户
INSERT INTO rxr_user VALUES (0,'sys',sysdate(),
                             '系统默认管理员',NULL,NULL,'','admin','超级管理员',
                             'e545c4349f6a4d5a9040adabc510fc52c5e3c74fc023dbcc8c4d4a94715c5fea',
                             'ed561d0470580ddb5eebf76f5d22223d',1,'',''
);