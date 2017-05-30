package com.microstrategy.custom.sso;

import java.net.URL;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nets.websso.ssoclient.authcheck.AuthStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kt.bit.bidw.common.authority.SessionInfo;
import com.kt.bit.bidw.common.authority.SessionInfoSupport;
import com.kt.bit.bidw.common.authority.SsoUtil;
import com.kt.bit.bidw.common.util.CustomProperties;
import com.microstrategy.utils.MSTRUncheckedException;
import com.microstrategy.utils.StringUtils;
import com.microstrategy.utils.localization.LocaleInfo;
import com.microstrategy.utils.serialization.EnumWebPersistableState;
import com.microstrategy.web.app.AbstractExternalSecurity;
import com.microstrategy.web.app.EnumWebParameters;
import com.microstrategy.web.beans.RequestKeys;
import com.microstrategy.web.objects.WebIServerSession;
import com.microstrategy.web.objects.WebObjectsException;
import com.microstrategy.web.objects.WebObjectsFactory;
import com.microstrategy.web.platform.ContainerServices;
import com.microstrategy.web.platform.DefaultURIBuilderImpl;
import com.microstrategy.web.platform.ParameterBuilder;
import com.microstrategy.webapi.EnumDSSXMLAuthModes;
import com.util.StringUtil;

/**
 * MSTR에서 제공하는 AbstractExternalSecurity를 상속받은 MSTR인증을 위한 class
 * 이 class가 호출되는 경우는 인증이 필요하거나, 프로젝트 변경 등이 발생할 경우
 * 인증은  기본적으로 세션변수, SSO 솔루션제공 API를 이용함
 * SSO 솔루션 제공  API이용할 경우는 쿠키를 이용하여, 브라우져가 종료되지 않은 상태라면 재접속이 가능하나,
 * 로그인창을 이용하여 세션변수만을 이용할 경우는 세션타임아웃시 세션종료 메세지를 표시 
 * <br/><b>History</b><br/>
 * <pre>
 * 2012. 4. 19. 최초작성
 * </pre>
 * @author 117258993
 * @version 1.0
 */
public class SSOESM extends AbstractExternalSecurity {

	final private static Logger logger = LoggerFactory.getLogger(SSOESM.class);
	
	/**   
	 *  MSTR 제공 주석<br/>
	 *  This class is an implementation of MicroStrategy External Security module.
	 *  It is a part of MicroStrategy Single Sign-On Sample. This class is useful 
	 *  when using MicroStrategy Web with identity management products. In such 
	 *  scenario, when a request is made to MicroStrategy, the identity management 
	 *  product authenticates the user and checks to see if he has access to 
	 *  MicroStrategy and if yes the request is sent to MicroStrategy Web with a 
	 *  token. MicroStrategy needs to use this token to validate the user 
	 *  and handle the request. The code in this class retrieves the token passed, 
	 *  validates the token by using the identity management product's API. 
	 *  If the token is valid, the ESM will create a new MicroStrategy Intelligence 
	 * 	Server session and return the newly created session to MicroStrategy Web.  
	 *  If it is invalid, then the ESM will tell MicroStrategy Web to redirect to 
	 *  a custom login page.<br>
	 * 
	 *  In order for this ESM to work properly, any initial request to MicroStrategy 
	 *  Web should include a parameter corresponding to the authentication server token.  
	 *  This parameter would normally be passed as part of the URL.  The name of this 
	 *  parameter should be defined in ssoesm.properties (default is SSO_TOKEN_NAME) 
	 *  and the value should be the token returned from the authentication server. <br>
	 *  
	 *  If a MicroStrategy Intelligence Server session does not exist, and an 
	 *  authentication server token is not found in the request or is not valid, then 
	 *  the ESM will direct MicroStrategy Web to redirect the request to a custom login 
	 *  page. This login page is defined in ssoesm.properties(as CUSTOM_LOGIN_URL). <br>
	 *
	 *  Finally, note that the ssoesm.properties file must define a URL that is used 
	 *  to authenticate the authentication server token.
	 *  The authentication URL is defined in ssoesm.properties(as SSO_URL).
	 */

    ////////////////////////////////////////////////
    //private static attributes
    
    //The name of properties file without file extension. 
    //The file contains SSO token parameter name, authentication URL and the custom Login page URL. 
    private static final String PROP_FILE_NAME = "ssoesm";
    
    //The below 3 constants define the names of the parameters in the properties file.
    
    //By default, the name of the property is the same as the name of the token parameter.
    private static final String SSO_TOKEN_NAME = "SSO_TOKEN_NAME";
    //The name in the properties file for the SSO custom login URL.
    private static final String CUSTOM_LOGIN_URL = "CUSTOM_LOGIN_URL";
    
    private static final String DEFAULT_SERVER = "DEFAULT_SERVER"; 
    //private static final String DEFAULT_PROJECT = "DEFAULT_PROJECT"; 
    private static final String SSO_TOKEN_VALUE = "SSO_TOKEN_VALUE";
    //The below variables are used to hold the values for the properties in ssoesm.properties.
    private static String ssoTokenName;
    private static String customLoginURL; // The custom login page URL
    private static String defaultServer;
    //private static String defaultProject;
    private static String ssoTokenValue;

    //Session State is saved in Servlet Session, this is the name of the attribute 
    private static final String SESSION_STATE = "SESSION_STATE";
    
    //saves error info passed from getAuthenticationRequest to getCustomLoginURL
    //This information is saved in thread local storage, which ensures that this 
    //information is shared across all calls on a single thread.  Two different 
    //threads will not see each other's error information.
    private static ThreadLocal errorContainer;
    
    ////////////////////////////////////////////////////////////////
    //The static initializer reads the ssoesm.properties file and loads the values 
    //into static variables upon class load.
    
    static {
        //read properties from the properties file
        final ResourceBundle props = 	ResourceBundle.getBundle(PROP_FILE_NAME);

        customLoginURL = 		(String)props.getObject(CUSTOM_LOGIN_URL);
        ssoTokenName = 			(String)props.getObject(SSO_TOKEN_NAME);
        defaultServer = 		(String)props.getObject(DEFAULT_SERVER);
        //defaultProject = 		(String)props.getObject(DEFAULT_PROJECT);
        ssoTokenValue = 		(String)props.getObject(SSO_TOKEN_VALUE);
    }
    
    	/**
    	 * 기존 접속된 사용자인지를 확인하기 위한 구분자로 ssoToken을 사용한다.
    	 * @param cntSvcs session 변수를 접근하기 위한 접근자 제공 
    	 * @return
    	 */
    	private String getSsoToken(final ContainerServices cntSvcs) {
    		final String sessionValue = (String) cntSvcs.getSessionAttribute(ssoTokenName);
    		
    		return sessionValue;
    	}
    	
    	/**
    	 * 세션에 ssoToken저장
    	 * @param cntSvcs session 변수를 접근하기 위한 접근자 제공 
    	 * @param ssoToken session에 저장될 sso token (사용자 ID)
    	 * @return
    	 */
    	private void setSsoToken(final ContainerServices cntSvcs, final String ssoToken) {
    		cntSvcs.setSessionAttribute(ssoTokenName, ssoToken);
    	}
    	
    /** 
     * MSTR 제공 주석<br/>
     * MicroStrategy Web will call this method when it needs a session. 
     * This method tells MicroStrategy Web how to handle this authentication 
     * request. In this implementation of the handlesAuthenticationRequest 
     * method, we will either direct the application to collect a session 
     * ID from the ESM or redirect to the custom login page. <br>
     *  
     * @return Depending the reason, token in the request and the runtime 
     * condition, it returns one of the two values: USE_CUSTOM_LOGIN_PAGE 
     * and COLLECT_SESSION_NOW. <br> <ul> 
     * 
     * <li> USE_CUSTOM_LOGIN_PAGE: When SSOESM cannot create user Intelligence 
     * Server session correctly for whatever reasons, it saves error info in 
     * errorContainer, and returns USE_CUSTOM_LOGIN_PAGE. MicroStrategy Web 
     * will make a call to getCustomLoginURL() next to get a custom login 
     * URL. The reasons are defined in ErrorInfo class (ErrorInfo.java). 
     * 
     * <li> COLLECT_SESSION_NOW: When SSOESM can create user Intelligence Server session 
     * successfully, it returns this value. MicroStrategy Web will make a 
     * call to getWebIServerSession() next to get the Intelligence Server session. </ul>
     * 
     * @see com.microstrategy.web.app.ExternalSecurity#handlesAuthenticationRequest(com.microstrategy.web.beans.RequestKeys, com.microstrategy.web.platform.ContainerServices, int)
     * @see com.microstrategy.sdk.samples.sso.ErrorInfo
     */
    /* 세션에 존재하는 userid와 접근을 시도하는 userid가 같다면 이전 세션을 재사용함.
	 * 같지 않다면, 기존 세션 삭제 후 신규 생성
	 * ESM은 로그인외에 현재 프로젝트 변경과 같은 경우에도 호출되므로, 
	 * 로그인은 프로젝트 변경등으로 인한 경우와 구분이 필요하다.
	 * MSTR세션생성시 웹세션에 구분자를 저장하고, ESM호출시 웹세션의 구분자 존재여부를 확인
	 * 있다면 세션 재사용, 아닐경우는 세션 생성 처리한다.
	 *
	 * 1. userid조회 (validate 호출 (SSO 쿠키 처리))
	 * 2. userid가 null이면 SSO 로그인 페이지로
	 * 3. oldSsoToken조회 (세션에서 조회)
	 * 4. sessionState조회 (세션에서 조회)
	 * 5-A. oldSsoToken과  userid가 같고 sessionState가 null이 아닌 경우
	 *		5-A.1-A. reconnectISSession 호출이 성공한 경우
	 *		5-A.1-B. 5-A.1-A.가 아닌경우 웹세션변수 제거  
	 * 5-B. 5-A.가 아닌경우
	 *		5-B.1. sessionState가 null이 아니면 MSTR세션종료, 웹세션변수 초기화
	 *		5-B.2-A. MSTR세션 생성 성공시 COLLECT_SESSION_NOW 반환
	 *		5-B.2-B. MSTR세션 생성 실패시 USE_CUSTOM_LOGIN_PAGE 반환    	
     */
    public int handlesAuthenticationRequest(final RequestKeys reqKeys,
        final ContainerServices cntSvcs, final int reason) {

    	// NO_SESSION_FOUND = 	1 
    	//		This reason is passed in if MicroStrategy Web is unable to find an existing session.
    	// SESSION_CLOSED = 	2 
    	//		This reason is passed in if the current session is no longer valid because it was closed, 
    	//		either explicitly or through an inactivity time-out. 
    	//		In this case, the ESM can attempt to correct the session problem by asking 
    	//		the user for a new set of credentials or taking some other action. 
    	//		This is a subsequent request, rather than the initial request by a user.  
    	// LOGIN_FIRST = 		4
    	//		This reason is passed in if a login-first session is needed. 
    	//		A login-first session must exist or be created in order for the Welcome page 
    	//		to display the list of all available projects for a particular user. 
    	//		This is a special case, which applies only when login-first is enabled. 
    	//		A login-first session is not an actual session on the Intelligence Server, 
    	//		but instead a template that will be used 
    	//		when an actual Intelligence Server session is created later in the authentication workflow. 
    	//		To create a login-first session, you need a valid set of credentials.
    	StringUtil.debugging(logger, "handlesAuthenticationRequest");
    	StringUtil.debugging(logger, "reason:" + reason);
    	
        //create errorContainer obj
        errorContainer = new ThreadLocal();

        // 1.
        final SessionInfo info =	validate(cntSvcs);
        
        StringUtil.debugging(logger, "loginInfo:" + info);
        
        // 2. userid를 얻을 수 없다면, SSO세션 유지시간 초과이거나, SSO포털접속없이 접근 
        if (info == null) {
        	StringUtil.debugging(logger, "loginInfo is empty.");
        	
        	return USE_CUSTOM_LOGIN_PAGE;
        }
        
        // 3.
        final String oldSsoToken = getSsoToken(cntSvcs);
        
        StringUtil.debugging(logger, "oldSsoToken:" + oldSsoToken);

        // 4.
        final String sessionState = (String)cntSvcs.getSessionAttribute(SESSION_STATE);
        
        // 5-A.
        final String userid =	(String)info.getMstrUserId();
        
        if (StringUtils.isEmpty(userid)) {
        	return USE_CUSTOM_LOGIN_PAGE;
        }
        
        if (StringUtils.isEqual(userid, oldSsoToken) && StringUtils.isNotEmpty(sessionState)) {
        	// 5-A.1-A.
            if (reconnectISSession(sessionState, cntSvcs, reqKeys)) {
                return COLLECT_SESSION_NOW;
            // 5-A.1-B.
            } else {
                // Cannot use existing session state; clear session state and token 
                // and redirect to custom login page.
                cntSvcs.setSessionAttribute(SESSION_STATE, null);
                setSsoToken(cntSvcs, null);
                    
                return USE_CUSTOM_LOGIN_PAGE;
            } 
        // 5-B.
        } else {
            // close Intelligence Server session, clear session state and clear oldToken
        	// 5-B.1.
            if (sessionState != null) {
                closeISSession(sessionState);
                cntSvcs.setSessionAttribute(SESSION_STATE, null);
                setSsoToken(cntSvcs, null);
            }

            // 5-B.2-A. MSTR세션 생성
            if (createISSession(info, reqKeys, cntSvcs)) {
                //session created successfully
                return COLLECT_SESSION_NOW;
            // 5-B.2-B.
            } else {
                //cannot create session
                return USE_CUSTOM_LOGIN_PAGE;
            }        	
        }
    }

    /**
     * MSTR 제공 주석<br/>
     * Get the URL of Custom Login Page
     * @return The custom login page url with the following URL parameters. 
     * To fetch,use request.getParameter().
     * <ul>
     * 	<li> ErrorCode : the error code 
     *  <li> ErrorMessage: the error message
     * 	<li> OriginalURL : the originalURL of user's request
     * 	<li> Server: the Intelligence Server name
     * 	<li> Port: the Intelligence Server Port number
     *  <li> Project : the MicroStrategy Project name
     * </ul> 
     * @see com.microstrategy.web.app.ExternalSecurity#getCustomLoginURL(java.lang.String, java.lang.String, int, java.lang.String)
     */
    public String getCustomLoginURL(final String originalURL, final String desiredServer,
        final int desiredPort, final String desiredProject) {
    	
    	StringUtil.debugging(logger, "getCustomLoginURL");
    	StringUtil.debugging(logger, "originalURL:" + originalURL + ",customLoginURL:" + customLoginURL);
    	
        //get errorInfo
        final ErrorInfo errorInfo = (ErrorInfo) errorContainer.get();
        final StringBuffer urlBuf =	new StringBuffer();
        
        //clear errorContainer
        errorContainer.set(null);
        
        /////////////////////////////////////////////////////////
        //Generate a URL for the custom logon URL and add URL paramaters 
        //to this url by using Parameter builder
        //Jan 20,2005:the desiredServer, desiredPort, and desiredProject are not always passed correctly
        //ref TQMS: 210171
        
        //add server parameter in the url if it is not there
        if (desiredServer != null && originalURL.toLowerCase(Locale.ENGLISH).indexOf("server=") == -1) {
        	urlBuf.append("&server=").append(desiredServer);
        }
        
        //add server port parameter in the originalURL if it is not there
        if (originalURL.toLowerCase(Locale.ENGLISH).indexOf("port=") == -1) {
        	urlBuf.append("&port=").append(desiredPort);
        }
        //add project name parameter in the originalURL if it is not there
        if (desiredProject != null && originalURL.toLowerCase(Locale.ENGLISH).indexOf("project=") == -1) {
        	urlBuf.append("&project=").append(desiredProject);
        }
        
        final ParameterBuilder parameterBuilder = new DefaultURIBuilderImpl();

        try {
        	final URL url = new URL(customLoginURL);

        	parameterBuilder.setTargetBase(url.getProtocol() + "://" + url.getAuthority() + url.getPath());
        	parameterBuilder.setTargetSuffix(url.getQuery());
            
            // 로그인페이지는 별도로 제공하지 않으며, 포털 로그인을 이용해야 한다. (SSO 또는 로그인)            
            /*
            if (errorInfo != null) {
                pb.addParameter("ErrorCode", Integer.toString(errorInfo.getCode()));
                pb.addParameter("ErrorMessage", errorInfo.getMessage());
            }
            pb.addParameter("OriginalURL", originalURL);
            pb.addParameter("Server", desiredServer);
            pb.addParameter("Port", Integer.toString(desiredPort));
            pb.addParameter("Project", desiredProject);
            */
            
            String mid = "5001";
            if (errorInfo != null) {
            	if (errorInfo.getCode() == ErrorInfo.AGENT_ACCESS_ERR) {
            		mid = "5003";
            	} else
            	if (errorInfo.getCode() == ErrorInfo.NO_ACCOUNT_ERR) {
            		mid = "5006";
            	}
            }
            parameterBuilder.addParameter("mid", mid);
            return parameterBuilder.toString();
        } catch (Exception e) {
            //throw an runtime exception
            throw new MSTRUncheckedException(e);
        }
    }
    
    /** 
     * MSTR 제공 주석<br/>
     * This method will return the WebIServerSession created for the user.  
     * This method is called by MicroStrategy Web when handlesAuthenticationRequest 
     * returns COLLECT_SESSION_NOW.
     * @return The WebIServerSession object containing the session that is to 
     * be used to communicate with the Intelligence Server.
     * @see com.microstrategy.web.app.ExternalSecurity#getWebIServerSession(com.microstrategy.web.beans.RequestKeys, com.microstrategy.web.platform.ContainerServices)
     */
    public WebIServerSession getWebIServerSession(final RequestKeys reqKeys,
        final ContainerServices cntSvcs) {
        
        try {
            ///////////////////////////////////////////////////
            //Step 1. Confirm session is non-empty - if empty, throw error.
            final String sessionState = (String) cntSvcs.getSessionAttribute(SESSION_STATE);

            if (StringUtils.isEmpty(sessionState)) {	
                throw new MSTRUncheckedException("WebIServerSession.getWebIServerSession(): Unable to restore session");
            }
            
            //////////////////////////////////////////////
            //Step 2. Restore session and ensure session is still live.
            final WebIServerSession userSession = WebObjectsFactory.getInstance().getIServerSession();

            userSession.restoreState(sessionState);
            if (!userSession.isAlive()) {
                userSession.reconnect();
                final String newState = userSession.saveState(EnumWebPersistableState.MAXIMAL_STATE_INFO);

                cntSvcs.setSessionAttribute(SESSION_STATE, newState);
            }
            return userSession;
            
        } catch (WebObjectsException e) {
            //In case of exception, most likely credential has been changed. 
            //clear session state and old session 
            cntSvcs.setSessionAttribute(SESSION_STATE, null);
            logger.debug("WebIServerSession.getWebIServerSession(): Unable to restore session");
            throw new MSTRUncheckedException(e);
        }
    }
    
    /** This method validates the token. It connects to the external 
     *  authentication URL and passes the token, read and validates the 
     *  XML response returned. If return code is pass, it checks for UserID 
     *  returned and returns the UserID. If return code is fail, it returns null.
     *  
     * @return If success, returns the UserID, otherwise saves errorInfo 
     * and returns null. 
     * 
     * SsoUtil을 호출이 성공하는 경우는 SSO사용자의 접근 및 세션타임아웃 발생에 의해서 ESM이 재호출되었을 때이며,
     * SSO를 통하지 않은 경우(로그인 창을 이용하는 경우)는 세션시간 경과시 재접속 할수 없으므로 안내메세지를 표시한다.<br/>
     * * 로그인 창을 이용한 로그인 시 세션변수는 이전 호출모듈에서 생성되어져야 한다.<br/>
     * * 협력사가 SSO를 통하여 특정시스템에 링크를 이용하여 접근하는 경우만 허용하며, 그 외의 경우는 허용되지 않은 접근경로임을 
     * 알리는 메세지를 표시한다. 확인을 위하여 링크를 호출하는 페이지에서 특정 세션변수를 할당하도록 하고, ESM에서 이를 이용하도록 한다. 
     *   >> 세션타임아웃에 의해서 세션변수는 삭제될 수 있슴, 메모리 쿠키사용
     * @param cntSvcs The ContainerServices object passed from MicroStrategy Web.     
     * @return 성공시 SessionInfo 반환, 실패시 null 반환
     */
    private SessionInfo validate(final ContainerServices cntSvcs) {
    	// 먼저 세션변수를 참고하고 없으면, SSO함수를 이용하여 세션변수를 만든다.
    	StringUtil.debugging(logger, "validate");
    	
        final HttpServletRequest request = 		((com.microstrategy.web.app.platform.ServletContainerServices)cntSvcs).getRequest();
        final HttpServletResponse response =	((com.microstrategy.web.app.platform.ServletContainerServices)cntSvcs).getResponse();    	
        
    	SessionInfo info =	SessionInfoSupport.getSessionAttribute(request.getSession());
    	if (info == null) {
	        final SsoUtil sso =	new SsoUtil(request, response);
	        
	        if (sso.getAuthStatus() == AuthStatus.SSOSuccess) {
				// SSO인증 성공, 세션변수 생성
				final Map<String, String> cookies =   
                        				sso.getCookies();

				String userId =			null;
				String userName =       null;
				String userOrgCd =      null;
				String userOrgName =	null;

				if (cookies != null) {
					userId =		cookies.get("userid");
					userName =      cookies.get("username");
					userOrgCd =     cookies.get("deptcd");
					userOrgName =	cookies.get("department");
				}
				
				info =	new SessionInfo();
				info.setRemoteIp(request.getRemoteAddr());
				info.setUserId(userId);
				info.setMstrAuthMode(EnumDSSXMLAuthModes.DssXmlAuthTrusted);
				info.setUserName(userName);
				info.setUserOrgCd(userOrgCd);
				info.setUserOrgName(userOrgName);    
				info.setAMode(SessionInfoSupport.A_MODE_SSO);
				
				SessionInfoSupport.setSessionAttribute(request.getSession(), info);
			}
    	} 
    	
    	SessionInfo result =	info;
    	final CustomProperties prop =	CustomProperties.getInstance();
    	final String allowAgent =		prop.getProperty("agent.login.allow");
    	
    	// 협력사의 허용되지 않은 접근에 대한 처리
    	// TODO: 허용 URL패턴 확정 필요
    	if (info != null && info.isAgent() && !"y".equalsIgnoreCase(allowAgent)) {
    		final String referer =			request.getHeader("referer");
    		final String defRef =			prop.getProperty("valid.agentreferer.exp");
    		final String[] defRefs =		defRef.split("\\|");
    		boolean isMatch =				false;
    		
    		for (int i = 0; i < defRefs.length; i++) {
    			StringUtil.debugging(logger, "referer:" + referer + ",defRef:" + defRefs[i]);
    			
    			if (referer.matches(defRef)) {
    				isMatch =	true;
    				break;
    			}
    		}
    		
    		if (isMatch) {
    			errorContainer.set(new ErrorInfo(ErrorInfo.AGENT_ACCESS_ERR, "direct access not allowed"));
    			result =	info;
    		}
    	} else 
    	if (info != null && info.isAgent()) {
    		result =	info;
    	}
    	
    	// MSTR사용자를 찾지 못한 경우
    	if (info != null && info.isMstrUser()) {
    		result =	info;
    	} else 
    	if (info != null && !info.isMstrUser()) {
			errorContainer.set(new ErrorInfo(ErrorInfo.NO_ACCOUNT_ERR, "mstr account not found"));
    	}
        
        return result;
    }
    
    /** 
     * MSTR제공 주석<br/>
     * This method will attempt to create a session on the Intelligence Server.
     * This is a multi-step process in this sample.  
     * First, this method will create a session using an administrator login on 
     * the Intelligence Server.  It will then look for a user whose login name 
     * is the SSO user ID.  If one is found, then the user's password is changed 
     * to a random password(for security reasons), and that user/password will 
     * then be used to create a session.  If one is not found, a new user is 
     * created on the fly.  The newly created user will also have a random password 
     * set for it.  In either case, the user and password newly set will be used 
     * to create a session for the user.  The newly created session's state will 
     * be saved as a session variable, if successful.
     * the name/value pairs that are passed in the request.
     * and false will be returned.
     * @param info	session 정보
     * @param reqKeys	The request keys passed from MicroStrategy Web.  These contain 
     * @param cntSvcs	The container services passed from MicroStrategy Web.
     * @return Returns whether the operation was successful.  If the operation was 
     * not successful, then the error information will be saved in the errorContainer, 
     */
    private boolean createISSession(final SessionInfo info, final RequestKeys reqKeys, final ContainerServices cntSvcs) {
        // TODO: SSOUserID가 협력사인 경우 stanard 인증을 위한 userid로 변환이 필요함
    	// 협력사 로그인을 위한 테이블 설계 후 처리 필요
    	//   >> 현재 계정매핑은 사용하지 않음
    	
    	//////////////////////////////////////////////////////
        //Step 1. get admin session from cache, use (Server Name, Server Port) 
        //	      as Cache Hint
        	
        //get IServer and Port Number from request keys
    	StringUtil.debugging(logger, "createISession");
    	
        String IServer = reqKeys.getValue(EnumWebParameters.WebParameterLoginServerName);
        final String port = reqKeys.getValue(EnumWebParameters.WebParameterLoginPort);
        final String project = reqKeys.getValue(EnumWebParameters.WebParameterLoginProjectName);
        
        if (StringUtils.isEmpty(IServer)) {
        	IServer = defaultServer;
        }
        
        int portNum = 0;

        try {
            if (!StringUtils.isEmpty(port)) {
                portNum = Integer.parseInt(port);
            }
        } catch (Exception e) {
            //if the port is not a valid number, use 0 as port number
            portNum = 0;
        }
        
        //if (StringUtils.isEmpty(project)) {
        //	project = defaultProject;
        //}
        	
        ///////////////////////////////////////////////
        //Step 4. create user session
        try {
            final WebIServerSession userSession = WebObjectsFactory.getInstance().getIServerSession();

            userSession.setServerName(IServer);
            if (StringUtils.isNotEmpty(project)) {
            	userSession.setProjectName(project);
            }
            userSession.setServerPort(portNum);
            
            final String userid =	info.getMstrUserId();
            final String pwd =		info.getMstrUserPwd();
            final int authtype =	info.getMstrAuthMode();
            
            userSession.setLogin(userid);
            if (authtype == EnumDSSXMLAuthModes.DssXmlAuthStandard) {
            	userSession.setPassword(pwd);
            	userSession.setAuthMode(EnumDSSXMLAuthModes.DssXmlAuthStandard);
            } else 
            if (authtype == EnumDSSXMLAuthModes.DssXmlAuthSimpleSecurityPlugIn) {
            	userSession.setAuthMode(EnumDSSXMLAuthModes.DssXmlAuthSimpleSecurityPlugIn);
            } else
            if (authtype == EnumDSSXMLAuthModes.DssXmlAuthLDAP) {
            	userSession.setPassword(pwd);
            	userSession.setAuthMode(EnumDSSXMLAuthModes.DssXmlAuthLDAP);
            }
            
            userSession.setDisplayLocale(LocaleInfo.getInstance(1042).getLocale());
            userSession.setLocale(LocaleInfo.getInstance(1042).getLocale());
            
            StringUtil.debugging(logger, "server,porject,userid,pwd,authtype:" + userSession.getServerName() + "," + userSession.getProjectName() + "," + userSession.getLogin() + "," + pwd + "," + userSession.getAuthMode());
            
            //set client id. TQMS 230484
            userSession.setClientID(cntSvcs.getRemoteAddress());
            userSession.setTrustToken(ssoTokenValue);
            
            userSession.getSessionID();
            final String sessionState = userSession.saveState(EnumWebPersistableState.MAXIMAL_STATE_INFO);

            cntSvcs.setSessionAttribute(SESSION_STATE, sessionState);
            setSsoToken(cntSvcs, userid);
        } catch (Exception e) {
            errorContainer.set(new ErrorInfo(ErrorInfo.MAKE_SESSION_ERR, e.getMessage()));
            return false;
        }

        return true;
    }
    
    /** 
     * MSTR 제공주석<br/>
     * This method will load the given session state, and ensure that the 
     * session is live.  If it is not live, then it will attempt to use 
     * the session to reconnect to the Intelligence Server.  
     * If it reconnects successfully, the session state will be re-saved 
     * on the container services object as a session variable.  
     * If the reconnect fails, then the session state and any existing
     * authentication server token will be removed.
     * @param sessionState The session state.
     * @param cntSvcs 	The container services obj for clearing session 
     * state and token when we cannot reconnect.
     * @return If a live session is saved in the session variable after calling
     * this method, then true is returned.  If the session was missing or 
     * invalid and not able to be reconnected, then false is returned, and 
     * the error is saved in the errorInfo thread local variable.
     */
    private boolean reconnectISSession(final String sessionState, final ContainerServices cntSvcs, final RequestKeys reqKeys) {
    	StringUtil.debugging(logger, "reconnectISSession");
        
    	//reconnect session
    	final WebIServerSession iss = WebObjectsFactory.getInstance().getIServerSession();
    	iss.restoreState(sessionState);
    	
    	final String newProject = reqKeys.getValue(EnumWebParameters.WebParameterLoginProjectName);
    	if (newProject != null && StringUtils.isNotEqual(newProject, iss.getProjectName())) {
    		StringUtil.debugging(logger, "set to new procjet:" + newProject);
    	
    		//Note: This sample code doesn't support single sign-on across Intelligence Servers 
    		iss.setProjectName(newProject);
    	}
    	
    	try {
    		if (!iss.isAlive()) {
    			StringUtil.debugging(logger, "reconnect session");
    			
                iss.setTrustToken(ssoTokenValue);
    			iss.reconnect();
    			final String newState = iss.saveState(EnumWebPersistableState.MAXIMAL_STATE_INFO);
    			cntSvcs.setSessionAttribute(SESSION_STATE, newState);
    		}
    	} catch (WebObjectsException e) {
    		//if cannot reconnect, most likely the credential has been changed.
    		errorContainer.set(new ErrorInfo(ErrorInfo.NO_TOKEN_FOUND, "No token found and session state cannot be reused"));
    		return false;
    	}
    	return true;
    }
    
    /** 
     * MSTR 제공 주석<br/>
     * Closes the Intelligence Server session contained in the given session state.
     * @param sessionState The session state.
     * @return Returns true if the session was successfully closed.  
     * Returns false if the session could not be closed.
     */
    private boolean closeISSession(final String sessionState) {
        try {
            final WebIServerSession iss = WebObjectsFactory.getInstance().getIServerSession();

            iss.restoreState(sessionState);
            iss.closeSession();
            return true;
        } catch (WebObjectsException e) {
            //most likely the ISeverSession has already expired or closed. 
            return false;
        }
    }
}
 