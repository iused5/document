# Jeinkins - source 하위 directory의 resource
Jenkins를 이용하여 build할때 eclipse에서와는 달리 source하위 directory에 포함된 resource는 포함되지 않는다.  
포함하려면 pom.xml을 다음과 같이...
```xml
<project...>
...
    <build>
        <resources>
            <resource>
                <directory>src/main/resources</directory>
            </resource>
            <resource>
                <directory>src/main/java</directory>
                <excludes>
                    <exclude>**/*.java</exclude>
                </excludes>
            </resource>
            </resources>
    </build>
...
</project>
```
