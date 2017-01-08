# Java 실행 환경 구성
## class path 설정
Java 1.6 이후 부터 특정경로내에 포함된 여러개의 jar를 Class Path에 지정가능
```bash
java -cp .:/custom-jar-lib/*:/custom-class-directory mainJava
```
* ':' : Classpath간 구분자 (Windows에서는 ';'를 사용) 
* /custom-jar-lib/* : jar파일들이 위치한 경로가 /custom-jar-lib이라고 할때 여기에 위치한 모든 jar를 포함 
* /custom-class-directory : class파일들이 위치한 경로가 /custom-class-directory이라고 할때 이 경로 하위에 포함된 모든 class파일들을 포함
* classpath를 기준으로 resource를 참고할 경우 classpath에 선언된 순서를 기준으로 수행
