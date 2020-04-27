DROP TABLE IF EXISTS `report`;

CREATE TABLE IF NOT EXISTS `report` (
    `year`            int(11)           NOT NULL COMMENT '년도',
    `month`           int(11)           NOT NULL COMMENT '월',
    `loan_small`      DECIMAL(20, 2)    NOT NULL COMMENT '중소기업 대출(조)',
    `loan_major`      DECIMAL(20, 2)    NOT NULL COMMENT '대기업 대출(조)',
    `loan_total`      DECIMAL(20, 2)    NOT NULL COMMENT '전체기업 대출(조)',
    `update_date`     timestamp    NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`year`, `month`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='기업대출현황';
