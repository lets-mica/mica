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

## 扩展阅读
### AesUtil <-> js 互通
```javascript
/**
 * 使用：https://github.com/brix/crypto-js
 *
 * 或者直接导入下面这2个 js
 * <script src="https://cdn.bootcss.com/crypto-js/4.0.0/crypto-js.min.js"></script>
 * <script src="https://cdn.bootcss.com/crypto-js/4.0.0/aes.min.js"></script>
 */

/**
 * aes 解密方法，同java：AesUtil.decryptFormBase64ToString(encrypt, aesKey);
 */
function decrypt(data, key) {
    let keyBytes = CryptoJS.enc.Utf8.parse(key);
    let decrypted = CryptoJS.AES.decrypt(data, keyBytes, {
        iv: keyBytes,
        mode: CryptoJS.mode.CBC,
        padding: CryptoJS.pad.Pkcs7
    });
    return CryptoJS.enc.Utf8.stringify(decrypted);
}

/**
 * aes 加密方法，同java：AesUtil.encryptToBase64(text, aesKey);
 */
function encrypt(data, key) {
    let dataBytes = CryptoJS.enc.Utf8.parse(data);
    let keyBytes = CryptoJS.enc.Utf8.parse(key);
    let encrypted = CryptoJS.AES.encrypt(dataBytes, keyBytes, {
        iv: keyBytes,
        mode: CryptoJS.mode.CBC,
        padding: CryptoJS.pad.Pkcs7
    });
    return CryptoJS.enc.Base64.stringify(encrypted.ciphertext);
}

console.log('aes 加密', encrypt('我爱mica', 'dIK8YXdYaVasUyq1LvUoxCpgPKthHtAW'));
console.log('aes 解密', decrypt('m44gDqhWr86Vl1V0ORNShA==', 'dIK8YXdYaVasUyq1LvUoxCpgPKthHtAW'));
```

### AesUtil <-> dart 互通
```dart
import 'package:encrypt/encrypt.dart';

void main(List<String> arguments) {
  // 明文数据和密钥
  final plainText = '{"auPhone":"10008376857","auPassword":"123456"}';
  final plainKey = 'eELB8HbcgKWkndH0g9qRcBMZG5zgPkMc';

  // 初始化加密工具
  final key = Key.fromUtf8(plainKey);
  final iv = IV.fromUtf8(plainKey.substring(0, 16));
  final encrypter = Encrypter(AES(key, mode: AESMode.cbc));

  // dart 端加密
  final encrypted = encrypter.encrypt(plainText, iv: iv);
  print(encrypted.bytes);
  print(encrypted.base64);

  // 解密 java 端密文 AesUtil.encryptToBase64
  final javaEncryptedText = 'BLo1Q73NNBWMxpqBF66sKrI+PYfikhD5YU2O0A+tkBRuiy1FkPxSnxUHw5rV2QZK';
  final decrypted = encrypter.decrypt64(javaEncryptedText, iv: iv);

  print(decrypted);
}
```

### DesUtil <-> js 互通
```javascript
/**
 * 使用：https://github.com/brix/crypto-js
 *
 * 或者直接导入下面这2个 js
 * <script src="https://cdn.bootcss.com/crypto-js/4.0.0/crypto-js.min.js"></script>
 * <script src="https://cdn.bootcss.com/crypto-js/4.0.0/aes.min.js"></script>
 */

/**
 * des 解密方法，同java：DesUtil.encryptToBase64(text, aesKey)
 */
function encrypt(data, key) {
    var keyHex = CryptoJS.enc.Utf8.parse(key);
    var encrypted = CryptoJS.DES.encrypt(data, keyHex, {
        mode: CryptoJS.mode.ECB,
        padding: CryptoJS.pad.Pkcs7
    });
    return encrypted.toString();
}

/**
 * des 加密方法，同java：DesUtil.decryptFormBase64(encryptBase64, aesKey);
 */
function decrypt(data, key) {
    var keyHex = CryptoJS.enc.Utf8.parse(key);
    var decrypted = CryptoJS.DES.decrypt({
        ciphertext: CryptoJS.enc.Base64.parse(data)
    }, keyHex, {
        mode: CryptoJS.mode.ECB,
        padding: CryptoJS.pad.Pkcs7
    });
    return decrypted.toString(CryptoJS.enc.Utf8);
}

console.log('des 加密', encrypt('我爱mica', 'QLPz39vD2Nkf9A4E'));
console.log('des 解密', decrypt('49o5mK4KQGGAYY4IwoOG4A==', 'QLPz39vD2Nkf9A4E'));
```

### RsaUtil <-> js 互通
```javascript
/**
 * 使用：https://github.com/travist/jsencrypt
 *
 * 或者直接导入下面这个 js
 * <script src="https://cdn.bootcss.com/jsencrypt/2.3.0/jsencrypt.js"></script>
 */

/**
 * 前端加密
 *
 * <p>
 * 对应java解密方法为：
 * String decrypt = RsaUtil.decryptFromBase64(privateBase64, data);
 * </p>
 */
function encrypt(data, publicKey) {
    var encrypt = new JSEncrypt();
    encrypt.setPublicKey(publicKey);
    return encrypt.encrypt(data);
}

// 注意：解密老解不出来，不折腾了，谁有需求到时候再研究
```