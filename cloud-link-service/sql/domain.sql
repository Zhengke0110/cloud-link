CREATE TABLE `domain`
(
    `id`           bigint unsigned NOT NULL,
    `account_no`   bigint                                                 DEFAULT NULL COMMENT '用户自己绑定的域名',
    `domain_type`  varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  DEFAULT NULL COMMENT '域名类型，自建custom, 官方offical',
    `value`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
    `del`          int(1) unsigned zerofill DEFAULT '0' COMMENT '0是默认，1是禁用',
    `gmt_create`   datetime                                               DEFAULT CURRENT_TIMESTAMP,
    `gmt_modified` datetime                                               DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
