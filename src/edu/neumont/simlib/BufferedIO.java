package edu.neumont.simlib;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

/**
 * This is a standard edu.neumont.simlib.ConsoleIO Class that can be used to yield various types of console input from a user.
 * This was made because I was tired of rewriting the code every time, so I decided to make a special class specifically
 * for it.
 *
 * @author Simeon Widdis
 */
public abstract class BufferedIO implements TextIO {
    BufferedReader reader;

    public abstract void displayText(String s);
    public abstract void displayLine(String s);
    public abstract void displayErr(String s);

    public BufferedIO() {
        reader = new BufferedReader(new InputStreamReader(System.in));
    };

    public BufferedIO(BufferedReader r) {
        reader = r;
    }

    /**
     * Will parse a boolean expression as input from the user. Continually queries until valid input given.
     *
     * @param testTrueRegex If the input matches this regex, it will return true.
     * @param testFalseRegex If the input didn't match testTrueRegex but matched this, it will return false.
     * @return A boolean value based on the user's input.
     */
    @Override
    public boolean getBool(String testTrueRegex, String testFalseRegex) {
        return getBoolPrefix(null, testTrueRegex, testFalseRegex);
    }

    /**
     * Will parse a boolean expression as input from the user. Continually queries until valid input given.
     *
     * @param prefix Will use this to query user. A prefix of "Hello" will ask "Hello" every iteration.
     * @param testTrueRegex If the input matches this regex, it will return true.
     * @param testFalseRegex If the input didn't match testTrueRegex but matched this, it will return false.
     * @return A boolean value based on the user's input.
     */
    @Override
    public boolean getBoolPrefix(String prefix, String testTrueRegex, String testFalseRegex) {
        String bStr; // The raw input to be parsed
        boolean b = false; // Input boolean
        boolean exit = false; // Exit condition for the input loop
        do {
            exit = true;
            if (prefix != null) displayText(prefix);
            try {
                bStr = reader.readLine();
                if (Pattern.matches(testTrueRegex, bStr)) {
                    b = true;
                } else if (Pattern.matches(testFalseRegex, bStr)) {
                    b = false;
                } else {
                    exit = false;
                    displayErr("Sorry, I didn't understand your input.");
                }
            } catch (IOException e) {
                exit = false;
                displayErr("A problem occurred when handling your input.");
            }
        } while (!exit);
        return b;
    }

    /**
     * A special case  of boolean input that is specialized for Yes-or-No type input.
     *
     * @return A boolean value based on the user's input.
     */
    @Override
    public boolean getYN() {
        return getYNPrefix(null);
    }

    /**
     * A special case  of boolean input that is specialized for Yes-or-No type input.
     *
     * @param prefix Will use this to query user. A prefix of "Hello" will ask "Hello" every iteration.
     * @return A boolean value based on the user's input.
     */
    @Override
    public boolean getYNPrefix(String prefix) {
        // True regex checks for Yes or Y, False checks for No or N
        return getBoolPrefix(prefix, "^\\s*[Yy][Ee]?[Ss]?\\s*$", "\\s*[Nn][Oo]?\\s*$");
    }

    /**
     * A special case  of boolean input that is specialized for True-or-False type input.
     *
     * @return A boolean value based on the user's input.
     */
    @Override
    public boolean getTF() {
        return getTFPrefix(null);
    }

    /**
     * A special case  of boolean input that is specialized for True-or-False type input.
     *
     * @param prefix Will use this to query user. A prefix of "Hello" will ask "Hello" every iteration.
     * @return A boolean value based on the user's input.
     */
    public boolean getTFPrefix(String prefix) {
        return getBoolPrefix(prefix, "^\\s*[Tt][Rr]?[Uu]?[Ee]?\\s*$",
                "^\\s*[Ff][Aa]?[Ll]?[Ss]?[Ee]?\\s*$");
    }

    /**
     * Will parse an integer value as input from the user. Continually queries until valid input given.
     *
     * @return An integer value based on the user's input.
     */
    @Override
    public int getInt() {
        return this.getIntPrefix(null, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    /**
     * Will parse an integer value as input from the user. Continually queries until valid input given.
     *
     * @param prefix Will use this to query user. A prefix of "Hello" will ask "Hello" every iteration.
     * @return An integer value based on the user's input.
     */
    @Override
    public int getIntPrefix(String prefix) {
        return this.getIntPrefix(prefix, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    /**
     * Will parse an integer value as input from the user. Continually queries until valid input given.
     *
     * @param min The minimal acceptable value
     * @param max The maximal acceptable value
     * @return An integer value based on the user's input.
     */
    @Override
    public int getInt(int min, int max) {
        return this.getIntPrefix(null, min, max);
    }

    /**
     * Will parse an integer value as input from the user. Continually queries until valid input given.
     *
     * @param min The minimal acceptable value
     * @param max The maximal acceptable value
     * @param prefix Will use this to query user. A prefix of "Hello" will ask "Hello" every iteration.
     * @return An integer value based on the user's input.
     */
    @Override
    public int getIntPrefix(String prefix, int min, int max) {
        String nStr; // Raw input to parse
        long n = 0; // Input number (as a long to detect certain types of errors involving size)
        boolean exit = false; // Exit condition for the input loop
        do {
            exit = true;
            if (prefix != null) displayText(prefix);
            try {
                nStr = reader.readLine();
                n = Long.parseLong(nStr);
                if (n < min) {
                    exit = false;
                    displayErr("The number must be greater than or equal to " + min + ".");
                } else if (n > max) {
                    exit = false;
                    displayErr("The number must be less than or equal to " + max + ".");
                }
            } catch (NumberFormatException e) {
                exit = false;
                // This also catches numbers outside the range of longs, for now this will be ignored.
                displayErr("Your input wasn't a valid integer.");
            } catch (IOException e) {
                exit = false;
                displayErr("A problem occurred when handling your input.");
            }
        } while (!exit);
        return (int) n;
    }

    /**
     * Will parse a double value as input from the user. Continually queries until valid input given.
     *
     * @return A double value based on the user's input.
     */
    @Override
    public double getDouble() {
        return this.getDoublePrefix(null, Double.MIN_VALUE, Double.MAX_VALUE);
    }

    /**
     * Will parse a double value as input from the user. Continually queries until valid input given.
     *
     * @param prefix Will use this to query user. A prefix of "Hello" will ask "Hello" every iteration.
     * @return A double value based on the user's input.
     */
    @Override
    public double getDoublePrefix(String prefix) {
        return this.getDoublePrefix(null, Double.MIN_VALUE, Double.MAX_VALUE);
    }

    /**
     * Will parse a double value as input from the user. Continually queries until valid input given.
     *
     * @param min The minimal acceptable value
     * @param max The maximal acceptable value
     * @return A double value based on the user's input.
     */
    @Override
    public double getDouble(double min, double max) {
        return this.getDoublePrefix(null, min, max);
    }

    /**
     * Will parse a double value as input from the user. Continually queries until valid input given.
     *
     * @param min The minimal acceptable value
     * @param max The maximal acceptable value
     * @param prefix Will use this to query user. A prefix of "Hello" will ask "Hello" every iteration.
     * @return A double value based on the user's input.
     */
    @Override
    public double getDoublePrefix(String prefix, double min, double max) {
        String dStr; // Raw input to be parsed
        double d = 0; // Input double
        boolean exit = false; // Exit condition for the input loop
        do {
            exit = true;
            if (prefix != null) displayText(prefix);
            try {
                dStr = reader.readLine();
                d = Double.parseDouble(dStr);
                if (d < min) {
                    exit = false;
                    displayErr("The number must be greater than or equal to " + min + ".");
                } else if (d > max) {
                    exit = false;
                    displayErr("The number must be less than or equal to " + max + ".");
                }
            } catch (NumberFormatException e) {
                exit = false;
                displayErr("Your input wasn't a valid number.");
            } catch (IOException e) {
                exit = false;
                displayErr("A problem occurred when handling your input.");
            }
        } while (!exit);
        return d;
    }

    /**
     * Will parse a character value as input from the user. Continually queries until valid input given.
     *
     * @return A character value based on the user's input.
     */
    @Override
    public char getChar() {
        return getCharPrefix(null, ".*");
    }

    /**
     * Will parse a character value as input from the user. Continually queries until valid input given.
     *
     * @param prefix Will use this to query user. A prefix of "Hello" will ask "Hello" every iteration.
     * @return A character value based on the user's input.
     */
    @Override
    public char getCharPrefix(String prefix) {
        return getCharPrefix(prefix, ".*");
    }

    /**
     * Will parse a character value as input from the user. Continually queries until valid input given.
     *
     * @param regex Will only accept a character input that matches the regex.
     * @return A character value based on the user's input.
     */
    @Override
    public char getChar(String regex) {
        return getCharPrefix(null, regex);
    }

    /**
     * Will parse a character value as input from the user. Continually queries until valid input given.
     *
     * @param regex Will only accept a character input that matches the regex.
     * @param prefix Will use this to query user. A prefix of "Hello" will ask "Hello" every iteration.
     * @return A character value based on the user's input.
     */
    @Override
    public char getCharPrefix(String prefix, String regex) {
        String cStr = ""; // Raw input to parse, should only hold a character.
        char c = '\0'; // Character input
        boolean exit = false; // Exit condition for the input loop
        do {
            exit = true;
            if (prefix != null) displayText(prefix);
            try {
                cStr = reader.readLine();
                if (cStr.length() > 1) {
                    exit = false;
                    displayErr("The input was too long.");
                } else if (cStr.length() == 0) {
                    exit = false;
                    displayErr("The input was empty.");
                } else if (!Pattern.matches(regex, cStr)) {
                    exit = false;
                    displayErr("Sorry, I didn't understand your input.");
                } else {
                    c = cStr.charAt(0);
                }
            } catch (IOException e) {
                exit = false;
                displayErr("A problem occurred when handling your input.");
            }
        } while (!exit);
        return c;
    }

    /**
     * Will parse a string value as input from the user. Continually queries until valid input given.
     *
     * @return A string value based on the user's input.
     */
    @Override
    public String getString() {
        // This regex matches anything.
        return getStringPrefix(null, ".*");
    }

    /**
     * Will parse a string value as input from the user. Continually queries until valid input given.
     *
     * @param prefix Will use this to query user. A prefix of "Hello" will ask "Hello" every iteration.
     * @return A string value based on the user's input.
     */
    @Override
    public String getStringPrefix(String prefix) {
        return getStringPrefix(prefix, ".*");
    }

    /**
     * Will parse a string value as input from the user. Continually queries until valid input given.
     *
     * @param regex Will only accept a string input that matches the regex.
     * @return A string value based on the user's input.
     */
    @Override
    public String getString(String regex) {
        return getStringPrefix(null, regex);
    }

    /**
     * Will parse a string value as input from the user. Continually queries until valid input given.
     *
     * @param regex Will only accept a string input that matches the regex.
     * @param prefix Will use this to query user. A prefix of "Hello" will ask "Hello" every iteration.
     * @return A string value based on the user's input.
     */
    @Override
    public String getStringPrefix(String prefix, String regex) {
        String str = ""; // Raw string input
        boolean exit = false; // Exit condition for the input loop
        do {
            exit = true;
            if (prefix != null) displayText(prefix);
            try {
                str = reader.readLine();
                if (!Pattern.matches(regex, str)) {
                    exit = false;
                    displayErr("Sorry, I didn't understand your input.");
                }
            } catch (IOException e) {
                exit = false;
                displayErr("A problem occurred when handling your input.");
            }
        } while (!exit);
        return str;
    }
}
