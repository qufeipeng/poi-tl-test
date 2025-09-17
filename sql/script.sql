CREATE database test;

use test;

CREATE table report_records(
                               id bigint NOT NULL AUTO_INCREMENT,
                               title varchar(100) NULL,
                               content varchar(1000) NULL,
                               record_type varchar(20) NULL,
                               author varchar(20) NULL,
                               create_time datetime DEFAULT CURRENT_TIMESTAMP,
                               PRIMARY KEY (id) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

INSERT INTO report_records(title,content,record_type,author,create_time) values
                                                                             ('招商永隆银行', 'POC中,8月7日启动,总体完成80%', 'sales', '卢智凌', '2025-08-07'),
                                                                             ('邢台银行股份有限公司', 'POC进行中，完成75%', 'support', '周国超', '2025-09-05'),
                                                                             ('山东城商行', 'POC待启动，暂定9月15日开始poc，应用场景只有24、25、26才有时间能开始测试，根据这个时间计算，在15号可以开始运维场景测试，然后进行应用场景测试，总体完成0%', 'feedback', '张利民', '2025-09-15');

SELECT id, title, content, record_type, author, create_time
FROM report_records;