# Baza danych - Opis i diagram

## Wybór bazy danych

Do przechowywania danych lokalnych wykorzystano **Room** - bibliotekę abstrakcyjną dla SQLite.
Wybór Room podyktowany jest:
- Łatwą integracją z Androidem
- Obsługą relacji między danymi
- Wsparciem dla adnotacji i generowania kodu DAO
- Możliwością pracy offline

## Konfiguracja

Baza danych jest inicjalizowana jako singleton w klasie `AppDatabase`:

```java
AppDatabase db = AppDatabase.getInstance(context);
```

Room automatycznie tworzy schemat bazy na podstawie encji. Wersja bazy danych to 1.

## Schemat bazy danych

### Tabela: users

| Kolumna | Typ | Ograniczenia | Opis |
|---------|-----|-------------|------|
| id | TEXT | PK | Unikalny identyfikator użytkownika (UUID) |
| username | TEXT | NOT NULL | Nazwa użytkownika |
| email | TEXT | NOT NULL | Adres email |
| created_at | TEXT | NOT NULL | Data utworzenia konta |

### Tabela: loans

| Kolumna | Typ | Ograniczenia | Opis |
|---------|-----|-------------|------|
| id | TEXT | PK | Unikalny identyfikator pożyczki (UUID) |
| user_id | TEXT | FK -> users.id, NOT NULL | Identyfikator użytkownika |
| amount | REAL | NOT NULL | Kwota pożyczki |
| interest_rate | REAL | NOT NULL | Oprocentowanie roczne w % |
| status | TEXT | NOT NULL, domyślnie 'active' | Status: 'active' lub 'paid' |
| due_date | TEXT | NOT NULL | Data spłaty |
| created_at | TEXT | NOT NULL | Data utworzenia pożyczki |

## Diagram relacyjny

```
┌──────────────────┐          ┌──────────────────┐
│      users       │          │      loans       │
├──────────────────┤          ├──────────────────┤
│ id (TEXT) ───────┼──┐       │ id (TEXT)        │
│ username         │  │       │ user_id (FK) ────┼──┘
│ email            │  └───────│ amount           │
│ created_at       │    1:N   │ interest_rate    │
└──────────────────┘          │ status           │
                              │ due_date         │
                              │ created_at       │
                              └──────────────────┘
```

Relacja: **Jeden użytkownik może mieć wiele pożyczek** (1:N).
Klucz obcy `user_id` z opcją `CASCADE` - usunięcie użytkownika usuwa wszystkie jego pożyczki.

## Room Entities

### UserEntity
```java
@Entity(tableName = "users")
public class UserEntity {
    @PrimaryKey
    private String id;
    private String username;
    private String email;
    @ColumnInfo(name = "created_at")
    private String createdAt;
}
```

### LoanEntity
```java
@Entity(tableName = "loans",
        foreignKeys = @ForeignKey(entity = UserEntity.class,
                parentColumns = "id",
                childColumns = "user_id",
                onDelete = ForeignKey.CASCADE),
        indices = @Index("user_id"))
public class LoanEntity {
    @PrimaryKey
    private String id;
    @ColumnInfo(name = "user_id")
    private String userId;
    private double amount;
    @ColumnInfo(name = "interest_rate")
    private double interestRate;
    private String status;
    @ColumnInfo(name = "due_date")
    private String dueDate;
    @ColumnInfo(name = "created_at")
    private String createdAt;
}
```

## Operacje DAO

### UserDao
- `insert(UserEntity)` - zapis/zastąpienie użytkownika
- `getById(String)` - pobranie użytkownika po ID
- `deleteAll()` - usunięcie wszystkich użytkowników

### LoanDao
- `insert(LoanEntity)` - zapis pojedynczej pożyczki
- `insertAll(List<LoanEntity>)` - zapis listy pożyczek
- `getByUserId(String)` - pobranie pożyczek użytkownika (posortowane malejąco po dacie)
- `getById(String)` - pobranie pożyczki po ID
- `updateStatus(String, String)` - aktualizacja statusu pożyczki
- `deleteByUserId(String)` - usunięcie pożyczek użytkownika
- `deleteAll()` - usunięcie wszystkich pożyczek

## Uwagi

- Baza danych służy głównie do przechowywania danych offline (cache)
- Podstawowym źródłem danych jest API backendu
- Przy braku połączenia z internetem dane są wyświetlane z lokalnej bazy Room
- Token autoryzacyjny JWT jest przechowywany w SharedPreferences, nie w bazie Room
- W wersji 1 bazy danych nie ma mechanizmu migracji (exportSchema = false)
