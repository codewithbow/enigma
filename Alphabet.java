package enigma;

import java.util.HashSet;
import java.util.Set;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Bowie Lam
 */
class Alphabet {

    /** A new alphabet containing CHARS.  Character number #k has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {
        _chars = chars;
        _length = _chars.length();

        String validAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789"
                + "abcdefghijklmnopqrstuvwxyz_.";

        for (int i = 0; i < _length; i += 1) {
            if (validAlphabet.indexOf(_chars.charAt(i)) == -1) {
                throw new EnigmaException("Input contains invalid characters");
            }
        }

        char[] charArray = _chars.toCharArray();
        Set<Character> set = new HashSet<>();
        for (char each : charArray) {
            if (!set.add(each)) {
                throw new EnigmaException("Duplicate character found");
            }
        }
    }

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /** Returns the size of the alphabet. */
    int size() {
        return _length;
    }

    /** Returns true if CH is in this alphabet. */
    boolean contains(char ch) {
        for (int i = 0; i < size(); i += 1) {
            if (_chars.charAt(i) == ch) {
                return true;
            }
        }
        return false;
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        if (index >= 0 && index <= size()) {
            return _chars.charAt(index);
        }
        throw new EnigmaException("Index not within 0 and size()");
    }

    /** Returns the index of character CH which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {
        if (_chars.indexOf(ch) <= 0 && _chars.indexOf(ch) >= size()) {
            throw new EnigmaException("Character not in alphabet");
        }
        return _chars.indexOf(ch);
    }

    /** String of alphabet. */
    private String _chars;

    /** Length of _chars. */
    private int _length;
}
