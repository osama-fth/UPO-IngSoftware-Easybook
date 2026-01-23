-- 1. Tabella BIBLIOTECARIO
CREATE TABLE IF NOT EXISTS Bibliotecario
(
    matricola TEXT PRIMARY KEY,
    nome      TEXT        NOT NULL,
    cognome   TEXT        NOT NULL,
    username  TEXT UNIQUE NOT NULL,
    password  TEXT        NOT NULL
);

-- 2. Tabella UTENTE
CREATE TABLE IF NOT EXISTS Utente
(
    cf                  TEXT PRIMARY KEY,
    nome                TEXT NOT NULL,
    cognome             TEXT NOT NULL,
    stato               TEXT    DEFAULT 'ATTIVO',
    num_prestiti_attivi INTEGER DEFAULT 0
);

-- 3. Tabella LIBRO
CREATE TABLE IF NOT EXISTS Libro
(
    isbn              TEXT PRIMARY KEY,
    titolo            TEXT    NOT NULL,
    autore            TEXT    NOT NULL,
    copie_totali      INTEGER NOT NULL,
    copie_disponibili INTEGER NOT NULL CHECK (copie_disponibili <= copie_totali)
);

-- 4. Tabella PRESTITO
CREATE TABLE IF NOT EXISTS Prestito
(
    id                INTEGER PRIMARY KEY AUTOINCREMENT,
    utente_cf         TEXT NOT NULL,
    libro_isbn        TEXT NOT NULL,
    data_inizio       TEXT NOT NULL,
    data_scadenza     TEXT NOT NULL,
    data_restituzione TEXT,

    -- Vincoli di integrità referenziale con azioni a cascata
    FOREIGN KEY (utente_cf) REFERENCES Utente (cf)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    FOREIGN KEY (libro_isbn) REFERENCES Libro (isbn)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- 5. Tabella SANZIONE
CREATE TABLE IF NOT EXISTS Sanzione
(
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    prestito_id INTEGER NOT NULL,
    importo     REAL    DEFAULT 0.0,
    pagata      INTEGER DEFAULT 0,

    FOREIGN KEY (prestito_id) REFERENCES Prestito (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- DATI DI TEST
INSERT OR IGNORE INTO Bibliotecario (matricola, nome, cognome, username, password)
VALUES ('BIB001', 'Admin', 'Sistema', 'admin', 'admin');

INSERT OR IGNORE INTO Libro (isbn, titolo, autore, copie_totali, copie_disponibili)
VALUES ('978-8804668231', 'Il Nome della Rosa', 'Umberto Eco', 5, 5);

INSERT OR IGNORE INTO Utente (cf, nome, cognome)
VALUES ('RSSMRA80A01H501U', 'Mario', 'Rossi');