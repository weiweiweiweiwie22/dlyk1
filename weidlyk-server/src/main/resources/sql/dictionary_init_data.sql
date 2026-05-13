-- 字典管理模块初始化数据脚本
-- 包含字典类型和字典值的初始数据

-- 1. 插入字典类型数据
INSERT INTO t_dic_type (type_code, type_name, remark) VALUES 
('appellation', '称呼', '客户称呼类型'),
('clueState', '线索状态', '线索的各种状态'),
('intentionProduct', '意向产品', '客户意向的产品类型'),
('needLoan', '是否需要贷款', '客户是否需要贷款'),
('intentionState', '意向状态', '客户意向的状态'),
('source', '线索来源', '线索的来源渠道'),
('noteWay', '跟踪方式', '跟踪客户的方式');

-- 2. 插入字典值数据

-- 称呼类型
INSERT INTO t_dic_value (type_code, type_value, `order`, remark) VALUES 
('appellation', '先生', 1, ''),
('appellation', '女士', 2, ''),
('appellation', '总经理', 3, ''),
('appellation', '董事长', 4, '');

-- 线索状态
INSERT INTO t_dic_value (type_code, type_value, `order`, remark) VALUES 
('clueState', '新建', 1, '新创建的线索'),
('clueState', '跟进中', 2, '正在跟进的线索'),
('clueState', '已成交', 3, '已经成交的线索'),
('clueState', '已失效', 4, '已失效的线索'),
('clueState', '暂停', 5, '暂停跟进的线索');

-- 意向产品
INSERT INTO t_dic_value (type_code, type_value, `order`, remark) VALUES 
('intentionProduct', '产品A', 1, ''),
('intentionProduct', '产品B', 2, ''),
('intentionProduct', '产品C', 3, ''),
('intentionProduct', '产品D', 4, '');

-- 是否需要贷款
INSERT INTO t_dic_value (type_code, type_value, `order`, remark) VALUES 
('needLoan', '需要', 1, ''),
('needLoan', '不需要', 2, ''),
('needLoan', '待定', 3, '');

-- 意向状态
INSERT INTO t_dic_value (type_code, type_value, `order`, remark) VALUES 
('intentionState', '强意向', 1, ''),
('intentionState', '中意向', 2, ''),
('intentionState', '弱意向', 3, ''),
('intentionState', '无意向', 4, '');

-- 线索来源
INSERT INTO t_dic_value (type_code, type_value, `order`, remark) VALUES 
('source', '电话', 1, ''),
('source', '邮件', 2, ''),
('source', '网站', 3, ''),
('source', '展会', 4, ''),
('source', '转介绍', 5, ''),
('source', '其他', 6, '');

-- 跟踪方式
INSERT INTO t_dic_value (type_code, type_value, `order`, remark) VALUES 
('noteWay', '电话', 1, ''),
('noteWay', '邮件', 2, ''),
('noteWay', '短信', 3, ''),
('noteWay', '微信', 4, ''),
('noteWay', '面访', 5, '');
