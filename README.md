# Loan for Lawn Mobile

Aplikacja mobilna na system Android do zarządzania pożyczkami online, działająca w pełni lokalnie z wyjątkiem API NBP do pobierania kursów walut.

## Wymagania

- **Android Studio** (najnowsza stabilna wersja)
- **JDK 11+**
- Urządzenie lub emulator z **Android 10+ (API 29+)**
- Połączenie z internetem (tylko do kursów walut NBP)

## Pobranie i uruchomienie krok po kroku

### 1. Sklonuj repozytorium

Otwórz terminal i wykonaj:

```bash
git clone <URL_REPOZYTORIUM>
cd Loan-for-Lawn-Mobile
```

Alternatywnie pobierz archiwum ZIP z GitHub i rozpakuj je.

### 2. Otwórz projekt w Android Studio

1. Uruchom **Android Studio**
2. Kliknij **File → Open**
3. Wybierz folder `Loan-for-Lawn-Mobile` (ten, w którym znajduje się `settings.gradle.kts`)
4. Kliknij **Open**

### 3. Poczekaj na synchronizację Gradle

Android Studio automatycznie pobierze wszystkie zależności:

- Android SDK (jeśli brakuje, postępuj według提示 w IDE)
- Biblioteki: Room, Retrofit, Gson, CardView, Material Design

Proces może potrwać kilka minut przy pierwszym otwarciu.

### 4. Skonfiguruj emulator lub podłącz urządzenie

**Emulator:**
1. Kliknij **Device Manager** w prawym górnym rogu
2. Kliknij **Create device**
3. Wybierz dowolny model (np. Pixel 6) z systemem Android 10+ (API 29+)
4. Kliknij **Finish**, a następnie uruchom emulator zielonym przyciskiem

**Urządzenie fizyczne:**
1. Włącz **Opcje deweloperskie** na telefonie (Ustawienia → Informacje o telefonie → Numer kompilacji → kliknij 7 razy)
2. Włącz **Debugowanie USB** (Ustawienia → Opcje deweloperskie → Debugowanie USB)
3. Podłącz telefon kablem USB do komputera
4. Zaakceptuj klucz debugowania na telefonie

### 5. Uruchom aplikację

1. Wybierz urządzenie/emulator z rozwijanej listy u góry (obok zielonego trójkąta)
2. Kliknij zielony przycisk **Run** (▶) lub naciśnij **Shift + F10**
3. Poczekaj na zbudowanie i wdrożenie aplikacji

### 6. Korzystanie z aplikacji

Po uruchomieniu zobaczysz ekran główny z następującymi opcjami:

- **Zaloguj się** — logowanie do istniejącego konta
- **Zarejestruj się** — utworzenie nowego konta
- **Kursy walut** — przeglądanie 10 najpopularniejszych kursów NBP
- **O nas** — informacje o platformie
- **Kontakt** — dane kontaktowe i formularz

Po zalogowaniu dodatkowo dostępne:

- **Panel** — przeglądanie pożyczek i zarządzanie nimi
- **Nowa pożyczka** — wybór oferty z 8 predefiniowanych opcji

## Struktura projektu

```
Loan-for-Lawn-Mobile/
├── app/                          # Moduł aplikacji
│   ├── src/main/java/.../        # Kod źródłowy Java
│   │   ├── *Activity.java        # 8 ekranów aplikacji
│   │   ├── data/                 # Warstwa danych (Room, API)
│   │   └── utils/                # Narzędzia (TokenManager, LoanCalculator, PasswordUtil)
│   ├── src/main/res/             # Zasoby (layouty, drawable, values)
│   └── build.gradle.kts          # Konfiguracja modułu
├── gradle/                       # Konfiguracja Gradle
├── docs/                         # Dokumentacja projektu
└── README.md                     # Ta instrukcja
```

## Technologie

| Technologia | Zastosowanie |
|-------------|-------------|
| Java 11 | Główny język programowania |
| Android SDK 29+ | Minimalna wersja systemu |
| Material Design 3 | Komponenty interfejsu |
| Room 2.6.1 | Lokalna baza danych SQLite |
| Retrofit 2.11.0 | Klient HTTP (API NBP) |
| Gson 2.11.0 | Parsowanie JSON |
| SHA-256 | Hashowanie haseł |

## Dokumentacja

Szczegółowa dokumentacja znajduje się w folderze `docs/`:

- `opis-aplikacji.md` — funkcjonalności i architektura
- `podzial-pracy.md` — struktura komponentów i biblioteki
- `baza-danych.md` — schemat bazy danych Room
- `schemat-nawigacji.md` — diagram i opis ekranów
