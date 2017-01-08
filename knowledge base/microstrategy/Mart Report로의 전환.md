# Mart Report로의 전환
* 대상 리포트 작성시 제한 사항 (예: 프롬프트를 포함 불가)
* 대용량 마트 리포트 수행시 오류 발생, 리포트빈의 setMaxWati(-1)를 적절한 곳에 적용 
```java
package com;
import java.sql.Timestamp;
import com.microstrategy.web.beans.EnumReportBeanEvents;
import com.microstrategy.web.beans.GenericRequestKeys;
import com.microstrategy.web.beans.GenericWebEvent;
import com.microstrategy.web.beans.ReportBean;
import com.microstrategy.web.beans.RequestKeys;
import com.microstrategy.web.beans.WebBeanFactory;
import com.microstrategy.web.beans.WebEvent;
import com.microstrategy.web.objects.WebIServerSession;
import com.microstrategy.web.objects.WebObjectInfo;
import com.microstrategy.web.objects.WebObjectsException;
import com.microstrategy.web.objects.WebObjectsFactory;
import com.microstrategy.web.objects.WebReportInstance;
import com.microstrategy.webapi.EnumDSSXMLApplicationType;
import com.microstrategy.webapi.EnumDSSXMLDisplayMode;
import com.microstrategy.webapi.EnumDSSXMLExecutionFlags;
import com.microstrategy.webapi.EnumDSSXMLStatus;
    
public class ConvertToMartReport {
    private static final String SERVER = "server"; // BI Server name
    private static final String PROJECT = "project"; // Project name
    private static final String ADMIN_ID = "administrator"; // User name
    private static final String ADMIN_PWD = "*********"; // User password
    private static final String REPORT_ID = "reportid"; // Report ID
    
    private WebIServerSession session = null; // BI Server session
    private ReportBean bean = null; // Report bean - Mart report로 변환될 report, Object ID는 REPORT_ID 
    private ReportBean mart = null; // Mart report bena - Report bean으로부터 변환됨
    private WebObjectInfo dbrole = null; // Mart report로 변환시 이용될 DB Role
    
        /* Log에 사용하기 위하여 현재 시각을 출력 */
        public final String getNow() {
            Timestamp t = new Timestamp(System.currentTimeMillis());
            return t.toString();
        }
    
        /* Log를 출력 */
        private final void logging(final String log) {
            System.out.format("[%s] %s\n", getNow(), log);
        }
        
        /* BI Server Session을 생성 */
        private void connectSession() {
            WebObjectsFactory factory = WebObjectsFactory.getInstance();
            
            session = factory.getIServerSession();
    
            session.setServerName(SERVER);
            session.setServerPort(0);
            session.setProjectName(PROJECT);
            session.setLogin(ADMIN_ID);
            session.setPassword(ADMIN_PWD);
            session.setApplicationType(EnumDSSXMLApplicationType.DssXmlApplicationCustomApp);
        }
        
        /* BI Sever Session close */
        private void closeSession() throws WebObjectsException {
            session.closeSession();
        }
        
        /* Report Bean을 생성 */
        private void setReportBean() throws WebObjectsException {
            final String sessionID = session.getSessionID();
            
            WebBeanFactory beanFactory = WebBeanFactory.getInstance();
            bean = beanFactory.newReportBean();
            bean.setSessionInfo(session);
            bean.setObjectID(REPORT_ID);
        }
    
        /* Mart Report Bean을 생성*/
        private void setMartReportBean(final String rid) throws WebObjectsException {
            final String sessionID = session.getSessionID();
            
            WebBeanFactory beanFactory = WebBeanFactory.getInstance();
            mart = beanFactory.newReportBean();
            mart.setSessionInfo(session);
            mart.setObjectID(rid);
        }
        
        public boolean hasFinishedExecution(int status) {
            logging("polling ... [" + status +"]");
            
            switch (status) {
            case EnumDSSXMLStatus.DssXmlStatusResult:
            case EnumDSSXMLStatus.DssXmlStatusXMLResult:
            case EnumDSSXMLStatus.DssXmlStatusErrMsgXML:
                return true;
            default:
                return false;
            }
        }
    /*
        * 주 처리 함수
        *      Requirement
        *          사용자가 생성한 Report의 수행결과 Data를 Table에 저장함.
        *          대상 리포트의 Row수는 약 3,200,000 건
        * 참조 문서
        *      MSDL의 Home > Web SDK > Customizing MicroStrategy Web > 
        *      Part II: Advanced Customization Topics > Using the Web Objects API > Data Marts > Examples > 
        *      Converting a Report to a Data Mart Report Using Report Bean Events and Saving the Report Definition
        * 발생 오류 설명
        *      Report에 반환하는 data가 작을 경우 정상 작동하나, 
        *      Large Report를 수행할 경우 bean.handleRequest(mr) 수행시 오류가 발생함
        */  
    public void convertToDatamart() throws WebObjectsException, InterruptedException {
        logging("start converToDatamart");
        
        /* Session을 생성 */
        connectSession();
        
        try {
            /* 변환될 Report Bean을 생성 */
            setReportBean();
            
            WebReportInstance inst = bean.getReportInstance();
                        
            /* Ready 상태가 되었음을 확인하기 위해 pollStatus() 호출 */
            inst.setAsync(false);
            
            while (true) {
                if (hasFinishedExecution(inst.pollStatus())) {
                    break;
                }
                Thread.sleep(2000);
            }
            
            /* Report Bean Data collect - 수행하지 않을 경우 we에 할당된 값들이 정상적으로 할당되지 않음 */
            bean.collectData();
            
            WebEvent we = bean.getWebEvent(EnumReportBeanEvents.REPORT_EVENT_DEFINE_DATAMART);
            
            /* Mart Report로 변환하기 위한 Argument를 할당 */
            RequestKeys mr = new GenericRequestKeys();
            mr.add(GenericWebEvent.URL_EVENT_NAME, String.valueOf(EnumReportBeanEvents.REPORT_EVENT_DEFINE_DATAMART));
            mr.add(we.getArgumentName(EnumReportBeanEvents.REPORT_EVENT_ARGUMENT_DM_TABLE_NAME), "report_converted"); // Table Name 지정
            mr.add(we.getArgumentName(EnumReportBeanEvents.REPORT_EVENT_ARGUMENT_DM_NAME_MACRO), "1");
            mr.add(we.getArgumentName(EnumReportBeanEvents.REPORT_EVENT_ARGUMENT_DM_APPEND), "2");                      
            mr.add(we.getArgumentName(EnumReportBeanEvents.REPORT_EVENT_ARGUMENT_DB_ROLE_ID), "24DF21C748DE10E13465E4B59F48EA8D"); // 이용될 DB Role을 지정
            
            bean.handleRequest(mr); // 오류가 발생함
    
            bean.collectData();
            
            WebEvent weSave = bean.getWebEvent(EnumReportBeanEvents.REPORT_EVENT_SAVE_AS);
            
            /* 변환된 Mart Report를 저장 */
            RequestKeys mrSave = new GenericRequestKeys();
            mrSave.add(GenericWebEvent.URL_EVENT_NAME, String.valueOf(EnumReportBeanEvents.REPORT_EVENT_SAVE_AS));
            mrSave.add(weSave.getArgumentName(EnumReportBeanEvents.REPORT_EVENT_ARGUMENT_SAVE_AS_NAME), "MARTREPORT1"); // Report Name 지정
            mrSave.add(weSave.getArgumentName(EnumReportBeanEvents.REPORT_EVENT_ARGUMENT_SAVE_AS_PARENT_FOLDER_ID), "EB21DBDF11E1C5AD207700802FD7E34C"); // 저장될 폴더 위치를 지정
            mrSave.add(weSave.getArgumentName(EnumReportBeanEvents.REPORT_EVENT_ARGUMENT_DISPLAY_FLAGS), String.valueOf(EnumDSSXMLDisplayMode.DssXmlDisplayModeDatamart)); 
            
            bean.handleRequest(mrSave);
            
            String rid = bean.getObjectID();
            
            setMartReportBean(rid); // 저장된 Mart Report를 Report Bean으로 생성 
            
            mart.setExecutionFlags(EnumDSSXMLExecutionFlags.DssXmlExecutionGenerateDatamart); // Mart Report 수행시 Target Table로 Data가 Insert 되도록 설정 
            mart.collectData(); // Mart Report 수행
        } catch (Exception e) {
            logging("error");
            e.printStackTrace();
        }
        
        closeSession(); // BI Session close;
        
        logging("end converToDatamart");
    }   
    
    public static void main(String[] args) throws WebObjectsException, InterruptedException {
        ConvertToMartReport convert =   new ConvertToMartReport();
        convert.convertToDatamart();
    }
    
}
```