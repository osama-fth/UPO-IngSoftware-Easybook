-- Bibliotecari
INSERT INTO Bibliotecario (matricola, nome, cognome, username, password)
VALUES ('BIB001', 'Alessia', 'Ferrari', 'alessia.ferrari', 'Password123!');

-- Libri
INSERT INTO Libro (isbn, titolo, autore, copie_totali, copie_disponibili)
VALUES ('978-8804668231', 'Il Nome della Rosa', 'Umberto Eco', 5, 3),
       ('978-8806222413', '1984', 'George Orwell', 4, 4),
       ('978-8807901591', 'Il Piccolo Principe', 'Antoine de Saint-Exupéry', 10, 9),
       ('978-8811811651', 'Divina Commedia', 'Dante Alighieri', 3, 2),
       ('978-8804719137', 'Lo Hobbit', 'J.R.R. Tolkien', 6, 6),
       ('978-8869183157', 'Harry Potter e la Pietra Filosofale', 'J.K. Rowling', 8, 7);

-- Utenti
INSERT INTO Utente (cf, nome, cognome, stato, num_prestiti_attivi)
VALUES ('RSSMRA80A01H501U', 'Mario', 'Rossi', 'ATTIVO', 2),
       ('BNCGNN90B02F205Z', 'Giovanna', 'Bianchi', 'ATTIVO', 1),
       ('VRDLGU75C03L117X', 'Luigi', 'Verdi', 'SOSPESO', 0),
       ('FRNCST88D04H501Y', 'Francesca', 'Totti', 'ATTIVO', 0);

-- Prestiti
INSERT INTO Prestito (utente_cf, libro_isbn, data_inizio, data_scadenza, data_restituzione)
VALUES ('RSSMRA80A01H501U', '978-8804668231', '2024-01-10', '2024-01-25', NULL),
       ('RSSMRA80A01H501U', '978-8811811651', '2024-01-15', '2024-01-30', NULL),
       ('BNCGNN90B02F205Z', '978-8869183157', '2024-01-20', '2024-02-04', NULL);
