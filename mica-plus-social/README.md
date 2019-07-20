# 第三方登录组件

`mica-plus-social`，是 `mica-social` 的自动配置组件。

## 添加依赖
### maven
```xml
<dependency>
  <groupId>net.dreamlu</groupId>
  <artifactId>mica-plus-social</artifactId>
  <version>${version}</version>
</dependency>
```

### gradle
```groovy
compile("net.dreamlu:mica-plus-social:${version}")
```

### 配置
```yaml
mica:
  social:
    qq:
      client-id: xxxxxx
      client-secret: xxxxx
      redirect-uri: http://www.dreamlu.net/api/qq/callback
```

### 构造授权地址
```java
@Autowired
private AuthRequestFactory requestFactory;


@GetMapping("auth/{source}")
public String auth(@PathVariable String source) {
	AuthRequest authRequest = requestFactory.get(source);
    return "redirect:" + authRequest.authorize();
}

@GetMapping("callback/{source}")
public String callback(@PathVariable String source, String code) {
	AuthRequest authRequest = requestFactory.get(source);
	AuthResponse authResponse = authRequest.login(code);
    // 业务代码
    
}
```

### API 列表
|  :computer: source(平台)  | 测试通过 | 类 |
|:------:|--------|--------|
|  gitee  | ✔️ | AuthGiteeRequest |
|  github  | ✔️ | AuthGithubRequest |
|  weibo  | ✔️ | AuthWeiboRequest |
|  dingtalk  | ✔️ | AuthDingTalkRequest |
|  baidu  | ✔️ | AuthBaiduRequest |
|  coding  | 未申请应用 | AuthCodingRequest |
|  tencent_cloud  | ✔️ | AuthTencentCloudRequest |
|  oschina  | ✔️ | AuthOschinaRequest |
|  alipay  | 未申请应用 | AuthAlipayRequest |
|  qq  | ✔️ | AuthQqRequest |
|  wechat  | ✔️ | AuthWeChatRequest |
|  taobao  | 未申请应用 | AuthTaobaoRequest |
|  google  | ✔️ | AuthGoogleRequest |
|  facebook  | ✔️ | AuthFacebookRequest |
|  douyin  | 未申请应用 | AuthDouyinRequest |
|  linkedin  | ✔️ | AuthLinkedinRequest |
|  microsoft  | ✔️ | AuthMicrosoftRequest |
|  mi  | ✔️ | AuthMiRequest |
|  toutiao  | 未申请应用 | AuthToutiaoRequest |
|  csdn  | 未申请应用 | AuthCsdnRequest |
|  teambition  | ✔️ | AuthTeambitionRequest |
|  renren  | ✔️ | AuthRenrenRequest |
|  pinterest  | ✔️️ | AuthPinterestRequest |
|  stack_overflow  | ✔️ | AuthStackOverflowRequest |

`注`：CSDN 的授权开放平台已经下线。so, 本项目中的 CSDN 登录只能针对少部分用户使用。
