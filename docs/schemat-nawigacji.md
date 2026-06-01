# Schemat nawigacji po aplikacji

## Diagram nawigacji

```
                            ┌─────────────────────┐
                            │                     │
                            │    MainActivity     │
                            │   (Ekran główny)    │
                            │                     │
                            └──────┬───┬───┬───┬──┘
                                   │   │   │   │
            ┌──────────────────────┘   │   │   └──────────────┐
            │            ┌─────────────┘   └─────────────┐    │
            ▼            ▼                               ▼    ▼
    ┌───────────┐ ┌───────────┐                  ┌───────────┐ ┌───────────┐
    │  Login    │ │ Register  │                  │   Rates   │ │  About    │
    │ Activity  │ │ Activity  │                  │ Activity  │ │ Activity  │
    └─────┬─────┘ └─────┬─────┘                  └───────────┘ └───────────┘
          │             │                               
          └──────┬──────┘                               
                 ▼                                       
          ┌────────────────┐                             
          │  Dashboard     │                             
          │  Activity      │                             
          └───┬────────┬───┘                             
              │        │                                 
              ▼        ▼                                 
    ┌───────────┐    ┌──────────────────┐                
    │   Loan    │    │   Contact        │                
    │ Activity  │    │   Activity       │                
    └───────────┘    └──────────────────┘                
```

## Opis ekranów

### 1. MainActivity (Ekran główny)
- **Ścieżka**: uruchomienie aplikacji
- **Zawartość**: 
  - Górny pasek nawigacyjny z linkami: Kursy, O nas, Kontakt
  - Hero section z tytułem, opisem i przyciskami CTA
  - Dla niezalogowanych: "Weź pożyczkę", "Załóż konto", "Zaloguj się"
  - Dla zalogowanych: powitanie z nazwą użytkownika, przyciski "Panel" i "Wyloguj"
  - Sekcja "Dlaczego my?" z 4 kartami funkcji
  - Stopka z informacjami o prawach autorskich i disclaimerem
- **Nawigacja**: do LoginActivity, RegisterActivity, RatesActivity, AboutActivity, ContactActivity, DashboardActivity, LoanActivity

### 2. LoginActivity (Logowanie)
- **Ścieżka**: MainActivity → kliknięcie "Zaloguj się"
- **Zawartość**: 
  - Formularz: email + hasło
  - Przycisk "Zaloguj się"
  - Link do rejestracji
- **Po zalogowaniu**: przejście do DashboardActivity

### 3. RegisterActivity (Rejestracja)
- **Ścieżka**: MainActivity → kliknięcie "Załóż konto" / LoginActivity → link "Zarejestruj się"
- **Zawartość**: 
  - Formularz: nazwa użytkownika, email, hasło, potwierdzenie hasła
  - Walidacja po stronie klienta (długość hasła, zgodność haseł)
  - Link do logowania
- **Po rejestracji**: przejście do DashboardActivity

### 4. DashboardActivity (Panel użytkownika)
- **Ścieżka**: zalogowanie/rejestracja / kliknięcie "Panel" na stronie głównej
- **Zawartość**: 
  - Powitanie z nazwą użytkownika
  - Statystyki: wszystkie pożyczki, aktywne, spłacone
  - Lista pożyczek z kwotą, oprocentowaniem, datą ważności i statusem
  - Przycisk "Spłać" dla aktywnych pożyczek
  - Przycisk "Nowa pożyczka"
  - Przycisk "Wyloguj"
- **Wymaga autoryzacji**: jeśli brak tokena → przekierowanie do LoginActivity

### 5. LoanActivity (Weź pożyczkę)
- **Ścieżka**: DashboardActivity → "Nowa pożyczka" / MainActivity → "Weź pożyczkę" (wymaga logowania)
- **Zawartość**: 
  - 8 predefiniowanych ofert pożyczek
  - Dla każdej oferty: kwota, okres, oprocentowanie, miesięczna rata, całkowita spłata
  - Kliknięcie oferty → zaznaczenie
  - Przycisk "Potwierdź" → dialog potwierdzenia z podsumowaniem
  - Po potwierdzeniu → wysłanie żądania do API
- **Wymaga autoryzacji**: jeśli brak tokena → przekierowanie do LoginActivity

### 6. RatesActivity (Kursy walut)
- **Ścieżka**: MainActivity → kliknięcie "Kursy" w górnym pasku
- **Zawartość**: 
  - Pobranie danych z API NBP
  - Wybór walut przez kliknięcie (popularne + pozostałe)
  - Tabela z kursami wybranych walut
  - Data publikacji kursów

### 7. AboutActivity (O nas)
- **Ścieżka**: MainActivity → kliknięcie "O nas" w górnym pasku
- **Zawartość**: 
  - Opis platformy
  - Sekcja "Nasza misja"
  - Sekcja "Jak działamy?" (4 kroki)
  - Sekcja "Bezpieczeństwo"

### 8. ContactActivity (Kontakt)
- **Ścieżka**: MainActivity → kliknięcie "Kontakt" w górnym pasku
- **Zawartość**: 
  - Dane kontaktowe (email, telefon, adres, godziny otwarcia)
  - Formularz kontaktowy (imię, email, wiadomość)
  - Po wysłaniu: komunikat sukcesu

## Zasady nawigacji

1. **Nawigacja liniowa**: użytkownik może swobodnie poruszać się między ekranami
2. **Przycisk "Powrót"**: każdy ekran (oprócz głównego) ma przycisk "← Powrót" w toolbarze
3. **Przycisk "Wstecz" systemowy**: standardowe działanie Androida (powrót do poprzedniej aktywności)
4. **Autoryzacja**: próba dostępu do DashboardActivity lub LoanActivity bez tokena → przekierowanie do LoginActivity
5. **Wylogowanie**: usuwa token i przekierowuje do MainActivity
