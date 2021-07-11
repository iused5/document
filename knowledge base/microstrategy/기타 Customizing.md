# MicroStrategy Customizing
## Object Browser의 이상동작
* Object Browser에서 하위계층에 위치한 객체들을 새 리포트에 적용시 Object Browser가 refresh되어 기존 작업폴더에서 root 폴더로 위치가 이동되어 표시되므로 특정폴더내의 객체에 대한 연속적인 작업시 불편한 문제(LGT case)
* WEB Server 설정값 중 보안 > 쿠키 보안 단계의 값을 '쿠키 활성화'로 지정

## Report명과 내용만 표시
* MSDL에서 Displaying only the Report Name and Content in a Portlet

## "Return to" 기능 (path page section에 있는)에 페이지를 포함시킬지 여부 결정
```xml
<!-- track="true" -->
<page desc="My Reports" desc-id="mstrWeb.3" feature-id="enable-profile-reports" login-required="true" name="my" persist-mode="8" track="true">
```

## Long Description 조회
* TN35132 - How to retrieve the long description of an object using the MicroStrategy Web SDK 9.0.x and 9.2.1?

## Folder Browsing Page에서 표시 아이콘 변경 등
* Home > Web SDK > Customizing MicroStrategy Web > Part I: Fundamentals of Customization > Scenarios: Cosmetic Changes for Look and Feel > Modifying the Look and Feel of the List View for Folder Contents on Folder Browsing Pages

## 사용내역분석
* TN16124 - What are the corresponding statistics tables for each module in statistics configuration for MicroStrategy Intelligence Server 8.x ?
* TN20537 - New feature in MicroStrategy Enterprise Manager 9.0: Ability to log Statistics on prompt answers used during report execution.

## 로그아웃 버튼 관리자에게만 표시
* How to hide the logout link from non-administrators with the MicroStrategy Web SDK 10.9

## SDK를 이용하여 프롬프트 정보 조회 시 캐쉬 초기화
* '개체 캐쉬'를 삭제하여 프롬프트 캐쉬 정보 초기화

## 인증 시 시간 소요 현상 대응
* How to disable reverse DNS lookup for MicroStrategy Web
* https://community.microstrategy.com/s/article/KB44758-How-to-disable-reverse-DNS-look-up-for-MicroStrategy-Web?
language=en_US
* https://community.microstrategy.com/s/article/KB40984-How-to-disable-DNS-lookup-on-MicroStrategy-Web?language=undefined

## MSTR 클러스터 환경에서 큐브의 메모리 적재
* https://community.microstrategy.com/s/article/KB35142-New-feature-in-MicroStrategy-Intelligence-Server-9-0-2?language=en_US
* https://community.microstrategy.com/s/article/How-do-cubes-and-caches-synchronize-in-a-clustered-environment?language=en_US
* https://www2.microstrategy.com/producthelp/Current/Library/en-us/Content/Monitoring_and_modifying_Intelligent_Cube_status.htm