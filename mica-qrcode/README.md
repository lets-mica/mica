# mica-qrcode（二维码工具）

本模块参考 nutzmore（nutz-plugins-qrcode）进行了大量修改，使用更加简单，添加多个流和 byte 数组接口，对微服务更加友好。

## 功能
1. 服务端二维码生成
2. 服务端二维码识别
3. 二维码添加logo

## 依赖引用
### maven
```xml
<dependency>
  <groupId>net.dreamlu</groupId>
  <artifactId>mica-qrcode</artifactId>
  <version>${version}</version>
</dependency>
```

### gradle
```groovy
compile("net.dreamlu:mica-qrcode:${version}")
```

## 使用
```java
// 生成 二维码
QrCode.form("牛年大吉")
    .size(512) // 默认 512，可以不设置
    .backGroundColor(Color.WHITE) // 默认白色，可以不设置
    .foreGroundColor(Color.BLACK) // 默认黑色，可以不设置
    .encode(Charsets.UTF_8) // 默认 UTF_8，可以不设置
    .imageFormat("png") // 默认 png，可以不设置
    .deleteMargin(true) // 删除白边，默认为 true，可以不设置
    .logo("/Users/lcm/Desktop/mica-mqtt-01.png") // 设置二维码 logo，支持 URL 远程图片、文件和流
    .toFile("/Users/lcm/Desktop/xxx1.png"); // 写出，同类方法有 toImage、toStream、toBytes

// 二维码读取
String text = QrCode.read("/Users/lcm/Desktop/xxx1.png");
System.out.println(text);
```

## 参考
nutzmore（nutz-plugins-qrcode）: https://github.com/nutzam/nutzmore/tree/master/nutz-plugins-qrcode
