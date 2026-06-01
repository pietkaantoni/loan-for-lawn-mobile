# Podział pracy

## Struktura projektu

```
Loan-for-Lawn-Mobile/
├── app/                           # Moduł aplikacji Android
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/loan_for_lawn_mobile/
│   │   │   │   ├── MainActivity.java           # Ekran główny
│   │   │   │   ├── LoginActivity.java          # Logowanie
│   │   │   │   ├── RegisterActivity.java       # Rejestracja
│   │   │   │   ├── DashboardActivity.java      # Panel użytkownika
│   │   │   │   ├── LoanActivity.java           # Wybór pożyczki
│   │   │   │   ├── RatesActivity.java          # Kursy walut
│   │   │   │   ├── AboutActivity.java          # O nas
│   │   │   │   ├── ContactActivity.java        # Kontakt
│   │   │   │   ├── data/
│   │   │   │   │   ├── AppDatabase.java        # Baza danych Room
│   │   │   │   │   ├── entity/
│   │   │   │   │   │   ├── UserEntity.java     # Encja użytkownika
│   │   │   │   │   │   └── LoanEntity.java     # Encja pożyczki
│   │   │   │   │   ├── dao/
│   │   │   │   │   │   ├── UserDao.java        # DAO użytkownika
│   │   │   │   │   │   └── LoanDao.java        # DAO pożyczki
│   │   │   │   │   └── api/
│   │   │   │   │       ├── ApiClient.java       # Klient Retrofit
│   │   │   │   │       ├── ApiService.java      # Interfejs API
│   │   │   │   │       └── ApiModels.java       # Modele API
│   │   │   │   └── utils/
│   │   │   │       ├── TokenManager.java        # Zarządzanie tokenem
│   │   │   │       └── LoanCalculator.java      # Kalkulator rat
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
| `LoginActivity` | Formularz logowania (email + hasło) z walidacją |
| `RegisterActivity` | Formularz rejestracji (nazwa, email, hasło, potwierdzenie) |
| `DashboardActivity` | Panel użytkownika: statystyki, lista pożyczek, przycisk spłaty |
| `LoanActivity` | Wybór oferty pożyczki z kalkulacją i dialogiem potwierdzenia |
| `RatesActivity` | Przeglądarka kursów walut z API NBP |
| `AboutActivity` | Strona informacyjna o firmie |
| `ContactActivity` | Strona kontaktowa z danymi i formularzem |

### Warstwa danych (Data)

| Komponent | Opis |
|-----------|------|
| `UserEntity` | Encja Room dla tabeli użytkowników |
| `LoanEntity` | Encja Room dla tabeli pożyczek |
| `UserDao` | Operacje na tabeli użytkowników (insert, getById, delete) |
| `LoanDao` | Operacje na tabeli pożyczek (insert, getByUserId, updateStatus) |
| `AppDatabase` | Klasa bazy danych Room (singleton) |
| `ApiService` | Interfejs Retrofit z endpointami API |
| `ApiClient` | Klient Retrofit (singleton) z obsługą autoryzacji JWT |
| `ApiModels` | Modele danych dla żądań i odpowiedzi API |

### Warstwa narzędziowa (Utils)

| Komponent | Opis |
|-----------|------|
| `TokenManager` | Zarządzanie tokenem JWT w SharedPreferences |
| `LoanCalculator` | Funkcje kalkulacji raty miesięcznej i daty spłaty |

## API Endpointy (backend)

### Autoryzacja
- `POST /api/auth/register` - Rejestracja użytkownika
- `POST /api/auth/login` - Logowanie
- `GET /api/auth/me` - Profil użytkownika (wymaga tokena)

### Pożyczki (wymagają tokena)
- `GET /api/loans` - Lista pożyczek użytkownika
- `POST /api/loans` - Utworzenie nowej pożyczki
- `GET /api/loans/:id` - Szczegóły pożyczki
- `POST /api/loans/:id/repay` - Spłata aktywnej pożyczki

### Kursy walut (bezpośrednio z NBP)
- `GET https://api.nbp.pl/api/exchangerates/tables/A/` - Lista kursów

### Kontakt
- `POST /api/contact` - Wysłanie wiadomości przez formularz

### Status
- `GET /api/health` - Sprawdzenie statusu serwera

## Użyte biblioteki zewnętrzne

| Biblioteka | Wersja | Zastosowanie |
|-----------|--------|-------------|
| Room Runtime | 2.6.1 | Lokalna baza danych SQLite |
| Room Compiler | 2.6.1 | Generowanie kodu DAO (annotation processor) |
| Retrofit | 2.11.0 | Klient HTTP do komunikacji z API |
| Converter Gson | 2.11.0 | Serializacja JSON |
| Gson | 2.11.0 | Parsowanie JSON |
| OkHttp Logging | 4.12.0 | Logowanie ruchu sieciowego |
