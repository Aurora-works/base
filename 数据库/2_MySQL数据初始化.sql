use base_db;
-- delete
set foreign_key_checks = 0;
delete from t_system_dict;
delete from t_system_error_log;
delete from t_system_generate_code;
delete from t_system_menu;
delete from t_system_request_log;
delete from t_system_param;
delete from t_system_role;
delete from t_system_role_menu;
delete from t_system_table;
delete from t_system_table_column;
delete from t_system_user;
delete from t_system_user_role;
set foreign_key_checks = 1;

-- insert
set @sysdate = sysdate(3);
-- 系统用户表
insert into t_system_user(id, username, password, salt, nickname, sex, email, mobile_phone_number, status, user_type, is_deleted, description, create_time, create_user_id, version) values
(1, 'admin', 'f045f7394e091b30b35ca79c9d8ff8a6efcd0666c541b819e92c242139d2c7bc', '944f7a6293ac19da1fdf54a3e60c3b41', '超级管理员', '1', 'admin@abc.com', '10000000000', '1', 'ADMIN', '0', '超级管理员', @sysdate, 1, 0),
(2, 'todo', null, null, '未登录用户', null, null, null, '1', 'TODO', '0', '未登录用户标识', @sysdate, 1, 0);

-- 系统角色表
insert into t_system_role(id, role_code, role_name, parent_id, order_by, status, create_time, create_user_id, version) values
(101, 'ADMIN', '超级管理员', null, '001', '1', @sysdate, 1, 0),
(102, 'SYS_MGR', '系统管理员', 101, '001001', '1', @sysdate, 1, 0),
(103, 'SYS_MGR_A', '系统管理员A', 101, '001002', '1', @sysdate, 1, 0),
(104, 'SYS_MGR_B', '系统管理员B', 101, '001003', '1', @sysdate, 1, 0),
(105, 'MGR', '管理员', 104, '001003001', '1', @sysdate, 1, 0);

-- 系统用户和系统角色的关系表
insert into t_system_user_role(id, user_id, role_id, create_time, create_user_id, version) values
(201, 1, 101, @sysdate, 1, 0);

-- 系统功能菜单表
insert into t_system_menu(id, menu_code, menu_name, href, parent_id, order_by, css, is_open, status, description, create_time, create_user_id, version) values
(301, 'sys', '系统管理', null, null, '991', 'icon-sum', '1', '1', '父级', @sysdate, 1, 0),
(302, 'sys_user', '人员管理', 'sys/user/list', 301, '991001', 'icon-filter', '1', '1', '子菜单', @sysdate, 1, 0),
(303, 'sys_role', '角色管理', 'sys/role/list', 301, '991002', 'icon-filter', '1', '1', '子菜单', @sysdate, 1, 0),
(304, 'sys_menu', '菜单管理', 'sys/menu/list', 301, '991003', 'icon-filter', '1', '1', '子菜单', @sysdate, 1, 0),
(305, 'sys_auth', '权限管理', 'sys/auth/list', 301, '991004', 'icon-filter', '1', '1', '子菜单', @sysdate, 1, 0),
(306, 'log', '系统日志', null, null, '992', 'icon-sum', '1', '1', '父级', @sysdate, 1, 0),
(307, 'sys_request', '操作日志', 'log/request/list', 306, '992001', 'icon-filter', '1', '1', '子菜单', @sysdate, 1, 0),
(308, 'sys_error', '错误记录', 'log/error/list', 306, '992002', 'icon-filter', '1', '1', '子菜单', @sysdate, 1, 0),
(309, 'dev', '开发中心', null, null, '993', 'icon-sum', '1', '1', '父级', @sysdate, 1, 0),
(310, 'sys_dict', '数据字典', 'dev/dict/list', 309, '993001', 'icon-filter', '1', '1', '子菜单', @sysdate, 1, 0),
(311, 'sys_param', '系统参数', 'dev/param/list', 309, '993002', 'icon-filter', '1', '1', '子菜单', @sysdate, 1, 0),
(312, 'sys_table', '系统表单', 'dev/table/list', 309, '993003', 'icon-filter', '1', '1', '子菜单', @sysdate, 1, 0),
(313, 'sys_generate', '生成代码', 'dev/generate/list', 309, '993004', 'icon-filter', '1', '1', '子菜单', @sysdate, 1, 0);

-- 系统角色对系统功能菜单的操作权限表
insert into t_system_role_menu(id, create_op, update_op, delete_op, read_op, role_id, menu_id, create_time, create_user_id, version) values
(401, '1', '1', '1', '1', 101, 302, @sysdate, 1, 0),
(402, '1', '1', '1', '1', 101, 303, @sysdate, 1, 0),
(403, '1', '1', '1', '1', 101, 304, @sysdate, 1, 0),
(404, '1', '1', '1', '1', 101, 305, @sysdate, 1, 0),
(405, '1', '1', '1', '1', 101, 307, @sysdate, 1, 0),
(406, '1', '1', '1', '1', 101, 308, @sysdate, 1, 0),
(407, '1', '1', '1', '1', 101, 310, @sysdate, 1, 0),
(408, '1', '1', '1', '1', 101, 311, @sysdate, 1, 0),
(409, '1', '1', '1', '1', 101, 312, @sysdate, 1, 0),
(410, '1', '1', '1', '1', 101, 313, @sysdate, 1, 0);

-- 系统数据字典表
insert into t_system_dict(id, dict_code, dict_key, dict_value, order_by, status, description, create_time, create_user_id, version) values
(501, 'STATUS', '1', '启用', '1', '1', '系统数据状态', @sysdate, 1, 0),
(502, 'STATUS', '0', '停用', '2', '1', '系统数据状态', @sysdate, 1, 0),
(503, 'YES_OR_NO', '1', '是', '1', '1', '是否', @sysdate, 1, 0),
(504, 'YES_OR_NO', '0', '否', '2', '1', '是否', @sysdate, 1, 0),
(505, 'SEX', '1', '男', '1', '1', '性别', @sysdate, 1, 0),
(506, 'SEX', '0', '女', '2', '1', '性别', @sysdate, 1, 0),
(507, 'COLUMN_TYPE', 'Integer', 'Integer', '1', '1', '数据类型', @sysdate, 1, 0),
(508, 'COLUMN_TYPE', 'Long', 'Long', '2', '1', '数据类型', @sysdate, 1, 0),
(509, 'COLUMN_TYPE', 'String', 'String', '3', '1', '数据类型', @sysdate, 1, 0),
(510, 'COLUMN_TYPE', 'LocalDate', 'LocalDate', '4', '1', '数据类型', @sysdate, 1, 0),
(511, 'COLUMN_TYPE', 'LocalTime', 'LocalTime', '5', '1', '数据类型', @sysdate, 1, 0),
(512, 'COLUMN_TYPE', 'LocalDateTime', 'LocalDateTime', '6', '1', '数据类型', @sysdate, 1, 0),
(513, 'COLUMN_TYPE', 'Boolean', 'Boolean', '7', '1', '数据类型', @sysdate, 1, 0),
(514, 'COLUMN_TYPE', 'Double', 'Double', '8', '1', '数据类型', @sysdate, 1, 0),
(515, 'COLUMN_TYPE', 'BigDecimal', 'BigDecimal', '9', '1', '数据类型', @sysdate, 1, 0),
(516, 'USER_TYPE', 'ADMIN', '系统管理员', '1', '1', '用户类型', @sysdate, 1, 0),
(517, 'USER_TYPE', 'USER', '普通用户', '2', '1', '用户类型', @sysdate, 1, 0),
(518, 'USER_TYPE', 'TODO', '系统留用', '3', '1', '用户类型', @sysdate, 1, 0);

-- 系统参数表
insert into t_system_param(id, param_code, param_desc, param_value, description, create_time, create_user_id, version) values
(601, 'SYS_DEFAULT_PASSWORD', '系统默认密码', '123456', '新增、重置系统用户时使用的默认密码', @sysdate, 1, 0),
(602, 'SYS_DEFAULT_THEMES', '系统默认主题', 'gray', '用户进入系统默认加载的主题\n取值范围：black/bootstrap/default/gray/material/material-blue/material-teal/metro', @sysdate, 1, 0),
(603, 'SYS_DEFAULT_TABLE_COLUMN', '默认添加字段', 'table_base_entity', '系统建表时默认添加的字段所在表的表名', @sysdate, 1, 0),
(604, 'SYS_GENERATE_PATH', '代码生成目录', 'D:/SystemGenerateCode', '生成代码文件的本地文件夹路径', @sysdate, 1, 0),
(605, 'SYS_PROJECT_NAME', '项目名称', 'base', '项目名称/包名', @sysdate, 1, 0);

-- 系统数据库表单表
insert into t_system_table(id, table_desc, table_name, entity_name, is_real, create_time, create_user_id, version) values
(701, '建表时默认添加的字段', 'table_base_entity', 'BaseEntity', '0', @sysdate, 1, 0),
(702, '系统用户表', 't_system_user', 'SysUser', '1', @sysdate, 1, 0),
(703, '系统角色表', 't_system_role', 'SysRole', '1', @sysdate, 1, 0),
(704, '系统功能菜单表', 't_system_menu', 'SysMenu', '1', @sysdate, 1, 0),
(705, '系统用户和系统角色的关系表', 't_system_user_role', 'SysUserRole', '1', @sysdate, 1, 0),
(706, '系统角色对系统功能菜单的操作权限表', 't_system_role_menu', 'SysRoleMenu', '1', @sysdate, 1, 0),
(707, '系统数据字典表', 't_system_dict', 'SysDict', '1', @sysdate, 1, 0),
(708, '系统参数表', 't_system_param', 'SysParam', '1', @sysdate, 1, 0),
(709, '系统操作日志表', 't_system_request_log', 'SysRequestLog', '1', @sysdate, 1, 0),
(710, '系统错误记录表', 't_system_error_log', 'SysErrorLog', '1', @sysdate, 1, 0),
(711, '系统数据库表单表', 't_system_table', 'SysTable', '1', @sysdate, 1, 0),
(712, '系统数据库表单字段表', 't_system_table_column', 'SysTableColumn', '1', @sysdate, 1, 0),
(713, '系统生成代码表', 't_system_generate_code', 'SysGenerateCode', '1', @sysdate, 1, 0);

-- 系统数据库表单字段表
insert into t_system_table_column(id, table_id, column_name, display_name, entity_name, column_type, column_length, is_primary, is_nullable, is_unique, is_insertable, is_updatable, order_by, foreign_table_id, dict_code, create_time, create_user_id, version) values
(801, 701, 'id', '编号', 'id', 'Long', null, '1', '0', '1', '1', '1', '000', null, null, @sysdate, 1, 0),
(802, 701, 'description', '描述', 'description', 'String', null, '0', '1', '0', '1', '1', '994', null, null, @sysdate, 1, 0),
(803, 701, 'create_time', '创建时间', 'createTime', 'LocalDateTime', null, '0', '1', '0', '1', '0', '995', null, null, @sysdate, 1, 0),
(804, 701, 'create_user_id', '创建人', 'createUserId', 'Long', null, '0', '1', '0', '1', '0', '996', 702, null, @sysdate, 1, 0),
(805, 701, 'last_time', '最后修改时间', 'lastTime', 'LocalDateTime', null, '0', '1', '0', '0', '1', '997', null, null, @sysdate, 1, 0),
(806, 701, 'last_user_id', '最后修改人', 'lastUserId', 'Long', null, '0', '1', '0', '0', '1', '998', 702, null, @sysdate, 1, 0),
(807, 701, 'version', '版本', 'version', 'Integer', null, '0', '0', '0', '0', '1', '999', null, null, @sysdate, 1, 0),
-- 系统用户表
(901, 702, 'username', '用户名', 'username', 'String', '50', '0', '0', '1', '1', '1', '001', null, null, @sysdate, 1, 0),
(902, 702, 'password', '密码', 'password', 'String', '64', '0', '1', '0', '1', '1', '002', null, null, @sysdate, 1, 0),
(903, 702, 'salt', '加密验证', 'salt', 'String', '32', '0', '1', '0', '1', '1', '003', null, null, @sysdate, 1, 0),
(904, 702, 'nickname', '姓名', 'nickname', 'String', '50', '0', '0', '0', '1', '1', '004', null, null, @sysdate, 1, 0),
(905, 702, 'sex', '性别', 'sex', 'String', '20', '0', '1', '0', '1', '1', '005', null, 'SEX', @sysdate, 1, 0),
(906, 702, 'email', '电子邮件', 'email', 'String', '50', '0', '1', '0', '1', '1', '006', null, null, @sysdate, 1, 0),
(907, 702, 'mobile_phone_number', '手机号码', 'mobilePhoneNumber', 'String', '11', '0', '0', '1', '1', '1', '007', null, null, @sysdate, 1, 0),
(908, 702, 'avatar_image_path', '头像', 'avatarImagePath', 'String', null, '0', '1', '0', '1', '1', '008', null, null, @sysdate, 1, 0),
(909, 702, 'status', '状态', 'status', 'String', '20', '0', '0', '0', '1', '1', '009', null, 'STATUS', @sysdate, 1, 0),
(910, 702, 'user_type', '用户类型', 'userType', 'String', '20', '0', '0', '0', '1', '1', '010', null, 'USER_TYPE', @sysdate, 1, 0),
(911, 702, 'is_deleted', '是否删除', 'isDeleted', 'String', '20', '0', '0', '0', '1', '1', '011', null, 'YES_OR_NO', @sysdate, 1, 0),
-- 系统角色表
(1001, 703, 'role_code', '角色编码', 'roleCode', 'String', '50', '0', '0', '1', '1', '1', '001', null, null, @sysdate, 1, 0),
(1002, 703, 'role_name', '角色名称', 'roleName', 'String', '50', '0', '0', '0', '1', '1', '002', null, null, @sysdate, 1, 0),
(1003, 703, 'parent_id', '父角色', 'parentId', 'Long', null, '0', '1', '0', '1', '1', '003', '703', null, @sysdate, 1, 0),
(1004, 703, 'order_by', '排序', 'orderBy', 'String', '20', '0', '0', '0', '1', '1', '004', null, null, @sysdate, 1, 0),
(1005, 703, 'status', '状态', 'status', 'String', '20', '0', '0', '0', '1', '1', '005', null, 'STATUS', @sysdate, 1, 0),
-- 系统功能菜单表
(1101, 704, 'menu_code', '菜单编码', 'menuCode', 'String', '50', '0', '0', '1', '1', '1', '001', null, null, @sysdate, 1, 0),
(1102, 704, 'menu_name', '菜单名称', 'menuName', 'String', '50', '0', '0', '0', '1', '1', '002', null, null, @sysdate, 1, 0),
(1103, 704, 'href', '菜单链接', 'href', 'String', null, '0', '1', '0', '1', '1', '003', null, null, @sysdate, 1, 0),
(1104, 704, 'parent_id', '父菜单', 'parentId', 'Long', null, '0', '1', '0', '1', '1', '004', '704', null, @sysdate, 1, 0),
(1105, 704, 'order_by', '排序', 'orderBy', 'String', '20', '0', '0', '0', '1', '1', '005', null, null, @sysdate, 1, 0),
(1106, 704, 'css', '菜单图标', 'css', 'String', null, '0', '1', '0', '1', '1', '006', null, null, @sysdate, 1, 0),
(1107, 704, 'is_open', '默认展开', 'isOpen', 'String', '20', '0', '0', '0', '1', '1', '007', null, 'YES_OR_NO', @sysdate, 1, 0),
(1108, 704, 'status', '状态', 'status', 'String', '20', '0', '0', '0', '1', '1', '008', null, 'STATUS', @sysdate, 1, 0),
-- 系统用户和系统角色的关系表
(1201, 705, 'user_id', '用户', 'userId', 'Long', null, '0', '0', '0', '1', '1', '001', '702', null, @sysdate, 1, 0),
(1202, 705, 'role_id', '角色', 'roleId', 'Long', null, '0', '0', '0', '1', '1', '002', '703', null, @sysdate, 1, 0),
-- 系统角色对系统功能菜单的操作权限表
(1301, 706, 'create_op', '新增权限', 'createOp', 'String', '20', '0', '0', '0', '1', '1', '001', null, 'STATUS', @sysdate, 1, 0),
(1302, 706, 'update_op', '修改权限', 'updateOp', 'String', '20', '0', '0', '0', '1', '1', '002', null, 'STATUS', @sysdate, 1, 0),
(1303, 706, 'delete_op', '删除权限', 'deleteOp', 'String', '20', '0', '0', '0', '1', '1', '003', null, 'STATUS', @sysdate, 1, 0),
(1304, 706, 'read_op', '查询权限', 'readOp', 'String', '20', '0', '0', '0', '1', '1', '004', null, 'STATUS', @sysdate, 1, 0),
(1305, 706, 'role_id', '角色', 'roleId', 'Long', null, '0', '0', '0', '1', '1', '005', '703', null, @sysdate, 1, 0),
(1306, 706, 'menu_id', '菜单', 'menuId', 'Long', null, '0', '0', '0', '1', '1', '006', '704', null, @sysdate, 1, 0),
-- 系统数据字典表
(1401, 707, 'dict_code', '字典编码', 'dictCode', 'String', '50', '0', '0', '0', '1', '1', '001', null, null, @sysdate, 1, 0),
(1402, 707, 'dict_key', '字典键', 'dictKey', 'String', '20', '0', '0', '0', '1', '1', '002', null, null, @sysdate, 1, 0),
(1403, 707, 'dict_value', '字典值', 'dictValue', 'String', '50', '0', '0', '0', '1', '1', '003', null, null, @sysdate, 1, 0),
(1404, 707, 'order_by', '排序', 'orderBy', 'String', '20', '0', '0', '0', '1', '1', '004', null, null, @sysdate, 1, 0),
(1405, 707, 'status', '状态', 'status', 'String', '20', '0', '0', '0', '1', '1', '005', null, 'STATUS', @sysdate, 1, 0),
-- 系统参数表
(1501, 708, 'param_code', '参数编码', 'paramCode', 'String', '50', '0', '0', '1', '1', '1', '001', null, null, @sysdate, 1, 0),
(1502, 708, 'param_desc', '参数名称', 'paramDesc', 'String', '50', '0', '0', '1', '1', '1', '002', null, null, @sysdate, 1, 0),
(1503, 708, 'param_value', '参数值', 'paramValue', 'String', null, '0', '0', '0', '1', '1', '003', null, null, @sysdate, 1, 0),
-- 系统操作日志表
(1601, 709, 'request_controller', '请求方法', 'requestController', 'String', null, '0', '1', '0', '1', '1', '001', null, null, @sysdate, 1, 0),
(1602, 709, 'request_url', 'URL', 'requestUrl', 'String', null, '0', '1', '0', '1', '1', '002', null, null, @sysdate, 1, 0),
(1603, 709, 'request_method', '请求方式', 'requestMethod', 'String', '10', '0', '1', '0', '1', '1', '003', null, null, @sysdate, 1, 0),
(1604, 709, 'request_ip', 'IP', 'requestIp', 'String', '50', '0', '1', '0', '1', '1', '004', null, null, @sysdate, 1, 0),
(1605, 709, 'request_parameters', '参数列表', 'requestParameters', 'String', null, '0', '1', '0', '1', '1', '005', null, null, @sysdate, 1, 0),
-- 系统错误记录表
(1701, 710, 'error_name', '异常名称', 'errorName', 'String', null, '0', '1', '0', '1', '1', '001', null, null, @sysdate, 1, 0),
(1702, 710, 'error_stack_trace', '堆栈跟踪', 'errorStackTrace', 'String', null, '0', '1', '0', '1', '1', '002', null, null, @sysdate, 1, 0),
(1703, 710, 'error_controller', '异常方法', 'errorController', 'String', null, '0', '1', '0', '1', '1', '003', null, null, @sysdate, 1, 0),
(1704, 710, 'request_url', 'URL', 'requestUrl', 'String', null, '0', '1', '0', '1', '1', '004', null, null, @sysdate, 1, 0),
(1705, 710, 'request_method', '请求方式', 'requestMethod', 'String', '10', '0', '1', '0', '1', '1', '005', null, null, @sysdate, 1, 0),
(1706, 710, 'request_ip', 'IP', 'requestIp', 'String', '50', '0', '1', '0', '1', '1', '006', null, null, @sysdate, 1, 0),
(1707, 710, 'request_parameters', '参数列表', 'requestParameters', 'String', null, '0', '1', '0', '1', '1', '007', null, null, @sysdate, 1, 0),
-- 系统数据库表单表
(1801, 711, 'table_desc', '表描述', 'tableDesc', 'String', '50', '0', '0', '0', '1', '1', '001', null, null, @sysdate, 1, 0),
(1802, 711, 'table_name', '表名', 'tableName', 'String', '50', '0', '0', '1', '1', '1', '002', null, null, @sysdate, 1, 0),
(1803, 711, 'entity_name', '实体类名', 'entityName', 'String', '50', '0', '0', '1', '1', '1', '003', null, null, @sysdate, 1, 0),
(1804, 711, 'is_real', '是否实体表', 'isReal', 'String', '20', '0', '0', '0', '1', '1', '004', null, 'YES_OR_NO', @sysdate, 1, 0),
-- 系统数据库表单字段表
(1901, 712, 'table_id', '表单', 'tableId', 'Long', null, '0', '0', '0', '1', '1', '001', '711', null, @sysdate, 1, 0),
(1902, 712, 'column_name', '字段名称', 'columnName', 'String', '50', '0', '0', '0', '1', '1', '002', null, null, @sysdate, 1, 0),
(1903, 712, 'display_name', '显示名称', 'displayName', 'String', '50', '0', '0', '0', '1', '1', '003', null, null, @sysdate, 1, 0),
(1904, 712, 'entity_name', '实体类字段名', 'entityName', 'String', '50', '0', '0', '0', '1', '1', '004', null, null, @sysdate, 1, 0),
(1905, 712, 'column_type', '字段类型', 'columnType', 'String', '20', '0', '0', '0', '1', '1', '005', null, 'COLUMN_TYPE', @sysdate, 1, 0),
(1906, 712, 'column_length', '字段长度', 'columnLength', 'String', '50', '0', '1', '0', '1', '1', '006', null, null, @sysdate, 1, 0),
(1907, 712, 'is_primary', '是否主键', 'isPrimary', 'String', '20', '0', '0', '0', '1', '1', '007', null, 'YES_OR_NO', @sysdate, 1, 0),
(1908, 712, 'is_nullable', '可否空值', 'isNullable', 'String', '20', '0', '0', '0', '1', '1', '008', null, 'YES_OR_NO', @sysdate, 1, 0),
(1909, 712, 'is_unique', '是否唯一', 'isUnique', 'String', '20', '0', '0', '0', '1', '1', '009', null, 'YES_OR_NO', @sysdate, 1, 0),
(1910, 712, 'is_insertable', '可插入', 'isInsertable', 'String', '20', '0', '0', '0', '1', '1', '010', null, 'YES_OR_NO', @sysdate, 1, 0),
(1911, 712, 'is_updatable', '可修改', 'isUpdatable', 'String', '20', '0', '0', '0', '1', '1', '011', null, 'YES_OR_NO', @sysdate, 1, 0),
(1912, 712, 'order_by', '排序', 'orderBy', 'String', '20', '0', '0', '0', '1', '1', '012', null, null, @sysdate, 1, 0),
(1913, 712, 'foreign_table_id', '外键表', 'foreignTableId', 'Long', null, '0', '1', '0', '1', '1', '013', '711', null, @sysdate, 1, 0),
(1914, 712, 'dict_code', '数据字典', 'dictCode', 'String', '50', '0', '1', '0', '1', '1', '014', null, null, @sysdate, 1, 0),
-- 系统生成代码表
(2001, 713, 'menu_id', '菜单', 'menuId', 'Long', null, '0', '0', '0', '1', '1', '001', '704', null, @sysdate, 1, 0),
(2002, 713, 'table_id', '表单', 'tableId', 'Long', null, '0', '0', '0', '1', '1', '002', '711', null, @sysdate, 1, 0);