package com.easybook.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Foutih Osama 20054809
 */
class LibroTest {

    @Test
    void testCreazioneLibro() {
        Libro libro = new Libro("12345", "Java Programming", "Mario Rossi", 5, 5);

        assertEquals("12345", libro.getIsbn());
        assertEquals("Java Programming", libro.getTitolo());
        assertEquals(5, libro.getCopieDisponibili());
    }

    @Test
    void testModificaDisponibilita() {
        Libro libro = new Libro("12345", "Java Programming", "Mario Rossi", 5, 5);

        // Simulo un prestito
        libro.setCopieDisponibili(3);

        assertEquals(3, libro.getCopieDisponibili());
    }
}