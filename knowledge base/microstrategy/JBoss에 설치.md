# MicroStrategy JBoss에 설치
## WEB Application 설치
* {$JBOSS_BASE}\jboss-as\server\default\conf\jboss-service.xml를 다음과 같이 변경
```xml
<mbean code="org.jboss.deployment.scanner.URLDeploymentScanner" name="jboss.deployment:type=DeploymentScanner,flavor=URL">
    ...
    <attribute name="URLs">
        deploy/,
        file:/D:/WorkSpace.share/project/MicroStrategy/MicroStrategy.9.2.1.war
    </attribute>
    ...
</mbean>
```
## JBoss 사용자그룹 및 사용자 권한 설정
* {$APP_BASE}\WEB-INF\jboss-web.xml 생성 - security domain은 jboss에서 기본적으로 제공하는 jmx-console로 사용 
```xml
<jboss-web>
    <!-- Uncomment the security-domain to enable security. You will
    need to edit the htmladaptor login configuration to setup the
    login modules used to authentication users.
    -->
    <security-domain>java:/jaas/jmx-console</security-domain>
</jboss-web>
```
* {$APP_BASE}\WEB-INF\web.xml 변경 - <role-name>admin</role-name>문자열을 <role-name>JBossAdmin</role-name>로 모두 변경 
```xml
...
<web-app>
    ...
    <security-role-ref>
        <role-name>ADMIN</role-name>
        <!-- <role-link>admin</role-link> -->
        <role-link>JBossAdmin</role-link>
    </security-role-ref>
    <servlet>
        ...
        <!-- <role-link>admin</role-link> -->
        <role-link>JBossAdmin</role-link>
    </servlet>
    <security-constraint>
        ...
        <!-- <role-name>admin</role-name> -->
        <role-name>JBossAdmin</role-name>
        ...
    </security-constraint>
    ...
</web-app>
```