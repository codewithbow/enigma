package enigma;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class MyAlphabetTests {

    @Test
    public void testAlphabetSize() {
        Alphabet e = new Alphabet("A");
        assertEquals(1, e.size());

        Alphabet f = new Alphabet("ABC");
        assertEquals(3, f.size());
    }

    @Test(expected = EnigmaException.class)
    public void testInvalidAlphabetSize() {
        Alphabet a = new Alphabet("");
        a.size();

        Alphabet b = new Alphabet(" ");
        b.size();

        Alphabet c = new Alphabet("AB C");
        c.size();

        Alphabet d = new Alphabet("AB*C");
        d.size();
    }

    @Test
    public void testContains() {
        Alphabet a = new Alphabet("ABCDEF");
        assertTrue(a.contains('A'));
        assertFalse(a.contains('G'));

        Alphabet b = new Alphabet("");
        b.contains('Z');
        b.contains('*');
    }

    @Test
    public void testToChar() {
        Alphabet a = new Alphabet("ABCDEF");
        assertEquals('A', a.toChar(0));
    }

    @Test(expected = EnigmaException.class)
    public void testInvalidToChar() {
        Alphabet a = new Alphabet("ABCDEF");
        a.toChar(-1);
        a.toChar(a.size() + 1);
        a.toChar(-100);
        a.toChar(100);
    }

    @Test
    public void testToInt() {
        Alphabet a = new Alphabet("ABCDEF");
        assertEquals(0, a.toInt('A'));
        a.toInt('G');
    }

}
