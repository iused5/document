# 리포트 SDK
## 일자 Filter 생성시 selection box의 폭이 너무 좁게 표시되는 경우
* mstrFilterEditor의 style을 width:100% !important; 로 지정

## Prompt Detail, Report Detail 표시 방지
* RW_Contents.jsp, Report_Contents.jsp의 id가 mstrPanelPortrait인 객체의 style.display = "none"으로 javascript에서 처리 (style 반영되지 않음)

## Report Bean을 Inbox에서 제거
생성된 Report Bean으로부터 프롬프트 정보등을 사용한 뒤 inbox로부터 제거
```java
public static void jobClose(WebReportInstance wrb) {        
    WebReportMessage oWebReportMessage;
    try {
        oWebReportMessage = wrb.getMessage();
        oWebReportMessage.removeFromInbox();
    } catch (WebObjectsException e) {
        e.printStackTrace(LLog.err);
    }
}
```

## How to create a standard report toolbar button that will open a new customized browser window and pass report state information to the new page in MicroStrategy Web 8.x.x
* Tech Support - TN12188

## Report toolbar에서 click event 처리
* TN36260: How to display a warning message when clicking on the "Insert New Metric"(fx) icon in the report toolbar before the editor is loaded in MicroStrategy Web 9.0.2?

## Report 에서 Cell 클릭시 선택된 Cell에 대한 값 표시
* MSDL: Home > Web SDK > Customizing MicroStrategy Web > Part I: Fundamentals of Customization > Scenarios: Customizing Existing Functionality > Displaying Objects Selected in a Report

## 리포트 이벤트
### 빈 리포트에서 attribute, matric double click, ondrag end event에 custom function 추가
* mstrReportObjectsImpl.js > mstrReportObjSelectionsImpl.prototype.ondragend
* mstrGridReport.js > mstrGridReport.prototype.ondrop
* core.js > mstr.controller.EventManager > E.notifyListenersOfId
* mstrGridReport.js > mstrGridInfo.prototype.buildGridUnitInfo (9705) 에서 matric의 객체 정보를 처리
* mstrGridReport.js > mstrGridReport.prototype.addUnit (3085) 에서 matric의 객체 정보를 처리
* template.js > mstrTemplateImpl.prototype.add (960)의 this.addFromObjInfo(objInfo) 문장 수행 후 화면에 표시
* mstrGridReport.js > mstrGridReport.prototype.addUnit (3129) 에서 drag, double click에 의한 event 수행됨

### report 실행시 ...
* mstrGridReport.js > this.submitAction(...) (7545)
* mstrGridReport.js > mstrGridReport.prototype.addAction (600)
* updateManager.js > UpdateHelper.submitRequest (1563) ... UpdateHelper.appendPageStateToParam(oParams) (1574)
* (실행되고 난 후) updateManager.js > mstrUpdateManager.prototype.flushAndSubmitChanges (678)
* mstrGridReport.js (3130) 에서 objInfo를 이용 (objInfo.alias 등)

### 빈리포트에서 object browser click event
* mstrTreeViewImpl.js > mstrTreeViewImpl.prototype.util_submitForm (378)

## Runtime 시 리포트 구성 요소 조회
* TN16443(TN6100-8X-2358) - How to retrieve the report template objects using the Microstrategy Web SDK 9.0.x

## Runtime 시 리포트 수행 Job Id 조회
* TN38518 - How to Retrieve the JobID for a Report that is Executing Using the MicroStrategy Web SDK 9.2.1

## 계층 프롬프트 처리 예시 Prompt 처리
* TN8759 - How to use web objects to manipulate and answer a hierarchy prompt in MicroStrategy SDK 8.1.x

## SDK를 이용한 Report Data 추출
* TN12288 (TN6100-8X-0412) - How to execute a simple static Report Services Document using the MicroStrategy Web SDK 9.2.1
* TN30679 - How to Attach Element Prompt to a Report Using the MicroStrategy Web SDK 9.2.1

## Timeout 처리, 대용량 리포트 처리
* TN12867 (TN5600-75x-1884) - How timeout settings in MicroStrategy Web and the MicroStrategy Intelligence Server affect users in MicroStrategy Web 8.x
* MSDL - Home > Web SDK > Customizing MicroStrategy Web > Part II: Advanced Customization Topics > Using the Web Beans API > Types of Web Beans > ReportBean > Code Sample
* MSDL - Home > Web SDK > Customizing MicroStrategy Web > Part II: Advanced Customization Topics > Using the Web Objects API > Data Marts > Examples > Converting a Report to a Data Mart Report Using Objects and Beans
* MSDL - Home > Web SDK > Customizing MicroStrategy Web > Part II: Advanced Customization Topics > Using the Web Objects API > Displaying Report Grid > Examples > Executing the Report with the Desired Result Flags
* TN20179 (TN6100-8X-2459) - How to update server busy timeout setting using MicroStrategy SDK
* TN6890 - How to programmatically change the maximum report execution time using MicroStrategy Server SDK 8.1.x
* TN6883 (TN5600-71X-0419): 'Error in report results' error message appears when running a large report in MicroStrategy Web 8.x

## ReportBean 생성 및 WebInstance 접근
* TN17117 (TN6100-8X-2393) - How to clear the filter expression of a report using the MicroStrategy Java Web SDK 9.x

## Mart Report 속성 지정
* MSDL - Home > Web SDK > Customizing MicroStrategy Web > Part II: Advanced Customization Topics > Using the Web Objects API > Data Marts > Extended Properties for Data Marts

## Report 삭제
* TN30311- How to delete a report using the MicroStrategy Web SDK version 9.0.0?

## 리포트에 행데이터를 이용한 URL링크 표시
* MSTR API: ReportGridCellMetricValueImpl
* TN31309: How to Display the Metric Value as a Tooltip when the Metric Cell is Showing a Threshold Image using the MicroStrategy Java Web SDK 9.2.x and 9.3.x 

## 도큐먼트에서 그리드 컬럼 Drag 방지
* ver 10.11
* 커스터마이징 OIVM_Content_Core.jsp
```html
...
<script type="text/javascript">
    jQuery(funcction() {
        // 도큐먼트가 iframe 내에서 실행될 경우, 부모윈도우의 view-type 속성을 참조한다.
        if ($(parent.document.body).attr("view-type") == "maindashboard") {
            mstrmojo.dnd.startDrag = function(x,a) { return; }
        }
    });
</script>
...
```

## 리포트 컬럼 Resize, Drag 방지, 마우스 동작 방지
* version 10.11
* bone-report.js 의 내용을 커스터마이징
```javascript
// Drag 이벤트 핸들러를 재지정
mstrGridReport.prototype.ondragstart = function(H) { return; }
// 컬럼 Resize 이벤트 핸들러를 재지정
mstrGridReport.prototype.onresizestart = function(B) { return; }
// 마우스 동작 방지
mstrGridReport.prototype.onmousedown = function() { return; }
```

* mstr.css 의 내용을 커스터마이징
```css
/* Drag 시 커서를 일반상태의 커서로 변환 */
.mstrColWidthHandle { custor: auto; }
```

* bone-global.js 의 내용을 커스터마이징
```javascript
function rightClick(D) { return; }
```

## MSTR Library 링크 조회
``` javascript
mstrmojo.util.LibraryHelper.constructLibraryUrl() // 예시) "http://localhost:8080/MicroStrategyLibrary.2020/app"
mstrmojo.util.LibraryHelper.createTokenAndOpenLink(libUrl); // libUrl : 1)의 결과 또는 1)의 결과 중 /app 제거 
```