# Webbrowser Component에서 로컬 Javascript 실행
## 로컬 html파일을 webbrowser component에서 실행시 권한에 의한 오류 발생
* 스크립트로 실행 권한 부여 필요  
* 권한 상태 표시: icacls {파일명}
* 웹 실행권한 부여: icacls {파일명} /setintegritylevel1 Low

## webbrowser component에 표시되는 웹페이지의 javascript 실행
```javascript
Dim objIE As SHDocVw.InternetExplorer
Set objIE = Me.webbrowser.Object
objIE.Document.parentWindow.execScript ("setSearch(""" + "!@#$%^&*()" + """)")
    
<script type="text/javascript">
function setSearch(param) {
    $("#p1").text(window.location.search);
    $("body").append("<div>" + param + "</div>");
    archText();
    ...
}
```    