# MicroStrategy Export
## Export stream control
* com.microstrategy.web.app.transforms.ReportExportIServerTransform

## Export event
mstrGridReport.js  
mstrGridCommands.prototype.exec function 참조 (각 command에 대해 switch 문이 호출됨)  
내보내기 기능은 "export", "exportMenu" case에서 호출

## Export
common.js > var t = cmds[i].target (6101)  
DHTML.js > exportReport (3033)  
exportReport(defaultRunMode, target, type, viewMode, dontShowEmptyForm, dontShowMsg)  
exportReport(null, "_bland", "buttonPDF", 1); << 메뉴에서 내보내기 / PDF를 선택하였을 경우  
TN:34692 - How to use URL API to answer the prompts of a report and export to excel with formatting without showing the export options page in MicroStrategy Web 9.0.1.  

## PDF Link 제거
* TN13856 (TN5600-801-2120) - How to remove the PDF export link from the reports page in MicroStrategy Web 8.x
* TN13223 (TN6100-801-0432) - How to permanently remove toolbar shortcuts or a toolbar button from reports in MicroStrategy Web 8.x?
* TN12545 (TN5600-8X-1778) - How to disable the ability for users to export to and print a PDF in MicroStrategy Web 8.x
* TN38668 - How to Remove the Drop Down Export Menu on the Document Execution Page using the MicroStrategy Web SDK 9.2.1

## Export 시 response의 content-type 설정
* WEB-INF/xml/config/exportFormat.xml의 내용에 force-download="true" 엘리먼트를 추가함