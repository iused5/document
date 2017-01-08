# Spring Exception Handler 설정시 주의 사항
* HandlerExceptionResolver를 implements 하는 class에서 Order interface도 동시에 Implement하지 않으면, Order Property가 적용되지 않는다.  
(여러 Exception Resolver를 적용시 새로운 HandlerExceptionResolver는 우선순위를 적용할 수 없음)
* ExceptionResolver에서 ModelAndView를 지정했다고 하더라고 ViewResolver의 설정에 따라 재지정된다.  
예를 들어, Custom HandlerExceptionResolver에서 view를 String Type으로 지정 InternalResourceResolver에서 처리되는것을 의도하였다 하더라도, 호출된 URL에 .json 문자열이 포함되어 있다면 ContentNegotiatingViewResolver에 의하여 application/json Type으로 처리된다.
