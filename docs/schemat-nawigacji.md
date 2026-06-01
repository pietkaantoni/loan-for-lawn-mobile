# Schemat nawigacji po aplikacji

## Diagram nawigacji

```
                             ┌─────────────────────┐
                             │                     │
                             │    MainActivity     │
                             │   (Ekran główny)    │
                             │                     │
                             └───┬───┬───┬───┬───┬──┘
                                 │   │   │   │   │
               ┌─────────────────┘   │   │   │   └──────────────────┐
               │          ┌──────────┘   │   └──────────┐          │
               │          │        ┌─────┘              │          │
               ▼          ▼        ▼                    ▼          ▼
         ┌─────────┐ ┌─────────┐ ┌────────┐      ┌─────────┐ ┌─────────┐
         │  Login  │ │ Register│ │  Rates │      │  About  │ │ Contact │
         │ Activity│ │ Activity│ │Activity│      │ Activity│ │ Activity│
         └────┬────┘ └────┬────┘ └────────┘      └─────────┘ └─────────┘
              │           │
              └─────┬─────┘
                    │
                    ▼
              ┌────────────────┐
              │  Dashboard     │
              │  Activity      │
              └────────┬───────┘
                       │
                       ▼
              ┌────────────────┐
              │    Loan        │
              │   Activity     │
              └────────────────┘
```

## Opis ekranów

### 1. MainActivity (Ekran główny)
- **Ścieżka**: uruchomienie aplikacji
- **Zawartość**:
  - Hero section z tytułem, opisem i przyciskami CTA
  - Dla niezalogowanych: "Weź pożyczkę", "Załóż konto", "Zaloguj się"
  - Dla zalogowanych: powitanie z nazwą użytkownika, przyciski "Panel" i "Wyloguj"
  - Przyciski w toolbarze: "Kursy", "O nas", "Kontakt"
  - Sekcja "Dlaczego my?" z 4 kartami funkcji
- **Nawigacja**: do LoginActivity, RegisterActivity, RatesActivity, AboutActivity, ContactActivity, DashboardActivity

### 2. LoginActivity (Logowanie)
- **Ścieżka**: MainActivity → kliknięcie "Zaloguj się"
- **Zawartość**:
  - Formularz: email + hasło (TextInputLayout Material Design)
  - Przycisk "Zaloguj się"
  - Link do rejestracji
- **Logika**: weryfikacja hasła przez Room (SHA-256), zapis sesji do SharedPreferences
- **Po zalogowaniu**: przejście do DashboardActivity

### 3. RegisterActivity (Rejestracja)
- **Ścieżka**: MainActivity → kliknięcie "Załóż konto" / LoginActivity → link "Zarejestruj się"
- **Zawartość**:
  - Formularz: nazwa użytkownika, email, hasło, potwierdzenie hasła
  - Walidacja po stronie klienta (długość hasła, zgodność haseł, unikalność email/nazwy)
  - Link do logowania
- **Logika**: hash hasła SHA-256, zapis do Room, automatyczne zalogowanie
- **Po rejestracji**: przejście do DashboardActivity

### 4. DashboardActivity (Panel użytkownika)
- **Ścieżka**: zalogowanie/rejestracja / kliknięcie "Panel" na stronie głównej
- **Zawartość**:
  - Powitanie z nazwą użytkownika
  - Statystyki: wszystkie pożyczki, aktywne, spłacone
  - Lista pożyczek z kwotą, oprocentowaniem, datą ważności i statusem
  - Przycisk "Spłać" dla aktywnych pożyczek (aktualizacja statusu w Room)
  - Przycisk "Nowa pożyczka"
  - Przycisk "Wyloguj"
- **Wymaga autoryzacji**: jeśli brak sesji → przekierowanie do LoginActivity

### 5. LoanActivity (Weź pożyczkę)
- **Ścieżka**: DashboardActivity → "Nowa pożyczka"
- **Zawartość**:
  - 8 predefiniowanych ofert pożyczek (kwoty 1000-25000 PLN, okresy 6-24 mies.)
  - Dla każdej oferty: kwota, okres, oprocentowanie, miesięczna rata, całkowita spłata
  - Kliknięcie oferty → dialog potwierdzenia z podsumowaniem
  - Po potwierdzeniu → zapis do Room z lokalnym UUID
- **Wymaga autoryzacji**: jeśli brak sesji → przekierowanie do LoginActivity

### 6. RatesActivity (Kursy walut)
- **Ścieżka**: MainActivity → kliknięcie "Kursy" w toolbarze / przycisk "Kursy walut"
- **Zawartość**:
  - Pobranie danych z API NBP (jedyny endpoint zewnętrzny)
  - Lista 10 najpopularniejszych kursów średnich walut względem PLN (EUR, USD, GBP, CHF, JPY, CZK, DKK, NOK, SEK, CAD)
  - Data pochodzenia danych z NBP
  - Lista tylko do odczytu — brak interakcji
  - Wiersz: kod waluty, nazwa, kurs z 4 miejscami po przecinku + "PLN"

### 7. AboutActivity (O nas)
- **Ścieżka**: MainActivity → kliknięcie "O nas" w toolbarze
- **Zawartość**:
  - Opis platformy
  - Sekcja "Nasza misja"
  - Sekcja "Jak działamy?" (4 kroki)
  - Sekcja "Bezpieczeństwo"

### 8. ContactActivity (Kontakt)
- **Ścieżka**: MainActivity → kliknięcie "Kontakt" w toolbarze
- **Zawartość**:
  - Nagłówek "Masz pytania? Skontaktuj się z nami!"
  - Dane kontaktowe (email, telefon, adres, godziny otwarcia)
  - Nagłówek "Napisz do nas"
  - Formularz kontaktowy (imię, email, wiadomość)
  - Po wysłaniu: komunikat sukcesu (lokalnie, bez wywołania API)

## Zasady nawigacji

1. **Nawigacja liniowa**: użytkownik może swobodnie poruszać się między ekranami
2. **Przycisk "Powrót"**: każdy ekran (oprócz głównego) ma przycisk "← Powrót" w toolbarze
3. **Przycisk "Wstecz" systemowy**: standardowe działanie Androida (powrót do poprzedniej aktywności)
4. **Autoryzacja**: próba dostępu do DashboardActivity lub LoanActivity bez sesji → przekierowanie do LoginActivity
5. **Wylogowanie**: usuwa sesję z SharedPreferences i przekierowuje do MainActivity
