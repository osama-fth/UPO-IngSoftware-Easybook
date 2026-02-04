package com.easybook;

import com.easybook.model.Libro;
import com.easybook.model.Sanzione;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Riccardo Negrini 20054675
 */

public class SanzioneTest {
    @Test
    void testCreazioneSanzione() {
        Sanzione sanzione=new Sanzione(1, 10.5);

        assertEquals(1, sanzione.getIdprestito());
        assertEquals(10.5, sanzione.getImporto());
        assertEquals(0, sanzione.getPagata());
    }
}
