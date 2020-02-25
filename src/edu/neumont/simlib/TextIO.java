package edu.neumont.simlib;

public interface TextIO {
    boolean getBool(String testTrueRegex, String testFalseRegex);
    boolean getBoolPrefix(String prefix, String testTrueRegex, String testFalseRegex);
    boolean getYN();
    boolean getYNPrefix(String prefix);
    boolean getTF();
    boolean getTFPrefix(String prefix);

    int getInt();
    int getIntPrefix(String prefix);
    int getInt(int min, int max);
    int getIntPrefix(String prefix, int min, int max);

    double getDouble();
    double getDoublePrefix(String prefix);
    double getDouble(double min, double max);
    double getDoublePrefix(String prefix, double min, double max);

    char getChar();
    char getCharPrefix(String prefix);
    char getChar(String regex);
    char getCharPrefix(String prefix, String regex);

    String getString();
    String getStringPrefix(String prefix);
    String getString(String regex);
    String getStringPrefix(String prefix, String regex);
}

