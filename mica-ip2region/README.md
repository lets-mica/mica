# mica-ip2region
`mica-ip2region` 是 `ip2region` 的封装，方便 `spring boot` 用户使用。

## 使用
### maven
```xml
<dependency>
  <groupId>net.dreamlu</groupId>
  <artifactId>mica-ip2region</artifactId>
  <version>${version}</version>
</dependency>
```

### gradle
```groovy
compile("net.dreamlu:mica-ip2region:${version}")
```

## 配置说明（已经内置，忽略）

| 配置项                           | 默认值                            | 说明                                |
| ------------------------------- | -------------------------------- | ---------------------------------- |
| mica.ip2region.db-file-location | classpath:ip2region/ip2region.db | ip2region.db 文件的地址，默认内置的文件 | 

**maven 自定义 ip2region.db 注意事项:**

**maven** `resources` 拷贝文件是默认会做 `filter`，会导致我们的文件发生变化，导致不能读，`pom` 中你需要添加下面的配置。

```xml
<plugin>
	<artifactId>maven-resources-plugin</artifactId>
	<configuration>
		<nonFilteredFileExtensions>
			<nonFilteredFileExtension>db</nonFilteredFileExtension>
		</nonFilteredFileExtensions>
	</configuration>
</plugin>
```

## 关于 ip2region

Gitee：https://gitee.com/lionsoul/ip2region

一般我们都会同步更新 `ip2region.db` 文件，不需要手动配置。

## 使用文档

### 注入 bean
```java
@Autowired
private Ip2regionSearcher regionSearcher;
```

### 方法说明

```java
/**
 * ip 位置 搜索
 *
 * @param ip ip
 * @return 位置
 */
@Nullable
IpInfo memorySearch(long ip);

/**
 * ip 位置 搜索
 *
 * @param ip ip
 * @return 位置
 */
@Nullable
IpInfo memorySearch(String ip);

/**
 * ip 位置 搜索
 *
 * @param ptr ptr
 * @return 位置
 */
@Nullable
IpInfo getByIndexPtr(long ptr);

/**
 * ip 位置 搜索
 *
 * @param ip ip
 * @return 位置
 */
@Nullable
IpInfo btreeSearch(long ip);

/**
 * ip 位置 搜索
 *
 * @param ip ip
 * @return 位置
 */
@Nullable
IpInfo btreeSearch(String ip);

/**
 * ip 位置 搜索
 *
 * @param ip ip
 * @return 位置
 */
@Nullable
IpInfo binarySearch(long ip);

/**
 * ip 位置 搜索
 *
 * @param ip ip
 * @return 位置
 */
@Nullable
IpInfo binarySearch(String ip);
```