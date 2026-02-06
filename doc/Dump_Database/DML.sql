-- 1. BIBLIOTECARI
INSERT INTO Bibliotecario (matricola, nome, cognome, username, password)
VALUES ('BIB001', 'Osama', 'Foutih', 'osama.foutih', 'osama123'),
       ('BIB002', 'Lorenzo', 'Bellotti', 'lorenzo.bellotti', 'lorenzo123'),
       ('BIB003', 'Riccardo', 'Negrini', 'riccardo.negrini', 'riccardo123');

-- 2. UTENTI
INSERT INTO Utente (cf, nome, cognome, stato, num_prestiti_attivi)
VALUES ('RSSMRA80A01H501U', 'Mario', 'Rossi', 'ATTIVO', 1),
       ('VRDLCU90B02F205Z', 'Luca', 'Verdi', 'ATTIVO', 0),
       ('BNCGLU95C43H501T', 'Giulia', 'Bianchi', 'ATTIVO', 2),
       ('FRRMTT88D15L219K', 'Matteo', 'Ferrari', 'ATTIVO', 1),
       ('ESPANT92E56A001Q', 'Anita', 'Esposito', 'SOSPESO', 0),
       ('RMNLSS85M45H501A', 'Alessia', 'Romano', 'ATTIVO', 0),
       ('CLMBR99R12F205J', 'Bruno', 'Colombo', 'ATTIVO', 1),
       ('RICFRA75T22L219P', 'Francesca', 'Ricci', 'ATTIVO', 0),
       ('MRNMTT82S10H501L', 'Mattia', 'Marino', 'ATTIVO', 0),
       ('GRGREC91P55F205N', 'Rebecca', 'Greco', 'ATTIVO', 1);

-- 3. LIBRI
INSERT INTO Libro (isbn, titolo, autore, copie_totali, copie_disponibili)
VALUES ('9788804668237', '1984', 'George Orwell', 5, 3),
       ('9788807900365', 'Il nome della rosa', 'Umberto Eco', 3, 2),
       ('9788806218034', 'Il giovane Holden', 'J.D. Salinger', 4, 4),
       ('9780545010221', 'Harry Potter e i doni della morte', 'J.K. Rowling', 10, 8),
       ('9788804616979', 'Il Signore degli Anelli', 'J.R.R. Tolkien', 2, 1),
       ('9788817124355', 'Dieci piccoli indiani', 'Agatha Christie', 6, 6),
       ('9788806143042', 'Calvino: Lezioni Americane', 'Italo Calvino', 3, 3),
       ('9788845292613', 'Il codice da Vinci', 'Dan Brown', 5, 5),
       ('9788807018381', 'Cecità', 'José Saramago', 4, 4),
       ('9788806225964', 'Fahrenheit 451', 'Ray Bradbury', 5, 5);

-- 4. PRESTITI
INSERT INTO Prestito (id, utente_cf, libro_isbn, data_inizio, data_scadenza, data_restituzione)
VALUES (1, 'RSSMRA80A01H501U', '9788806218034', '2025-09-01', '2025-10-01', '2025-09-28'),
       (2, 'VRDLCU90B02F205Z', '9788817124355', '2025-11-10', '2025-12-10', '2025-12-05'),
       (3, 'ESPANT92E56A001Q', '9788804616979', '2025-10-01', '2025-11-01', '2025-11-15'),
       (4, 'VRDLCU90B02F205Z', '9788804668237', '2025-12-01', '2026-01-01', '2026-01-04'),
       (5, 'RICFRA75T22L219P', '9780545010221', '2025-12-15', '2026-01-15', '2026-01-24'),
       (6, 'RSSMRA80A01H501U', '9788804668237', '2026-01-25', '2026-02-25', NULL),
       (7, 'BNCGLU95C43H501T', '9788804668237', '2026-01-28', '2026-02-28', NULL),
       (8, 'BNCGLU95C43H501T', '9788807900365', '2026-02-02', '2026-03-02', NULL),
       (9, 'FRRMTT88D15L219K', '9780545010221', '2026-02-05', '2026-03-05', NULL),
       (10, 'CLMBR99R12F205J', '9780545010221', '2026-02-10', '2026-03-10', NULL),
       (11, 'GRGREC91P55F205N', '9788804616979', '2026-02-14', '2026-03-14', NULL);

-- 5. SANZIONI
INSERT INTO Sanzione (id, prestito_id, importo, pagata)
VALUES (1, 3, 13.50, 0),
       (2, 4, 10.00, 1),
       (3, 5, 11.00, 1);
