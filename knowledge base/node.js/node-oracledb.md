# Node.js - Oracle - Windows 7
## 2017 . 03 . 19
## 참조 링크
https://www.bountysource.com/issues/36391290-unable-to-install-node-oracledb-on-windows-10
https://github.com/oracle/node-oracledb/blob/master/INSTALL.md#instwin
https://community.oracle.com/docs/DOC-931127

## 1차 시도
* Node.js 6.9.2, Visual Studio 2015, Python 2.7.11 환경에서 시도하였으나 실패

## 2차 시도
* Visual Studio 2015, Python 2.17.11 Uninstall
* Open an elevated CMD prompt (run as Administrator).  
* Navigate to your app folder
```console
npm install --global npm
npm install --global --production windows-build-tools
npm install instantclient
set PATH=%cd%\instantclient;%PATH%
set OCI_LIB_DIR=%cd%\instantclient\sdk\lib\msvc
set OCI_INC_DIR=%cd%\instantclient\sdk\include
npm install oracledb 
```
* 출력결과
```console
C:\workspace\project\nodejs.lab\node-oracledb>npm install oracledb

> oracledb@1.13.0 install C:\workspace\project\nodejs.lab\node-oracledb\node_modules\oracledb
> node-gyp rebuild

C:\workspace\project\nodejs.lab\node-oracledb\node_modules\oracledb>if not defined npm_config_node_gyp (node "C:\workspace\nodejs\node_modules\npm\bin
\node-gyp-bin\\..\..\node_modules\node-gyp\bin\node-gyp.js" rebuild )  else (node "" rebuild )
이 솔루션의 프로젝트를 한 번에 하나씩 빌드합니다. 병렬 빌드를 사용하려면 "/m" 스위치를 추가하십시오.
  njsOracle.cpp
  njsPool.cpp
  njsConnection.cpp
  njsResultSet.cpp
  njsMessages.cpp
  njsIntLob.cpp
  dpiEnv.cpp
  dpiEnvImpl.cpp
  dpiException.cpp
  dpiExceptionImpl.cpp
  dpiConnImpl.cpp
  dpiDateTimeArrayImpl.cpp
  dpiPoolImpl.cpp
  dpiStmtImpl.cpp
  dpiUtils.cpp
  dpiLob.cpp
  dpiCommon.cpp
  win_delay_load_hook.cc
     Creating library C:\workspace\project\nodejs.lab\node-oracledb\node_modules\oracledb\build\Release\oracledb.lib and object C:\workspace\project\
  nodejs.lab\node-oracledb\node_modules\oracledb\build\Release\oracledb.exp
  Generating code
  Finished generating code
  oracledb.vcxproj -> C:\workspace\project\nodejs.lab\node-oracledb\node_modules\oracledb\build\Release\\oracledb.node
  oracledb.vcxproj -> C:\workspace\project\nodejs.lab\node-oracledb\node_modules\oracledb\build\Release\oracledb.pdb (Full PDB)
C:\workspace\project\nodejs.lab\node-oracledb
`-- oracledb@1.13.0

npm WARN enoent ENOENT: no such file or directory, open 'C:\workspace\project\nodejs.lab\node-oracledb\package.json'
npm WARN node-oracledb No description
npm WARN node-oracledb No repository field.
npm WARN node-oracledb No README data
npm WARN node-oracledb No license field.
```
* 설치 결과
  * Python 2.7.11, Microsoft Visual C++ Build Tools 14.0.25420.1

