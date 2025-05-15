# user-service

```plantuml
skinparam linetype ortho

class user_info  <<用户信息>> {
    id bigint 主键
    username varchar(128) 用户名
    vir_mac varchar(64) 虚拟机mac地址
    version integer 版本号
    create_by varchar(128) 创建人
    create_time datetime 创建时间
    update_by varchar(128) 更新人
    update_time datetime 更新时间
    del_flag tinyint(1) 删除标识
}
```