# #用户表
# DROP TABLE if exists t_user;
# CREATE TABLE t_user
# (
#     id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
#     user_name VARCHAR(20) NOT NULL,
#     pwd VARCHAR(150) NOT NULL
# );
# # 角色表
# DROP TABLE if exists t_role;
# CREATE TABLE t_role
# (
#     id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
#     role_name VARCHAR(20) NOT NULL,
#     description VARCHAR(128) NOT NULL
# );
# # 权限表
# DROP TABLE if exists t_permission;
# CREATE TABLE t_permission
# (
#     id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
#     permission_name VARCHAR(20) NOT NULL,
#     description VARCHAR(128) NOT NULL
# );
# # 用户角色关系表
# DROP TABLE if exists t_user_role;
# CREATE TABLE t_user_role
# (
#     id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
#     user_id INT NOT NULL,
#     role_id INT NOT NULL
# );
# # 角色权限关系表
# DROP TABLE if exists t_role_permission;
# CREATE TABLE t_role_permission
# (
#     id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
#     role_id INT NOT NULL,
#     permission_id INT NOT NULL
# );
#
#
