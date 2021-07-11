# 주요 URL API
## 비정형
evt: 3005  
src: mstrWeb.3005  
reportViewMode: 1  
reportDesignMode: 0:실행모드, 1:디자인모드 (EnumReportViewModes)  
reportID: {Report ID}  
currentTab: 2 (모든 개체)

## 정형 - Report
evt: 4001  
src: mstrWeb.4001  
reportID: {Report ID}  
hiddenSections: path,footer ...  
promptsAnswerXML: {Answer XML}

## 정형 - Document
evt: 2048001  
src: mstrWeb.2048001  
documentID: {Document ID}  
hiddenSections: path,footer ...  
promptsAnswerXML: {Answer XML}

## Excel - Report  
evt: 3067  
src: mstrWeb.3067  
group: export  
showOptionsPage: false  
defaultRunMode: excelFormattingGrids  
reportID: {ReportID}  
promptsAnswerXML: {Answer XML}

## Excel - Document
evt: 3069  
src: mstrWeb.3069  
executionMode: 4  
documentID: {Document ID}  
promptsAnswerXML: {Answer XML}

## PDF - Report
evt: 3062  
src: mstrWeb.3062  
reportViewMode: 1  
reportID: {Report ID}  
showOptionsPage: false  
promptsAnswerXML: {Answer XML}

## PDF - Document
evt: 3069  
src: mstrWeb.3069  
documentID: {Document ID}  
promptsAnswerXML: {Answer XML}
