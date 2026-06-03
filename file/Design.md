# 3. Design

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
|06/03/2026 | 1.00| First Documentation| 차두선|

---

## =Contents=

1. [Introduction](#1-introduction)
2. [Class Diagram](#2-class-diagram)
3. [Sequence Diagram](#3-sequence-diagram)
4. [State Machine Diagram](#4-state-machine-diagram)
5. [Implementation Requirements](#5-implementation-requirements)
6. [Glossary](#6-glossary)
7. [References](#7-references)

---

## 1. Introduction

### 1.1 Summary
 본 문서는 비밀번호 통합 관리 시스템인 MasterKey의 설계(Design) 내용을 기술한다. 본 설계 문서는 Conceptualization 단계에서 정의한 시스템 목표와 Analysis 단계에서 도출한 요구사항, Use Case, Domain Model을 기반으로 작성되었다. 본 문서에서는 시스템을 실제로 구현하기 위한 소프트웨어 구조와 설계 방안을 제시하며, Class Diagram, Sequence Diagram, State Machine Diagram을 통해서 주요 클래스들의 관계, 객체 간 상호작용 과정, 상태 변화, 그리고 구현 요구사항을 설명할 예정이다.
 
 또한 데이터베이스 생성 및 관리, 비밀번호 생성, 암호화 및 복호화, 엔트리 관리, 데이터베이스 백업, 비밀번호 변경 이력 관리, 자동 입력 기능 등 주요 기능들이 어떤 구조로 설계되는지와 어떻게 동작하는 지를 다룬다. 이를 통해 구현 단계에서 일관성 있고 유지보수가 용이한 시스템을 개발할 수 있도록 하는 것을 목표로 한다.
 
### 1.2 Important Design Points
 MasterKey 시스템의 가장 중요한 설계 목표는 보안성(Security) 과 사용 편의성(Usability) 을 동시에 제공하는 것이다.

 첫째, 모든 계정 정보는 하나의 암호화된 데이터베이스 파일에 저장되며, 사용자가 설정한 마스터 비밀번호를 기반으로 데이터베이스 접근 권한을 관리한다. 데이터베이스 파일은 암호화된 형태로 저장되어 외부 사용자가 파일을 획득하더라도 내용을 확인할 수 없도록 설계하였다.

 둘째, 시스템의 유지보수성과 확장성을 높이기 위해 핵심 객체들을 중심으로 객체지향 구조를 설계하였다. Database, Entry, Password, PasswordHistory, Backup, UserSettings, Alert 등의 클래스는 실제 비밀번호 관리 시스템에서 사용되는 데이터와 기능을 담당하며, Class Diagram을 통해 이들 객체 간의 관계와 책임을 표현한다. 반면, 데이터베이스 암호화 및 복호화 처리와 사용자 인터페이스 제어 기능은 구현 단계에서 별도의 모듈 및 GUI 컴포넌트를 통해 수행될 예정이며, 본 설계 문서의 Class Diagram에서는 UI부분과 암호화, 복호화 클래스는 다루지 않는다.

 셋째, 사용자의 편의성을 향상시키기 위해 무작위 비밀번호 생성, 비밀번호 자동 입력, 클립보드 복사, 비밀번호 변경 이력 조회, 비밀번호 변경 주기 알림 등의 기능을 제공한다. 이를 통해 사용자는 높은 보안 수준을 유지하면서도 복잡한 비밀번호를 직접 관리해야 하는 부담을 줄일 수 있다.

---

## 2. Class Diagram
본 Class Diagram에는 UI 클래스, 암호화 및 복호화 클래스, 데이터베이스 관리를 위한 알고리즘 클래스 등등의 JAVA 표준 라이브러리 및 외부 라이브러리에서 사용할 클래스는 포함하지 않았다.
<img width="1563" height="940" alt="Image" src="https://github.com/user-attachments/assets/5e61b824-f14b-48ac-ac23-726f396081c3" />
<br><br>

### 1. Database

| **Attribute** | **Description** |
|-|-|
| databaseName: String | 데이터베이스의 이름을 저장한다. |
| filePath: String | 데이터베이스 파일이 저장된 경로를 저장한다. |
| createdAt: LocalDateTime | 데이터베이스가 최초로 생성된 날짜와 시간을 저장한다. |
| modifiedAt: LocalDateTime | 데이터베이스가 마지막으로 수정된 날짜와 시간을 저장한다. |
| opened: boolean | 데이터베이스가 현재 열려 있는지 여부를 나타낸다. |

| **Operation** | **Description** |
|-|-|
| createDatabase(databaseName: String, filePath: String, masterPassword: String): Database | 새로운 데이터베이스를 생성하고 데이터베이스 이름, 저장 경로, 마스터 비밀번호를 초기화한다. |
| openDatabase(inputPassword: String): boolean | 사용자가 입력한 마스터 비밀번호를 검증하여 데이터베이스를 열 수 있는지 판단한다. |
| closeDatabase(): void | 현재 열려 있는 데이터베이스를 닫고 opened 상태를 false로 변경한다. |
| addEntry(entry: Entry): void | 데이터베이스에 새로운 Entry를 추가한다. |
| updateEntry(entry: Entry): boolean | 기존 Entry의 정보를 수정하고 성공 여부를 반환한다. |
| deleteEntry(entryId: String): boolean | 전달받은 Entry ID에 해당하는 Entry를 삭제한다. |
| findEntry(entryId: String): Entry | 전달받은 Entry ID에 해당하는 Entry를 검색하여 반환한다. |
| getEntries(): List<Entry> | 데이터베이스에 저장된 전체 Entry 목록을 반환한다. |
| saveDatabase(): boolean | 현재 데이터베이스의 변경 내용을 파일에 저장한다. |
| backupDatabase(backupPath: String): Backup | 현재 데이터베이스를 지정한 경로에 백업하고 Backup 객체를 반환한다. |

### 2. MasterKey

| **Attribute** | **Description** |
|-|-|
| passwordHash: String | 마스터 비밀번호의 원문이 아닌 해시값을 저장한다. |
| salt: byte[] | 비밀번호 해시 생성 과정에서 사용되는 난수값을 저장한다. |
| createdAt: LocalDateTime | 마스터키가 최초로 생성된 날짜와 시간을 저장한다. |
| modifiedAt: LocalDateTime | 마스터키가 마지막으로 변경된 날짜와 시간을 저장한다. |

| **Operation** | **Description** |
|-|-|
| setMasterPassword(password: String): void | 사용자가 입력한 마스터 비밀번호를 기반으로 해시값과 salt를 생성하여 저장한다. |
| verifyPassword(inputPassword: String): boolean | 입력된 비밀번호가 저장된 마스터 비밀번호와 일치하는지 검증한다. |
| changeMasterPassword(oldPassword: String, newPassword: String): boolean | 기존 마스터 비밀번호를 확인한 뒤 새로운 마스터 비밀번호로 변경한다. |

### 3. Entry

| **Attribute** | **Description** |
|-|-|
| entryId: String | Entry를 구분하기 위한 고유 식별자이다. |
| siteName: String | 계정이 사용되는 사이트 또는 서비스 이름을 저장한다. |
| siteUrl: String | 해당 사이트 또는 서비스의 URL을 저장한다. |
| userId: String | 로그인에 사용하는 사용자 ID를 저장한다. |
| createdAt: LocalDateTime | Entry가 최초로 생성된 날짜와 시간을 저장한다. |
| modifiedAt: LocalDateTime | Entry가 마지막으로 수정된 날짜와 시간을 저장한다. |

| **Operation** | **Description** |
|-|-|
| updateEntry(siteName: String, siteUrl: String, userId: String): void | Entry의 사이트 이름, URL, 사용자 ID 정보를 수정한다. |
| changePassword(newPassword: Password): void | 현재 Password를 새로운 Password로 변경하고, 기존 비밀번호를 PasswordHistory에 기록한다. |
| getPassword(): Password | Entry가 현재 가지고 있는 Password 객체를 반환한다. |
| addPasswordHistory(history: PasswordHistory): void | 비밀번호 변경 이력을 PasswordHistory 목록에 추가한다. |
| getPasswordHistories(): List<PasswordHistory> | 해당 Entry의 모든 비밀번호 변경 이력을 반환한다. |
| isPasswordExpired(settings: UserSettings): boolean | UserSettings의 비밀번호 변경 주기를 기준으로 현재 비밀번호의 변경 시점이 만료되었는지 판단한다. |

### 4. Password

| **Attribute** | **Description** |
|-|-|
| passwordValue: String | 사용자의 실제 비밀번호 값을 저장한다. 단, 파일 저장 시에는 데이터베이스 전체가 암호화된 상태로 저장된다. |
| strengthLevel: int | 비밀번호의 보안 강도를 수치 또는 등급 형태로 저장한다. |
| createdAt: LocalDateTime | 비밀번호가 최초로 생성된 날짜와 시간을 저장한다. |
| lastChangedAt: LocalDateTime | 비밀번호가 마지막으로 변경된 날짜와 시간을 저장한다. |

| **Operation** | **Description** |
|-|-|
| generatePassword(policy: PasswordPolicy): Password | PasswordPolicy에 정의된 조건을 기반으로 새로운 무작위 비밀번호를 생성한다. |
| evaluateStrength(): int | 비밀번호의 길이와 문자 조합을 기준으로 보안 강도를 평가한다. |
| updatePassword(newValue: String): void | 현재 비밀번호 값을 새로운 값으로 변경하고 마지막 변경일을 갱신한다. |
| isValid(policy: PasswordPolicy): boolean | 현재 비밀번호가 PasswordPolicy의 조건을 만족하는지 검사한다. |

### 5. PasswordPolicy

| **Attribute** | **Description** |
|-|-|
| length: int | 생성할 비밀번호의 길이를 저장한다. |
| useUppercase: boolean | 비밀번호 생성 시 대문자를 포함할지 여부를 저장한다. |
| useLowercase: boolean | 비밀번호 생성 시 소문자를 포함할지 여부를 저장한다. |
| useNumbers: boolean | 비밀번호 생성 시 숫자를 포함할지 여부를 저장한다. |
| useSpecialCharacters: boolean | 비밀번호 생성 시 특수문자를 포함할지 여부를 저장한다. |
| excludedCharacters: String | 비밀번호 생성 시 제외할 문자들을 저장한다. |

| **Operation** | **Description** |
|-|-|
| validatePolicy(): boolean | 현재 설정된 비밀번호 생성 규칙이 유효한지 검사한다. |
| getAvailableCharacters(): String | 현재 정책에 따라 비밀번호 생성에 사용할 수 있는 문자 집합을 반환한다. |
| setLength(length: int): void | 생성할 비밀번호의 길이를 설정한다. |
| setCharacterOptions(useUppercase: boolean, useLowercase: boolean, useNumbers: boolean, useSpecialCharacters: boolean): void | 대문자, 소문자, 숫자, 특수문자 사용 여부를 설정한다. |

### 6. PasswordHistory

| **Attribute** | **Description** |
|-|-|
| historyId: String | 비밀번호 변경 이력을 구분하기 위한 고유 식별자이다. |
| previousPassword: String | 이전에 사용했던 비밀번호 값을 저장한다. |
| changedAt: LocalDateTime | 해당 비밀번호가 변경된 날짜와 시간을 저장한다. |

| **Operation** | **Description** |
|-|-|
| recordPassword(password: Password): void | 변경 전 Password 객체의 값을 PasswordHistory로 기록한다. |
| getChangedAt(): LocalDateTime | 비밀번호가 변경된 날짜와 시간을 반환한다. |
| getPreviousPassword(): String | 이전에 사용했던 비밀번호 값을 반환한다. |

### 7. Backup

| **Attribute** | **Description** |
|-|-|
| backupId: String | 백업 정보를 구분하기 위한 고유 식별자이다. |
| backupPath: String | 백업 파일이 저장되는 경로를 저장한다. |
| backupFileName: String | 생성된 백업 파일의 이름을 저장한다. |
| backupDate: LocalDateTime | 백업이 수행된 날짜와 시간을 저장한다. |

| **Operation** | **Description** |
|-|-|
| validateBackupPath(path: String): boolean | 사용자가 지정한 백업 경로가 유효한지 검사한다. |
| getBackupFilePath(): String | 백업 파일의 전체 경로를 반환한다. |

### 8. UserSettings

| **Attribute** | **Description** |
|-|-|
| passwordChangePeriodDays: int | 비밀번호 변경 권장 주기를 일 단위로 저장한다. |
| alertEnabled: boolean | 비밀번호 변경 주기 알림 기능의 활성화 여부를 저장한다. |
| autoBackupEnabled: boolean | 자동 백업 기능의 활성화 여부를 저장한다. |

| **Operation** | **Description** |
|-|-|
| setPasswordChangePeriod(days: int): void | 비밀번호 변경 권장 주기를 설정한다. |
| getPasswordChangePeriod(): int | 현재 설정된 비밀번호 변경 권장 주기를 반환한다. |
| enableAlert(): void | 비밀번호 변경 주기 알림 기능을 활성화한다. |
| disableAlert(): void | 비밀번호 변경 주기 알림 기능을 비활성화한다. |
| isAlertEnabled(): boolean | 알림 기능이 활성화되어 있는지 여부를 반환한다. |
| enableAutoBackup(): void | 자동 백업 기능을 활성화한다. |
| disableAutoBackup(): void | 자동 백업 기능을 비활성화한다. |

### 9. Alert

| **Attribute** | **Description** |
|-|-|
| alertId: String | 알림 객체를 구분하기 위한 고유 식별자이다. |
| expired: boolean | 비밀번호 변경 주기가 초과되었는지 여부를 저장한다. |
| message: String | 사용자에게 표시할 알림 메시지를 저장한다. |
| checkedAt: LocalDateTime | 알림 상태를 마지막으로 확인한 날짜와 시간을 저장한다. |

| **Operation** | **Description** |
|-|-|
| checkExpired(entry: Entry, settings: UserSettings): boolean | Entry의 마지막 비밀번호 변경일과 UserSettings의 변경 주기를 비교하여 만료 여부를 판단한다. |
| generateMessage(entry: Entry): String | 변경 주기가 초과된 Entry에 대해 사용자에게 표시할 알림 메시지를 생성한다. |
| activateAlert(): void | 알림 상태를 활성화하고 expired 값을 true로 설정한다. |
| clearAlert(): void | 알림 상태를 해제하고 expired 값을 false로 설정한다. |
| isExpired(): boolean | 현재 알림 객체의 만료 상태를 반환한다. |

---


## 3. Sequence Diagram

### 1. Create Database
### 2. Generate Password
### 3. Open Database
### 4. Add Entries in DB
### 5. Delete Entries in DB
### 6. Show Entries
### 7. Copy Password
### 8. Autofill Password
### 9. Show Password History
### 10. Last Change Alert
### 11. Database Backup

---

## 4. State Machine Diagram

## 5. Implementation Requirements

### Hardware Requirements

| Component | Requirement |
|------------|-------------|
| CPU | |
| RAM | |
| Storage | |

### Software Requirements

| Component | Requirement |
|------------|-------------|
| Operating System | |
| Programming Language | |
| Framework | |
| Database | |

---

## 6. Glossary

| Term | Description |
|--------|-------------|
| Attribute | 객체의 속성 |
| Method | 객체의 기능 |
| Class Diagram | 클래스 구조를 표현한 UML 다이어그램 |
| Sequence Diagram | 객체 간 상호작용을 시간 순서대로 표현한 다이어그램 |
| State Machine Diagram | 상태 전이를 표현한 UML 다이어그램 |

---

## 7. References

- [starUML](https://staruml.io/)
