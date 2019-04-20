# cloud 增强包

## 功能
1. 本地开发，默认不注册到 eureka，避免影响联调环境。
2. feign 增强，添加自动 fallback 和 header 透传。
3. RestTemplate 统一配置，使用 okhttp 增强，添加请求日志和 header 透传。
4. hystrix 熔断器增强，支持 header 透传和当前用户获取获取透传。