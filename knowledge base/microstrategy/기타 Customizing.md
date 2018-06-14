# MicroStrategy Customizing
## Object Browser의 이상동작
* Object Browser에서 하위계층에 위치한 객체들을 새 리포트에 적용시 Object Browser가 refresh되어 기존 작업폴더에서 root 폴더로 위치가 이동되어 표시되므로 특정폴더내의 객체에 대한 연속적인 작업시 불편한 문제(LGT case)
* WEB Server 설정값 중 보안 > 쿠키 보안 단계의 값을 '쿠키 활성화'로 지정

## SendNow 화면 이용시 이메일주소 입력 중 특정 도메인만 허용할 경우
* /javascript/editor.js의 내용 중 mstrEditorImpl.validateInputs=function(obj,enforceValidation)의 내용 수정

## Report명과 내용만 표시
* MSDL에서 Displaying only the Report Name and Content in a Portlet

## "Return to" 기능 (path page section에 있는)에 페이지를 포함시킬지 여부 결정
```xml
<!-- track="true" -->
<page desc="My Reports" desc-id="mstrWeb.3" feature-id="enable-profile-reports" login-required="true" name="my" persist-mode="8" track="true">
```

## Listing User Groups
* TN36127: How to Programmatically Return a List of the Top Level User Groups using the MicroStrategy Java Web SDK 9.x
* TN39722: How to Show the List of Groups a User Belongs to using the MicroStrategy Web SDK 9.4.1

## LDAP 사용자 생성
* TN31207 - How to Create a New User Using the MicroStrategy Web 9.0.x API and Initialize the Profile Folders.

## 사용자 Profile 폴더 생성
* TN20088 - How to create a user with LDAP credentials using MicroStrategy Web SDK version 9.2.1?

## Top Level User Group 목록 생성
* TN36027 - How to construct WebSearch object to return only the top level User Group objects using MicroStrategy SDK 9.0.2- 9.2.1

## 사용자ID로 사용자객체 검색
* TN12322 (TN6100-75x-0418) - How to search for a User Object based on Login ID using the MicroStrategy Java Web SDK 9.x

## WebSearch 결과를 WebUser로 변환
* TN13809 (TN6100-80X-0464) - How to Search for a MicroStrategy User Object and Access its Properties Using MicroStrategy Java Web SDK 9.0.1

## ACL 처리 예시
* {MSTR Install Path}/SDK/samples/java/webobjects/AccessControlListSample.java

## Long Description 조회
* TN35132 - How to retrieve the long description of an object using the MicroStrategy Web SDK 9.0.x and 9.2.1?

## User가 Access가능한 Project리스트
* TN12363 (TN6100-800-0414) - How to get a list of projects a user can log into in MicroStrategy Java Web SDK 9.0.1

## 구독 링크, 메뉴, 아이콘 제거
* TN38678 - How to remove the Subscriptions link from within a report for all reports in MicroStrategy Web 9.x
* TN33047 - How to remove the link for subscriptions displayed for reports and documents in the folder page using the MicroStrategy Web SDK version 9.0.1?
* TN39697 - How to Remove the "My Reports" Shortcut for all Pages Using the MicroStrategy Web SDK 9.2.1

## Folder Browsing Page에서 표시 아이콘 변경 등
* Home > Web SDK > Customizing MicroStrategy Web > Part I: Fundamentals of Customization > Scenarios: Cosmetic Changes for Look and Feel > Modifying the Look and Feel of the List View for Folder Contents on Folder Browsing Pages

## 사용내역분석
* TN16124 - What are the corresponding statistics tables for each module in statistics configuration for MicroStrategy Intelligence Server 8.x ?
* TN20537 - New feature in MicroStrategy Enterprise Manager 9.0: Ability to log Statistics on prompt answers used during report execution.

## 로그아웃 버튼 관리자에게만 표시
* How to hide the logout link from non-administrators with the MicroStrategy Web SDK 10.9

## SDK를 이용하여 프롬프트 정보 조회 시 캐쉬 초기화
* '개체 캐쉬'를 삭제하여 프롬프트 캐쉬 정보 초기화