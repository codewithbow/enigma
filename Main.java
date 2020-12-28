package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Bowie Lam
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }
        _config = getInput(args[0]);
        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }
        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        _m = readConfig();
        setUp(_m, _input.nextLine());
        while (_input.hasNextLine()) {
            String inputLine = _input.nextLine();
            if (inputLine.startsWith("*")) {
                setUp(_m, inputLine);
                try {
                    printMessageLine(_m.convert(_input.nextLine().replaceAll(
                            "\\s+", "")));
                } catch (NoSuchElementException excp) {
                    return;
                }
            } else {
                printMessageLine(_m.convert(inputLine.replaceAll("\\s+", "")));
            }
        }

    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            _alphabet = new Alphabet(_config.nextLine());
            String[] nums = _config.nextLine().trim().split("\\s+");
            if (nums.length != 2) {
                throw new EnigmaException("Rotor info not complete");
            }
            _numRotors = Integer.parseInt(nums[0]);
            _numPawls = Integer.parseInt(nums[1]);
            _allRotors = new ArrayList<>();
            while (_config.hasNext()) {
                _allRotors.add(readRotor());
            }
            return new Machine(_alphabet, _numRotors, _numPawls, _allRotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }


    /** Return a rotor, reading its description from _config.
     * Received regex guidance from fellow CS 61B classmate
     * Name: Ranelle Gomez SID: 3033691782
     * email: ranellegomez@berkeley.edu  */
    private Rotor readRotor() {
        try {
            String rotorName = _config.next();
            String typeAndNotches = _config.next();
            char rotorType = typeAndNotches.charAt(0);
            String notches = typeAndNotches.substring(1);
            String cycles = "";
            while (_config.hasNext("\\(.*")) {
                cycles += " " + _config.next("(\\([^\\)]*\\))*");
            }
            if (rotorType == 'R') {
                return new Reflector(rotorName,
                        new Permutation(cycles, _alphabet));
            } else if (rotorType == 'N') {
                return new FixedRotor(rotorName,
                        new Permutation(cycles, _alphabet));
            } else if (rotorType == 'M') {
                return new MovingRotor(rotorName,
                        new Permutation(cycles, _alphabet), notches);
            }
            throw new EnigmaException("Rotor type not found");
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }


    /** Set M according to the specification given in SETTINGS,
     *  which must have the format specified in the assignment.
     *  Received conceptual help from fellow CS 61B classmate
     *  Name: Ranelle Gomez SID: 3033691782
     *  email: ranellegomez@berkeley.edu  */
    private void setUp(Machine M, String settings) {
        String firstLine = settings;
        String[] firstLineArr = firstLine.split("\\s");
        if (!firstLineArr[0].equals("*")) {
            throw new EnigmaException("Asterisk not in first column");
        }
        String cycles = "";
        if (firstLineArr.length >= _numRotors + 2) {
            for (String eachString : firstLineArr) {
                if (eachString.charAt(0) == '(') {
                    cycles += " " + eachString;
                }
            }
            String[] rotorsArray = new String[_numRotors];
            for (int i = 0; i < _numRotors; i += 1) {
                rotorsArray[i] = firstLineArr[i + 1];
            }
            M.insertRotors(rotorsArray);
            M.setRotors(firstLineArr[_numRotors + 1]);
            M.setPlugboard(new Permutation(cycles, _alphabet));
        } else {
            String[] rotorsArray = new String[_numRotors];
            for (int i = 0; i < _numRotors; i += 1) {
                rotorsArray[i] = firstLineArr[i + 1];
            }
            M.insertRotors(rotorsArray);
            M.setRotors(firstLineArr[_numRotors + 1]);
            M.setPlugboard(new Permutation("", _alphabet));
        }
    }


    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        if (msg.length() == 0) {
            _output.println();
        } else if (msg.length() <= 5) {
            _output.println(msg);
        } else {
            _output.print(msg.substring(0, 5) + " ");
            printMessageLine(msg.substring(5));
        }
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;

    /** Collection of all rotors. */
    private Collection<Rotor> _allRotors;

    /** Number of rotors. */
    private int _numRotors;

    /** Number of pawls. */
    private int _numPawls;

    /** The machine. */
    private Machine _m;
}
