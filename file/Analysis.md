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
<img width="700" alt="Image" src="https://github.com/user-attachments/assets/fbacf458-8b69-4bad-b633-81f9984b3174" />

### 2.2. Use Case List
앞서 진행했던 'Conceptualization'의 Use case list 부분에서 UML 표준에 맞추어 Use case list를 정리하고 재작성했다.

| Use Case Name | Case ID | Korean Name | Actor |
| :--- | :--- | :--- | :--- |
|Create Database |#1 |데이터베이스 생성 | User |
|Generate Password |#2|무작위 비밀번호 생성 | User|
|Open Database | #3|데이터베이스 열기 | User|
|Add Entries in DB|#4|데이터베이스의 엔트리 추가|User|
|Delete Entries in DB|#5|데이터베이스의 엔트리 삭제|User|
|Database Backup|#6|데이터베이스 백업|User|
|Autofill Password|#7|비밀번호 자동 입력|User|
|Copy Password|#8|비밀번호 복사|User|
|Show Password History|#9|비밀변호 변경 이력 보기|User|
|Last Change Alert|#10|비밀번호 변경 주기 경고|User|
|Show Entries|#11|Entries 보기|User|

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
|2|사용자가 데이터베이스 만들기를 선택한다.|
|3|시스템이 데이터베이스의 마스터 비밀번호 설정창을 보여준다.|
|4|사용자가 마스터 비밀번호를 설정한다.|
|5|시스템이 데이터베이스의 이름과 데이터베이스를 저장할 경로를 선택할 수 있는 창을 보여준다.|
|7|사용자가 데이터베이스 이름과 경로를 선택한다.|
|6|DB가 생성되면 이 기능은 종료된다.|
|**EXTENSION SCENARIOS**|**Contents**|
|Step |Branching Action|
|4|4a.사용자가 마스터 비밀번호를 설정할 때 4자리 미만으로 설정하고 다음버튼을 클릭한다.<br>&nbsp;&nbsp;&nbsp; 4a1. GUI창에 위험할 수 있다는 경고창을 띄운다.<br>&nbsp;&nbsp;&nbsp; 4a2. 사용자가 경고창의 확인 버튼을 클릭한다.<br>&nbsp;&nbsp;&nbsp; 4a3. 다음 스텝으로 이동한다. (Use case #1 - 5)|
|5|5a. 사용자가 잘못된 파일 저장 경로를 선택한다.<br>&nbsp;&nbsp;&nbsp; 5a1. 잘못된 경로 경고창을 띄운다.<br>&nbsp;&nbsp;&nbsp; 5a2. 다시 경로를 선택하는 단계로 돌아간다. (Use case #1 - 5)|
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
|Preconditions |(Use case #1 - 3)의 단계까지 성공적으로 진입하거나 데이터베이스를 열고 (Use case #4 - 3 )단계까지 성공적으로 진입한 경우이어야 한다.
|Trigger|비밀번호를 설정할 때 비밀번호 난수 생성 버튼을 클릭한 경우|
|Success Post Condition|사용자가 설정한 비밀번호 강도에 따라 비밀번호가 생성되어 비밀번호 입력창에 난수 비밀번호가 입력된다.|
|Failed Post Condition |사용자는 무작위로 생성된 난수 비밀번호를 사용할 수 없고 오직 사용자가 만든 비밀번호만 입력 가능해진다.|
|**MAIN SUCCESS SCENARIO**|**Contents**|
|Step|Action|
|1|사용자가 비밀번호를 설정창까지 도달한다.|
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
|4|시스템은 비밀번호가 일치하는지 검사한다.|
|5|비밀번호가 일치하면 파일을 복호화하여 Entries 목록을 보여주고 이 기능은 종료한다.|
|**EXTENSION SCENARIOS**|**Contents**|
|Step |Branching Action|
|4|4a. 마스터 비밀번호가 일치하지 않아 파일 열기에 실패한다.<br>&nbsp;&nbsp;&nbsp; 4a1. 다시 비밀번호 입력창으로 돌아간다. (Use case #3 - 2).
|**RELATED INFORMATION**|**Contents**|
|Performance|<10 seconds(암호화된 DB를 복호화 알고리즘을 통해 파일을 열기까지의 처리 시간) |
|Frequency |NONE|
|\<Concurrency\>|NONE|
|Due Date |2026.05.30|
<br><br>

| **Use Case # 4** |**Add Entries in DB** |
| :--- | :--- |
| **GENERAL CHARACTERISTICS**|**Contents**|
| Summary|사이트의 비밀번호 정보 등을 저장하고 있는 Entries를 추가하는 기능| 
|Scope|MasterKey System|
|Level|User level|
|Author|차두선|
|Last Update|2026.05.06|
|Status|Analysis|
|Primary Actor|User|
|Preconditions |정상적으로 데이터베이스(DB)를 연 상태여야 한다.|
|Trigger|DB내에서 Entries를 추가하기 위해 추가 버튼을 클릭할 때|
|Success Post Condition|Entries리스트에 Entries를 추가한다.|
|Failed Post Condition |Entries리스트에 Entries가 추가되지 않는다.|
|**MAIN SUCCESS SCENARIO**|**Contents**|
|Step|Action|
|1|사용자는 Entries 목록 GUI창에서 Entries 추가 버튼을 클릭한다.|
|2|시스템은 추가할 Entries의 정보를 입력할 수 있는 창을 띄워준다.|
|3|사용자는 Entries의 이름, 아이디, 비밀번호를 작성하고 확인버튼을 누른다.|
|4|시스템은 Entries의 내용을 암호화하여 데이터베이스를 최신화한다.|
|5|추가된 Entries를 반영하는 Entries 목록 화면으로 돌아가면 이 기능은 종료된다.|
|**EXTENSION SCENARIOS**|**Contents**|
|Step |Branching Action|
|4|4a. 시스템이 수정된 데이터베이스 암호화에 실패한다.<br>&nbsp;&nbsp;&nbsp; 4a1. 데이터베이스 암호화 실패 경고창을 보여준다.<br>&nbsp;&nbsp;&nbsp; 4a2. 다시 Entries의 정보를 입력할 수 있는 창으로 돌아간다. (Use case #4 - 2)
|**RELATED INFORMATION**|**Contents**|
|Performance|<10 seconds(수정된 내용을 암호화하여 반영될 때까지의 처리) |
|Frequency |NONE|
|\<Concurrency\>|NONE|
|Due Date |2026.05.30|
<br><br>

| **Use Case # 5** |**Delete Entries in DB** |
| :--- | :--- |
| **GENERAL CHARACTERISTICS**|**Contents**|
| Summary|사이트의 비밀번호 정보 등을 저장하고 있는 Entries를 삭제하는 기능| 
|Scope|MasterKey System|
|Level|User level|
|Author|차두선|
|Last Update|2026.05.06|
|Status|Analysis|
|Primary Actor|User|
|Preconditions |정상적으로 데이터베이스(DB)를 연 상태이고 이미 추가된 Entries가 존재해야 한다.|
|Trigger|DB내에서 Entries를 삭제하기 위해 삭제 버튼을 클릭할 때|
|Success Post Condition|Entries리스트에서 Entries를 삭제한다.|
|Failed Post Condition |Entries리스트에서 Entries가 삭제되지 않고 그대로 남아있다.|
|**MAIN SUCCESS SCENARIO**|**Contents**|
|Step|Action|
|1|사용자는 Entries를 삭제하기 위해 Entries 목록에서 삭제할 Entries를 선택한다.|
|2|사용자가 삭제 버튼을 클릭한다.|
|3|시스템은 선택된 Entries를 데이터베이스에서 삭제하고 암호화하여 저장한다.|
|4|삭제된 Entries를 반영하는 Entries 목록 화면으로 돌아가면 이 기능은 종료된다.|
|**EXTENSION SCENARIOS**|**Contents**|
|Step |Branching Action|
|3|3a. 시스템이 수정된 데이터베이스 암호화에 실패한다.<br>&nbsp;&nbsp;&nbsp; 3a1. 데이터베이스 암호화 실패 경고창을 보여준다.<br>&nbsp;&nbsp;&nbsp; 3a2. Entries를 선택할 수 있는 Entries목록 화면으로 돌아간다. (Use case #5 - 1)
|**RELATED INFORMATION**|**Contents**|
|Performance|<10 seconds(수정된 내용을 암호화하여 반영될 때까지의 처리) |
|Frequency |NONE|
|\<Concurrency\>|NONE|
|Due Date |2026.05.30|
<br><br>

| **Use Case # 6** |**Database Backup** |
| :--- | :--- |
| **GENERAL CHARACTERISTICS**|**Contents**|
| Summary|데이터베이스의 내용을 다른 경로에 백업하는 기능| 
|Scope|MasterKey System|
|Level|User level|
|Author|차두선|
|Last Update|2026.05.06|
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
|3|3a. 사용자가 잘못된 백업 경로를 선택한다.<br>&nbsp;&nbsp;&nbsp; 3a1. 백업 경로 에러 경고창을 띄운다.<br>&nbsp;&nbsp;&nbsp; 3a2. 다시 백업 경로를 지정할 창으로 돌아간다. (Use case #6 - 2)|
|**RELATED INFORMATION**|**Contents**|
|Performance|<3 seconds(데이터베이스가 백업되기까지의 처리 시간) |
|Frequency |NONE|
|\<Concurrency\>|NONE|
|Due Date |2026.05.30|
<br><br>

| **Use Case # 7** |**Autofill Password** |
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

| **Use Case # 8** |**Copy Password** |
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

| **Use Case # 9** |**Show Password History** |
| :--- | :--- |
| **GENERAL CHARACTERISTICS**|**Contents**|
| Summary|Entries에 대해 비밀번호를 마지막으로 바꾼 날짜와 비밀번호 변경 기록을 보여주는 기능| 
|Scope|MasterKey System|
|Level|User level|
|Author|차두선|
|Last Update|2026.05.07|
|Status|Analysis|
|Primary Actor|User|
|Preconditions |DB가 정상적으로 열려 있고 Entries 목록 창이 띄워져 있어야 한다.|
|Trigger|각 Entries의 비밀번호 변경이력을 자세히 보기 위해 변경이력 버튼을 클릭할 때|
|Success Post Condition|Entries 목록창에 비밀번호 변경이력과 마지막으로 변경한 날짜를 볼 수 있다.|
|Failed Post Condition |Entries 목록창에 비밀번호 변경이력과 마지막으로 변경한 날짜를 볼 수 없다.|
|**MAIN SUCCESS SCENARIO**|**Contents**|
|Step|Action|
|1|Entries목록에서 비밀번호 변경이력이 보고 싶은 Entries를 선택한다.|
|2|변경이력 보기 버튼을 클릭한다.|
|3|Entries에 대해 비밀번호 변경이력과 마지막으로 변경한 날짜를 보여주는 창을 띄워준다.|
|4|사용자가 창을 닫으면 이 기능은 종료된다.|
|**EXTENSION SCENARIOS**|**Contents**|
|Step |Branching Action|
|1|1a. 사용자가 Entries를 선택하지 않거나 생성된 Entries가 없다.<br>&nbsp;&nbsp;&nbsp; 1a1. Entries목록에서 선택된 엔트리가 없으므로 아무일도 일어나지 않는다.|
|**RELATED INFORMATION**|**Contents**|
|Performance|< 2 seconds(History를 화면창에 출력해주는 처리 시간)|
|Frequency |NONE|
|\<Concurrency\>|NONE|
|Due Date |2026.05.30|
<br><br>

| **Use Case # 10** |**Last Change Alert** |
| :--- | :--- |
| **GENERAL CHARACTERISTICS**|**Contents**|
| Summary|비밀번호 변경 주기를 설정하고 변경 주기가 지난 Entries에 대해 사용자에게 시각적으로 알려주는 알람 기능| 
|Scope|MasterKey System|
|Level|User level|
|Author|차두선|
|Last Update|2026.05.07|
|Status|Analysis|
|Primary Actor|User|
|Preconditions |DB가 정상적으로 열려 있고 Entries 목록 창이 띄워져 있어야 한다.|
|Trigger|설정한 비밀번호 변경 주기가 지난 Entries를 보고 싶을 때|
|Success Post Condition|Entries 목록창에 비밀번호 변경주기가 지난 Entries를 붉은색으로 표시해 보여준다.|
|Failed Post Condition |Entries 목록창이 이 기능을 실행하기 전과 변함이 없다.|
|**MAIN SUCCESS SCENARIO**|**Contents**|
|Step|Action|
|1|Entries목록 GUI창에서 비밀번호 변경 주기 설정 버튼을 클릭한다.|
|2|시스템이 비밀번호 변경 주기 설정을 할 수 있는 창으로 이동한다.|
|3|사용자가 비밀번호 변경 주기 설정을 하고 저장한다.|
|4|사용자가 기능을 활성화 하기 위해서 알람 아이콘을 클릭한다.|
|5|사용자가 설정한 변경 주기를 넘어간 Entries에 대해 붉은색으로 표시하고 기능은 종료된다.|
|**EXTENSION SCENARIOS**|**Contents**|
|Step |Branching Action|
|4|4a. 변경 주기를 설정한 후 이 기능을 해제하기 위해 아이콘을 다시 클릭한다.<br>&nbsp;&nbsp;&nbsp; 4a1. 기능이 해제되었음을 사용자에게 알리기 위해 아이콘의 색을 흑백으로 바꾼다.<br>&nbsp;&nbsp;&nbsp; 4a2. 알람 기능이 꺼진 Entries 목록 화면으로 돌아간다. (Use case #11 - 2)<br><br>4b. 알람 기능을 해제하고 다시 이 기능을 사용하기 위해 아이콘을 다시 클릭한다.<br>&nbsp;&nbsp;&nbsp; 4b1. 기능이 실행 되었음을 알리기 위해 아이콘의 색을 노란색으로 바꾼다.<br>&nbsp;&nbsp;&nbsp; 4b2. 알람이 적용된 화면을 보여준다. (Use case #10 - 5)<br>&nbsp;&nbsp;&nbsp;<br>4c. 사용자가 주기를 설정하지 않고 바로 아이콘 버튼을 클릭한다.<br>&nbsp;&nbsp;&nbsp; 4c1. 시스템이 주기를 30일을 기본값으로 설정한다.<br>&nbsp;&nbsp;&nbsp; 4c2. 사용자가 최초로 주기를 설정하지 않아도 기본값인 30일을 기준으로 기능을 활성화 해준다.
|**RELATED INFORMATION**|**Contents**|
|Performance|NONE|
|Frequency |NONE|
|\<Concurrency\>|NONE|
|Due Date |2026.05.30|
<br><br>

| **Use Case # 11** |**Show Entries** |
| :--- | :--- |
| **GENERAL CHARACTERISTICS**|**Contents**|
| Summary|사용자가 추가한 Entries 목록을 사용자에게 보여주는 기능| 
|Scope|MasterKey System|
|Level|User level|
|Author|차두선|
|Last Update|2026.05.07|
|Status|Analysis|
|Primary Actor|User|
|Preconditions |DB가 성공적으로 열려있어야 한다.|
|Trigger|관리할 Entries를 보기 위해 목록 보기를 선택했을 때|
|Success Post Condition|사용자가 추가한 Entries의 목록을 보여준다.|
|Failed Post Condition |사용자가 Entries를 볼 수 없어 추가적인 기능을 사용하지 못한다.|
|**MAIN SUCCESS SCENARIO**|**Contents**|
|Step|Action|
|1|사용자가 GUI창에서 목록 보기 버튼을 클릭한다.|
|2|시스템은 Entries 목록을 화면에 보여준다.|
|3|시스템이 성공적으로 목록을 보여주면 이 기능은 종료된다.|
|**EXTENSION SCENARIOS**|**Contents**|
|Step |Branching Action|
|1|1a. 사용자가 Entries를 추가하지 않고 바로 목록 보기 버튼을 클릭한다.<br>&nbsp;&nbsp;&nbsp; 1a1. 추가한 Entries가 없으므로 빈 목록의 리스트를 화면에 보여준다. (Use case #11 - 2)|
|**RELATED INFORMATION**|**Contents**|
|Performance|NONE|
|Frequency |NONE|
|\<Concurrency\>|NONE|
|Due Date |2026.05.30|

---

## 3. Domain analysis

### 1) Database
 Database 클래스는 시스템의 최상위 객체로서 사용자의 모든 비밀번호 데이터를 관리하는 역할을 수행한다. 데이터베이스의 이름, 저장 경로, 생성 날짜와 같은 메타데이터를 저장하며, 여러 개의 Entry 객체를 포함하여 사용자의 계정 정보를 통합적으로 관리한다. 또한 데이터베이스 접근을 위한 MasterKey 객체와 연결되며, 사용자 환경설정을 저장하는 UserSettings, 데이터베이스 백업 정보를 저장하는 Backup 객체들을 관리한다.

### 2) MasterKey
 MasterKey 클래스는 데이터베이스 접근을 위한 마스터 비밀번호를 관리하는 역할을 수행한다. 사용자가 입력한 마스터 비밀번호를 기반으로 데이터베이스 접근 권한을 검증하며, 데이터베이스 암호화 및 복호화 과정의 기준 키로 사용된다. 보안을 위해 실제 비밀번호 원문 대신 해시 형태로 사용할 예정이다.

### 3) Entry
 Entry 클래스는 하나의 사이트 계정 정보를 표현하는 객체이다. 사이트 이름, 사용자 아이디, 비밀번호 등의 로그인 정보를 저장하며, 각 계정의 생성 날짜 및 수정 날짜를 관리한다. 또한 현재 사용 중인 Password 객체와 연결되며, 비밀번호 변경 기록을 저장하는 PasswordHistory 객체들을 포함한다. 비밀번호 변경 주기 알림 기능을 위해 Alert 객체와도 연관된다.

### 4) Password
 Password 클래스는 사용자의 비밀번호 정보를 독립적으로 관리하기 위한 객체이다. 단순 문자열 형태의 비밀번호뿐 아니라 비밀번호 강도와 생성 시점 등의 보안 관련 정보를 함께 저장한다. 또한 비밀번호 생성 규칙을 정의하는 PasswordPolicy 객체와 연결되어 보안 강도가 높은 비밀번호를 생성할 수 있도록 지원한다.

### 5) PasswordPolicy
 PasswordPolicy 클래스는 비밀번호 생성 규칙을 정의하는 역할을 수행한다. 비밀번호 길이, 숫자 사용 여부, 특수문자 사용 여부, 대문자 사용 여부 등의 조건을 저장하며, 무작위 비밀번호 생성 기능에서 보안 수준에 맞는 비밀번호를 생성할 수 있도록 기준을 제공한다.

### 6) PasswordHistory
 PasswordHistory 클래스는 사용자가 이전에 사용했던 비밀번호와 변경 날짜를 저장하는 역할을 수행한다. 각 Entry 객체에 대한 비밀번호 변경 기록을 관리하며, 사용자가 비밀번호 변경 이력을 조회할 수 있도록 지원한다. 또한 비밀번호 변경 주기 확인 기능에서 마지막 변경 시점을 제공한다.

### 7) Backup
 Backup 클래스는 데이터베이스 백업 정보를 저장하기 위한 객체이다. 백업 파일의 저장 경로와 백업 날짜 등의 정보를 관리하며, 사용자가 데이터베이스를 안전하게 복구할 수 있도록 지원한다.

### 8) UserSettings
 UserSettings 클래스는 사용자 환경설정 정보를 관리하는 역할을 수행한다. 비밀번호 변경 주기, 알람 활성화 여부 등의 사용자 설정값을 저장하며, 사용자의 보안 설정을 유지하고 관리하는 기능을 제공한다.

### 9) Alert
 Alert 클래스는 비밀번호 변경 주기 초과 여부를 관리하기 위한 객체이다. 설정된 기간 이상 비밀번호가 변경되지 않은 Entry에 대해 경고 상태를 저장하며, 사용자에게 시각적인 알림을 제공하기 위한 정보를 관리한다.

---

## 4. User Interface prototype

### 4.1. Screen Layout
(이미지 및 설명 입력)

---

## 5. Glossary

| Terms | Description |
| :--- | :--- |
|DB(데이터베이스) |MasterKey 시스템에서 사용자가 비밀번호를 암호화 하여 저장할 수 있는 저장공간이다.|
|Entries| MasterKey 시스템에서 회원가입된 사이트의 아이디와 비밀번호를 데이터베이스에 저장할 수 있다. 이때 저장된 (아이디, 비밀번호)쌍 하나하나를 Entries라고 한다. 즉, (아이디, 비밀번호)쌍을 리스트에 저장하고 리스트에 저장된 목록 하나하나가 Entries가 된다|
|암호화|DB를 PC에 저장해 놓았을 때 다른 사용자가 DB를 열어볼 수 없도록, 혹은 열어봐도 깨진 파일이 나오도록 한다.|
|복호화|암호화된 DB파일을 사용자가 마스터 비밀번호(Key)를 이용해서 원본 파일로 되돌리는 것이다.|

---

## 6. References
- [starUML](https://staruml.io/)
- [UML specification](https://www.omg.org/spec/UML/2.5.1/About-UML)
