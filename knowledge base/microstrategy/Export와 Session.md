# MicroStrategy Export와 Session
WebBrowser 컨트롤에서 팝업을 호출할 경우 세션을 유지할 수 없으므로 다음과 같이 MSTR 세션문자열을 이용
## Export 에 세션 유지 - born-global.js (10.3)
```js
...
function exportReport(D, G, I, B, K, J) {
    ...
    microstrategy.updateManager.newWindowName = G;
    
    /* change-start */
    var usrSmgr = window.mstrApp.sessionState;
    if (usrSmgr) {
        usrSmgr = "&usrSmgr=" + usrSmgr;
    }
    
    microstrategy.updateManager.setFormAction(microstrategy.servletName + "?name="
        + Date.parse(new Date()) + usrSmgr);
    /* change-end */
    function F() {
    ...
}
```
## Grid Drill Popup 에 세션 유지 - born-report.js (10.3)
```js
...
mstrGridReport.prototype.drill = function (G, E, B, H) {
    ...
    if (D) {
        this.um.add([D]);
        this.um.setFormAction(microstrategy.servletName + "?name="
            + Date.parse(new Date()) + usrSmgr); //  apply & test
        this.um.flushAndSubmitChanges();
        this.um.acknowledgeRequest();
    }
    ...
}
```