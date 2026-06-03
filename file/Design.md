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
| backupDatabase(backupPath: String): Backup | 현재 데이터베이스를 지정한 경로에 백업하고, 생성된 백업 정보를 포함하는 Backup 객체를 반환한다. |
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
<img width="808" height="401" alt="Image" src="https://github.com/user-attachments/assets/3ec2f235-2733-4a8c-8f13-aa9abbed47b7" />

이 시퀀스 다이어그램은 사용자가 새로운 데이터베이스를 생성하는 과정을 나타낸다.
먼저 사용자는 데이터베이스 이름, 저장 경로, 마스터 비밀번호를 입력하고 Database 객체에 createDatabase(databaseName, filePath, masterPassword) 메시지를 보낸다. Database는 입력값이 유효한지 확인한 후, 정상적인 경우 MasterKey 객체에 setMasterPassword(masterPassword) 메시지를 전달하여 마스터 비밀번호 정보를 설정한다.
마스터 비밀번호 설정이 완료되면 Database는 자기 자신에게 saveDatabase() 메시지를 보내 현재 생성된 데이터베이스 정보를 파일로 저장한다. 저장이 성공하면 생성된 Database 객체를 사용자에게 반환한다.
예외 흐름에서는 저장 경로가 잘못되었거나 마스터 비밀번호가 유효하지 않은 경우 데이터베이스 생성을 중단하고 null을 반환한다.

### 2. Generate Password
<img width="621" height="659" alt="Image" src="https://github.com/user-attachments/assets/f54ff73a-3e3b-450e-bffb-44ad2353812e" />

사용자는 비밀번호 길이와 문자 옵션을 설정한 뒤 PasswordPolicy에 정책 검증을 요청한다. 정책이 유효하면 Password 객체가 generatePassword(policy)를 수행하고, PasswordPolicy로부터 사용 가능한 문자 집합을 받아 무작위 비밀번호를 생성한다. 이후 비밀번호 강도를 평가하고 정책 만족 여부를 확인한 뒤 생성된 Password를 반환한다. 정책이 유효하지 않으면 비밀번호 생성은 수행되지 않는다.

### 3. Open Database
<img width="571" height="491" alt="Image" src="https://github.com/user-attachments/assets/114c29ca-10e2-4de6-b9a4-414a33547364" />

사용자는 마스터 비밀번호를 입력하여 Database에 데이터베이스 열기를 요청한다. Database는 MasterKey의 verifyPassword(inputPassword)를 호출하여 비밀번호를 검증한다. 비밀번호가 일치하면 데이터베이스가 열리고 true를 반환한다. 비밀번호가 틀리면 false를 반환한다. 추가적으로 사용자가 마스터 비밀번호 변경을 요청하는 경우 changeMasterPassword()가 수행될 수 있으며, 사용자가 데이터베이스를 닫으면 closeDatabase()가 호출된다.

### 4. Add Entries in DB
<img width="728" height="866" alt="Image" src="https://github.com/user-attachments/assets/1feeb145-aa32-4558-be4b-08603a52930a" />

사용자는 새로운 Entry 정보를 입력한다. 먼저 입력된 비밀번호가 정책에 맞는지 Password.isValid(policy)로 검사한다. 입력값이 유효하면 Database.findEntry(entryId)로 동일한 Entry가 있는지 확인한다. Entry가 없으면 addEntry(entry)로 추가하고 저장한다. 이미 존재하는 Entry라면 updateEntry(entry)를 통해 정보를 수정한다. 비밀번호가 변경되는 경우 기존 비밀번호를 PasswordHistory에 기록한 뒤 Entry.changePassword(newPassword)와 Password.updatePassword(newValue)를 통해 현재 비밀번호를 갱신한다.

### 5. Delete Entries in DB
<img width="323" height="368" alt="Image" src="https://github.com/user-attachments/assets/45f29c68-c697-4e4b-b0fa-f191ffc0da53" />

사용자는 삭제할 Entry를 선택한다. Database.findEntry(entryId)를 통해 해당 Entry가 존재하는지 먼저 확인한다. Entry가 존재하면 deleteEntry(entryId)를 호출하여 삭제하고, 변경된 내용을 saveDatabase()로 저장한다. Entry가 존재하지 않으면 삭제를 수행하지 않고 실패 결과를 반환한다.

### 6. Show Entries
<img width="272" height="308" alt="Image" src="https://github.com/user-attachments/assets/34e8cc15-ebc5-4e84-a20f-09ad36452a74" />

사용자는 특정 Entry의 비밀번호 변경 이력을 요청한다. 먼저 Database.findEntry(entryId)로 Entry를 찾는다. Entry가 존재하면 Entry.getPasswordHistories()를 호출하여 비밀번호 변경 이력 목록을 가져온다. 이력이 존재하면 loop를 통해 각 PasswordHistory 객체의 이전 비밀번호와 변경 날짜를 조회한다. 이력이 없으면 빈 목록을 반환한다.

### 7. Copy Password
<img width="304" height="340" alt="Image" src="https://github.com/user-attachments/assets/deb1f8ab-6e89-46bb-b04e-9cf9c5369e4e" />

사용자는 복사할 Entry를 선택한다. Database.findEntry(entryId)를 통해 Entry를 검색하고, Entry가 존재하면 Entry.getPassword()로 현재 비밀번호를 가져온다. 이후 외부 시스템 클립보드에 비밀번호를 저장한다. Entry가 존재하지 않으면 복사를 수행하지 않고 실패 결과를 반환한다.

### 8. Autofill Password
<img width="312" height="381" alt="Image" src="https://github.com/user-attachments/assets/2c58772b-8b0b-4f8b-9cb5-2b5fa6da56be" />

사용자는 자동 입력할 Entry를 선택한다. 시스템은 Database.findEntry(entryId)로 Entry를 검색한다. Entry가 존재하고 브라우저 입력창에 포커스가 맞춰져 있으면 Entry.getPassword()로 현재 비밀번호를 가져온 뒤, 외부 브라우저 입력창에 사용자 ID와 비밀번호를 자동 입력한다. Entry가 없거나 브라우저 입력 위치가 올바르지 않으면 자동 입력은 실패한다.

### 9. Show Password History
<img width="500" height="542" alt="Image" src="https://github.com/user-attachments/assets/388ce913-7986-4015-83d4-7dc944955b51" />

사용자는 특정 Entry의 비밀번호 변경 이력을 요청한다. 먼저 Database.findEntry(entryId)로 Entry를 찾는다. Entry가 존재하면 Entry.getPasswordHistories()를 호출하여 비밀번호 변경 이력 목록을 가져온다. 이력이 존재하면 loop를 통해 각 PasswordHistory 객체의 이전 비밀번호와 변경 날짜를 조회한다. 이력이 없으면 빈 목록을 반환한다.

### 10. Last Change Alert
<img width="654" height="1140" alt="Image" src="https://github.com/user-attachments/assets/d4f4bc61-8519-4edb-a9c5-860679ff4db9" />

사용자는 비밀번호 변경 주기 알림을 확인한다. 먼저 알림 설정을 변경하는 경우 setPasswordChangePeriod(days)와 enableAlert() 또는 disableAlert()를 호출한다. 이후 isAlertEnabled()로 알림 기능 활성화 여부를 확인한다. 알림이 비활성화되어 있으면 검사를 수행하지 않는다. 알림이 활성화되어 있으면 Database.findEntry(entryId)로 Entry를 찾고, Entry.isPasswordExpired(settings)를 통해 비밀번호 변경 주기 초과 여부를 확인한다. 변경 주기가 초과된 경우 Alert.generateMessage(entry)와 activateAlert()를 수행하고, 초과되지 않은 경우 clearAlert()를 수행한다.

### 11. Database Backup
<img width="616" height="666" alt="Image" src="https://github.com/user-attachments/assets/2a37bbe4-4415-438e-be7f-50f21701baf4" />

사용자가 백업 경로를 입력한 뒤 Database.backupDatabase(backupPath)를 호출하면, Database는 백업 정보를 저장할 Backup 객체를 생성한다. 이후 Backup.validateBackupPath(backupPath)로 경로 유효성을 검사한다.

백업 경로가 유효하면 Database는 현재 데이터베이스 파일을 해당 경로에 복사하여 백업 파일을 생성하고, Backup.getBackupFilePath()를 통해 생성된 백업 파일 경로를 확인한다. 이후 Database.saveDatabase()를 호출하여 백업 정보를 저장하고 생성된 Backup 객체를 반환한다.

경로가 유효하지 않으면 백업을 수행하지 않고 null을 반환한다. 자동 백업 설정을 변경하는 경우에는 선택적으로 UserSettings.enableAutoBackup() 또는 UserSettings.disableAutoBackup()이 먼저 수행된다.

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
|salt|솔트(salt)는 데이터, 비밀번호, 통과암호를 해시 처리하는 단방향 함수의 추가 입력으로 사용되는 랜덤 데이터|
|LocalDateTime|날짜(LocalDate)와 시간(LocalTime)을 결합한 클래스로, 연도, 월, 일, 시, 분, 초를 함께 처리할 수 있는 java.time 패키지의 클래스|

---

## 7. References

- [starUML](https://staruml.io/)
- [plantUML](https://plantuml.com/ko/)
