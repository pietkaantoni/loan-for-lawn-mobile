# Opis aplikacji - Loan for Lawn Mobile

## Przegląd

Loan for Lawn Mobile to aplikacja mobilna na system Android do zarządzania pożyczkami online. Aplikacja umożliwia użytkownikom rejestrację, logowanie, zaciąganie pożyczek z oprocentowaniem, przeglądanie historii pożyczek oraz sprawdzanie aktualnych kursów walut względem PLN.

Aplikacja jest mobilnym odpowiednikiem strony internetowej Loan for Lawn, zaprojektowanym specjalnie z myślą o urządzeniach mobilnych. W przeciwieństwie do wersji webowej, aplikacja działa w pełni lokalnie — jedyna komunikacja z zewnętrznym serwerem to API NBP po kursy walut.

## Funkcjonalności

### 1. System autoryzacji
- Rejestracja nowego użytkownika (nazwa użytkownika, email, hasło)
- Logowanie do istniejącego konta
- Hasło przechowywane jako hash SHA-256 w lokalnej bazie Room
- Stan zalogowania przechowywany w SharedPreferences

### 2. Zarządzanie pożyczkami
- 8 gotowych ofert pożyczek do wyboru – użytkownik wybiera jedną z predefiniowanych opcji
- Oprocentowanie zależne od kwoty i okresu
- Automatyczna kalkulacja miesięcznej raty i łącznej kwoty spłaty dla każdej oferty
- Dialog potwierdzenia przed zaciągnięciem pożyczki
- Możliwość spłaty aktywnej pożyczki z poziomu dashboardu
- Przeglądanie wszystkich pożyczek użytkownika
- Podgląd statusu pożyczki (aktywna/spłacona)
- Pożyczki przechowywane lokalnie w Room z UUID generowanym po stronie aplikacji

### 3. Kursy walut
- Integracja z API Narodowego Banku Polskiego (NBP)
- Wyświetlanie 10 najpopularniejszych kursów średnich walut względem PLN (EUR, USD, GBP, CHF, JPY, CZK, DKK, NOK, SEK, CAD)
- Lista tylko do odczytu — brak interakcji
- Jedyna komunikacja z zewnętrznym serwerem

### 4. Formularz kontaktowy
- Dane kontaktowe (email, telefon, adres, godziny otwarcia)
- Formularz do wysyłania wiadomości — działa lokalnie (brak wywołania API)

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
- **Room** - lokalna baza danych SQLite (główne źródło danych)
- **Retrofit 2** - klient HTTP (tylko do API NBP)
- **Gson** - serializacja/deserializacja JSON

### Zewnętrzne API
- **API NBP** - jedyne zewnętrzne API (kursy walut)

## Architektura

Aplikacja została zbudowana w architekturze wieloaktywnościowej (Multiple Activities), gdzie każdy ekran to osobna aktywność:

1. **MainActivity** - ekran główny z hero i funkcjami
2. **LoginActivity** - logowanie użytkownika (weryfikacja hasła przez Room)
3. **RegisterActivity** - rejestracja nowego użytkownika (hash hasła SHA-256)
4. **DashboardActivity** - panel użytkownika z listą pożyczek
5. **LoanActivity** - wybór oferty pożyczki (zapis do Room)
6. **RatesActivity** - przeglądarka 10 popularnych kursów walut (NBP API, lista tylko do odczytu)
7. **AboutActivity** - strona informacyjna
8. **ContactActivity** - strona kontaktowa z formularzem (lokalnie)

Aplikacja działa w **trybie offline** — auth, pożyczki i kontakt są przechowywane w lokalnej bazie Room. Jedynie kursy walut wymagają połączenia z internetem (API NBP).
