# mica-ddddocr

带带弟弟 OCR 通用验证码识别 Java 版本，基于 ONNX Runtime 实现 OCR 识别功能

## 使用

### Maven 依赖

```xml
<dependency>
  <groupId>net.dreamlu</groupId>
  <artifactId>mica-ddddocr-core</artifactId>
  <version>${version}</version>
</dependency>
```

### Spring Bean 注入

```java
@Autowired
private OCREngine ocrEngine;
```

### 验证码识别

```java
String result = ocrEngine.detectBase64Image(base64Image);
```

## 链接

- [ddddocr python原版](https://github.com/sml2h3/ddddocr)
