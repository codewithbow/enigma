package enigma;

import java.util.ArrayList;
import java.util.Arrays;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Bowie Lam
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        _length = _alphabet.size();

        String newCycles = cycles.replaceAll("\\s", "");
        newCycles = newCycles.replace("(", "");
        String[] holder = newCycles.split("\\)");
        _cyclesArrayList = new ArrayList<String>(Arrays.asList(holder));
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        _cyclesArrayList.addAll(Arrays.asList(cycle));
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _length;
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        char pChar = _alphabet.toChar(wrap(p));
        for (String string : _cyclesArrayList) {
            for (int i = 0; i < string.length(); i += 1) {
                if (string.charAt(i) == pChar) {
                    if (string.indexOf(string.charAt(i))
                            == string.length() - 1) {
                        return _alphabet.toInt(string.charAt(0));
                    }
                    return _alphabet.toInt(string.charAt(i + 1));
                }
            }
        }
        return p;
    }

    /** Return the result of applying the inverse of this permutation
     *  to C modulo the alphabet size. */
    int invert(int c) {
        char cChar = _alphabet.toChar(wrap(c));
        for (String string : _cyclesArrayList) {
            for (int i = string.length() - 1; i >= 0; i -= 1) {
                if (string.charAt(i) == cChar) {
                    if (string.indexOf(string.charAt(i)) == 0) {
                        char convertChar = string.charAt(string.length() - 1);
                        return _alphabet.toInt(convertChar);
                    }
                    return _alphabet.toInt(string.charAt(i - 1));
                }
            }
        }
        return c;
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) throws EnigmaException {
        return _alphabet.toChar(permute(_alphabet.toInt(p)));
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) throws EnigmaException {
        return _alphabet.toChar(invert(_alphabet.toInt(c)));
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        int count = 0;
        for (String eachString : _cyclesArrayList) {
            count += eachString.length();
        }
        return _length <= count;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** Length of this permutation. */
    private int _length;

    /** ArrayList of cycles. */
    private ArrayList<String> _cyclesArrayList;

}
