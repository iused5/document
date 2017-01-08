# 안드로이드 카메라 오리엔테이션 결정
## Camera Preview 기초
### Landscape, Portrait
* Activity를 Landscape로 전환하는 것이 SW 설계기준임
* 실제로 google의 공식적 App 및 기본 카메라앱은 Landscape기준

### Preview / Picture Orientation
* Activity가 Landscape일 때
    * Preview는 조절 불필요
    * Picture는 CameraInfo의 orientation 값 만큼 회전 필요
* Activity가 Portrait일 때
    * Preview는 조절 필요
    * Picture는 CameraInfo의 orientation 값 만큼 회전 필요

## 각 상황별 Orientation (Activity가 Portrait일 때)
구분|Front|Back
----|-----|----
Preview|<, 거울 역상|<
Picture|>|<
\* ^가 기본 Orientation, <는 시계방향으로 90도, >는 시계방향으로 270도
각 Camera 정보 (Preview / Picture 해상도, Orientation)

### FRONT CAMERA
format:[4], width:[720], height:[1060]  
current facing:[1]  
picture size:[640x480]  
preview size:[640x480]  

__Suppoerted Picture Sizes__  
1280x960, 640x480, 352x288, 320x240, 176x144  

__Suppoerted Preview Sizes__  
1280x960, 640x480, 352x288, 320x240, 176x144  
facing:[0], orientation:[90]

__Current cameraId:[1]__  
facing:[1],orientation:[270]  
rotation:[0], info.orientation:[270]

### BACK CAMERA
format:[4], width:[720], height:[1060]  
current facing:[0]  
picture size:[640x480]  
preview size:[640x480]

__Suppoerted Picture Sizes__  
3264x2448, 3264x1836, 2560x1920, 3072x1728, 2048x1536, 2304x1296, 1280x960, 1536x864, 640x480, 352x288, 320x240, 176x144  

__Suppoerted Preview Sizes__  
3264x2448, 3264x1836, 2560x1920, 3072x1728, 2048x1536, 2304x1296, 1280x960, 1536x864, 640x480, 352x288, 320x240, 176x144  

__Current cameraId:[0]__  
facing:[0], orientation:[90]  
facing:[1], orientation:[270]  
rotation:[0], info.orientation:[90]  

## 기종별 처리 결과
### setDiaplyLocation(90) 결과
* 삼성 계열 및 LG 계열 문제없슴
### setDiaplayLocation(270) 결과
* 삼성 계열은 상하 반전된 형태, LG 계열은 정상
