# Sistemska knjižnica

Library management desktop app. Java course project at TVZ: JavaFX, Maven, H2, JDBC.

## Tehnologije

- Java 25 (JPMS modul)
- JavaFX 21 + FXML
- H2 baza (embedded), JDBC
- JSON-B (Yasson) za izvoz/uvoz u JSON, JAXB za XML zapis promjena
- SLF4J + Logback
- Maven, JUnit 5

## Funkcionalnosti

- prijava knjižničara (lozinke se spremaju kao PBKDF2 hash, nakon 3 neuspjela pokušaja prijava se zaključava na 30 sekundi)
- unos i pretraga knjiga i članova, brisanje (desni klik na redak u tablici)
- posudba i povrat knjige, s automatskim ažuriranjem broja dostupnih primjeraka u istoj transakciji
- sigurnosna kopija: kopija tablice knjiga u bazi, izvoz svih podataka u JSON i binarnu datoteku (serijalizacija)
- vraćanje podataka iz JSON kopije
- povijest promjena: svaka akcija se zapisuje u `log.xml` i može se pregledati u aplikaciji
- dugotrajne operacije (backup, učitavanje) izvršavaju se na virtualnim dretvama

## Pokretanje

Potreban je JDK 25.

```bash
./mvnw javafx:run
```

Na Windowsu: `mvnw.cmd javafx:run`

Pri prvom pokretanju baza se kreira u mapi `database/` i puni s nekoliko primjera.

Demo prijava: **admin** / **admin123**

## Konfiguracija baze

Postavke se čitaju iz `src/main/resources/database.properties`. Ako ta datoteka ne postoji, koristi se `database.properties.example` (lokalna H2 baza u mapi projekta). Za vlastite postavke kopiraj primjer:

```bash
cp src/main/resources/database.properties.example src/main/resources/database.properties
```

`database.properties` je u `.gitignore` i ne ide u repozitorij.

## Struktura

```
src/main/java
├── com/example/sistemska_knjiznica   kontroleri i pokretanje aplikacije
├── entity                            model (Book, Member, Loan, Librarian...)
├── repository                        pristup bazi
├── utils                             baza, datoteke, validacija, poruke
└── exceptions                        vlastite iznimke
src/main/resources
├── schema.sql                        shema i početni podaci
└── com/example/sistemska_knjiznica   FXML ekrani i CSS
```

Datoteke koje aplikacija stvara tijekom rada (`database/`, `backup/`, `logs/`, `log.xml`) nisu dio repozitorija.

## Testovi

```bash
./mvnw test
```
