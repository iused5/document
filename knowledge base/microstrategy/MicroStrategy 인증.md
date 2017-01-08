# MicroStrategy 인증
## 사용내역목록표시, ESM, Preference
* MicroStrategy Support Site에서 검색 (TN:14608, TN:13827)
* ESM에서 세션 생성시 preference설정값들은 무시되므로 ESM내에서 설정 필요
* 주의! ESM외에 별도로 세션을 이용하는 부분이 있다면 이 부분에도 설정값들을 반영해야 함
## Login Flow
* login to projects
```text
Uid=administrator&Pwd=mstradmin2010&ConnMode=1&3054=%EB%A1%9C%EA%B7%B8%EC%9D%B8&evt=3054&src=mstrWeb.3054&Project=&key=&target=evt%3D3001%26src%3DmstrWeb.3001&Port=0&Server=&mstrWeb=-..0_&login=*-1.*-1.0.0.0&lb=*-1.*-1.0.0.evt%3D3019%26welcome%3D***-1*.***-1*.0*.0*.0%26mstrWeb%3D*-*.*.0*_%26src%3DmstrWeb*.3019%26Port%3D0
```
* projects to desktop
```text
evt=3010 &src=mstrWeb.3010&Project=EDW&loginReq=true&Port=0&Server=LGDAMRP1&mstrWeb=-..0_&welcome=*-1.*-1.0.0.0
```
* desktop to login page
```text
evt=3019&src=mstrWeb.3019&mstrWeb=-LGDAMRP1.EDW.0_&desktop=*-1.*-1.0.0.0&preferences=*-1.*-1.0.0.user.general..1.
```