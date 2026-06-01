# Podział pracy

## Struktura projektu

```
Loan-for-Lawn-Mobile/
├── app/                           # Moduł aplikacji Android
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/loan_for_lawn_mobile/
│   │   │   │   ├── MainActivity.java           # Ekran główny
│   │   │   │   ├── LoginActivity.java          # Logowanie (lokalne)
│   │   │   │   ├── RegisterActivity.java       # Rejestracja (lokalna)
│   │   │   │   ├── DashboardActivity.java      # Panel użytkownika
│   │   │   │   ├── LoanActivity.java           # Wybór pożyczki
│   │   │   │   ├── RatesActivity.java          # Kursy walut (NBP API)
│   │   │   │   ├── AboutActivity.java          # O nas
│   │   │   │   ├── ContactActivity.java        # Kontakt (lokalny)
│   │   │   │   ├── data/
│   │   │   │   │   ├── AppDatabase.java        # Baza danych Room
│   │   │   │   │   ├── entity/
│   │   │   │   │   │   ├── UserEntity.java     # Encja użytkownika
│   │   │   │   │   │   └── LoanEntity.java     # Encja pożyczki
│   │   │   │   │   ├── dao/
│   │   │   │   │   │   ├── UserDao.java        # DAO użytkownika
│   │   │   │   │   │   └── LoanDao.java        # DAO pożyczki
│   │   │   │   │   └── api/
│   │   │   │   │       ├── ApiClient.java       # Klient Retrofit (tylko NBP)
│   │   │   │   │       ├── ApiService.java      # Interfejs API (tylko NBP)
│   │   │   │   │       └── ApiModels.java       # Modele API (tylko NBP)
│   │   │   │   └── utils/
│   │   │   │       ├── TokenManager.java        # Zarządzanie sesją (SharedPreferences)
│   │   │   │       ├── LoanCalculator.java      # Kalkulator rat
│   │   │   │       └── PasswordUtil.java        # Hashowanie haseł (SHA-256)
│   │   │   ├── res/
│   │   │   │   ├── layout/                      # Pliki XML layoutów
│   │   │   │   ├── drawable/                    # Zasoby graficzne
│   │   │   │   ├── values/                      # Stringi, kolory, motywy
│   │   │   │   └── mipmap-*/                    # Ikony aplikacji
│   │   │   └── AndroidManifest.xml
│   │   ├── test/                                # Testy jednostkowe
│   │   └── androidTest/                         # Testy instrumentowane
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── gradle/
│   ├── libs.versions.toml                       # Wersje bibliotek
│   └── wrapper/
├── docs/                                        # Dokumentacja projektu
│   ├── opis-aplikacji.md
│   ├── podzial-pracy.md
│   ├── baza-danych.md
│   └── schemat-nawigacji.md
├── build.gradle.kts                             # Konfiguracja główna
├── settings.gradle.kts
├── gradle.properties
├── gradlew / gradlew.bat                        # Skrypty Gradle
└── .gitignore
```

## Podział na komponenty

### Warstwa prezentacji (Activities)

| Komponent | Opis |
|-----------|------|
| `MainActivity` | Ekran główny z hero, sekcją funkcji i nawigacją |
| `LoginActivity` | Formularz logowania (email + hasło) z walidacją; weryfikacja hasła przez Room + SHA-256 |
| `RegisterActivity` | Formularz rejestracji (nazwa, email, hasło, potwierdzenie); hash hasła SHA-256 przed zapisem |
| `DashboardActivity` | Panel użytkownika: statystyki, lista pożyczek z Room, przycisk spłaty |
| `LoanActivity` | Wybór oferty pożyczki z kalkulacją i dialogiem potwierdzenia; zapis do Room |
| `RatesActivity` | Przeglądarka kursów walut z API NBP (jedyny endpoint zewnętrzny) |
| `AboutActivity` | Strona informacyjna o firmie |
| `ContactActivity` | Strona kontaktowa z danymi i formularzem (działa lokalnie, bez API) |

### Warstwa danych (Data)

| Komponent | Opis |
|-----------|------|
| `UserEntity` | Encja Room dla tabeli użytkowników (z polem password_hash) |
| `LoanEntity` | Encja Room dla tabeli pożyczek |
| `UserDao` | Operacje na tabeli użytkowników (insert, getById, getByEmail, getByUsername, delete) |
| `LoanDao` | Operacje na tabeli pożyczek (insert, getByUserId, updateStatus, delete) |
| `AppDatabase` | Klasa bazy danych Room (singleton, wersja 2, fallbackToDestructiveMigration) |
| `ApiService` | Interfejs Retrofit — tylko endpoint NBP (exchangerates/tables/A) |
| `ApiClient` | Klient Retrofit (singleton) — tylko NBP, bez OkHttp logging |
| `ApiModels` | Modele danych dla odpowiedzi NBP API |

### Warstwa narzędziowa (Utils)

| Komponent | Opis |
|-----------|------|
| `TokenManager` | Zarządzanie sesją użytkownika w SharedPreferences (userId, username, email) |
| `LoanCalculator` | Funkcje kalkulacji raty miesięcznej i daty spłaty |
| `PasswordUtil` | Hashowanie haseł SHA-256 i weryfikacja |

## Architektura komunikacji

W przeciwieństwie do wersji webowej, aplikacja mobilna nie komunikuje się z backendem (Node.js/Express).
Wszystkie operacje działają lokalnie:

| Operacja | Źródło danych |
|----------|--------------|
| Rejestracja | Zapis do lokalnej bazy Room (hash SHA-256) |
| Logowanie | Weryfikacja hasła względem hasha w Room |
| Pożyczki | UUID generowane lokalnie, zapis do Room |
| Spłata pożyczki | Aktualizacja statusu w Room |
| Kontakt | Komunikat sukcesu (lokalnie, brak API) |
| Kursy walut | API NBP (jedyny endpoint zewnętrzny) |

## Użyte biblioteki zewnętrzne

| Biblioteka | Wersja | Zastosowanie |
|-----------|--------|-------------|
| Room Runtime | 2.6.1 | Lokalna baza danych SQLite |
| Room Compiler | 2.6.1 | Generowanie kodu DAO (annotation processor) |
| Retrofit | 2.11.0 | Klient HTTP (tylko API NBP) |
| Converter Gson | 2.11.0 | Serializacja JSON |
| Gson | 2.11.0 | Parsowanie JSON |
