# xxl-job stater

xxl-job 官方地址：https://github.com/xuxueli/xxl-job

xxl-job 官方文档：https://www.xuxueli.com/xxl-job/

## 添加依赖
### maven
```xml
<dependency>
  <groupId>net.dreamlu</groupId>
  <artifactId>mica-jobs</artifactId>
  <version>${version}</version>
</dependency>
```

### gradle
```groovy
compile("net.dreamlu:mica-jobs:${version}")
```

## 配置
| 配置项 | 默认值 | 说明 |
| ----- | ------ | ------ |
| xxl.job.enabled | true | 是否启用分布式调度任务，默认：开启 |
| xxl.job.admin.address |  | 调度中心地址，如调度中心集群部署存在多个地址则用逗号分隔。执行器将会使用该地址进行"执行器心跳注册"和"任务结果回调"；为空则关闭自动注册；支持配置，{@code lb:// + ${service_name}} 从注册中心动态获取地址 |
| xxl.job.admin.access-token |  | 与调度中心交互的accessToken，非空时启用 |
| xxl.job.admin.context-path |  | job admin 的 context-path |
| xxl.job.executor.app-name |  | 执行器名称，执行器心跳注册分组依据；为空则关闭自动注册 |
| xxl.job.executor.ip |  | 执行器 IP，默认为空表示自动获取IP，多网卡时可手动设置指定IP，该IP不会绑定Host仅作为通讯实用；地址信息用于 "执行器注册" 和 "调度中心请求并触发任务" |
| xxl.job.executor.log-path |  | 执行器日志位置 |
| xxl.job.executor.log-retention-days | -1 | 执行器日志保留天数，默认值：-1，值大于3时生效，启用执行器Log文件定期清理功能，否则不生效 |
| xxl.job.executor.port | -1 | 执行器端口，小于等于0则自动获取；默认端口为9999，单机部署多个执行器时，注意要配置不同执行器端口 |

