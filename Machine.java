package enigma;


import java.util.ArrayList;

import java.util.Collection;



/** Class that represents a complete enigma machine.
 *  @author Bowie Lam */

class Machine {
    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _numPawls = pawls;
        _allRotors = new Rotor[allRotors.size()];

        if (_numRotors <= 1) {
            throw new EnigmaException("Not enough rotor slots");
        }
        if (_numPawls < 0) {
            throw new EnigmaException("There are no pawls");
        }
        if (_numRotors <= _numPawls) {
            throw new EnigmaException("_numRotors is <= _numPawls");
        }
        int index = 0;
        for (Rotor eachRotor : allRotors) {
            _allRotors[index] = eachRotor;
            index += 1;
        }
        for (int x = 0; x < _allRotors.length; x += 1) {
            for (int y = x + 1; y < _allRotors.length; y += 1) {
                if (_allRotors[x].name().equals(_allRotors[y].name())) {
                    throw new EnigmaException("Duplicate rotor names found");
                }
            }
        }
    }


    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _numPawls;
    }


    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        for (Rotor eachRotor : _allRotors) {
            if (rotors[0].equals(eachRotor.name()) && !eachRotor.reflecting()) {
                throw new EnigmaException("First rotor not a Reflector");
            }
        }
        _myRotorSlots = new ArrayList<>();
        for (String eachRotorName : rotors) {
            for (Rotor eachRotor : _allRotors) {
                if (eachRotorName.equals(eachRotor.name())
                        && !_myRotorSlots.contains(eachRotor)) {
                    eachRotor.set(0);
                    _myRotorSlots.add(eachRotor);
                }
            }
        }
        int movingCounter = 0;
        for (Rotor eachRotor : _myRotorSlots) {
            if (eachRotor.rotates()) {
                movingCounter += 1;
            }
        }
        if (movingCounter > _numPawls) {
            throw new EnigmaException("More moving rotors than pawls");
        }
        int counter = 0;
        for (int i = 0; i < _myRotorSlots.size(); i += 1) {
            if (_myRotorSlots.get(i).reflecting()) {
                counter += 1;
            }
        }
        if (counter != 1) {
            throw new EnigmaException("Not exactly 1 reflector");
        }
        if (!_myRotorSlots.get(0).reflecting()) {
            throw new EnigmaException("Reflector not in 0th position");
        }
        if (rotors.length != _myRotorSlots.size()) {
            throw new EnigmaException("Not all rotor names in my rotor slots");
        }
    }


    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        if (setting.length() != numRotors() - 1) {
            throw new EnigmaException("Incorrect length.");
        }
        for (int i = 0; i < setting.length(); i += 1) {
            if (!_alphabet.contains(setting.charAt(i))) {
                throw new EnigmaException("Character not in alphabet");
            }
        }
        for (int i = 1; i < _myRotorSlots.size(); i += 1) {
            char charSetting = setting.charAt(i - 1);
            _myRotorSlots.get(i).set(charSetting);
        }
    }


    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % _alphabet.size();
        if (r < 0) {
            r += _alphabet.size();
        }
        return r;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine.
     *  Received conceptual help from fellow CS 61B classmate
     *  Name: Ranelle Gomez SID: 3033691782
     *  email: ranellegomez@berkeley.edu  */
    int convert(int c) {
        for (int i = _myRotorSlots.size() - 1; i > 0; i -= 1) {
            if (_myRotorSlots.get(i).rotates()
                    && _myRotorSlots.get(i).atNotch()
                    && _myRotorSlots.get(i - 1).rotates()) {
                if (_myRotorSlots.get(i - 1).atNotch()
                        && _myRotorSlots.get(i - 2).rotates()) {
                    _myRotorSlots.get(i - 2).advance();
                }
                _myRotorSlots.get(i - 1).advance();
                if (_myRotorSlots.get(i)
                        != _myRotorSlots.get(_myRotorSlots.size() - 1)) {
                    _myRotorSlots.get(i).advance();
                }
            }
        }
        _myRotorSlots.get(_myRotorSlots.size() - 1).advance();
        int result = _plugboard.permute(c);
        for (int x = _myRotorSlots.size() - 1; x >= 0; x -= 1) {
            result = _myRotorSlots.get(x).convertForward(result);
        }
        for (int y = 1; y < _myRotorSlots.size(); y += 1) {
            result = _myRotorSlots.get(y).convertBackward(result);
        }
        return _plugboard.permute(result);
    }


    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        char[] msgArray = new char[msg.length()];
        for (int i = 0; i < msg.length(); i += 1) {
            int charIndex = _alphabet.toInt(msg.charAt(i));
            char convertedChar = _alphabet.toChar(convert(charIndex));
            msgArray[i] = convertedChar;
        }
        return new String(msgArray);
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** Number of rotors. */
    private int _numRotors;

    /** Number of pawls. */
    private int _numPawls;

    /** An arraylist of rotors. */
    private ArrayList<Rotor> _myRotorSlots;

    /** The plugboard. */
    private Permutation _plugboard;

    /** An array of rotors. */
    private Rotor[] _allRotors;

}
