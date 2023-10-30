# mica-holidays

`mica-holidays` 用来判断日期是否工作日，更具 php 版修改。支持2019年起至2024年 中国法定节假日，以国务院发布的公告为准，随时调整及增加；http://www.gov.cn/zfwj/bgtfd.htm 或 http://www.gov.cn/zhengce/xxgkzl.htm

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