# pom.xml 에서 shell script 사용 예시
## after_deploy.sh
```bash
export remoteDir=/app/domains
wasId='REAL181'
cd $remoteDir/tices
echo '###################' $wasId 'Server Start        ####################'
./startTices.sh
echo '###################' $wasId 'Sever Start End     ####################'
```
## before_deploy.sh
```bash
#!/bin/sh
wasId='REAL181'
echo '####################' $wasId 'War File Delete....  ####################'
rm -rf ./tices.war
echo '####################' $wasId 'Server Stop          ####################'
export remoteDir=/app/domains
cd $remoteDir/tices
./stopTices.sh
echo '####################' $wasId 'Server Stop End      ####################'
```
## pom.xml
```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <groupId>com.kt</groupId>
    <artifactId>TICES</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <packaging>war</packaging>
    
    <name>TICES</name>
    
    <properties>
        <java-version>1.6</java-version>
        <org.springframework.version>3.0.5.RELEASE</org.springframework.version>
        <org.aspectj-version>1.6.10</org.aspectj-version>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>
    
    <dependencies>
        <!-- Core utilities used by other modules. Define this if you use Spring 
            Utility APIs (org.springframework.core.*/org.springframework.util.*) -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-core</artifactId>
            <version>${org.springframework.version}</version>
        </dependency>
        ...        
        <!-- log4j lib -->
        <dependency>
            <groupId>log4j</groupId>
            <artifactId>log4j</artifactId>
            <version>1.2.15</version>
            <exclusions>
                <!-- <exclusion>
                    <groupId>javax.mail</groupId>
                    <artifactId>mail</artifactId>
                </exclusion> -->
                <exclusion>
                    <groupId>javax.jms</groupId>
                    <artifactId>jms</artifactId>
                </exclusion>
                <exclusion>
                    <groupId>com.sun.jdmk</groupId>
                    <artifactId>jmxtools</artifactId>
                </exclusion>
                <exclusion>
                    <groupId>com.sun.jmx</groupId>
                    <artifactId>jmxri</artifactId>
                </exclusion>
            </exclusions>
            <scope>runtime</scope>
        </dependency>
        ...
    </dependencies>
    
    <profiles>
        <!--  DEV Server Information Setting  -->
        <profile>
            <id>dev109</id>
            <properties>
                <target-host>아이피</target-host>
                <target-command-path>/app/apps/tices</target-command-path>
                <target-remotedir>/app/apps/tices</target-remotedir>
                <target-username>아이디</target-username>
                <target-password>암호</target-password>
                <!-- <target-fileset>D:/ktProject/TICES/target</target-fileset> -->
                <target-fileset>/home/hudson/.jenkins/workspace/TICES_DEV_WAS01_10.217.52.109/TICES/target</target-fileset>
                <target-jbossHome>/app/domains/tices</target-jbossHome>
                <target-port>8180</target-port>
                <target-deploy-path>/app/apps/tices/tices.war</target-deploy-path>
                <target-serverName>tices</target-serverName>
                <target-serverId>tices</target-serverId>
            </properties>
        </profile>
    
    </profiles>
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>2.3.2</version>
                <configuration>
                    <source>${java-version}</source>
                    <target>${java-version}</target>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>2.7.1</version>
                <configuration>
                    <skip>true</skip>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-deploy-plugin</artifactId>
                <version>2.5</version>
                <configuration>
                    <skip>true</skip>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-war-plugin</artifactId>
                <version>2.1.1</version>
                <configuration>
                    <warName>tices</warName>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-antrun-plugin</artifactId>
                <configuration>
                    <tasks>
                        <!-- WebApplicationServer file Deploy -->
                        <sshexec 
                                host="${target-host}" 
                                username="${target-username}"
                                password="${target-password}" 
                                trust="true" 
                                timeout="50000"
                                failonerror="false" 
                                command="${target-command-path}/before_deploy.sh" />
                        
                        <ftp 
                                server="${target-host}" 
                                remotedir="${target-remotedir}"
                                userid="${target-username}" 
                                password="${target-password}">
                            
                                <fileset dir="${target-fileset}">
                                    <include name="*.war" />
                                </fileset>
                        </ftp>
                        <sshexec 
                                host="${target-host}" 
                                username="${target-username}" 
                                password="${target-password}" 
                                trust="true" 
                                timeout="50000" 
                                failonerror="false" 
                                command="${target-command-path}/after_deploy.sh" />
                        
                        <sshexec 
                                host="${target-host}" 
                                username="${target-username}"
                                password="${target-password}" 
                                trust="true" 
                                timeout="50000"
                                failonerror="false" 
                                command="${target-command-path}/ps_kill.sh" />
                    </tasks>
                </configuration>
                <executions>
                    <execution>
                        <id>ftp</id>
                        <phase>deploy</phase>
                        <goals>
                            <goal>run</goal>
                        </goals>
                    </execution>
                </executions>
                <dependencies>
                    <dependency>
                        <groupId>org.apache.ant</groupId>
                        <artifactId>ant-jsch</artifactId>
                        <version>1.7.1</version>
                    </dependency>
                    <dependency>
                        <groupId>com.jcraft</groupId>
                        <artifactId>jsch</artifactId>
                        <version>0.1.38</version>
                    </dependency>
                    <dependency>
                        <groupId>ant</groupId>
                        <artifactId>ant-commons-net</artifactId>
                        <version>1.6.5</version>
                    </dependency>
                    <dependency>
                        <groupId>commons-net</groupId>
                        <artifactId>commons-net</artifactId>
                        <version>1.4.1</version>
                    </dependency>
                </dependencies>
            </plugin>
            <!-- 
            <plugin>
                <groupId>org.codehaus.mojo</groupId>
                <artifactId>jboss-maven-plugin</artifactId>
                <version>1.5.0</version>
                <configuration>
                    <jbossHome>${target-jbossHome}</jbossHome>
                    <serverName>${target-serverName}</serverName>
                    <hostName>${target-host}</hostName>
                    <serverId>${target-serverId}</serverId>
                    <port>${target-port}</port>
                    <type>remote</type>
                    <fileNames>
                        <fileName>${target-deploy-path}</fileName>
                    </fileNames>
                </configuration>
                <executions></executions>
            </plugin>
            -->
        </plugins>
    </build>
    
    <!-- Distribution Information -->
    <distributionManagement>
        <repository>
            <id>nexus</id>
            <name>Tices Repository</name>
            <url>http://10.217.52.113:8080/nexus/content/groups/tices_group/
            </url>
        </repository>
    <!-- <snapshotRepository> 
            <id>snapshot</id> 
            <name>Snapshot Repository</name> 
            <url>http://10.217.52.64:8080/nexus/content/repositories/snapshots</url> 
        </snapshotRepository> -->
    </distributionManagement>
</project>
```
## settings.xml
```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                        http://maven.apache.org/xsd/settings-1.0.0.xsd">
    
    <servers>
    <server>
        <id>tices</id>
        <username>아이디</username>
        <password>암호</password>
    </server>
    <!-- 
    <server>
    <id>nexus</id>
    <username>tices</username>
    <password>tices</password>
    </server>
    -->
    </servers>
    <mirrors>
    <mirror>
        <id>nexus</id>
        <mirrorOf>central</mirrorOf>
        <url>http://10.217.52.113:8080/nexus/content/groups/tices_group/</url>
    </mirror>
    </mirrors>
</settings>
```