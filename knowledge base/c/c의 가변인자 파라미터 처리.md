# C의 가변인자 파라미터 처리
## 가변인자 처리 예제
```java
#include <stdio.h>
#include <stdarg.h>
    
void f_BatchLog_RP(const char *batch, const char *fmt, ...)
{
    va_list ap;
    va_start(ap, fmt);
    printf("%s>", batch);
    vprintf(fmt, ap);
    va_end(ap);
}
    
int main(int argc, char *argv[])
{
    f_BatchLog_RP("VARGS", "%s=[%ld]\n", "buy_amt", 1000000000L);
    
    return 0;
}
```