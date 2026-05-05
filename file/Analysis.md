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
(`유스케이스`)

### 2.2. Use Case List
앞서 진행했던 'Conceptualization'의 Use case list 부분에서 UML 표준에 맞추어 Use case list를 정리하고 재작성했다.

| Use Case Name | Case ID | Korean Name | Actor |
| :--- | :--- | :--- | :--- |
|Create Database |#1 |데이터베이스 생성 | User |
|Generate Password |#2|무작위 비밀번호 생성 | User|
|Open Database | #3|데이터베이스 열기 | User|
|Manage Entries in DB|#4|데이터베이스안의 엔트리 관리|User|
|Database Backup|#5|데이터베이스 백업|User|
|Autofill Password|#6|비밀번호 자동 입력|User|
|Copy Password|#7|비밀번호 복사|User|
|Show Password History|#8|비밀변호 변경 이력 보기|User|
|Last Change Alert|#9|비밀번호 변경 주기 경고|User|

### 2.3. Use Case Description

| Use Case # 1 :|유스케이스 이름 |
| :--- | :--- |
| **GENERAL CHARACTERISTICS**|**Contents**|
| Summary|| 
|Scope|MasterKey System|
|Level|User level|
|Author|차두선|
|Last Update|2026.05.03|
|Status|Analysis|
|Primary Actor|User|
|Preconditions ||
|Trigger||
|Success Post Condition||
|Failed Post Condition ||
|**MAIN SUCCESS SCENARIO**|**Contents**|
|Step|Action|
|1||
|2||
|**EXTENSION SCENARIOS**|**Contents**|
|Step |Branching Action|
|**RELATED INFORMATION**|**Contents**|
|Performance||
|Frequency ||
|\<Concurrency\>||
|Due Date ||



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
|DB(데이터베이스) |MasterKey 시스템에서 사용자가 비밀번호를 암호화 하여 저장할 수 있는 저장공간이다. |
|Entries| MasterKey 시스템에서 DB를 생성하고 DB 열면 회원가입된 사이트의 아이디와 비밀번호를 리스트 목록에 저장할 수 있다. 이때 저장된 리스트의 목록 하나하나를 엔트리라고 한다.|

---

## 6. References
* Reference 1: [starUML](https://staruml.io/)
