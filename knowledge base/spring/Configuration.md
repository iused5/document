# Spring Configuration
## XML 화일의 구성
* MVC Model에서 Controller, View 와 관련된 설정은 servlet-context.xml에 (WAC: Web Application Context)에, View Layer에 포함되지 않은 datasource, transaction등에 관련된 설정은 root-context.xml에서 처리한다.

## 구성방법
* \<import resource="{configuration file name}.xml"/>를 사용  
root-context.xml에서 transaction, exception 설정 xml화일을 대상으로 사용
````xml
<context-param>
    <param-name>contextConfigLocation</param-name>    
    <!-- 여러 리소스들을 지정 -->
    <param-value>/WEB-INF/classes/spring/context-*.xml</param-value>
</context-param>

<listener>     
    <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
</listener>

<servlet>
    <servlet-name>springapp</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <init-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>/WEB-INF/config/springmvc/context-*.xml</param-value>
    </init-param>
    <load-on-startup>1</load-on-startup>
</servlet>
````
* web.xml에서 여러화일 지정  
설정파일위치:  
  * src/main/resources/spring/context-common.xml, context-datasource.xml,context-sqlMap.xml, context-transaction.xml ... 
  * src/main/webapp/WEB-INF/config/springmvc/context-servlet.xml

## Multi Datasource에 대한 DAO 처리
* myBatis의 SqlSessionDaoSupport에서 SqlSession member에 대한 autowired annotation은 SqlSessionFactory나 SqlSessionTemplate가 2개 이상 있을 경우, autowired될 bean을 결정하지 못해 오류가 발생할 수 있슴.

* 검토.1. sqlSessionFactory, SqlSessionTemplate가 2개이고, 모두 id가 sqlSessionFactory, sqlSessoinTemplate이 아님  
&rArr; loading시 autowired대상 bean을 찾지 못해 오류
* 검토.2. sqlSessionFactory, SqlSessionTemplate가 2개이고, id가 sqlSessionFactory, sqlSessoinTemplate인 bean이 존재  
&rArr; loading시 이상없슴, dao를 이용한 method수행시 미확인, sqlSessionFactory의 Datasource가 이용될 것으로 추측됨
* 검토.3. xml bean선언으로 가능한지 확인  
&rArr; 오류발생

* 결론.1. 각 DataSource별로 SqlSessionDaoSupport를 대치하는 Super Class를 작성하고 이를 상속하여 개발  
&rArr; 각 Super Class에는 @Resource(name = "SqlSessoinFactory1") 형식으로 DI 수행

## Multi Datasource에 대한 Transaction 처리
* 검토.1. TransactionManager 2개 생성, 각각의 TransactionManager의 id는 transactionManager가 아님, <tx:annotation-drive transaction-manager="tranactionManager1"/>** 형식으로 각각 선언  
&rArr; 먼저 선언된 TransactionManager만 유효
* 결론.1. TransactionManager에 qualifier 선언하고 \<tx:annotation-driven/>으로 변경, Service의 method 에서 @Transactional("qualifier1") 형식으로 처리