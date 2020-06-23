# mica 核心包

## 功能
1. Cglib Bean copy 增强，支持链式 bean、Map、优化性能和支持类型转换。
2. 表单枚举接收增强，同 jackson 保持一致。
3. 服务异常。
4. 统一返回模型。
5. 常用工具包。

## 添加依赖
### maven
```xml
<dependency>
  <groupId>net.dreamlu</groupId>
  <artifactId>mica-core</artifactId>
  <version>${version}</version>
</dependency>
```

### gradle
```groovy
compile("net.dreamlu:mica-core:${version}")
```

## 工具类说明

### 常量池
| 类名 | 说明 |
| ----- | ------ |
| Charsets | 字符集常量池 |
| CharPool | Char常量池 |
| StringPool | 字符串常量池 |

### 常用工具类
| 类名 | 说明 |
| ----- | ------ |
| StringUtil | 字符串工具类 |
| ObjectUtil | Object工具类 |
| JsonUtil | json处理工具 |
| DateUtil | 时间处理工具 |
| BeanUtil | bean处理工具 |
| NumberUtil | Number工具类 |
| CollectionUtil | 集合工具类 |
| FileUtil | File工具类 |
| IoUtil | io工具类 |

### 签名加密工具类
| 类名 | 说明 |
| ----- | ------ |
| HexUtil | hex工具类 |
| DigestUtil | md5、sha、hmc等 |
| Base64Util | Base64 |
| AesUtil | Aes |
| DesUtil | Des |
| RsaUtil | Rsa |

### 类、反射工具
| 类名 | 说明 |
| ----- | ------ |
| ClassUtil | 类工具类 |
| ConvertUtil | 类型转换工具类 |
| ReflectUtil | 反射工具类 |

### 线程、异常等工具类
| 类名 | 说明 |
| ----- | ------ |
| ThreadUtil | 线程工具类 |
| ThreadLocalUtil | 本地线程工具类 |
| Exceptions | 异常处理工具类 |
| Unchecked | lambda 异常包装类 |

### 其他工具类
| 类名 | 说明 |
| ----- | ------ |
| CountMap | 计数器 |
| Lazy | 延迟加载 |
| Once | 一次加载 |
| Holder | 部分常量 |
| Version | Version工具 |
| XmlHelper | xml处理 |
| INetUtil | 网络工具类 |
| PathUtil | 路径工具类 |
| ResourceUtil | 资源工具类 |
| SystemUtil | 系统工具类 |
| RuntimeUtil | 系统运行时工具类 |
| UrlUtil | url工具类 |
| WebUtil | web工具类 |
