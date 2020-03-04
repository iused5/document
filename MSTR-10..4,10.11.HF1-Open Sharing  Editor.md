## MicroStrategy 10.4 - 외부에서 공유 편집창을 호출
#### 일시 / 솔루션 / 버전 / 작업유형
2020-02-16, MSTR 10.4, 기능구현이 가능한 Javascript 함수 식별하여 외부에서 호출
#### 작업내용 - html5-vi.js 분석
* action의 처리
```javascript
mstrmojo.mstr._ObjectActions = mstrmojo.provide("mstrmojo.mstr._ObjectActions", { // line:241709
...
```
* 공유편집창표시 구현부를 SharingEditor명칭으로 검색 발견, breakpoint를 설정 후 this를 evaluate해서 'id'속성이 'mstr44'임을 확인
```javascript
ShowSharingEditor: function X() { // line:251672
...
```
* mstrmojo.all["mstr44"]를 evaluate 시 this와 동일결과 확인 
* mstrmojo.all["mstr44"].ShowSharingEditor({})
#### 테스트케이스
* 프리젠테이션모드에서 사용 가눙  
* 디자인모드에서 사용 가능  
* Viewer 권한 사용자 프리젠테이션/디자인 모드 사용 가능
* 공유기능에 의해 표시된 URL로 접속 후 프리젠테이션/디자인 모드에서 사용 가능
***
#### 일시 / 솔루션 / 버전 / 작업유형
2020-02-28, MSTR 10.11 HF1 (다른 버전에 대한 검토), 기능구현이 가능한 Javascript 함수/API 식별하여 외부에서 호출
#### 작업내용 - html5-vi.js 분석
* mstrmojo.all 에서 ShowSharingEditor 함수를 가진 속성을 검색
```javascript
$.each(mstrmojo.all, function(i, v) { if (v.ShowSharingEditor) { console.log(i); } })
// 11.1 HF1 버전에서 "mstr51" 반환
```
#### 작업내용 - Task를 이용한 방법
* /servlet/taskAdmin을 접속 (10.11 기준) 하여 'savePersonalView' Task에 대한 내용 확인, 요구 파라미터 messageId는 도시에의 경우 mstrApp.getMsgId(), mstrApp.sessionState로 획득 
* 전달되는 파라미터의 예시는 도시에에서 '공유'메뉴을 선택 시 전달되는 request로 확인