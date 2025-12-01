# Documento dei Requisiti — Sistema Gestionale Biblioteca "EasyBook"

## 1. Requisiti funzionali 

| ID | Funzionalità | Descrizione | Criterio di accettazione |
| --- | --- | --- | --- |
| F01 | Catalogazione libri | Inserimento Titolo, Autore, ISBN, copie | Libro ricercabile; CopieDisponibili inizializzate |
| F02 | Consultazione catalogo (App) | Ricerca e visualizzazione stato (Disponibile se CopieDisponibili>0) | Stato coerente con DB |
| F03 | Visualizzazione prestiti | Area privata mostra prestiti e scadenze | Lista filtrata per ID_Utente |
| F04 | Erogazione prestito | Registrazione prestito al bancone con controlli (max 3, no ritardi, no multe) | Operazione bloccata se vincoli non rispettati; decremento CopieDisponibili |
| F05 | Restituzione | Registrazione rientro e aggiornamento disponibilità | DataRestituzione salvata; CopieDisponibili incrementato |
| F06 | Iscrizione notifiche | Utente si iscrive per avviso quando libro torna | Record in NotifichePendenti creato |
| F07 | Invio notifiche | Alla restituzione invio mail a iscritti e cancellazione richieste | Mail inviate e record rimossi |
| F08 | Gestione scadenze (batch) | Job notturno per preavviso e solleciti | Mail inviate su Scadenza-1, Scadenza+3, Scadenza+7 |
| F09 | Applicazione multe | Creazione/aggiornamento multe da 8° giorno di ritardo | Multa creata a Scadenza+8; aggiornamento successivo |
| F10 | Pagamento multe | Registrazione pagamento da bibliotecario | Stato multa → "Pagato" |

## 2. Requisiti non funzionali

| Categoria | Requisito | Specifica |
| --- | --- | --- |
| Prestazioni | Data Freshness | Aggiornamenti visibili su App entro 2s |
| Affidabilità | Transazioni ACID | Evitare prestiti > copie fisiche |
| Sicurezza | Protezione accessi | HTTPS + autenticazione token (JWT) |
| Scalabilità | Concorrenza | Supporto per ~500 utenti concorrenti |

## 3. Rischi
- Race condition sulle prenotazioni
- Email in spam .  
---  
