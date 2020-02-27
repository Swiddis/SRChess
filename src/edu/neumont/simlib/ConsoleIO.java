package edu.neumont.simlib;

import java.io.BufferedReader;

public class ConsoleIO extends BufferedIO {
    public ConsoleIO() {
        super();
    }

    public ConsoleIO(BufferedReader r) {
        super(r);
    }

    public void displayText(String s) {
        System.out.print(s);
    }
    public void displayLine(String s) {
        System.out.println(s);
    }
    public void displayErr(String s) {
        displayLine(s);
    }
}
