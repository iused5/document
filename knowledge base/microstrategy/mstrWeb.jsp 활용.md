# mstrWeb.jsp 활용
## Report 객체 및 데이터 접근
```jsp
...
<%@ page import="com.microstrategy.web.app.beans.PageComponent"
%><%@ page import="com.microstrategy.webapi.EnumDSSXMLNodeType"
%><%@ page import="com.microstrategy.web.beans.*"
%><%@ page import="com.microstrategy.web.objects.*"
...
PageComponent mstrPage = (PageComponent)request.getAttribute("mstrPage");
    
try {
    WebComponent wc = mstrPage.getTargetBean();
    
    if (wc instanceof com.microstrategy.web.beans.ReportBean) {
        ReportBean rb = (ReportBean)wc;     
        WebReportInstance wri = rb.getReportInstance();
        WebReportData wrd = wri.getResults();
        String SQL = wrd.getSQL(); // 사용된 SQL의 출력
    
        WebGridRows wgr = wrd.getWebReportGrid().getGridRows();   
        System.out.println(wrd.getWebReportGrid().getGridRows().size());
        for (int i = 0; i < wgr.size(); i++) {
            WebRow wr = wgr.get(i);
            for (int j = 0; j < wr.size(); j++) {
                // 현재리포트화면에 표시되는 매트릭값을 출력, 
                // 현재 리포트에 표시되는 데이터만 출력되므로 
                // 여러 페이지에 출력되는 경우는 1페이지 혹은 현재 페이지의 데이터만 출력
                WebRowValue wrv =   wr.get(j);
                System.out.println(wrv.getValue());
            }
        }
    } else 
    if (wc instanceof com.microstrategy.web.beans.RWBean) {
        RWBean db = (RWBean)wc;     
    }
} catch (Exception e) {
    e.printStackTrace(System.out);
}
```
## Active Session 접근
```jsp
...
%><%@ page import="com.microstrategy.web.app.WebAppSessionManager"
%><%
...
%><%
    PageComponent mstrPage = (PageComponent)request.getAttribute("mstrPage");
    WebAppSessionManager sessionManager = mstrPage.getAppContext().getAppSessionManager();
    WebIServerSession activeSession = sessionManager.getActiveSession();
    ...
%>
```

## 객체정보저장
```jsp
WebIServerSession isess = page.getAppContext().getAppSessionManager().getActiveSession();
WebObjectSource wos = isess.getFactory().getObjectSource();
wos.setFlags(wos.getFlags()| EnumDSSXMLObjectFlags.DssXmlObjectTotalObject
                        |EnumDSSXMLObjectFlags.DssXmlObjectComments 
                        |EnumDSSXMLObjectFlags.DssXmlObjectBrowser);
WebObjectInfo woi = wos.getObject(newDid, EnumDSSXMLObjectTypes.DssXmlTypeReportDefinition, true);
woi.setComments(newComments);
woi = wos.save(woi);
woi.populate();
```