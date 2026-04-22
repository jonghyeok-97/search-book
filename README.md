# 프로젝트
- 도서 검색 프로젝트

# 작업 소개
- 외부 API 연동 -> OpenFeign 단위 테스트

# 요구사항
- 도서 검색 기능
  - 도서 제목으로 도서를 검색하여 도서 정보를 제공
  - 검색 결과는 페이징으로 제공
- 도서 검색 통계 기능
  - 사용자들이 검색한 도서에 대한 검색어 횟수를 제공
  - 사용자들이 검색한 도서 순위를 상위 5개 제공
- ex Naver-(https://developers.naver.com/docs/serviceapi/search/book/book.md)

# 작업 목록
EPIC: SB-0
- SB-1: 규모 추정
- SB-2: 요구사항 작성
- SB-3: 멀티모듈 구성
- SB-4: 외부 api 연동
- SB-5: api 서버 구현
- SB-6: DB 연결
- SB-7: 검색 통계 기능 구현
- SB-8: 문서화
- SB-9: 고가용성 설계
- SB-10: Docker 접목, MySQL 개발환경 구축
- SB-11: db 모듈 분리

# Self 고민 포인트 QnA
- Q: external 모듈의 naver-client 와 kakao-client 를 패키지 or 모듈 분리?
  - A: 수정 주기가 다르다는 점에서 모듈로 분리
- Q: core의 BookRepository 구현체인 NaverBookRepository/KakaoBookRepository 의 위치는 external 모듈 or core 모듈?
  - A: 