-- 1. BIBLIOTECARI
INSERT INTO Bibliotecario (matricola, nome, cognome, username, password)
VALUES ('BIB001', 'Osama', 'Foutih', 'osama.foutih', 'osama123'),
       ('BIB002', 'Lorenzo', 'Bellotti', 'lorenzo.bellotti', 'lorenzo123'),
       ('BIB003', 'Riccardo', 'Negrini', 'riccardo.negrini', 'riccardo123'),
       ('BIB004', 'Admin', 'Sistema', 'admin', 'admin');

-- 2. UTENTI
INSERT INTO Utente (cf, nome, cognome, stato, num_prestiti_attivi)
VALUES ('RSSMRA80A01H501U', 'Mario', 'Rossi', 'ATTIVO', 2),
       ('VRDLCU90B02F205Z', 'Luca', 'Verdi', 'ATTIVO', 0),
       ('BNCGLU95C43H501T', 'Giulia', 'Bianchi', 'ATTIVO', 2),
       ('FRRMTT88D15L219K', 'Matteo', 'Ferrari', 'ATTIVO', 1),
       ('ESPANT92E56A001Q', 'Anita', 'Esposito', 'SOSPESO', 1),
       ('RMNLSS85M45H501A', 'Alessia', 'Romano', 'ATTIVO', 0),
       ('CLMBR99R12F205J', 'Bruno', 'Colombo', 'ATTIVO', 1),
       ('RICFRA75T22L219P', 'Francesca', 'Ricci', 'ATTIVO', 0),
       ('MRNMTT82S10H501L', 'Mattia', 'Marino', 'ATTIVO', 3),
       ('GRGREC91P55F205N', 'Rebecca', 'Greco', 'ATTIVO', 0),
       ('GLLLRZ93A01H501O', 'Lorenzo', 'Gallo', 'ATTIVO', 1),
       ('CNTFRC88B02L219I', 'Federico', 'Conti', 'ATTIVO', 0),
       ('DLCGNN85C03F205M', 'Giovanni', 'De Luca', 'ATTIVO', 0),
       ('CSTVLR92D04H501E', 'Valeria', 'Costa', 'SOSPESO', 1),
       ('BRNAND90E05L219U', 'Andrea', 'Bruno', 'ATTIVO', 2),
       ('FNTLNE88F06A001R', 'Elena', 'Fontana', 'ATTIVO', 1),
       ('MLNMAR95G07H501D', 'Marco', 'Molinari', 'ATTIVO', 0),
       ('RZZLRA91H08F205K', 'Laura', 'Rizzo', 'ATTIVO', 1),
       ('MRTLRI85I09L219X', 'Ilaria', 'Moretti', 'ATTIVO', 0),
       ('GRSMNL80L10A001V', 'Emanuele', 'Grassi', 'ATTIVO', 0);

-- 3. LIBRI
INSERT INTO Libro (isbn, titolo, autore, copie_totali, copie_disponibili)
VALUES ('9788804668237', '1984', 'George Orwell', 5, 2),
       ('9788807900365', 'Il nome della rosa', 'Umberto Eco', 3, 1),
       ('9788806218034', 'Il giovane Holden', 'J.D. Salinger', 4, 4),
       ('9780545010221', 'Harry Potter e i doni della morte', 'J.K. Rowling', 10, 7),
       ('9788804616979', 'Il Signore degli Anelli', 'J.R.R. Tolkien', 2, 0),
       ('9788817124355', 'Dieci piccoli indiani', 'Agatha Christie', 6, 6),
       ('9788806143042', 'Calvino: Lezioni Americane', 'Italo Calvino', 3, 3),
       ('9788845292613', 'Il codice da Vinci', 'Dan Brown', 5, 4),
       ('9788807018381', 'Cecità', 'José Saramago', 4, 3),
       ('9788806225964', 'Fahrenheit 451', 'Ray Bradbury', 5, 5),
       ('9788806223533', 'Lo Hobbit', 'J.R.R. Tolkien', 3, 1),
       ('9788804675433', 'Delitto e Castigo', 'Fëdor Dostoevskij', 2, 2),
       ('9788807901591', 'Moby Dick', 'Herman Melville', 4, 3),
       ('9788806203559', 'Il barone rampante', 'Italo Calvino', 3, 3),
       ('9788817107464', 'Orgoglio e pregiudizio', 'Jane Austen', 5, 4),
       ('9788806179133', 'Se questo è un uomo', 'Primo Levi', 6, 6),
       ('9788804616337', 'Odissea', 'Omero', 2, 0),
       ('9788807902109', 'Cime tempestose', 'Emily Brontë', 4, 4),
       ('9788817013345', 'L''ombra del vento', 'Carlos Ruiz Zafón', 5, 4),
       ('9788806214739', 'La coscienza di Zeno', 'Italo Svevo', 3, 3);

-- 4. PRESTITI
INSERT INTO Prestito (id, utente_cf, libro_isbn, data_inizio, data_scadenza, data_restituzione)
VALUES (1, 'RSSMRA80A01H501U', '9788806218034', '2025-09-01', '2025-10-01', '2025-09-28'),
       (2, 'VRDLCU90B02F205Z', '9788817124355', '2025-11-10', '2025-12-10', '2025-12-05'),
       (3, 'RMNLSS85M45H501A', '9788845292613', '2025-12-10', '2026-01-09', '2026-01-05'),
       (4, 'VRDLCU90B02F205Z', '9788804668237', '2025-11-15', '2025-12-15', '2025-12-19'),
       (5, 'RICFRA75T22L219P', '9780545010221', '2025-12-15', '2026-01-14', '2026-01-24'),

       (6, 'BNCGLU95C43H501T', '9788804668237', '2026-01-20', '2026-02-20', NULL),
       (7, 'FRRMTT88D15L219K', '9780545010221', '2026-02-01', '2026-03-03', NULL),
       (8, 'CLMBR99R12F205J', '9780545010221', '2026-02-05', '2026-03-07', NULL),
       (9, 'RSSMRA80A01H501U', '9788804668237', '2026-01-25', '2026-02-24', NULL),
       (10, 'RSSMRA80A01H501U', '9788804616979', '2026-01-30', '2026-03-01', NULL),
       (11, 'BNCGLU95C43H501T', '9788807900365', '2026-02-02', '2026-03-04', NULL),
       (12, 'GLLLRZ93A01H501O', '9788845292613', '2026-01-15', '2026-02-14', NULL),
       (13, 'BRNAND90E05L219U', '9788817107464', '2026-01-20', '2026-02-19', NULL),
       (14, 'BRNAND90E05L219U', '9788817013345', '2026-02-01', '2026-03-03', NULL),
       (15, 'MRNMTT82S10H501L', '9788807018381', '2026-01-10', '2026-02-09', NULL),
       (16, 'MRNMTT82S10H501L', '9788806223533', '2026-01-15', '2026-02-14', NULL),
       (17, 'MRNMTT82S10H501L', '9788807901591', '2026-01-20', '2026-02-19', NULL),
       (18, 'FNTLNE88F06A001R', '9788804616337', '2026-02-01', '2026-03-03', NULL),
       (19, 'RZZLRA91H08F205K', '9788806223533', '2026-02-03', '2026-03-05', NULL),
       (20, 'ESPANT92E56A001Q', '9788804616979', '2025-12-21', '2026-01-20', NULL),
       (21, 'CSTVLR92D04H501E', '9788804616337', '2026-01-02', '2026-02-01', NULL);

-- 5. SANZIONI
INSERT INTO Sanzione (id, prestito_id, importo, pagata)
VALUES (1, 4, 10.00, 1),
       (2, 5, 11.50, 1),
       (3, 20, 15.50, 0),
       (4, 21, 10.00, 0);