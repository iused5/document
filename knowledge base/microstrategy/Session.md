# MicroStrategy Session
## WebIServerSesion.setApplicationType
* ESM에서 사용 시 EnumDSSXMLApplicationType.DssXmlApplicationDSSWeb으로 지정,
EnumDSSXMLApplicationType.DssXmlApplicationCustomApp로 지정 시 유효하지 않은 사용자 Session 생성 요청 시 오류 미발생
* EnumDSSXMLApplicationType.DssXmlApplicationCustomApp로 지정 시 어플리케이션의 로그인 모드에 '트러스트된 인증 요청'으로 선택되어 있지 않아도 사용 가능

## WebIServerSession.setClientID
* 사용통계에 사용자IP가 표시되려면 setClientID에 사용자IP가 지정되어야 함