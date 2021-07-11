# 도시에 SDK
## API를 이용한 도큐먼트와 Visual Insight (dossier) 의 구별
* How do I differentiate between a dossier and a document with the data I get from the rest API?

구분 | 비교식
--- | --- 
도큐먼트 | WebObjectInfo.getViewMediaSettings().getDefaultMode() == EnumDSSXMLViewMedia.DssXmlViewMediaViewStatic
Visual Insight | WebObjectInfo.getViewMediaSettings().getDefaultMode() == EnumDSSXMLViewMedia.DSSXmlViewMediaHTML5Dashboard

## 도시에에서 선택된 필터항목 조회
* version 2019
```javascript
mstrmojo.all["mstr408"].selectIndices
...
// 오브젝트 속송명 확인을 위한 코드
jQuery.each(mstrmojo.all, function(i, v) { if (v.selectedIndices) { console.log("=>", i, v.selectedIndices); }});
jQuery(".mstrmojo-ui-CheckList").each(function(i, v) { var eid = $(v).attr("id"); console.log("=>", eid, mstrmojo.all[eid].selectedIndices); });
```

## Dossier Close 버튼 처리
1. Configuration 파일 설정
2. HTML5VI_Content_Core.jsp 의 <web:displayBean beanName="menuBean" /> 을 상단에 하고, close 메뉴정보를 처리한 다음 new mstrmojo.vi.VisualInsightApp(....)의 파라미터로 전달
* 또는, .mstrmojo-VIBoxPanelContainer-closeBtn { display:none !important; }


## 커스텀그룹 만들기 호출
* 소스 html5-vi.js, mojo_home.js
* mstrmogo.mstr._ObjectActions.openCustomGroupEditor({});

## 공유 메뉴 기능 호출
* mstrmojo.all["mstr52"].ShowSharingEditor ({});

## ESC 키 프리젠테이션 모드 토글 방지
```` javascript
/* html5-vi.js */
function Aw(Bm) {
    if (Bm.keyCode === 27) {
        this.togglePresentationMode();
    }
}
````
