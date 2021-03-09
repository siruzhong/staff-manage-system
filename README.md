# staff-manage-system


---

@[TOC](目录)

**项目成效图**：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201006001816440.gif#pic_center)

---


# （一）环境搭建

## 1. 新建一个SpringBoot项目
![image-20200929115311891](https://img-blog.csdnimg.cn/img_convert/c666f0039e2b5e569788f164b640406e.png)
![image-20200929115509112](https://img-blog.csdnimg.cn/img_convert/452f5c6ca4057ba5d119e7efec57fd7a.png)
 选择配件时勾选`SpringWeb`和`Thymeleaf`
![image-20200929120314188](https://img-blog.csdnimg.cn/img_convert/61abb70cb97d0039d6c2f396949a86d4.png)
点击`next`，然后`finish`创建完成即可

---
## 2. 导入静态资源	

> 首先创建不存在的**静态资源目录**`public`和`resources`
>
> <img src="https://gitee.com/zhong_siru/images/raw/master//img/image-20201001104802315.png" alt="image-20201001104802315" style="zoom:50%;" />

将`html`静态资源放置`templates`目录下
<img src="https://gitee.com/zhong_siru/images/raw/master//img/image-20201002002900913.png" alt="image-20201002002900913" style="zoom: 50%;" />
将`asserts`目录下的`css`、`img`、`js`等静态资源放置`static`目录下
<img src="https://gitee.com/zhong_siru/images/raw/master//img/image-20201002002957401.png" alt="image-20201002002957401" style="zoom:50%;" />

---

## 3. 模拟数据库

### 1. 创建数据库实体类

> 在主程序同级目录下新建`pojo`包，用来存放实体类
>
> 在`pojo`包下创建一个部门表`Department`和一个员工表`Employee`
>
> <img src="https://gitee.com/zhong_siru/images/raw/master//img/image-20201003203826040.png" alt="image-20201003203826040" style="zoom:50%;" />
>
> 为了方便，我们导入`lombok`
>
> ```xml
> <dependency>
>  <groupId>org.projectlombok</groupId>
>  <artifactId>lombok</artifactId>
> </dependency>
> ```

**部门表**：

```java
package com.zsr.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//部门表
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Department {
    private Integer id;
    private String departmentName;
}
```

**员工表**：

```java
package com.zsr.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

//员工表
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    private Integer id;
    private String lastName;
    private String email;
    private Integer gender;//0:女 1:男
    private Department department;
    private Date date;
}
```

### 2. 编写dao层（模拟数据）

> 在主程序同级目录下新建`dao`包
>
> 然后分别编写`DepartmentDao`和`EmployeeDao`，并在其中模拟数据库的数据
>
> <img src="https://gitee.com/zhong_siru/images/raw/master//img/image-20201003204632860.png" alt="image-20201003204632860" style="zoom: 67%;" />

`DepartmentDao`：

```java
package com.zsr.dao;

import com.zsr.pojo.Department;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

//注册到IOC容器中
@Repository
public class DepartmentDao {
    //模拟数据库中的数据
    private static Map<Integer, Department> departments = null;

    static {
        departments = new HashMap<>();//创建一个部门表
        departments.put(1, new Department(1, "技术部"));
        departments.put(2, new Department(2, "市场部"));
        departments.put(3, new Department(3, "调研部"));
        departments.put(4, new Department(4, "后勤部"));
        departments.put(5, new Department(5, "运营部"));
    }

    //获得部门的所有信息
    public Collection<Department> departments() {
        return departments.values();
    }

    //通过id得到部门
    public Department getDepartmentById(int id) {
        return departments.get(id);
    }
}
```

**EmployeeDao**:

```java
package com.zsr.dao;

import com.zsr.pojo.Department;
import com.zsr.pojo.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//注册到IOC容器中
@Repository
public class EmployeeDao {
    //模拟数据库中员工表的数据
    static private Map<Integer, Employee> employees;
    
    @Autowired//自动
    private DepartmentDao departmentDao;

    static {
        employees = new HashMap<>();//创建一个员工表
        employees.put(1, new Employee(1, "zsr", "1234@qq.com", 1, new Department(1, "技术部"), new Date()));
        employees.put(2, new Employee(2, "lyr", "1345@qq.com", 1, new Department(2, "市场部"), new Date()));
        employees.put(3, new Employee(3, "gcc", "5665@qq.com", 0, new Department(3, "调研部"), new Date()));
        employees.put(4, new Employee(4, "zyx", "7688@qq.com", 1, new Department(4, "后勤部"), new Date()));
        employees.put(5, new Employee(5, "zch", "8089@qq.com", 1, new Department(5, "运营部"), new Date()));
    }

    //主键自增
    private static Integer initialID = 6;

    //增加一个员工
    public void addEmployee(Employee employee) {
        if (employee.getId() == null)
            employee.setId(initialID);
        employee.setDepartment(departmentDao.getDepartmentById(employee.getDepartment().getId()));
        employees.put(employee.getId(), employee);
    }

    //查询全部员工信息
    public Collection<Employee> getAllEmployees() {
        return employees.values();
    }

    //通过id查询员工
    public Employee getEmployeeByID(Integer id) {
        return employees.get(id);
    }

    //通过id删除员工
    public void deleteEmployeeByID(int id) {
        employees.remove(id);
    }
}
```
---
<br>


# （二）首页实现

> 在主程序同级目录下新建`config`包用来存放自己的配置类
>
> 在其中新建一个自己的配置类`MyMvcConfig`，进行视图跳转
>
> <img src="https://gitee.com/zhong_siru/images/raw/master//img/image-20201003204653826.png" alt="image-20201003204653826" style="zoom:50%;" />

```java
package com.zsr.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyMvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/index.html").setViewName("index");
    }
}
```

我们启动主程序访问测试一下，访问`localhost:8080/`或者`locahost:8080/index.html`

出现以下页面则成功
![image-20201003204716720](https://img-blog.csdnimg.cn/img_convert/01f86621f3fdb22378625c4da0a9908f.png)

> 上述测试可以看到页面有图片没有加载出来，且没有css和js的样式，这就是因为我们`html`页面中静态资源引入的语法出了问题，在SpringBoot中，推荐使用`Thymeleaf`作为模板引擎，我们将其中的语法改为`Thymeleaf`，所有页面的静态资源都需要使用其接管

注意所有html都需要引入`Thymeleaf`命名空间

```xml
xmlns:th="http://www.thymeleaf.org"
```

然后修改所有页面静态资源的引入，使用`@{...}` 链接表达式

例如`index.html`中：

**注意**：第一个`/`代表项目的classpath，也就是这里的`resources`目录
![image-20201004110530910](https://img-blog.csdnimg.cn/img_convert/725a549a1c04d8c948151129ae4eec28.png)
其他页面亦是如此，再次测试访问，正确显示页面
![image-20201003210843605](https://img-blog.csdnimg.cn/img_convert/3aead6e5aed69acf9ecece8e6fa13989.png)

---
<br>


# （三）页面国际化

## 1. 统一properties编码

> 首先在IDEA中统一设置properties的编码为`UTF-8`
![image-20201002155450905](https://img-blog.csdnimg.cn/img_convert/efef5e8c592ac44eae2305a78821f0bb.png)
---
## 2. 编写i18n国际化资源文件

> 在`resources`目录下新建一个`i18n`包，其中放置国际化相关的配置
>
> ![image-20201002111459210](https://img-blog.csdnimg.cn/img_convert/c9c298787d493735c83b668dfac02807.png)
>
> 其中新建三个配置文件，用来配置语言：
>
> + `login.properties`：无语言配置时候生效
> + `login_en_US.properties`：英文生效
> + `login_zh_CN.properties`：中文生效

**命名方式**是下划线的组合：文件名`_`语言`_`国家.properties；

以此方式命名，IDEA会帮我们识别这是个国际化配置包，自动绑定在一起转换成如下的模式：
<img src="https://gitee.com/zhong_siru/images/raw/master//img/image-20201002112113378.png" alt="image-20201002112113378" style="zoom:67%;" />
绑定在一起后，我们想要添加更过语言配置，只需要在大的资源包**右键添加到该绑定配置文件**即可
![image-20201002124319575](https://img-blog.csdnimg.cn/img_convert/f9968720c0714690c5a49213480e68dc.png)
此时只需要输入`区域名`即可创建成功，比如输入`en_US`，就会自动识别
<img src="https://gitee.com/zhong_siru/images/raw/master//img/image-20201002124645801.png" alt="image-20201002124645801" style="zoom:67%;" />
然后打开英文或者中文语言的配置文件，点击`Resource Bundle`进入可视化编辑页面
![image-20201002123244011](https://img-blog.csdnimg.cn/img_convert/d91ca83c5a2f675bf404b28be30496bb.png)
进入到可视化编辑页面后，点击加号，添加属性，首先新建一个`login.tip`代表首页中的提示![image-20201002125032293](https://img-blog.csdnimg.cn/img_convert/1a3386aed1bdf3375fabdf7b57161779.png)
然后对该提示分别做三种情况的语言配置，在三个对应的输入框输入即可（**注意：IDEA2020.1可能无法保存，建议直接在配置文件中编写**）
![image-20201002125224669](https://img-blog.csdnimg.cn/img_convert/43fa01fce78a552860be88158fe1e495.png)
接下来再配置所有要转换语言的变量（**注意：IDEA2020.1可能无法保存，建议直接在配置文件中编写**）
![image-20201002154806923](https://img-blog.csdnimg.cn/img_convert/5c236ba96c8c20fa7cd8955192e94159.png)
然后打开三个配置文件的查看其中的文本内容，可以看到已经做好了全部的配置

`login.properties`

```properties
login.tip=请登录
login.password=密码
login.remember=记住我
login.btn=登录
login.username=用户名
```

`login_en_US.properties`

```properties
login.tip=Please sign in
login.password=password
login.remember=remember me
login.btn=login
login.username=username
```

`login_zh_CN.properties`

```properties
login.tip=请登录
login.password=密码
login.remember=记住我
login.btn=登录
login.username=用户名
```
---
## 3. 配置国际化资源文件名称

> 在Spring程序中，国际化主要是通过`ResourceBundleMessageSource`这个类来实现的
>
> Spring Boot通过`MessageSourceAutoConfiguration`为我们自动配置好了管理国际化资源文件的组件

我们在IDEA中查看以下`MessageSourceAutoConfiguration`类

```java
@Configuration(proxyBeanMethods = false)
@ConditionalOnMissingBean(name = AbstractApplicationContext.MESSAGE_SOURCE_BEAN_NAME, search = SearchStrategy.CURRENT)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@Conditional(ResourceBundleCondition.class)
@EnableConfigurationProperties
public class MessageSourceAutoConfiguration {

	private static final Resource[] NO_RESOURCES = {};

	@Bean
	@ConfigurationProperties(prefix = "spring.messages")
	public MessageSourceProperties messageSourceProperties() {
		return new MessageSourceProperties();
	}

	@Bean
	public MessageSource messageSource(MessageSourceProperties properties) {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		if (StringUtils.hasText(properties.getBasename())) {
			messageSource.setBasenames(StringUtils
					.commaDelimitedListToStringArray(StringUtils.trimAllWhitespace(properties.getBasename())));
		}
		if (properties.getEncoding() != null) {
			messageSource.setDefaultEncoding(properties.getEncoding().name());
		}
		messageSource.setFallbackToSystemLocale(properties.isFallbackToSystemLocale());
		Duration cacheDuration = properties.getCacheDuration();
		if (cacheDuration != null) {
			messageSource.setCacheMillis(cacheDuration.toMillis());
		}
		messageSource.setAlwaysUseMessageFormat(properties.isAlwaysUseMessageFormat());
		messageSource.setUseCodeAsDefaultMessage(properties.isUseCodeAsDefaultMessage());
		return messageSource;
	}
	//......
}
```

主要了解messageSource()这个方法：

```java
public MessageSource messageSource(MessageSourceProperties properties);
```

可以看到，它的参数为`MessageSourceProperties`对象，我们看看这个类

```java
public class MessageSourceProperties {

	/**
	 * Comma-separated list of basenames (essentially a fully-qualified classpath
	 * location), each following the ResourceBundle convention with relaxed support for
	 * slash based locations. If it doesn't contain a package qualifier (such as
	 * "org.mypackage"), it will be resolved from the classpath root.
	 */
	private String basename = "messages";

	/**
	 * Message bundles encoding.
	 */
	private Charset encoding = StandardCharsets.UTF_8;

```

类中首先声明了一个属性`basename`,默认值为`messages`;

我们翻译其注释：
![image-20201005212335370](https://img-blog.csdnimg.cn/img_convert/d83078a65292938b047c6457d7aecbb3.png)

```shell
- 逗号分隔的基名列表(本质上是完全限定的类路径位置)
- 每个都遵循ResourceBundle约定，并轻松支持于斜杠的位置
- 如果不包含包限定符(例如"org.mypackage"),它将从类路径根目录中解析
```

**意思是**：

+ 如果你不在springboot配置文件中指定以`.`分隔开的国际化资源文件名称的话
+ 它默认会去类路径下找messages.properties作为国际化资源文件

这里我们自定义了国际化资源文件，因此我们需要在SpringBoot配置文件`application.properties`中加入以下配置指定我们配置文件的名称

```
spring.messages.basename=i18n.login
```

其中`i18n`是存放资源的文件夹名，`login`是资源文件的基本名称。

---
## 4. 首页获取显示国际化值

利用`#{...}` 消息表达式，去首页`index.html`获取国际化的值
![](https://img-blog.csdnimg.cn/img_convert/267a42b24052e11d9b8787c5fdf62fd8.png)
重启项目，访问首页，可以发现已经自动识别为中文
![image-20201003212448367](https://img-blog.csdnimg.cn/img_convert/95887522e480d194d4faaf9b7bda2d88.png)

---
## 5. 配置国际化组件实现中英文切换

### 1. 添加中英文切换标签链接

上述实现了登录首页显示为中文，我们在`index.html`页面中可以看到两个标签

```html
<a class="btn btn-sm">中文</a>
<a class="btn btn-sm">English</a>
```

也就对应着视图中的

![image-20201003212704250](https://img-blog.csdnimg.cn/img_convert/ce0a5335f499c443e1cc77bf4666e905.png)

那么我们怎么通过这两个标签实现中英文切换呢?

首先在这两个标签上加上跳转链接并带上相应的参数

```html
<!--这里传入参数不需要使用?使用key=value-->
<a class="btn btn-sm" th:href="@{/index.html(l='zh_CN')}">中文</a>
<a class="btn btn-sm" th:href="@{/index.html(l='en_US')}">English</a>
```

### 2. 自定义地区解析器组件

> 怎么实现我们自定义的地区解析器呢？我们首先来分析一波源码
>
> 在Spring中有关于国际化的两个类：
>
> + `Locale`：代表地区，每一个Locale对象都代表了一个特定的地理、政治和文化地区
> + `LocaleResolver`：地区解析器

首先搜索`WebMvcAutoConfiguration`，可以在其中找到关于一个方法`localeResolver()`

```java
@Bean
@ConditionalOnMissingBean
@ConditionalOnProperty(prefix = "spring.mvc", name = "locale")
public LocaleResolver localeResolver() {
    //如果用户配置了,则使用用户配置好的
   if (this.mvcProperties.getLocaleResolver() == WebMvcProperties.LocaleResolver.FIXED) {
      return new FixedLocaleResolver(this.mvcProperties.getLocale());
   }
    //用户没有配置,则使用默认的
   AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
   localeResolver.setDefaultLocale(this.mvcProperties.getLocale());
   return localeResolver;
}
```

该方法就是获取`LocaleResolver`地区对象解析器：

+ 如果用户配置了则使用用户配置的地区解析器；
+ 如果用户没有配置，则使用默认的地区解析器

我们可以看到默认地区解析器的是`AcceptHeaderLocaleResolver`对象，我们点入该类查看源码
![image-20201002200455221](https://img-blog.csdnimg.cn/img_convert/7025f52b96ae1a2b3dd366dfee2393da.png)
可以发现它继承了`LocaleResolver`接口，实现了地区解析

因此我们想要实现上述自定义的国际化资源生效，只需要编写一个自己的地区解析器，继承`LocaleResolver`接口，重写其方法即可


> 我们在config包下新建`MyLocaleResolver`，作为自己的国际化地区解析器
>
> ![image-20201005214022008](https://img-blog.csdnimg.cn/img_convert/e4719f56b8965763528a4c0666481e1b.png)

我们在`index.html`中，编写了对应的请求跳转

+ 如果点击中文按钮，则跳转到`/index.html(l='zh_CN')`页面
+ 如果点击English按钮，则跳转到`/index.html(l='en_US')`页面
![image-20201002201536255](https://img-blog.csdnimg.cn/img_convert/ba8c87e4702e1a2933f60a7450e0c285.png)
因此我们自定义的地区解析器`MyLocaleResolver`中，需要处理这两个带参数的链接请求

```java
package com.zsr.config;

import org.springframework.cglib.core.Local;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;

import javax.servl et.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

public class MyLocaleResolver implements LocaleResolver {
    //解析请求
    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        //获取请求中的国际化参数
        String language = request.getParameter("l");
        //默认的地区
        Locale locale = Locale.getDefault();
        //如果请求的链接参数不为空,携带了国际化参数
        if (!StringUtils.isEmpty(language)) {
            String[] split = language.split("_");//zh_CN(语言_地区)
            locale = new Locale(split[0], split[1]);
        }
        return locale;
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {

    }
}
```

为了让我们的区域化信息能够生效，我们需要再配置一下这个组件！在自己的`MvcConofig`配置类下添加bean；

```java
//自定义的国际化组件生效
@Bean
public LocaleResolver localeResolver() {
    return new MyLocaleResolver();
}
```

我们重启项目，来访问一下，发现点击按钮可以实现成功切换！

点击中文按钮，跳转到`http://localhost:8080/index.html?l=zh_CN`，显示为中文
![image-20201004104008854](https://img-blog.csdnimg.cn/img_convert/952788eebbf89bc96a5675ed9e0bad04.png)
点击`English`按钮，跳转到`http://localhost:8080/index.html?l=en_US`，显示为英文
![image-20201004104020956](https://img-blog.csdnimg.cn/img_convert/a8bea9c9536bc8ae08c6f25280ca991d.png)

---
<br>

# （四）登录功能的实现

> 登录，也就是当我们点击`登录`按钮的时候，会进入一个页面，这里进入`dashboard`页面

因此我们首先在`index.html`中的表单编写一个提交地址`/user/login`，并给名称和密码输入框添加`name`属性为了后面的传参
![image-20201004104357738](https://img-blog.csdnimg.cn/img_convert/91b29cbcc9e04e032a2938b157a247bb.png)
然后编写对应的**controller**

> 在主程序同级目录下新建`controller`包，在其中新建类`loginController`，处理登录请求

```java
package com.zsr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    @RequestMapping("/user/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, Model model) {
        //如果用户名和密码正确
        if ("admin".equals(username) && "123456".equals(password))
            return "dashboard";//跳转到dashboard页面
            //如果用户名或者密码不正确
        else {
            model.addAttribute("msg", "用户名或者密码错误");//显示错误信息
            return "index";//跳转到首页
        }
    }
}
```

然后我们在`index.html`首页中加一个标签用来显示controller返回的错误信息	

```html
<p style="color: red" th:text="${msg}"></p>
```
![image-20201004105231094](https://img-blog.csdnimg.cn/img_convert/5a84f1fa4a00cdf1e6696d3afb9c1a89.png)
我们再测试一下，启动主程序，访问`localhost:8080`

如果我们输入正确的用户名和密码
![image-20201004105810827](https://img-blog.csdnimg.cn/img_convert/c22f6964b8802cb10559549ec4e3122b.png)
则重新跳转到`dashboard`页面，浏览器url为`http://localhost:8080/user/login?username=admin&password=123456`
![image-20201004110853065](https://img-blog.csdnimg.cn/img_convert/dcb9fbf5ffe9423f76af3553a7295ba4.png)
随便输入错误的用户名`12`，输入错误的密码`12`

浏览器url为`http://localhost:8080/user/login?username=12&password=123456`，页面上附有错误提示信息
![image-20201004110930424](https://img-blog.csdnimg.cn/img_convert/52357b17fce9cb560df17c4a029ddeda.png)
到此我们**的登录功能实现完毕**，但是有一个很大的**问题**，浏览器的url暴露了用户的用户名和密码，这在实际开发中可是重大的漏洞，**泄露了用户信息**，因此我们需要编写一个映射

我们在自定义的配置类`MyMvcConfig`中加一句代码

```java
registry.addViewController("/main.html").setViewName("dashboard");
```

也就是访问`/main.html`页面就跳转到`dashboard`页面

然后我们稍稍修改一下`LoginController`，当登录成功时重定向到`main.html`页面，也就跳转到了`dashboard`页面

```java
package com.zsr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    @RequestMapping("/user/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, Model model) {
        //如果用户名和密码正确
        if ("admin".equals(username) && "123456".equals(password))
            return "redirect:/main.html";//重定向到main.html页面,也就是跳转到dashboard页面
            //如果用户名或者密码不正确
        else {
            model.addAttribute("msg", "用户名或者密码错误");//显示错误信息
            return "index";//跳转到首页
        }
    }
}
```

我们再次重启测试，输入正确的用户名和密码登陆成功后，浏览器不再携带泄露信息
![image-20201004112509676](https://img-blog.csdnimg.cn/img_convert/293d742ad3a5021b3c1cd599bf055f66.png)
但是这又出现了**新的问题**，无论登不登陆，我们访问`localhost/main.html`都会跳转到`dashboard`的页面，这就引入了接下来的拦截器

---
<br>

# （五）登录拦截器

> 为了解决上述遗留的问题，我们需要自定义一个拦截器；
>
> 在`config`目录下，新建一个登录拦截器类`LoginHandlerInterceptor`

**用户登录成功后，后台会得到用户信息；如果没有登录，则不会有任何的用户信息；**

我们就可以利用这一点通过**拦截器进行拦截**：

+ 当用户登录时将用户信息存入session中，访问页面时首先判断session中有没有用户的信息
+ 如果没有，拦截器进行拦截；
+ 如果有，拦截器放行

因此我们首先在`LoginController`中当用户登录成功后，存入用户信息到session中

```java
package com.zsr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {
    @RequestMapping("/user/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, Model model, HttpSession session) {
        //如果用户名和密码正确
        if ("admin".equals(username) && "123456".equals(password)) {
            session.setAttribute("LoginUser", username);
            return "redirect:/main.html";//重定向到main.html页面,也就是跳转到dashboard页面
        }
        //如果用户名或者密码不正确
        else {
            model.addAttribute("msg", "用户名或者密码错误");//显示错误信息
            return "index";//跳转到首页
        }
    }
}
```

然后再在实现自定义的登录拦截器，继承`HandlerInterceptor`接口

+ 其中获取存入的session进行判断，如果不为空，则放行；

+ 如果为空，则返回错误消息，并且返回到首页，不放行。

```java
package com.zsr.config;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.ForkJoinPool;

public class LoginHandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //用户登录成功后,应该有自己的session
        Object session = request.getSession().getAttribute("LoginUser");
        if (session == null) {
            request.setAttribute("msg", "权限不够,请先登录");
            request.getRequestDispatcher("/index.html").forward(request, response);
            return false;
        } else {
            return true;
        }
    }
}
```

然后配置到bean中注册，在`MyMvcConfig`配置类中，重写关于拦截器的方法，添加我们自定义的拦截器，注意屏蔽静态资源及主页以及相关请求的拦截

```java
@Override
public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new LoginHandlerInterceptor())
            .addPathPatterns("/**")
            .excludePathPatterns("/index.html", "/", "/user/login", "/css/**", "/js/**", "/img/**");
}
```

然后重启主程序进行测试，直接访问`http://localhost:8080/main.html`
![image-20201004120406357](https://img-blog.csdnimg.cn/img_convert/dc061e57a7d8e9daa9052ac5c2357091.png)
提示权限不够，请先登录，我们登录一下
![image-20201004120439776](https://img-blog.csdnimg.cn/img_convert/16c1c2556714f90471e6288fc3ae9760.png)
进入到`dashboard`页面

如果我们再直接重新访问`http://localhost:8080/main.html`，也可以直接直接进入到`dashboard`页面，这是因为session里面存入了用户的信息，拦截器放行通过
![image-20201004120633323](https://img-blog.csdnimg.cn/img_convert/9e22036ccb8f8a797241bfe6a125e77d.png)

---
<br>


# （六）展示员工信息——查

## 1. 实现Customers视图跳转

> 目标：点击`dashboard.html`页面中的`Customers`展示跳转到`list.html`页面显示所有员工信息
![image-20201004121828903](https://img-blog.csdnimg.cn/img_convert/ebb8fcf9755382e1ced44b1d0b4f991d.png)

因此，我们首先给`dashboard.html`页面中`Customers`部分标签添加`href`属性，实现点击该标签请求`/emps`路径跳转到`list.html`展示所有的员工信息
![image-20201004181645979](https://img-blog.csdnimg.cn/img_convert/5835cf0741da8a305ee2e40f6337e31f.png)

```html
<li class="nav-item">
    <a class="nav-link" th:href="@{/emps}">
        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"
             fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"
             stroke-linejoin="round" class="feather feather-users">
            <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path>
            <circle cx="9" cy="7" r="4"></circle>
            <path d="M23 21v-2a4 4 0 0 0-3-3.87"></path>
            <path d="M16 3.13a4 4 0 0 1 0 7.75"></path>
        </svg>
        Customers
    </a>
</li>
```

同样修改`list.html`对应该的代码为上述代码
![image-20201004224657448](https://img-blog.csdnimg.cn/img_convert/3d4e8165a17891e47c19c055db27e363.png)
我们在`templates`目录下新建一个包`emp`，用来放所有关于员工信息的页面，我们将`list.html`页面移入该包中
<img src="https://gitee.com/zhong_siru/images/raw/master//img/image-20201004180721060.png" alt="image-20201004180721060" style="zoom:50%;" />
然后编写请求对应的controller，处理`/emps`请求，在`controller`包下，新建一个`EmployeeController`类

```java
package com.zsr.controller;

import com.zsr.dao.EmployeeDao;
import com.zsr.pojo.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;

@Controller
public class EmployeeController {
    @Autowired
    private EmployeeDao employeeDao;

    @RequestMapping("/emps")
    public String list(Model model) {
        Collection<Employee> employees = employeeDao.getAllEmployees();
        model.addAttribute(employees);
        return "emp/list";//返回到list页面
    }
}
```

然后我们重启主程序进行测试，登录到`dashboard`页面，再点击`Customers`，成功跳转到`/emps`
![image-20201004181151693](https://img-blog.csdnimg.cn/img_convert/0dbf9b54b0892003c81dfe613b9edd81.png)
**但是有些问题**：

1. 我们点击了`Customers`后，它应该处于高亮状态，但是这里点击后还是普通的样子，高亮还是在`Dashboard`上
2. `list.html`和`dashboard.html`页面的侧边栏和顶部栏是相同的，可以抽取出来

--- 

## 2. 提取页面公共部分

> 在`templates`目录下新建一个`commons`包，其中新建`commons.html`用来放置公共页面代码
>
> ![image-20201004225537650](https://img-blog.csdnimg.cn/img_convert/628a024dfa05cea86169e62c0b776e83.png)
>
> 利用`th:fragment`标签抽取公共部分（顶部导航栏和侧边栏）

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
    
<!--顶部导航栏,利用th:fragment提取出来,命名为topbar-->
<nav class="navbar navbar-dark sticky-top bg-dark flex-md-nowrap p-0" th:fragment="topbar">
    <a class="navbar-brand col-sm-3 col-md-2 mr-0" href="http://getbootstrap.com/docs/4.0/examples/dashboard/#">Company
        name</a>
    <input class="form-control form-control-dark w-100" type="text" placeholder="Search" aria-label="Search">
    <ul class="navbar-nav px-3">
        <li class="nav-item text-nowrap">
            <a class="nav-link" href="http://getbootstrap.com/docs/4.0/examples/dashboard/#">Sign out</a>
        </li>
    </ul>
</nav>
    
<!--侧边栏,利用th:fragment提取出来,命名为sidebar-->
<nav class="col-md-2 d-none d-md-block bg-light sidebar" th:fragment="siderbar">
    <div class="sidebar-sticky">
        <ul class="nav flex-column">
            <li class="nav-item">
                <a class="nav-link active" href="http://getbootstrap.com/docs/4.0/examples/dashboard/#">
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"
                         fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                         stroke-linejoin="round" class="feather feather-home">
                        <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"></path>
                        <polyline points="9 22 9 12 15 12 15 22"></polyline>
                    </svg>
                    Dashboard <span class="sr-only">(current)</span>
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="http://getbootstrap.com/docs/4.0/examples/dashboard/#">
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"
                         fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                         stroke-linejoin="round" class="feather feather-file">
                        <path d="M13 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V9z"></path>
                        <polyline points="13 2 13 9 20 9"></polyline>
                    </svg>
                    Orders
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="http://getbootstrap.com/docs/4.0/examples/dashboard/#">
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"
                         fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                         stroke-linejoin="round" class="feather feather-shopping-cart">
                        <circle cx="9" cy="21" r="1"></circle>
                        <circle cx="20" cy="21" r="1"></circle>
                        <path d="M1 1h4l2.68 13.39a2 2 0 0 0 2 1.61h9.72a2 2 0 0 0 2-1.61L23 6H6"></path>
                    </svg>
                    Products
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" th:href="@{/emps}">
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"
                         fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                         stroke-linejoin="round" class="feather feather-users">
                        <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path>
                        <circle cx="9" cy="7" r="4"></circle>
                        <path d="M23 21v-2a4 4 0 0 0-3-3.87"></path>
                        <path d="M16 3.13a4 4 0 0 1 0 7.75"></path>
                    </svg>
                    Customers
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="http://getbootstrap.com/docs/4.0/examples/dashboard/#">
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"
                         fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                         stroke-linejoin="round" class="feather feather-bar-chart-2">
                        <line x1="18" y1="20" x2="18" y2="10"></line>
                        <line x1="12" y1="20" x2="12" y2="4"></line>
                        <line x1="6" y1="20" x2="6" y2="14"></line>
                    </svg>
                    Reports
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="http://getbootstrap.com/docs/4.0/examples/dashboard/#">
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"
                         fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                         stroke-linejoin="round" class="feather feather-layers">
                        <polygon points="12 2 2 7 12 12 22 7 12 2"></polygon>
                        <polyline points="2 17 12 22 22 17"></polyline>
                        <polyline points="2 12 12 17 22 12"></polyline>
                    </svg>
                    Integrations
                </a>
            </li>
        </ul>

        <h6 class="sidebar-heading d-flex justify-content-between align-items-center px-3 mt-4 mb-1 text-muted">
            <span>Saved reports</span>
            <a class="d-flex align-items-center text-muted"
               href="http://getbootstrap.com/docs/4.0/examples/dashboard/#">
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none"
                     stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                     class="feather feather-plus-circle">
                    <circle cx="12" cy="12" r="10"></circle>
                    <line x1="12" y1="8" x2="12" y2="16"></line>
                    <line x1="8" y1="12" x2="16" y2="12"></line>
                </svg>
            </a>
        </h6>
        <ul class="nav flex-column mb-2">
            <li class="nav-item">
                <a class="nav-link" href="http://getbootstrap.com/docs/4.0/examples/dashboard/#">
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"
                         fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                         stroke-linejoin="round" class="feather feather-file-text">
                        <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path>
                        <polyline points="14 2 14 8 20 8"></polyline>
                        <line x1="16" y1="13" x2="8" y2="13"></line>
                        <line x1="16" y1="17" x2="8" y2="17"></line>
                        <polyline points="10 9 9 9 8 9"></polyline>
                    </svg>
                    Current month
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="http://getbootstrap.com/docs/4.0/examples/dashboard/#">
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"
                         fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                         stroke-linejoin="round" class="feather feather-file-text">
                        <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path>
                        <polyline points="14 2 14 8 20 8"></polyline>
                        <line x1="16" y1="13" x2="8" y2="13"></line>
                        <line x1="16" y1="17" x2="8" y2="17"></line>
                        <polyline points="10 9 9 9 8 9"></polyline>
                    </svg>
                    Last quarter
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="http://getbootstrap.com/docs/4.0/examples/dashboard/#">
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"
                         fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                         stroke-linejoin="round" class="feather feather-file-text">
                        <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path>
                        <polyline points="14 2 14 8 20 8"></polyline>
                        <line x1="16" y1="13" x2="8" y2="13"></line>
                        <line x1="16" y1="17" x2="8" y2="17"></line>
                        <polyline points="10 9 9 9 8 9"></polyline>
                    </svg>
                    Social engagement
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="http://getbootstrap.com/docs/4.0/examples/dashboard/#">
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"
                         fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                         stroke-linejoin="round" class="feather feather-file-text">
                        <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path>
                        <polyline points="14 2 14 8 20 8"></polyline>
                        <line x1="16" y1="13" x2="8" y2="13"></line>
                        <line x1="16" y1="17" x2="8" y2="17"></line>
                        <polyline points="10 9 9 9 8 9"></polyline>
                    </svg>
                    Year-end sale
                </a>
            </li>
        </ul>
    </div>
</nav>
</html>
```

 然后删除`dashboard.html`和`list.html`中顶部导航栏和侧边栏的代码
![image-20201004225706638](https://img-blog.csdnimg.cn/img_convert/f287c5b2433d42bc3bd4b7868dd75f7f.png)
我们再次重启主程序测试一下，登陆成功后，可以看到已经没有了顶部导航栏和侧边栏
![image-20201004225910009](https://img-blog.csdnimg.cn/img_convert/473bb1354072b5a1378c2a5d29c1121e.png)
这是因为我们删除了公共部分，还没有引入，我们分别在`dashboard.html`和`list.html`删除的部分插入提取出来的公共部分`topbar`和`sidebar`

```html
<!--顶部导航栏-->
<div th:replace="~{commons/commons::topbar}" }></div>
```

```html
<!--侧边栏-->
<div th:replace="~{commons/commons::siderbar}"></div>
```

![image-20201004230850304](https://img-blog.csdnimg.cn/img_convert/9bbaf15ade7c3cc51441493d3566f8a7.png)
再次重启主程序进行测试，登陆成功后，成功看到侧边栏和顶部栏，代表我们插入成功
![image-20201005234808381](https://img-blog.csdnimg.cn/img_convert/c3c5917ba6398cc64b6b0faf3dec920d.png)

---
<br>

## 3. 点击高亮处理

在页面中，使高亮的代码是`class="nav-link active"`属性
![image-20201004231353048](https://img-blog.csdnimg.cn/img_convert/4e932f61cb94c34cfd6681245e88fa29.png)
我们可以传递参数判断点击了哪个标签实现相应的高亮

首先在`dashboard.html`的侧边栏标签传递参数`active`为`dashboard.html`
![image-20201004232404146](https://img-blog.csdnimg.cn/img_convert/4feee16960b781932d9ff862ec82c4b1.png)

```html
<!--侧边栏-->
<div th:replace="~{commons/commons::siderbar(active='dashboard.html')}"></div>
```

同样在`list.html`的侧边栏标签传递参数`active`为`list.html`
![image-20201004232423859](https://img-blog.csdnimg.cn/img_convert/1d1a0aee3ccaa45ae104bb8ad7df6ea7.png)

```html
<!--侧边栏-->
<div th:replace="~{commons/commons::siderbar(active='list.html')}"></div>
```

然后我们在公共页面`commons.html`相应标签部分利用thymeleaf接收参数`active`，利用三元运算符判断决定是否高亮
![image-20201004233054099](https://img-blog.csdnimg.cn/img_convert/6d70f31701f845572667c8373832bb10.png)
再次重启主程序测试，登录成功后，首先`Dashboard`高亮
![image-20201004233211908](https://img-blog.csdnimg.cn/img_convert/988e346606c9372cff6fcc3f510446f9.png)
再点击`Customers`，`Customers`高亮，成功
![image-20201004233231716](https://img-blog.csdnimg.cn/img_convert/be3770e3502dbe1d9db23eb07471f546.png)

---
## 4. 显示员工信息

> 修改`list.html`页面，显示我们自己的数据值
![image-20201004234224403](https://img-blog.csdnimg.cn/img_convert/f5dbbba26067ac3525e108b001d41ba2.png)

修改完成后，重启主程序，登录完成后查看所有员工信息，成功显示
![image-20201004234518079](https://img-blog.csdnimg.cn/img_convert/ef924f911ca8e3205151311d27559bee.png)
接下来修改一下性别的显示和date的显示，并添加`编辑`和`删除`两个标签，为后续做准备

```html
<thead>
    <tr>
        <th>id</th>
        <th>lastName</th>
        <th>email</th>
        <th>gender</th>
        <th>department</th>
        <th>date</th>
        <th>操作</th>
    </tr>
</thead>
<tbody>
    <tr th:each="emp:${emps}">
        <td th:text="${emp.getId()}"></td>
        <td th:text="${emp.getLastName()}"></td>
        <td th:text="${emp.getEmail()}"></td>
        <td th:text="${emp.getGender()==0?'女':'男'}"></td>
        <td th:text="${emp.getDepartment().getDepartmentName()}"></td>
        <td th:text="${#dates.format(emp.getDate(),'yyyy-MM-dd HH:mm:ss')}"></td>
        <td>
            <a class="btn btn-sm btn-primary">编辑</a>
            <a class="btn btn-sm btn-danger">删除</a>
        </td>
    </tr>
</tbody>
```

再次重启主程序测试，成功
![image-20201004235127495](https://img-blog.csdnimg.cn/img_convert/ef6d63d1476daa3c8a79d017d8154629.png)

---


# （七）增加员工实现——增

## 1. list页面增加添加员工按钮

> 首先在`list.html`页面增添一个`增加员工`按钮，点击该按钮时发起一个请求`/add`
>
> ![image-20201005100249300](https://img-blog.csdnimg.cn/img_convert/555cfed82aaefdd170c5f4f36b122361.png)

```html
<h2><a class="btn btn-sm btn-success" th:href="@{/add}">添加员工</a></h2>
```

![image-20201004235751842](https://img-blog.csdnimg.cn/img_convert/902e9339293060c4f87a868be227322f.png)
然后编写对应的controller，处理点击`添加员工`的请求

这里通过`get`方式提交请求，在`EmployeeController`中添加一个方法`add`用来处理`list`页面点击提交按钮的操作，返回到`add.html`添加员工页面，我们即将创建

```java
@GetMapping("/add")
public String add(Model model) {
    //查出所有的部门信息,添加到departments中,用于前端接收
    Collection<Department> departments = departmentDao.getDepartments();
    model.addAttribute("departments", departments);
    return "emp/add";//返回到添加员工页面
}
```
---

## 2. 创建添加员工页面add

> 在`templates/emp`下新建一个`add.html`
>
> ![image-20201005002351454](https://img-blog.csdnimg.cn/img_convert/b2ff316367b5d40b0ab4ac1a6d8bc2de.png)

我们复制`list.html`中的内容，修改其中表格为：

```html
<form>
    <div class="form-group">
        <label>LastName</label>
        <input type="text" name="lastName" class="form-control" placeholder="lastname:zsr">
    </div>
    <div class="form-group">
        <label>Email</label>
        <input type="email" name="email" class="form-control" placeholder="email:xxxxx@qq.com">
    </div>
    <div class="form-group">
        <label>Gender</label><br/>
        <div class="form-check form-check-inline">
            <input class="form-check-input" type="radio" name="gender" value="1">
            <label class="form-check-label">男</label>
        </div>
        <div class="form-check form-check-inline">
            <input class="form-check-input" type="radio" name="gender" value="0">
            <label class="form-check-label">女</label>
        </div>
    </div>
    <div class="form-group">
        <label>department</label>
        <!--注意这里的name是department.id，因为传入的参数为id-->
        <select class="form-control" name="department.id">
            <option th:each="department:${departments}" th:text="${department.getDepartmentName()}" th:value="${department.getId()}"></option>
        </select>
    </div>
    <div class="form-group">
        <label>Birth</label>
        <!--springboot默认的日期格式为yy/MM/dd-->
        <input type="text" name="date" class="form-control" placeholder="birth:yyyy/MM/dd">
    </div>
    <button type="submit" class="btn btn-primary">添加</button>
</form>
```

我们重启主程序看看
![image-20201005002133908](https://img-blog.csdnimg.cn/img_convert/7f9c9187432cdf259fcab4eeb4d7b139.png)
点击添加员工，成功跳转到`add.html`页面
![image-20201005131019727](https://img-blog.csdnimg.cn/img_convert/fcb4bd83745236c2bf1e7e71ab76e183.png)
下拉框中的内容不应该是1、2、3、4、5；应该是所有的部门名，我们遍历得到

```html
<!--其中th:value用来表示部门的id,我们实际传入的值为id-->
<option th:each="department:${departments}" th:text="${department.getDepartmentName()}" th:value="${department.getId()}"></option>
```

重启测试，成功显示所有部门
![image-20201005004847095](https://img-blog.csdnimg.cn/img_convert/f4525650a6e6c9b4f08d6078619f48a2.png)
到此，添加员工页面编写完成

---

## 3. add页面添加员工请求

> 在`add.html`页面，当我们填写完信息，点击`添加`按钮，应该完成添加返回到`list`页面，展示新的员工信息；因此在`add.html`点击`添加`按钮的一瞬间，我们同样发起一个请求`/add`，与上述`提交按钮`发出的请求路径一样，但这里发出的是`post`请求

![image-20201005095943762](https://img-blog.csdnimg.cn/img_convert/b402bc6870368961ac00b1714479fd24.png)
然后编写对应的controller，同样在`EmployeeController`中添加一个方法`addEmp`用来处理点击`添加按钮`的操作

```java
@PostMapping("/add")
public String addEmp(Employee employee) {
    employeeDao.addEmployee(employee);//添加一个员工
    return "redirect:/emps";//重定向到/emps,刷新列表,返回到list页面
}
```

我们重启主程序，进行测试，进入添加页面，填写相关信息，注意日期格式默认为`yyyy/MM/dd`
![image-20201005105038255](https://img-blog.csdnimg.cn/img_convert/aa31cf744136b9f0c14f98cfca4f739c.png)
然后点击添加按钮，成功实现添加员工
![image-20201005105046710](https://img-blog.csdnimg.cn/img_convert/00a7b23ab4caab3a96384693dcf5e46d.png)
我们也可以添加多个员工
![image-20201005105422329](https://img-blog.csdnimg.cn/img_convert/f1b5783d8da6c8d3929dace8f9419fcb.png)

---
<br>


# （八）修改员工信息——改

## 1. list页面编辑按钮增添请求

> ![image-20201005222422018](https://img-blog.csdnimg.cn/img_convert/afb112050d32090298128ad6a21e82b1.png)
>
> 当我们点击`编辑`标签时，应该跳转到编辑页面`edit.html`（我们即将创建）进行编辑
>
> 因此首先将`list.html`页面的编辑标签添加`href`属性，实现点击请求`/edit/id号`到编辑页面
>
> ```html
> <a class="btn btn-sm btn-primary" th:href="@{/edit/{id}(id=${emp.getId()})}">编辑</a>
> ```

![image-20201005222946574](https://img-blog.csdnimg.cn/img_convert/404e4c83bb0576e8e02ec94fda247ff3.png)
然后编写对应的controller，在`EmployeeController`中添加一个方法`edit`用来处理`list`页面点击`编辑`按钮的操作，返回到`edit.html`编辑员工页面，我们即将创建

```java
//restful风格接收参数
@RequestMapping("/edit/{id}")
public String edit(@PathVariable("id") int id, Model model) {
    //查询指定id的员工,添加到empByID中,用于前端接收
    Employee employeeByID = employeeDao.getEmployeeByID(id);
    model.addAttribute("empByID", employeeByID);
    //查出所有的部门信息,添加到departments中,用于前端接收
    Collection<Department> departments = departmentDao.getDepartments();
    model.addAttribute("departments", departments);
    return "/emp/edit";//返回到编辑员工页面
}
```
---

## 2. 创建编辑员工页面edit

> 在`templates/emp`下新建一个`edit.html`
>
> ![image-20201005110750692](https://img-blog.csdnimg.cn/img_convert/b0ed508aca238efc404d35584c7d97ad.png)

复制`add.html`中的代码，稍作修改

```html
<main role="main" class="col-md-9 ml-sm-auto col-lg-10 pt-3 px-4">
    <form th:action="@{/edit}" method="post">
        <div class="form-group">
            <label>LastName</label>
            <input th:value="${empByID.getLastName()}" type="text" name="lastName" class="form-control"
                   placeholder="lastname:zsr">
        </div>
        <div class="form-group">
            <label>Email</label>
            <input th:value="${empByID.getEmail()}" type="email" name="email" class="form-control"
                   placeholder="email:xxxxx@qq.com">
        </div>
        <div class="form-group">
            <label>Gender</label><br/>
            <div class="form-check form-check-inline">
                <input th:checked="${empByID.getGender()==1}" class="form-check-input" type="radio"
                       name="gender" value="1">
                <label class="form-check-label">男</label>
            </div>
            <div class="form-check form-check-inline">
                <input th:checked="${empByID.getGender()==0}" class="form-check-input" type="radio"
                       name="gender" value="0">
                <label class="form-check-label">女</label>
            </div>
        </div>
        <div class="form-group">
            <label>department</label>
            <!--注意这里的name是department.id，因为传入的参数为id-->
            <select class="form-control" name="department.id">
                <option th:selected="${department.getId()==empByID.department.getId()}"
                        th:each="department:${departments}" th:text="${department.getDepartmentName()}"
                        th:value="${department.getId()}">
                </option>
            </select>
        </div>
        <div class="form-group">
            <label>Birth</label>
            <!--springboot默认的日期格式为yy/MM/dd-->
            <input th:value="${empByID.getDate()}" type="text" name="date" class="form-control"
                   placeholder="birth:yy/MM/dd">
        </div>
        <button type="submit" class="btn btn-primary">修改</button>
    </form>
</main>
```

启动主程序测试，点击编辑1号用户
![image-20201005184849563](https://img-blog.csdnimg.cn/img_convert/c873ae8235bd909df21106adbce79c03.png)
成功跳转到`edit.html`，且所选用户信息正确
![image-20201005190050045](https://img-blog.csdnimg.cn/img_convert/a48f916304b596599cf3859df289777f.png)
但是日期的格式不太正确，我们规定一下显示的日期格式

```html
<!--springboot默认的日期格式为yy/MM/dd-->
<input th:value="${#dates.format(empByID.getDate(),'yyyy/MM/dd')}" type="text" name="date" class="form-control"
       placeholder="birth:yy/MM/dd">
```
---

## 3. edit页面编辑完成提交请求

> 在`edit.html`点击`修改`按钮的一瞬间，我们需要返回到list页面，更新员工信息，因此我们需要添加`href`属性，实现点击按钮时发起一个请求`/edit`

![image-20201005180518533](https://img-blog.csdnimg.cn/img_convert/c2e45842988cc269597c5166f1cbe91a.png)
然后编写对应的controller，处理点击`修改`按钮的请求

同样在`EmployeeController`中添加一个方法`EditEmp`用来处理`edit`页面点击添加的操作

```java
@PostMapping("/add")
public String EditEmp(Employee employee) {
    employeeDao.addEmployee(employee);//添加一个员工
    return "redirect:/emps";//添加完成重定向到/emps,刷新列表
}
```

然后指定修改人的id

```html
<input type="hidden" name="id" th:value="${empByID.getId()}">
```

重启测试，同样修改1号用户名称为`dddd`
![image-20201005190833690](https://img-blog.csdnimg.cn/img_convert/f732198bf402d9da43d9e07a7499710d.png)
然后点击修改
![image-20201005190901846](https://img-blog.csdnimg.cn/img_convert/a3efcacaaac2a47e2ea7b3a909e60955.png)
成功修改并返回到`list.html`

---
<br>

# （九）删除员工信息——删

> ![image-20201005224835477](https://img-blog.csdnimg.cn/img_convert/f6a68ad221897fb6ea6a2806f6650e4f.png)
>
> 当我们点击`删除`标签时，应该发起一个请求，删除指定的用户，然后重新返回到`list`页面显示员工数据

```html
<a class="btn btn-sm btn-success" th:href="@{/delete/{id}(id=${emp.getId()})}">删除</a>
```

然后编写对应的controller，处理点击`删除`按钮的请求，删除指定员工，重定向到`/emps`请求，更新员工信息

```java
@GetMapping("/delete/{id}")
public String delete(@PathVariable("id") Integer id) {
    employeeDao.deleteEmployeeByID(id);
    return "redirect:/emps";
}
```

重启测试，点击删除按钮即可删除指定员工

---
<br>


# （十）404页面定制

> 只需要在`templates`目录下新建一个`error`包，然后将`404.html`放入其中，报错SpringBoot就会自动找到这个页面
>
> ![image-20201005230228695](https://img-blog.csdnimg.cn/img_convert/c4bc1292e4438f3da0cbf4ee1c6a139e.png)

我们可以启动程序测试，随便访问一个不存在的页面
![image-20201005230755988](https://img-blog.csdnimg.cn/img_convert/c384f5ccd8e858bd1c6142c5a934cb8f.png)
出现的404页面即是我们自己的`404.html`

---
<br>

# （十一）注销操作

在我们提取出来的公共`commons`页面，顶部导航栏处中的标签添加`href`属性，实现点击发起请求`/user/logout`
![image-20201005192507357](https://img-blog.csdnimg.cn/img_convert/3a496727470e35a87c155d9fab3df7b3.png)
然后编写对应的controller，处理点击`注销`标签的请求，在`LoginController`中编写对应的方法，清除session，并重定向到首页

```java
@RequestMapping("/user/logout")
public String logout(HttpSession session) {
    session.invalidate();
    return "redirect:/index.html";
}
```

重启测试，登录成功后，点击`log out`即可退出到首页
![image-20201005232944539](https://img-blog.csdnimg.cn/img_convert/c7e89fac6d31ec0bcf7d8693b1f7d04f.png)
