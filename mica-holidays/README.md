# mica-holidays

`mica-holidays` 用来判断日期是否工作日，更具 php 版修改。支持2019年起的中国法定节假日（当前内置数据至2027年），以国务院发布的公告为准，随时调整及增加；http://www.gov.cn/zfwj/bgtfd.htm 或 http://www.gov.cn/zhengce/xxgkzl.htm

## 数据维护
内置数据位于 `src/main/resources/data/`，每个年份一个文件，如 `2027_data.json`，内容格式为 `{"MMDD": 0|1|2}`（0 工作日、1 休息日、2 节假日）。
启动时会自动扫描 classpath 下 `data/*_data.json` 并从文件名识别年份，**新增年份只需放入对应的 json 文件，无需修改代码**。
也可以通过 `mica.holidays.ext-data` 配置自定义数据文件，优先级高于内置数据。

## 使用
### maven
```xml
<dependency>
  <groupId>net.dreamlu</groupId>
  <artifactId>mica-holidays</artifactId>
  <version>${version}</version>
</dependency>
```

### gradle
```groovy
compile("net.dreamlu:mica-holidays:${version}")
```

### 注入 bean
```java
@Autowired
private HolidaysApi holidaysApi;
```

### 接口使用
```java
/**
 * 获取日期类型
 *
 * @param localDate LocalDate
 * @return DaysType
 */
DaysType getDaysType(LocalDate localDate);

/**
 * 获取日期类型
 *
 * @param localDateTime LocalDateTime
 * @return DaysType
 */
DaysType getDaysType(LocalDateTime localDateTime);

/**
 * 获取日期类型
 *
 * @param date Date
 * @return DaysType
 */
DaysType getDaysType(Date date);

/**
 * 判断是否工作日
 *
 * @param localDate LocalDate
 * @return 是否工作日
 */
boolean isWeekdays(LocalDate localDate);

/**
 * 判断是否工作日
 *
 * @param localDateTime LocalDateTime
 * @return 是否工作日
 */
boolean isWeekdays(LocalDateTime localDateTime);

/**
 * 判断是否工作日
 *
 * @param date Date
 * @return 是否工作日
 */
boolean isWeekdays(Date date);
```

## 链接
- holidays_api PHP 版：https://gitee.com/web/holidays_api