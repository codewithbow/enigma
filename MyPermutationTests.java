package enigma;

import static org.junit.Assert.*;

import org.junit.Test;

public class MyPermutationTests {

    @Test
    public void testPermutationSize() {
        Permutation a = new Permutation("(ZWYX)", new Alphabet("WXYZ"));
        assertEquals(4, a.size());

        Permutation d = new Permutation("(CAB) (FED)", new Alphabet("ABCDEF"));
        assertEquals(6, d.size());
    }

    @Test(expected = EnigmaException.class)
    public void testInvalidPermutationSize() {
        Permutation b = new Permutation("(XYZ)", new Alphabet(""));

        Permutation c = new Permutation("(XYZ)", new Alphabet(" "));

        Permutation f = new Permutation("(XYZ)", new Alphabet("AB*C"));
    }

    @Test
    public void testPermuteInt() {

        Permutation a = new Permutation("(BACD)", new Alphabet("ABCDE"));
        assertEquals(2, a.permute(0));
        assertEquals(0, a.permute(1));
        assertEquals(3, a.permute(2));
        assertEquals(1, a.permute(3));
        assertEquals(4, a.permute(4));

        Permutation b = new Permutation("(BAC) (ED) (F)",
                new Alphabet("ABCDEFG"));
        assertEquals(2, b.permute(0));
        assertEquals(0, b.permute(1));
        assertEquals(1, b.permute(2));
        assertEquals(4, b.permute(3));
        assertEquals(3, b.permute(4));
        assertEquals(5, b.permute(5));
        assertEquals(6, b.permute(6));

        Permutation c = new Permutation("(BAC)", new Alphabet("ABC"));
        assertEquals(0, c.permute(-2));

        Permutation d = new Permutation("(BAC)", new Alphabet("ABC"));
        assertEquals(1, d.permute(8));

        Permutation e = new Permutation("(CAB) (ED) (F)",
                new Alphabet("ABCDEF"));
        assertEquals(3, e.permute(-20));

        Permutation f = new Permutation("(CAB) (ED) (F)",
                new Alphabet("ABCDEF"));
        assertEquals(0, f.permute(20));

    }

    @Test
    public void testInvertInt() {

        Permutation a = new Permutation("(ZWYX)", new Alphabet("WXYZA"));
        assertEquals(3, a.invert(0));
        assertEquals(2, a.invert(1));
        assertEquals(0, a.invert(2));
        assertEquals(1, a.invert(3));
        assertEquals(4, a.invert(4));

        Permutation b = new Permutation("(CBA) (ED) (F)",
                new Alphabet("ABCDEFG"));
        assertEquals(1, b.invert(0));
        assertEquals(2, b.invert(1));
        assertEquals(0, b.invert(2));
        assertEquals(4, b.invert(3));
        assertEquals(3, b.invert(4));
        assertEquals(5, b.invert(5));
        assertEquals(6, b.invert(6));

        Permutation c = new Permutation("(BAC)", new Alphabet("ABC"));
        assertEquals(2, c.invert(-2));

        Permutation d = new Permutation("(BAC)", new Alphabet("ABC"));
        assertEquals(0, d.invert(8));

        Permutation e = new Permutation("(CAB) (ED) (F)",
                new Alphabet("ABCDEF"));
        assertEquals(3, e.invert(-20));


        Permutation f = new Permutation("(CAB) (ED) (F)",
                new Alphabet("ABCDEF"));
        assertEquals(1, f.invert(20));

    }

    @Test
    public void testPermuteChar() {

        Permutation a = new Permutation("(ZWYX)", new Alphabet("WXYZA"));
        assertEquals('Y', a.permute('W'));
        assertEquals('Z', a.permute('X'));
        assertEquals('X', a.permute('Y'));
        assertEquals('W', a.permute('Z'));
        assertEquals('A', a.permute('A'));

        Permutation c = new Permutation("(BAC) (ED) (F)",
                new Alphabet("ABCDEFG"));
        assertEquals('C', c.permute('A'));
        assertEquals('A', c.permute('B'));
        assertEquals('B', c.permute('C'));
        assertEquals('E', c.permute('D'));
        assertEquals('D', c.permute('E'));
        assertEquals('F', c.permute('F'));
        assertEquals('G', c.permute('G'));

    }


    @Test
    public void testInvertChar() {

        Permutation a = new Permutation("(ZWYX)", new Alphabet("WXYZA"));
        assertEquals('Z', a.invert('W'));
        assertEquals('Y', a.invert('X'));
        assertEquals('W', a.invert('Y'));
        assertEquals('X', a.invert('Z'));
        assertEquals('A', a.invert('A'));

        Permutation c = new Permutation("(CBA) (ED) (F)",
                new Alphabet("ABCDEFG"));
        assertEquals('B', c.invert('A'));
        assertEquals('C', c.invert('B'));
        assertEquals('A', c.invert('C'));
        assertEquals('E', c.invert('D'));
        assertEquals('D', c.invert('E'));
        assertEquals('F', c.invert('F'));
        assertEquals('G', c.invert('G'));


    }

    @Test
    public void testDerangement() {
        Permutation a = new Permutation("(ABC)", new Alphabet("ABC"));
        assertTrue(a.derangement());
        Permutation b = new Permutation("(ABC)", new Alphabet("ABCD"));
        assertFalse(b.derangement());

    }

}
