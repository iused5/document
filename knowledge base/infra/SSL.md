# 사설 인증서 발급
## keytool 이용
* command line에서 mmc로 콘솔 접근
* 인증서 스냅인 추가
* 신뢰할 수 있는 루트 인증 기관 / 인증서 / KB card Bigdata CA Root
* 인증서 상세 정보 (예시)

|필드|값|
|---|---|
| 버전 | V3 |
| 서명 알고리즘 | sha256 |
| 발급자 | CN = KB card Bigdata CA Root, OU = KB card Bigdata, O = KB card, S = Seoul, C = KR |
| 유효 기간(시작) | 2018년 10월 8일 월요일 오전 9:09:41 |
| 유효 가간(끝) | 2028년 10월 5일 목요일 오전 9:09:41 |
| 주체 | CN = KB card Bigdata CA Root, OU = KB card Bigdata, O = KB card, S = Seoul, C = KR |
| 공개 키 | RSA (2048 Bits) |
| 공용 키 매개 변수 | 05 00 |
| 기본 제한 | Subject Type=CA, Path Length Constraint=None |
| 키 사용 | Certificate Signing, Off-line CRL Signing, CRL Signing (06) |
| Netscape Cert Type | SSL CA, SMIME CA (06) |
| 지문 알로리즘 | sha1 |
| 인증 경로 | KB card Bigdata CA Root |

## 키, 인증서 생성
* keytool -genkey -alias -alias kivew -keyalg RSA -keystore kview.jks

|항목|값|
|---|---|
| 개인키 비밀번호| kviewhttps
| 이름과 성 | KB card Kview CA Root
| 조직 단위 이름 | KB card Kview
| 구/군/시 | Seoul
| 시/도 | Seoul
| 국가 코드 | KR
* kview.jks 생성
* keytool -certreg -keyalg RSA -alias kview -file kview.csr -keystore kview.jks  
-> kview.csr 생성
* keytool -export -alias kview -storepass kviewhttps -file kview.cer -keystore kview.jks  
-> kview.cer 생성

## OpenSSL 이용
* set OPENSSL_CONF=...\OpenSSL\bin\openssl.cnf
* openssl genrsa -out kview.key 2048
* ooenssl req -new -key kview.key -out kview.csr
* openssl req -x509 -in kview.csr -signkey kview.key -out kview.crt -days 3650 -sha256 -extfile v3.ext