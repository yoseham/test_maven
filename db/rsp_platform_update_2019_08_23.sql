CREATE TABLE `sys_rsp_algorithm_paramter` (
  `id` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT '参数id',
	`algorithm_id` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT '算法id',
  `paramter_type` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '参数类型',
  `paramter_name` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '参数名称',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` int(1) NOT NULL DEFAULT '0' COMMENT '状态，0未完成，1完成',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='算法参数表';

CREATE TABLE `sys_rsp_train_mode` (
  `id` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT 'id',
  `mode_name` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '训练模式名',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` int(1) NOT NULL DEFAULT '0' COMMENT '状态，0未完成，1完成',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='训练模式表';