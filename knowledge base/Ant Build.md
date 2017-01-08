# Ant Build
## build.xml
```xml
<?xml version='1.0' encoding='UTF-8'?>
    
<project name='bas' default='dev' basedir='.'>
    
    <property file="build.properties" />
        
    <target name='dev' depends='init, copy, compile, replace_dev, deploy_dev'/>
    <target name='prd' depends='init, copy, compile, replace_prd, chmod_prd, deploy_prd'/>
    
    <target name='init'>
        <delete dir='${build.dir}' quiet='true' />
        <mkdir  dir='${build.dir}' />
        <mkdir  dir='${build.dir.web}' />
    </target>
    
    <target name='copy' depends='init'>
        <copy todir='${build.dir.web}' includeemptydirs='true'>
            <fileset dir='${src.dir.web}' />
        </copy>
        <copy todir='${build.dir.resources}' includeemptydirs='true'>
            <fileset dir='${src.dir.resources}' />
        </copy>
        <copy todir='${build.dir.web}/WEB-INF/lib' includeemptydirs='true'>
            <fileset dir='${lib.dir.runtime2}' />
        </copy>
    </target>
    
    <target name='compile' depends='init, copy'>
        <javac srcdir='${src.dir.java}' destdir='${build.dir.web}/WEB-INF/classes'
            debug='on'
            debuglevel='lines,vars,source'
            encoding='${java.encoding}'
            deprecation='true'
            fork='true' >
            <classpath>
                <fileset dir='${lib.dir.runtime}' />
                <fileset dir='${lib.dir.compile}' />
                <fileset dir='${lib.dir.runtime2}' />
            </classpath>
        </javac>
    </target>
    
    <target name='replace_dev' depends='init, copy, compile'>
        <move file='${src.dir.svrres}/WEB-INF/classes/properties/app-dev.xml'
                tofile='${build.dir.web}/WEB-INF/classes/properties/app.xml' />
        ...
    </target>
    
    <target name='replace_prd' depends='init, copy, compile'>
        <move file='${src.dir.svrres}/WEB-INF/classes/properties/app-prod.xml'
                tofile='${build.dir.web}/WEB-INF/classes/properties/app.xml' />
        ...        
    </target>
    
    <target name='chmod_prd' depends='init, copy, compile, replace_prd'>
            <chmod perm='664'>
                <fileset dir='${build.dir.xml}'>
                    <include name='**/*.xml' />
                </fileset>
            </chmod>
    </target>
    
    <target name='deploy_dev' depends='init, copy, compile, replace_dev'>
        <copy todir='${target.dir.web.dev}' includeemptydirs='true'>
            <fileset dir='${build.dir.web}' />
        </copy>
        
        <copy todir='${target.dir.htdocs.dev}' includeemptydirs='true'>
            <fileset dir='${build.dir.web}'>
                <exclude name='WEB-INF/**' />
                <exclude name='META-INF/**' />
            </fileset>
        </copy>
    </target>
    
    <target name='deploy_prd' depends='init, copy, compile, replace_prd, chmod_prd'>
        <ftp server='${prd.ip1}' userid='${prd.id}' password='${prd.pw}' remotedir='${target.dir.web.prd}' >
            <fileset dir='${build.dir.web}' />
        </ftp>
        
        <!-- <ftp server='${prd.ip1}' userid='${prd.id}' password='${prd.pw}' remotedir='${target.dir.web.prd}' > -->
        <ftp server='${prd.ip1}' userid='${prd.id}' password='${prd.pw}' remotedir='${target.dir.htdocs.prd}' >
            <fileset dir='${build.dir.web}'>
                <exclude name='WEB-INF/**' />
                <exclude name='META-INF/**' />
            </fileset>
        </ftp>
        
        <ftp server='${prd.ip2}' userid='${prd.id}' password='${prd.pw}' remotedir='${target.dir.web.prd}' >
            <fileset dir='${build.dir.web}' />
        </ftp>
        
        <!-- <ftp server='${prd.ip2}' userid='${prd.id}' password='${prd.pw}' remotedir='${target.dir.web.prd}' > -->
        <ftp server='${prd.ip2}' userid='${prd.id}' password='${prd.pw}' remotedir='${target.dir.htdocs.prd}' >
            <fileset dir='${build.dir.web}'>
                <exclude name='WEB-INF/**' />
                <exclude name='META-INF/**' />
            </fileset>
        </ftp>
    </target>
</project>
```
## build.properties
```properties
app.name=bas
java.encoding=UTF-8
    
src.dir.java=src/main/java
src.dir.web=src/main/webapp
src.dir.resources=src/main/resources
src.dir.svrres=src/main/resource-env
    
lib.dir.compile=build/lib.compile
lib.dir.runtime=${src.dir.web}/WEB-INF/lib
lib.dir.runtime2=build/lib.runtime
    
build.dir=tempbuild
build.dir.web=${build.dir}/${app.name}
build.dir.resources=${build.dir.web}/WEB-INF/classes
build.dir.xml=${build.dir.web}/WEB-INF/xml
    
target.dir.dev=/sorc001/olaptadm/applications
target.dir.web.dev=${target.dir.dev}/${app.name}
target.dir.htdocs.dev=${target.dir.dev}/htdocs/${app.name}
target.dir.prd=/sorc001/olapadm/applications
target.dir.web.prd=${target.dir.prd}/${app.name}
target.dir.htdocs.prd=${target.dir.prd}/htdocs/${app.name}
    
prd.ip1=127.0.0.1
prd.ip2=127.0.0.1
prd.id=id
prd.pw=pw
```
## build run shell command
```xml
<target name='permission1'>
    <telnet server='${prod.ap1.ip}'
            userid='${prod.userid.adm}'
            password='${prod.password.adm}'
            timeout='600'>
        <read>/engn001/pipadm ]</read>
        <write>cd /share001/PIPELINE</write>
        <read>/share001/PIPELINE ]</read>
        <write>chmod -R 775 mainWebApp</write>
        <read>/share001/PIPELINE ]</read>
        <write>pwd</write>
    </telnet>
</target>
```