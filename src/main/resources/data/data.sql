# insert into t_permission (permission_name, description) values ('all','拥有全部操作的权限');
# insert into t_permission (permission_name, description) values ('query','查看的权限');
# insert into t_permission (permission_name, description) VALUES ('save','添加资源的权限');
# insert into t_permission (permission_name, description) values ('del','删除资源的权限');
# insert into t_permission (permission_name, description) VALUES ('update','修改资源的权限');
#
# insert into t_role (role_name, description) VALUES ('admin','超级管理员');
# insert into t_role (role_name, description) VALUES ('test','拥有【查看】【添加】【修改】等权限');
# insert into t_role (role_name, description) values ('user','仅拥有【查看】权限');
#
# insert into t_user (user_name, pwd) VALUES ('mikasa','$2a$10$jhdMzFBgJ4/au2dm3Y0cBe36vtgcXFkUPtBVJv3vDDTKVHIIJG0zC');
# insert into t_user (user_name, pwd) VALUES ('test1','$2a$10$xMCTCE/qIraGsdq6S9EY3OvZpamDaVhKs4X/P9oOwzIPaG8tXun6K');
# insert into t_user (user_name, pwd) VALUES ('test2','$2a$10$khDPjc/QXiiK162Zn/lhCOp60xNSvljWMcyXuOUKHDmdTjJDzQ.sS');
# insert into t_user (user_name, pwd) VALUES ('lucy','$2a$10$lzp/TFLHjCFcw/XmTerWOegN6KqomNjy4mEeuEt1iROThuXaRaIUm');
# insert into t_user (user_name, pwd) VALUES ('tim','$2a$10$TWU6/.KBKPtfK/BpPLL77OETOMdIV94BiE40ytKnQuJCrRE6j91oa');
# insert into t_user (user_name, pwd)values ('wang','$2a$10$/0PA3PIJBxoU4gJ0LxgGjuiFD1jJrLkDjQjm1XGKJcbBbmu5cH0Zm');
#
# insert into t_user_role (user_id, role_id) VALUES (1,1);
# insert into t_user_role (user_id, role_id) VALUES (2,2);
# insert into t_user_role (user_id, role_id) values (3,2);
# insert into t_user_role (user_id, role_id) values (4,3);
# insert into t_user_role (user_id, role_id) values (5,3);
# insert into t_user_role (user_id, role_id) values (6,3);
#
# insert into t_role_permission (role_id, permission_id) VALUES (1,1);
# insert into t_role_permission (role_id, permission_id) VALUES (2,2);
# insert into t_role_permission (role_id, permission_id) VALUES (2,3);
# insert into t_role_permission (role_id, permission_id) VALUES (2,5);
# insert into t_role_permission (role_id, permission_id) VALUES (3,2);


select tp.permission_name from t_user_role ur left join t_role_permission trp on ur.role_id = trp.role_id left join t_permission tp on trp.permission_id = tp.id where ur.id = 1;