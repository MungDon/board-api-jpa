# Board API Project

&nbsp;     
&nbsp;    

## Project Overview
> - 기여도 : 100%
> - 담당 기능 : 전체

&nbsp;     
&nbsp;  

## Project Tech Stack
> - **Framework** : Spring Boot (3.2.2)
> - **IDE** : IntelliJ
> - **DB** :  AWS RDS - PostgreSQL
> - **ORM** : JPA
> - **Language** : Java(JDK 17)
> - **Build Tool** : Gradle
> - **VCS** : Git

&nbsp;     
&nbsp;    

## Project Architecture
```text
 src
  │
  ├─config
  │      AppConfig.java : 애플리케이션 전반에서 필요로 하는 빈(Bean)을 설정하고 관리하기 위한 구성 파일
  │      DatabaseConfig.java : HikariCP 를 이용해 데이터 소스를 설정하고, 애플리케이션과 데이터베이스를 연결 풀을 관리하기 위한 파일
  │      DSLConfig.java : QueryDSL을 사용하기 위한 설정 파일
  │      S3Config.java : AWS S3 버킷과 연결을 위한 파일
  │      SecurityConfig.java : Spring Security 설정 파일
  │      SwaggerConfig.java : API 문서화를 위한 Swagger 설정 파일 
  │      WebMvcConfig.java : 커스텀 어노테이션 사용을 위한 설정 파일
  │
  ├─controller
  │      BoardController.java : 게시판과 관련된 요청을 처리하는 컨트롤러
  │      MemberController.java : 회원과 관련된 요청을 처리하는 컨트롤러
  │
  ├─dslrepository
  │      CustomBoardRepository.java  : 게시판 관련 QueryDSL을 이용한 커스텀 쿼리를 작성하고 처리하는 레포지토리
  │      CustomBoardRepositoryImpl.java :  ``
  │
  ├─entity
  │      BaseTimeEntity.java : 생성일과 수정일을 자동으로 관리하는 엔티티
  │      Board.java : 게시판 엔티티
  │      Image.java : 이미지 엔티티
  │      Member.java : 회원 엔티티
  │      Token.java : JWT 엔티티
  │
  ├─enums
  │      DeleteYn.java : 삭제여부를 나타내는 enum 파일
  │      Role.java : 회원 권한을 나타내는 enum 파일
  │      TokenExpire.java : JWT 의 만료시간을 나타내는 enum 파일
  │      TokenType.java : JWT 중 Access 와 Refresh 의 타입을 나타내는 enum 파일
  │
  ├─exception
  │      CustomException.java : 커스텀 예외 클래스
  │      ErrorCode.java : 내가 원하는 상태코드와 에러 메세지를 나태낸 enum 파일
  │      ErrorResponse.java : 발생한 커스텀에러의 응답 데이터 포맷을 설정해주는 파일
  │      GlobalExceptionHandler.java : 전역에서 발생한 커스텀 예외, 유효성 검사 예외 등을 핸들링하는 클래스
  │
  ├─jwt
  │      JWTFilter.java : 요청마다 JWT 이 유효한지 검사하는 필터
  │      JWTService.java : JWT 의 쿠키와 엔티티를 삭제 등록하고 유효한지 검사하는 기능을 담당하는 서비스
  │      JWTUtil.java : accessToken 및 RefreshToken 생성 만료시간 검사하는 기능을 담당하는 유틸 클래스
  │
  ├─repository
  │      BoardRepository.java : 게시판과 관련된 데이터베이스 접근 담당
  │      ImageRepository.java : 이미지와 관련된 데이터베이스 접근 담당
  │      JWTRepository.java : JWT와 관련된 데이터베이스 접근 담당
  │      MemberRepository.java : 회원과 관련된 데이터베이스 접근 담당
  │
  ├─request
  │  ├─board
  │  │      ReqBoardAdd.java : 게시판 등록 요청 DTO
  │  │      ReqBoardUpdate.java : 게시판 업데이트 요청 DTO
  │  │
  │  └─member
  │          ReqSignup.java : 회원가입 요청 DTO
  │
  ├─response
  │  │  ResComResult.java : 공통 응답 형식을 정의하는 클래스
  │  │
  │  └─board
  │          PagedBoardResponse.java : 게시판 목록 응답 DTO
  │          ResBoardDetail.java : 게시판 상세 응답 DTO
  │
  ├─service
  │      BoardService.java : 게시판 관련 비즈니스 로직 처리
  │      ImageService.java : 이미지 관련 비즈니스 로직 처리
  │      MemberService.java : 회원 관련 비즈니스 로직 처리
  │
  ├─user
  │      CustomUserDetail.java : 로그인 후 유저 정보를 시큐리티에 저장하기 위한 커스텀 클래스
  │      CustomUserService.java : 사용자 인증을 위해 사용자 정보를 조회하는 클래스
  │      LoginFilter.java : Security의 기본 로그인 요청 시 사용되는 필터 대신 직접 커스텀한 필터
  │
  └─util
          AuthUserData.java : 커스텀 어노테이션 클래스
          AuthUserDataResolver.java : AuthUserData 을 사용했을 때 사용자 인증 정보를 전달하는 역할
          CommonUtils.java : 전역에서 사용하는 공통된 메서드를 모아놓은 유틸 클래스
          CookieUtils.java : 쿠키의 생성 및 삭제, 조회 기능을 수행하는 유틸 클래스
```
&nbsp;     
&nbsp;    

## 아쉬운 점 
> `Spring Security`와 `JWT`를 사용하여 회원 기능을 구성하면서 `필터` 처리, `시큐어 쿠키` 관리,      
> `CORS` 설정 등 처음 접하는 부분에서 어려움을 겪었습니다. 이를 통해 더 많은 공부가 필요하다고 느꼈습니다.      
> JPA를 활용해 비즈니스 로직을 구성했지만, `Entity`와 `Repository`, `QueryDSL`, `JPQL` 등에 대한 경험이 부족해 어려움을 겪었습니다.     
> 이로 인해 더 효율적으로 구성할 수 있었을 것이라는 아쉬움이 남습니다.      
> 또한, `JPA`의 이론적 부분인 `영속성 컨텍스트`와 `엔티티 매니저`의 동작 방식에 대해 더 깊이 공부해야겠다고 생각했습니다.
> 테스트를 진행하면서 `로그`를 찍어야 할 일이 많았고, 매번 로그를 찍는 것이 불편하고 번거롭게 느껴졌습니다.      
> 그래서 `AOP`를 이용해 메서드의 시작과 끝에 로그를 자동으로 적용할 수 있다는 것을 알게 되었고,     
> 추후에 이를 적용해 보려 합니다.
