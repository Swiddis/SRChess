package edu.neumont.simlib;

public class ConsoleIO extends BufferedIO {
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
