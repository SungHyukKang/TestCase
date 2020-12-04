# TestCase

------

## ![imge](https://img.shields.io/badge/ProjectType-TeamProject-green) ![imge](https://img.shields.io/badge/Language-Java-yellow) ![imge](https://img.shields.io/badge/TOOL-STS-green)

## 프로그램 소개 :thumbsup:

- 시험기간 동기들과 예상 시험문제와 답안지를 만들어 카카오톡 단체방에 서로 공유하여 풀던 기억을 되
  살려, 누구나 쉽게 문제를 내고 푸는 퀴즈 앱을 제작하기로 결심
- 누구나 쉽게 문제를 등록하고 풀어볼 수 있으며 더불어학습능력향상과 시험에 대비하는 능력을 키울
  수 있을 것으로 예상됨.
- application.yml 파일은 직접 resources 폴더에 생성해야 합니다.

## 주요 기술 요소

- SpringBoot + JPA + MySQL + Maven 을 이용한 프로젝트입니다.
- JWT 엑세스 토큰을 이용하여 로그인을 처리하였습니다.
- AWS EC2 Service를 이용하여 서버를 운용중입니다.
- Swift를 이용해 제작한 iOS App에서 쓰이는 API 서버입니다.
- 순환 참조 방지를 위하여 @JsonIgnoreProperties, @JsonBackReference 어노테이션을 사용하였습니다.
- Swagger를 이용하여 RESTful API를 문서화하였습니다.
- Spring Security를 이용하여 사용자의 권한에 따라 접근 가능한 페이지를 나누었습니다.
- DB와 OOP의 불일치성을 해결하기 위하여 JPA를 사용하였습니다.

## 전체 시스템 구조도

![image](https://user-images.githubusercontent.com/50865982/99974978-fcae3200-2de4-11eb-9605-4943ff2c5ffd.png)



------

## 프로그램 기능소개

|                 기능 명                 |                          기능 설명                           |
| :-------------------------------------: | :----------------------------------------------------------: |
|           로그인 및 회원가입            |          이메일 인증을 통한 회원가입 후 로그인 가능          |
|             문제 검색 기능              | 키워드를 이용하여 문제를 검색할 수 있습니다. 10개씩 페이징하여 데이터를 나누었습니다. |
|         문제 출제 및 풀기 기능          |    사용자는 직접 문제를 출제할 수 있으며 풀 수 있습니다.     |
|         다른 사용자와 비교 기능         | 자신이 푼 문제를 다른 사용자와 비교하여 둘 다 맞은 문제 , 자신만 맞은 문제 , 상대방만 맞은 문제를 확인할 수 있습니다. |
| 자신이 맞힌 문제 및 틀린 문제 확인 기능 |      자신이 맞힌 문제와 틀린 문제를 확인할 수 있습니다.      |
|              글 조회 기능               |                 게시글을 조회할 수 있습니다.                 |
|         회원 ID 및 PW 찾기 기능         | 회원가입시 기입한 이메일 정보로 ID 및 PW를 찾을 수 있습니다. |

 
<h2>시연 </h2>


![ezgif-6-b31cc3bb6778](https://user-images.githubusercontent.com/50865982/100862752-e84df180-34d6-11eb-8ef8-b61946433c56.gif)

![ezgif-4-b9dce5b14f70](https://user-images.githubusercontent.com/50865982/100862740-e4ba6a80-34d6-11eb-9dd4-e9494b283071.gif)

![ezgif-4-e7c4cd6b104a](https://user-images.githubusercontent.com/50865982/100862743-e6842e00-34d6-11eb-8840-8c5534de2fe9.gif)
