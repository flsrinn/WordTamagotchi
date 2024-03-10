# WordTamagotchi

## ✨ Introduction
2학년 2학기 객체지향언어2 기말프로젝트로 제작한 **산성비 게임**입니다. <br> <br>
This is a **Acid Rain Game** produced as an object-oriented language 2 final project for the second semester of the sophomore year. <br><br>
## 🔍 Language & Tools
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white) ![Eclipse](https://img.shields.io/badge/Eclipse-FE7A16.svg?style=for-the-badge&logo=Eclipse&logoColor=white)

## 🔨 Program Architecture
<img src="https://github.com/flsrinn/WordTamagotchi/assets/123474937/a13805c7-b0f8-4440-8b65-68cb48aaf957" width="500" height="400"> <img src="https://github.com/flsrinn/WordTamagotchi/assets/123474937/66c806e0-0687-4b48-ba67-7b8b37d78679" width="500" height="400">

## 🎮 Game Screen
### 🐈 Starting Screen
#### 메인 화면
<img src="https://github.com/flsrinn/WordTamagotchi/assets/123474937/82407bc9-0aec-490f-8e58-a075a3a379e1" width="500" height="300" ><br>
오디오 제어, 게임 시작, 랭킹 확인, 단어 추는 추가되지 않음)
0.5초 간격으로 단어가 떨어진다. 쉬움 단계일 경우 단어가 2.5초 간격으로 생성되고 어려움 단계일 경우 단어가 2.2초 간격으로 생성된다.<br> 
스테이지 1에서는 생명 3개가 주어진다. 오른쪽에 있는 pause 버튼을 누르면 게임이 중단되고 play 버튼을 누르면 다시 시작된다.<br>
스테이지 1에서는 점수가 올라갈 경우 알이 좌우로 움직이고, 100점에 도달할 경우 다음 스테이지로 넘어갈 수 있다. 
#### 다음 스테이지로 넘어가기
<img src="https://github.com/flsrinn/WordTamagotchi/assets/123474937/d9439fbf-39e4-447c-be5c-7b9b27e67216" width="500" height="300"><img src="https://github.com/flsrinn/WordTamagotchi/assets/123474937/c9d477b9-f65f-47b7-b01a-d836e44e0cea" width="500" height="300"><br>
각 스테이지에서 목표 점수에 도달할 경우 다음 스테이지로 넘어갈 수 있는 화면으로 전환된다.<br>
스페이스바를 연타하면 바가 채워지고, 바가 끝까지 다 채워지면 스테이지 클리어 효과음과 함께<br>
다음 스테이지로 넘어갈 수 있는 버튼이 나온다.<br>
#### 스테이지 2
<img src="https://github.com/flsrinn/WordTamagotchi/assets/123474937/f5d24526-199c-49d6-8b50-864e67303359" width="500" height="300"><br>
<img src="https://github.com/flsrinn/WordTamagotchi/assets/123474937/e6d1baca-130e-4a75-981e-e124fa91fb69" width="500" height="300"><img src="https://github.com/flsrinn/WordTamagotchi/assets/123474937/8a43c349-db0d-48cb-94a6-1f4469143a9a" width="500" height="300"><br>
0.4초 간격으로 단어가 떨어진다. 쉬움 단계일 경우 단어가 2.35초 간격으로 생성되고 어려움 단계일 경우 단어가 2초 간격으로 생성된다. <br>
스테이지 2에서는 생명 4개가 주어진다.<br>
스테이지 2부터 생명이 깎일 경우 몬스터가 눈물을 흘리고, 점수를 얻을 경우에는 몬스터가 웃으며 상하로 움직인다. <br>
250점에 도달할 경우 다음 스테이지로 넘어갈 수 있다. 
#### 스테이지 3
<img src="https://github.com/flsrinn/WordTamagotchi/assets/123474937/03e10f1b-11a2-4758-8775-0462afbb72e5" width="500" height="300"><br>
<img src="https://github.com/flsrinn/WordTamagotchi/assets/123474937/8e71db29-636e-4a55-9bf2-4a9cca1aa3a7" width="500" height="300"><img src="https://github.com/flsrinn/WordTamagotchi/assets/123474937/942e04fa-1922-4a8d-b45b-cd21e3e3709f" width="500" height="300"><br>
0.35초 간격으로 단어가 떨어진다. 쉬움 단계일 경우 단어가 2.2초 간격으로 생성되고 어려움 단계일 경우 단어가 1.8초 간격으로 생성된다.<br>
스테이지 3에서는 생명 5개가 주어진다. <br>
400점에 도달할 경우 다음 스테이지로 넘어갈 수 있다.
#### 게임 클리어
<img src="https://github.com/flsrinn/WordTamagotchi/assets/123474937/0a593af2-e627-4d02-ad57-e451f21fd58c" width="500" height="300"><br>
스테이지 3에서 Next Stage 버튼을 누르면 게임 클리어 효과음과 함께 게임 클리어 화면이 출력된다. <br>
EXTRA STAGE 버튼을 누를 경우 추가 스테이지가 실행되고,<br> 
RESTART 버튼을 누르면 첫 시작 화면으로 돌아간다.<br>
EXIT 버튼을 누르면 프로그램을 종료한다.
#### 추가 스테이지
<img src="https://github.com/flsrinn/WordTamagotchi/assets/123474937/40c1021e-b706-471d-80b6-f459aefceae0" width="500" height="300"><br>
EXTRA STAGE 버튼을 누르면 추가 스테이지로 넘어간다.<br>
스테이지 3과 구성이 동일하고, 게임 방식 또한 동일하다.<br>
플레이어가 목숨을 다 잃을 때까지 실행된다.
#### 게임 오버
<img src="https://github.com/flsrinn/WordTamagotchi/assets/123474937/5ddb2d09-8e9e-4483-9c69-45f558fcb22c" width="500" height="300"><img src="https://github.com/flsrinn/WordTamagotchi/assets/123474937/6d0a84a0-e51d-465a-8103-b454d418f1b1" width="500" height="300"><br>
생명이 0이 되면 랭킹 등록 여부를 물어보는 메시지가 출력된다. 예를 누르면 랭킹 텍스트 파일에 기록된다. <br>
다이얼로그 버튼 중 하나를 누르면 게임오버 효과음과 함께 게임 오버 화면이 나온다.<br>
RESTART 버튼을 누르면 처음 시작화면으로 돌아가고, EXIT 버튼을 누르면 게임을 종료한다.
