# C Macro의 가변인자 사용
MACRO 함수 내에서 가변인자를 사용하는 것은 여러 가지 상황에서 유용하게 쓰일 수 있다.  
그 동안 이 방법을 몰라 가변인자를 받아들이는 함수를 별도로 정의해서 사용했었는데 다음의
방법으로 해결한다. 이 부분 역시 C99 표준에서부터 지원한다.  
  
MACRO 함수 내에서 가변 인자를 사용하는 방법 역시 컴파일러 버전과 종류 그리고 UNIX 종류에 따라
틀리므로 주의해야 한다. 다음에 제시되는 형태는 IBM AIX서버의 xlc에서 사용했던 방법이다.
```c
/*--------------------------------------------------------------------------------------*/
/*  SERVICE를 제외한 기타 다른 함수 내에서의 로그처리를 위한 MACRO                      */
/*--------------------------------------------------------------------------------------*/
#define M_MSG_LOG(x1, ...) {
    mtr_isys_userlog(combuf, x1, __FILE__, (char *)__func__, __LINE__, __VA_ARGS__);
}
```
사용하는 방법은 printf 함수와 동일하다.
```c
M_MSG_LOG(ILOG, ">>>>> 이월처리 방식[%s] -> (10:정상이월처리 , 20:취소후이월처리)", COM_ACG_PRC_GB);
```
위와 같이 MACRO 함수의 가변인자 사용으로 인해 LOG 처리 함수의 경우 다양한 형태의 로그처리가 가능해진다.  
단, Pro\*C 전처리기에서 위에서 선언된 #define 부분을 오류로 인식하기 때문에 정상적으로 컴파일 되지 않는다.  
이를 가능하게 하기 위해 Pro\*C 컴파일러에게 위와 같은 부분에 대한 전처리기 예외를 인식시켜 줄 필요가 있다.
```c
/*----------------------------------------------------------------------------------------
-- PRO*C 컴파일러에서 sys_macro.h를 인식하지 못하도록 처리 : 
-- PRO*C에서 매크로함수 내의 가변인자를 syntax error로 처리(인식불가)하는 문제의 해결
-- PRO*C 전처리 컴파일 이후 C컴파일 시에는 정상적으로 인식함.
----------------------------------------------------------------------------------------*/
#ifndef ORA_PROC
#include "sys_macro.h"                          /* 시스템 매크로 함수 정의 Header file  */
#include "func_macro.h"                         /* 기능 매크로 함수 정의 Header file    */
#endif
/*--------------------------------------------------------------------------------------*/
```
위와 같이 sys_macro.h와 func_macro.h 파일에서 Pro\*C가 인식하지 못하는 형태의 MACRO 선언을 해둔 후  
\#ifndef ORA_PROC ~~~~ \#endif 사이에 include를 해두면 Pro\*C 전처리기에서 해당 파일을 제외한 후 컴파일을 한다.  
Pro\*C 컴파일 시에는 제외되지만 이후에 진행이되는 C 컴파일시에는 정상인식을 하기 때문에 컴파일 오류가
발생하지 않는다.