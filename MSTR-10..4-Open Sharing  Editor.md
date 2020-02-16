## MicroStrategy 10.4 - 외부에서 공유 편집창을 호출
#### 일시 / 솔루션 / 버전
2020-02-16, MSTR 10.4
#### 작업유형
기능구현이 가능한 Javascript 함수 식별
#### 작업내용
##### html5-vi.js  
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
##### 테스트케이스
* 프리젠테이션모드에서 사용 가눙  
* 디자인모드에서 사용 가능  
* Viewer 권한 사용자 프리젠테이션/디자인 모드 사용 가능
* 굥유기능에 의해 표시된 URL로 접속 후 프리젠테이션/디자인 모드에서 사용 가능