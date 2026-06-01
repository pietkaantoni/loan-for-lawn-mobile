# Opis aplikacji - Loan for Lawn Mobile

## Przegląd

Loan for Lawn Mobile to aplikacja mobilna na system Android do zarządzania pożyczkami online. Aplikacja umożliwia użytkownikom rejestrację, logowanie, zaciąganie pożyczek z oprocentowaniem, przeglądanie historii pożyczek oraz sprawdzanie aktualnych kursów walut względem PLN.

Aplikacja jest mobilnym odpowiednikiem strony internetowej Loan for Lawn, zaprojektowanym specjalnie z myślą o urządzeniach mobilnych.

## Funkcjonalności

### 1. System autoryzacji
- Rejestracja nowego użytkownika (nazwa użytkownika, email, hasło)
- Logowanie do istniejącego konta
- Autoryzacja JWT (JSON Web Token)
- Stan zalogowania przechowywany w SharedPreferences

### 2. Zarządzanie pożyczkami
- 8 gotowych ofert pożyczek do wyboru – użytkownik wybiera jedną z predefiniowanych opcji
- Oprocentowanie zależne od kwoty i okresu
- Automatyczna kalkulacja miesięcznej raty i łącznej kwoty spłaty dla każdej oferty
- Dialog potwierdzenia przed zaciągnięciem pożyczki
- Możliwość spłaty aktywnej pożyczki z poziomu dashboardu
- Przeglądanie wszystkich pożyczek użytkownika
- Podgląd statusu pożyczki (aktywna/spłacona)

### 3. Kursy walut
- Integracja z API Narodowego Banku Polskiego (NBP)
- Wyświetlanie aktualnych kursów średnich walut względem PLN
- Możliwość wyboru interesujących walut przez kliknięcie
- Podział na waluty popularne i pozostałe

### 4. Formularz kontaktowy
- Dane kontaktowe (email, telefon, adres, godziny otwarcia)
- Formularz do wysyłania wiadomości przez API backendu

### 5. Strony informacyjne
- Strona główna z hero i sekcją funkcji
- Podstrona "O nas" z informacjami o firmie i misji
- Podstrona "Kontakt" z danymi kontaktowymi i formularzem

## Technologie

### Frontend (aplikacja mobilna)
- **Java** - główny język programowania
- **Android SDK 29+** - minimalna wersja Androida
- **AndroidX** - biblioteki wsparcia Androida
- **Material Design 3** - komponenty interfejsu użytkownika
- **Room** - lokalna baza danych SQLite
- **Retrofit 2** - klient HTTP do komunikacji z API
- **Gson** - serializacja/deserializacja JSON
- **OkHttp** - klient HTTP z logowaniem

### Backend
- **Node.js** - środowisko uruchomieniowe
- **Express** - framework do budowania API REST
- **PostgreSQL** - relacyjna baza danych
- **JWT** - autoryzacja tokenami
- **API NBP** - zewnętrzne API kursów walut

## Architektura

Aplikacja została zbudowana w architekturze wieloaktywnościowej (Multiple Activities), gdzie każdy ekran to osobna aktywność:

1. **MainActivity** - ekran główny z hero i funkcjami
2. **LoginActivity** - logowanie użytkownika
3. **RegisterActivity** - rejestracja nowego użytkownika
4. **DashboardActivity** - panel użytkownika z listą pożyczek
5. **LoanActivity** - wybór oferty pożyczki
6. **RatesActivity** - przeglądarka kursów walut
7. **AboutActivity** - strona informacyjna
8. **ContactActivity** - strona kontaktowa z formularzem

Komunikacja z backendem odbywa się przez REST API (Retrofit). Lokalna baza danych (Room) służy do przechowywania danych offline. Token JWT jest przechowywany w SharedPreferences.
