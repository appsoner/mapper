#maven 依赖

```xml
<dependency>
    <groupId>io.terminus</groupId>
    <artifactId>mapper</artifactId>
    <version>1.0.0</version>
</dependency>
```


#使用

##1 配置yml
这里主要提供对`springboot`的使用。在`sqlSessionFactory`已经创建的前提下，只需在yml中配置

```yml
mapper:
  basePackage: com.domain.dao 
  //扫描mapper的位置，多个可以进行逗号分隔，或com.(domain|domain1|domain2).dao
```

## 2. 在`basePackage`指定的包下创建mapper,并继承通用的`Mapper<T>`,必须指定泛型`<T>`

例如下面的例子:

```java

public interface ShopAddressMapper extends Mapper<ShopAddressMapper> {

}
```	


一旦继承了`Mapper<T>`,继承的`Mapper`就拥有了`Mapper<T>`所有的通用方法。

##2. 泛型(实体类)`<T>`的类型必须符合要求

实体类按照如下规则和数据库表进行转换,注解全部是JPA中的注解:

1. 表名默认使用类名,驼峰转下划线小写(),如`shopAddress`默认对应的表名为`shop_address`。

2. 表名可以使用`@Table(name = "tableName")`进行指定,对不符合第一条默认规则的可以通过这种方式指定表名.

3. 字段默认和`@Column`一样,都会作为表字段,表字段默认为Java对象的`Field`名字驼峰转下划线形式.

4. 可以使用`@Column(name = "fieldName")`指定不符合第3条规则的字段名

5. 使用`@Transient`注解可以忽略字段,添加该注解的字段不会作为表字段使用.

6. <b>建议一定是有一个`@Id`注解作为主键的字段,可以有多个`@Id`注解的字段作为联合主键.</b>

7. <b>默认情况下,实体类中如果不存在包含`@Id`注解的字段,所有的字段都会作为主键字段进行使用(这种效率极低).</b>

8. <b>主键生成，`@GeneratedValue` 默认取`generator = "JDBC"` 形式，用于mysql主键自增</b>

9. <b>一个bean的示例</b>

```java

@Data
@Table(name = "shop_addresses")
public class ShopAddress implements Serializable {
    private static final long serialVersionUID = 485791797593817519L;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Id
    @GeneratedValue
    private Long id;

    private Long shopId;

    private String shopName;

    private String phone;

    private String province;

    private String city;

    private String region;

    private String street;

    private String detail;

    private Integer status;

    @Setter(AccessLevel.NONE)
    @JsonIgnore
    private String extraJson;

    @Setter(AccessLevel.NONE)
    @Transient
    private Map<String, String> extra;

    private Date createdAt;

    private Date updatedAt;

    @SneakyThrows
    public void setExtra(Map<String, String> extra) {
        this.extra = extra;
        if (extra == null || extra.isEmpty()) {
            this.extraJson = null;
        } else {
            this.extraJson = OBJECT_MAPPER.writeValueAsString(extra);
        }
    }

    @SneakyThrows
    public void setExtraJson(String extraJson) {
        this.extraJson = extraJson;
        if (Strings.isNullOrEmpty(extraJson)) {
            this.extra = Collections.emptyMap();
        } else {
            this.extra = OBJECT_MAPPER.readValue(extraJson,Map.class);
        }
    }
}

```

##3 注入mapper

```java

public class ShopAddressSerivice {
	@AutoWired
    private ShopAddressMapper shopAddressMapper;
}

```
这样就可以使用mapper中的方法。

#Mapper 提供的方法

##查询
1. `List<T> select(T record)` 根据实体中的非空字段进行where and等号查询
2. `T selectByPrimaryKey(Object key);` 根据主键查询
3. `List<T> selectAll();` 查询所有纪录
4. `List<T> selectByExample(Object example)` 根据`example`对象进行查询
##分页查询
1. `PageBean<T> pagingByExample(Example example,  Integer pageNo, Integer pageSize);`
根据`example`进行分页查询
2. `PageBean<T> pagingByEntity(Object entity, I3. nteger pageNo, Integer pageSize);`
根据实体类中的非空字段进行分页查询
3.`PageBean`是一个分页bean，通过`PageBean`的`getPaging()`方法可以得到`package io.terminus.common.model;`的`Paging`对象
##插入
1.`int insert(T record)`;
为空的字段将被忽略
2. `int insertList(List<T> recordList);`
##更新
1.`int updateByPrimaryKey(T record);`根据主键更新，为空的字段忽略
2.`int updateByExample(T record, Object example);`根据`example`条件进行更新，忽略`record`中为空的字段
##删除
1.`int delete(T record);`根据非空属性进行删除
2.`int deleteByPrimaryKey(Object key);`根据主键进行删除






