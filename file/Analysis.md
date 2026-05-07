# 2. Analysis

# Masterkey
**-비밀번호 종합 관리 프로그램-**<br>
<img width="200" alt="Image" src="https://github.com/user-attachments/assets/0a105668-7733-49f0-b60c-5b4cf5484b1a" /> <br><br>
**22212073 차두선** 

**ilove14@yu.ac.kr**

<br>

---

## [ Revision history ]

| Revision date | Version # | Description | Author |
| :--- | :--- | :--- | :--- |
|05/03/2026 | 1.00| First Documentation| 차두선|

---

## = Contents =

1. [Introduction](#1-introduction)
2. [Use case analysis](#2-use-case-analysis)
3. [Domain analysis](#3-domain-analysis)
4. [User Interface prototype](#4-user-interface-prototype)
5. [Glossary](#5-glossary)
6. [References](#6-references)

---

## 1. Introduction

### 1) Summary
현대 사람들은 수많은 인터넷 사이트에 회원가입하고 개인 비밀번호를 만든다. 하지만 모든 비밀번호를 전부 기억하지는 못한다. 대부분 비슷한 비밀번호를 돌려쓰거나 권장 비밀번호 변경 주기가 한참 지난 후에도 비밀번호를 변경하지 않는다. 회원 가입된 사이트들 중 하나의 사이트만이라도 비밀번호가 유출된다면 도미노처럼 다른 사이트의 비밀번호까지 연속적으로 유출될 위험이 있다. 이를 해결하기 위해 가입된 모든 사이트의 비밀번호와 회원 정보를 암호화하여 통합으로 관리할 수 있는 시스템을 만들게 되었다.
### 2) Goals
비밀번호 통합 관리자 'Masterkey'의 가장 큰 목표는 사용자의 모든 비밀번호를 하나의 암호화된 저장소(DB)에 안전하게 저장하는 것이다. 또한 강력한 비밀번호를 자동으로 생성하고 보안 수준을 분석하여 사용자의 보안 수준을 향상해 줌에 그 목적이 있다. 또한 매번 사이트에 로그인할 때마다 어려운 무작위 비밀번호를 직접 입력하거나 복사 붙여넣기 기능을 사용하지 않고도 자동으로 브라우저에 비밀번호를 채우는 기능을 만드는 것이 목표이다.

---

## 2. Use case analysis

### 2.1. Use Case Diagram
<img width="700" alt="Image" src="https://github.com/user-attachments/assets/dac21086-93ea-4407-a08f-6bf904cbebc5" />

### 2.2. Use Case List
앞서 진행했던 'Conceptualization'의 Use case list 부분에서 UML 표준에 맞추어 Use case list를 정리하고 재작성했다.

| Use Case Name | Case ID | Korean Name | Actor |
| :--- | :--- | :--- | :--- |
|Create Database |#1 |데이터베이스 생성 | User |
|Generate Password |#2|무작위 비밀번호 생성 | User|
|Open Database | #3|데이터베이스 열기 | User|
|Mganae Entries in DB|#4|데이터베이스의 엔트리 관리|User|
|Database Backup|#5|데이터베이스 백업|User|
|Autofill Password|#6|비밀번호 자동 입력|User|
|Copy Password|#7|비밀번호 복사|User|
|Show Password History|#8|비밀변호 변경 이력 보기|User|
|Last Change Alert|#9|비밀번호 변경 주기 경고|User|
|Show Entries|#10|엔트리 보기|User|

### 2.3. Use Case Description

| **Use Case # 1** |**Create Database** |
| :--- | :--- |
| **GENERAL CHARACTERISTICS**|**Contents**|
| Summary|사용자의 비밀번호들을 저장할 수 있는 DB를 생성하고 DB에 마스터 비밀번호를 설정하기 위한 기능| 
|Scope|MasterKey System|
|Level|User level|
|Author|차두선|
|Last Update|2026.05.05|
|Status|Analysis|
|Primary Actor|User|
|Preconditions |프로그램을 설치 및 실행하고 DB를 저장할 공간이 있어야 한다.|
|Trigger|사용자가 사이트 비밀번호를 암호화하여 저장할 DB를 생성하기 위해 프로그램을 실행했을 때|
|Success Post Condition|사용자가 설정한 마스터 비밀번호를 입력하면 DB에 접근할 수 있는 DB파일이 만들어진다.|
|Failed Post Condition |사용자는 DB 생성에 실패하고 프로그램을 사용하지 못한다.|
|**MAIN SUCCESS SCENARIO**|**Contents**|
|Step|Action|
|1|사용자가 프로그램을 클릭하여 실행한다.|
|2|GUI의 데이터베이스 만들기를 선택한다.|
|3|데이터베이스의 마스터 비밀번호를 설정한다.|
|4|데이터베이스의 이름과 데이터베이스를 저장할 경로를 설정한다.|
|5|DB가 생성되면 이 기능은 종료된다.|
|**EXTENSION SCENARIOS**|**Contents**|
|Step |Branching Action|
|3|3a.사용자가 마스터 비밀번호를 설정할 때 4자리 미만으로 설정하고 다음버튼을 클릭한다.<br>&nbsp;&nbsp;&nbsp; 3a1. GUI창에 위험할 수 있다는 경고창을 띄운다.<br>&nbsp;&nbsp;&nbsp; 3a2. 사용자가 경고창의 확인 버튼을 클릭한다.<br>&nbsp;&nbsp;&nbsp; 3a3. 다음 스텝으로 이동한다. (Use case #1 - 4)|
|4|4a. 사용자가 잘못된 파일 저장 경로를 선택한다.<br>&nbsp;&nbsp;&nbsp; 4a1. 잘못된 경로 경고창을 띄운다.<br>&nbsp;&nbsp;&nbsp; 4a2. 다시 경로를 선택하는 단계로 돌아간다. (Use case #1 - 4)|
|**RELATED INFORMATION**|**Contents**|
|Performance|<2 seconds(DB를 생성하기로 결정한 파일 경로에 DB가 생성되기 까지의 처리) |
|Frequency |NONE|
|\<Concurrency\>|NONE|
|Due Date |2026.05.30|
<br><br>

| **Use Case # 2** |**Generate Password** |
| :--- | :--- |
| **GENERAL CHARACTERISTICS**|**Contents**|
| Summary|사용자가 마스터 비밀번호나 사이트 비밀번호를 설정할 때 무작위 비밀번호를 생성하기 위한 기능| 
|Scope|MasterKey System|
|Level|User level|
|Author|차두선|
|Last Update|2026.05.05|
|Status|Analysis|
|Primary Actor|User|
|Preconditions |(Use case #1 - 3)의 단계까지 성공적으로 진입하거나 데이터베이스를 열고 (Use case #4)단계까지 성공적으로 진입한 경우이어야 한다.|
|Trigger|비밀번호를 설정할 때 비밀번호 난수 생성 버튼을 클릭한 경우|
|Success Post Condition|사용자가 설정한 비밀번호 강도에 따라 비밀번호가 생성되어 비밀번호 입력창에 난수 비밀번호가 입력된다.|
|Failed Post Condition |사용자는 무작위로 생성된 난수 비밀번호를 사용할 수 없고 오직 사용자가 만든 비밀번호만 입력 가능해진다.|
|**MAIN SUCCESS SCENARIO**|**Contents**|
|Step|Action|
|1|비밀번호를 설정창까지 도달한다.|
|2|GUI의 무작위 비밀번호 만들기를 선택하고 창을 띄운다.|
|3|비밀번호 길이와 문자 제한을 설정하고 확인 버튼을 누른다.|
|4|무작위 비밀번호가 생성되어 입력칸에 채워지면 이 기능은 종료된다.|
|**EXTENSION SCENARIOS**|**Contents**|
|Step |Branching Action|
|3|3a. 비밀번호를 무작위 생성하지 않고 사용자가 설정하기로 마음을 바꿔 창을 닫는다.<br>&nbsp;&nbsp;&nbsp; 3a1. 다시 비밀번호 입력창으로 돌아간다. (Use case #2 - 1).
|**RELATED INFORMATION**|**Contents**|
|Performance|<10 seconds(무작위 비밀번호를 생성하는데 걸리는 처리시간) |
|Frequency |NONE|
|\<Concurrency\>|NONE|
|Due Date |2026.05.30|
<br><br>

| **Use Case # 3** |**Open Database** |
| :--- | :--- |
| **GENERAL CHARACTERISTICS**|**Contents**|
| Summary|암호화된 DB파일을 마스터 비밀번호(Key)를 통해 복호화하여 열기 위한 기능| 
|Scope|MasterKey System|
|Level|User level|
|Author|차두선|
|Last Update|2026.05.06|
|Status|Analysis|
|Primary Actor|User|
|Preconditions |데이터베이스(DB)를 이미 생성한 상태이어야 한다.|
|Trigger|생성된 DB파일을 클릭하여 파일을 여는 경우|
|Success Post Condition|사용자는 파일을 성공적으로 열고 엔트리를 관리할 수 있다.|
|Failed Post Condition |사용자는 파일을 열기에 실패하고 아무 기능도 사용할 수 없다.|
|**MAIN SUCCESS SCENARIO**|**Contents**|
|Step|Action|
|1|DB파일을 선택하여 파일을 연다.|
|2|GUI창을 통해 마스터 비밀번호를 입력하라는 창을 띄운다.|
|3|사용자는 앞서 설정한 마스터 비밀번호를 입력하고 확인 버튼을 클릭한다.|
|4|비밀번호가 일치하여 정상적으로 파일이 열리면 이 기능은 종료된다.|
|**EXTENSION SCENARIOS**|**Contents**|
|Step |Branching Action|
|3|3a. 마스터 비밀번호가 일치하지 않아 파일 열기에 실패한다.<br>&nbsp;&nbsp;&nbsp; 3a1. 다시 비밀번호 입력창으로 돌아간다. (Use case #3 - 2).
|**RELATED INFORMATION**|**Contents**|
|Performance|<10 seconds(암호화된 DB를 복호화 알고리즘을 통해 파일을 열기까지의 처리 시간) |
|Frequency |NONE|
|\<Concurrency\>|NONE|
|Due Date |2026.05.30|
<br><br>

| **Use Case # 4** |**Mganae Entries in DB** |
| :--- | :--- |
| **GENERAL CHARACTERISTICS**|**Contents**|
| Summary|DB안의 모든 사이트 비밀번호 목록(Entries)을 관리(등록, 수정, 삭제)하기 위해 사용하는 기능| 
|Scope|MasterKey System|
|Level|User level|
|Author|차두선|
|Last Update|2026.05.07|
|Status|Analysis|
|Primary Actor|User|
|Preconditions |정상적으로 데이터베이스(DB)를 연 상태여야 한다.|
|Trigger|DB내에서 Entries를 관리(추가, 수정, 삭제)하고 싶을 때|
|Success Post Condition|목록에 Entries를 추가하여 새로 만들거나, 삭제하여 목록에서 보이지 않게 된다.|
|Failed Post Condition |Entries를 수정하여도 수정된 사항이 반영되지 않는다.|
|**MAIN SUCCESS SCENARIO**|**Contents**|
|Step|Action|
|1|Entries목록에서 관리하고 싶은 Entry를 선택한다.|
|2|GUI창에서 (추가, 수정, 삭제)버튼을 클릭한다.|
|3|사용자는 Entry의 내용을 바꾸고 확인 버튼을 클릭한다.|
|4|Entry의 내용이 바뀌면 이 기능은 종료된다.|
|**EXTENSION SCENARIOS**|**Contents**|
|Step |Branching Action|
|1|1a. 처음 DB를 만들고 생성한 Entry가 없는데 수정, 삭제를 선택한다.<br>&nbsp;&nbsp;&nbsp; 1a1. Entries목록에서 선택된 엔트리가 없으므로 아무일도 일어나지 않는다.
|**RELATED INFORMATION**|**Contents**|
|Performance|<3 seconds(수정된 내용이 반영될 때까지의 처리) |
|Frequency |NONE|
|\<Concurrency\>|NONE|
|Due Date |2026.05.30|
<br><br>

| **Use Case # 5** |**Database Backup** |
| :--- | :--- |
| **GENERAL CHARACTERISTICS**|**Contents**|
| Summary|데이터베이스의 내용을 다른 경로에 백업하는 기능| 
|Scope|MasterKey System|
|Level|User level|
|Author|차두선|
|Last Update|2026.05.07|
|Status|Analysis|
|Primary Actor|User|
|Preconditions |DB가 생성되어 있어야 한다.|
|Trigger|GUI창에서 백업하기 버튼을 눌렀을 때|
|Success Post Condition|사용자가 지정한 경로에 성공적으로 데이터베이스가 백업된다.|
|Failed Post Condition |사용자가 지정한 경로에 데이터베이스가 백업되지 않는다.|
|**MAIN SUCCESS SCENARIO**|**Contents**|
|Step|Action|
|1|GUI창에서 백업하기 버튼을 클릭한다.|
|2|백업을 진행할 경로를 지정할 창을 띄운다.|
|3|사용자는 백업을 진행할 경로를 지정한다.|
|4|백업이 다 되면 이 기능은 종료된다.|
|**EXTENSION SCENARIOS**|**Contents**|
|Step |Branching Action|
|3|3a. 사용자가 잘못된 백업 경로를 선택한다.<br>&nbsp;&nbsp;&nbsp; 3a1. 백업 경로 에러 경고창을 띄운다.<br>&nbsp;&nbsp;&nbsp; 3a2. 다시 백업 경로를 지정할 창으로 돌아간다. (Use case #5 - 2)|
|**RELATED INFORMATION**|**Contents**|
|Performance|<3 seconds(데이터베이스가 백업되기까지의 처리 시간) |
|Frequency |NONE|
|\<Concurrency\>|NONE|
|Due Date |2026.05.30|
<br><br>

| **Use Case # 6** |**Autofill Password** |
| :--- | :--- |
| **GENERAL CHARACTERISTICS**|**Contents**|
| Summary|DB에 저장된 보안 강도가 높은 비밀번호를 브라우저에 자동으로 채워주는 기능| 
|Scope|MasterKey System|
|Level|User level|
|Author|차두선|
|Last Update|2026.05.07|
|Status|Analysis|
|Primary Actor|User|
|Preconditions |DB가 정상적으로 열려 있어야 한다.|
|Trigger|브라우저에 비밀번호를 자동으로 채워넣기 위해서 GUI창에서 자동 채우기 버튼을 클릭할 때|
|Success Post Condition|브라우저에서 선택한 입력창에 비밀번호가 자동으로 채워 넣어진다.|
|Failed Post Condition |브라우저에서 선택한 입력창에 비밀번호가 채워 넣어지지 않는다.|
|**MAIN SUCCESS SCENARIO**|**Contents**|
|Step|Action|
|1|브라우저의 비밀번호 입력창을 클릭하여 포커스를 준다.|
|2|Entries목록에서 브라우저에 자동으로 채울 비밀번호를 가진 Entries를 선택한다.|
|3|자동 채우기 버튼을 클릭한다.|
|4|브라우저 비밀번호 입력창에 비밀번호가 자동으로 입력된다.|
|5|자동 입력이 끝나면 이 기능은 종료된다.|
|**EXTENSION SCENARIOS**|**Contents**|
|Step |Branching Action|
|2|2a. 사용자가 Entries를 선택하지 않거나 생성된 Entries가 없다.<br>&nbsp;&nbsp;&nbsp; 2a1. Entries목록에서 선택된 엔트리가 없으므로 아무일도 일어나지 않는다.|
|**RELATED INFORMATION**|**Contents**|
|Performance|<2 seconds(비밀번호가 브라우저에 입력되는 시간) |
|Frequency |NONE|
|\<Concurrency\>|NONE|
|Due Date |2026.05.30|
<br><br>

| **Use Case # 7** |**Copy Password** |
| :--- | :--- |
| **GENERAL CHARACTERISTICS**|**Contents**|
| Summary|DB에 저장된 보안 강도가 높은 비밀번호를 클립보드에 복사하는 기능| 
|Scope|MasterKey System|
|Level|User level|
|Author|차두선|
|Last Update|2026.05.07|
|Status|Analysis|
|Primary Actor|User|
|Preconditions |DB가 정상적으로 열려 있어야 한다.|
|Trigger|클립보드에 비밀번호를 복사하기 위해 클립보드에 복사하기 버튼을 클릭할 때|
|Success Post Condition|클립보드에 비밀번호가 복사된다.|
|Failed Post Condition |클립보드에 비밀번호가 복사되지 않는다.|
|**MAIN SUCCESS SCENARIO**|**Contents**|
|Step|Action|
|1|Entries목록에서 복사할 비밀번호를 가진 Entries를 선택한다.|
|2|클립보드에 복사 버튼을 클릭한다.|
|3|클립보드에 비밀번호를 복사해준다.|
|4|복사가 끝나면 이 기능은 종료된다.|
|**EXTENSION SCENARIOS**|**Contents**|
|Step |Branching Action|
|1|1a. 사용자가 Entries를 선택하지 않거나 생성된 Entries가 없다.<br>&nbsp;&nbsp;&nbsp; 1a1. Entries목록에서 선택된 엔트리가 없으므로 아무일도 일어나지 않는다.|
|**RELATED INFORMATION**|**Contents**|
|Performance|<1 seconds(비밀번호가 클립보드에 복사되는 시간) |
|Frequency |NONE|
|\<Concurrency\>|NONE|
|Due Date |2026.05.30|
<br><br>

| **Use Case # 8** |**Show Password History** |
| :--- | :--- |
| **GENERAL CHARACTERISTICS**|**Contents**|
| Summary|DB에 저장된 보안 강도가 높은 비밀번호를 클립보드에 복사하는 기능| 
|Scope|MasterKey System|
|Level|User level|
|Author|차두선|
|Last Update|2026.05.07|
|Status|Analysis|
|Primary Actor|User|
|Preconditions |DB가 정상적으로 열려 있고 Entries 목록 창이 띄워져 있어야 한다.|
|Trigger|클립보드에 비밀번호를 복사하기 위해 클립보드에 복사하기 버튼을 클릭할 때|
|Success Post Condition|클립보드에 비밀번호가 복사된다.|
|Failed Post Condition |클립보드에 비밀번호가 복사되지 않는다.|
|**MAIN SUCCESS SCENARIO**|**Contents**|
|Step|Action|
|1|Entries목록에서 복사할 비밀번호를 가진 Entries를 선택한다.|
|2|클립보드에 복사 버튼을 클릭한다.|
|3|클립보드에 비밀번호를 복사해준다.|
|4|복사가 끝나면 이 기능은 종료된다.|
|**EXTENSION SCENARIOS**|**Contents**|
|Step |Branching Action|
|1|1a. 사용자가 Entries를 선택하지 않거나 생성된 Entries가 없다.<br>&nbsp;&nbsp;&nbsp; 1a1. Entries목록에서 선택된 엔트리가 없으므로 아무일도 일어나지 않는다.|
|**RELATED INFORMATION**|**Contents**|
|Performance|<1 seconds(비밀번호가 클립보드에 복사되는 시간) |
|Frequency |NONE|
|\<Concurrency\>|NONE|
|Due Date |2026.05.30|

---

## 3. Domain analysis

### 3.1. Domain Concept Diagram

### 3.2. Conceptual Class Extraction

| Class | Description | Attributes |
| :--- | :--- | :--- |
| | | |

---

## 4. User Interface prototype

### 4.1. Screen Layout
(이미지 및 설명 입력)

---

## 5. Glossary

| Terms | Description |
| :--- | :--- |
|DB(데이터베이스) |MasterKey 시스템에서 사용자가 비밀번호를 암호화 하여 저장할 수 있는 저장공간이다.|
|Entries| MasterKey 시스템에서 DB를 생성하고 DB 열면 회원가입된 사이트의 아이디와 비밀번호를 리스트 목록에 저장할 수 있다. 이때 저장된 리스트의 목록 하나하나를 엔트리라고 한다.|
|암호화|DB를 PC에 저장해 놓았을 때 다른 사용자가 DB를 열어볼 수 없도록, 혹은 열어봐도 깨진 파일이 나오도록 한다.|
|복호화|암호화된 DB파일을 사용자가 마스터 비밀번호(Key)를 이용해서 원본 파일로 되돌리는 것이다.|

---

## 6. References
- [starUML](https://staruml.io/)
- [UML specification](https://www.omg.org/spec/UML/2.5.1/About-UML)
