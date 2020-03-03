package widdis.unroe.chess.ai;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Scanner;

// Communicates with the Stockfish UCI Engine: https://stockfishchess.org/
public class Stockfish implements AutoCloseable {
    // Class is designed for Stockfish, in theory will work with any UCI engine.
    // On our specific hardware, using the popcnt version ("modern" in the source repo) is better, we use
    // standard for more brevity.
    private static final String ENGINE_PATH = "engine/stockfish_11_x64.exe";
    private int level;
    // Level difficulty presets are the tried and true versions provided by lichess:
    // https://github.com/niklasf/fishnet/blob/master/fishnet.py#L116
    // Note they apply to an older version, they use 8 while we use 11.
    private static final int[] LVL_SKILL = new int[]{0, 3, 6, 10, 14, 18, 20};
    private static final int[] LVL_MOVETIMES = new int[]{50, 100, 150, 200, 300, 400, 500, 1000};
    private static final int[] LVL_DEPTHS = new int[]{1, 1, 2, 3, 5, 8, 13, 22};
    private Process engine;
    private Scanner engineOut;
    private BufferedWriter engineIn;

    public Stockfish(int level) throws IOException {
        this.initialize();
        this.setLevel(level);
    }

    public void close() throws IOException {
        if (engine == null) return;
        engineIn.write("quit\n");
        engineIn.close();
        engineOut.close();
    }

    private void initialize() {
        try {
            ProcessBuilder pb = new ProcessBuilder(ENGINE_PATH);
            engine = pb.start();
            engineOut = new Scanner(engine.getInputStream());
            engineIn = new BufferedWriter(new OutputStreamWriter(engine.getOutputStream()));
            engineIn.write("uci\n");
            engineIn.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Error starting Stockfish process");

        }
    }

    public void isready() throws IOException {
        engineIn.write("isready\n");
        engineIn.flush();
        String line;
        do {
            line = engineOut.nextLine();
            System.out.println(line);
        } while (!line.equals("readyok"));
    }

    public int getLevel() {
        return level + 1;
    }

    public void setLevel(int level) throws IOException {
        if (level < 1 || level > 8) {
            throw new IllegalArgumentException("Invalid level: " + level);
        }
        this.level = level - 1;
        this.setLevelProperties();
    }

    private void setLevelProperties() throws IOException {
        isready();
        int hashSize = 62 * (level + 1) + 16;
        engineIn.write(String.format(
                "setoption name Hash value %d\n" +
                "setoption name Skill Level value %d\n",
                hashSize,
                LVL_SKILL[level]));
        engineIn.flush();
    }

    public void resetGame() throws IOException {
        isready();
        engineIn.write("ucinewgame\n");
        engineIn.flush();
    }

    public String makeMove(ArrayList<String> moves) throws IOException {
        // Setup position
        isready();
        if (moves.size() > 0)
            engineIn.write("position startpos moves " + String.join(" ", moves) + "\n");
        else
            engineIn.write("position startpos\n");
        engineIn.flush();
        // Tell engine to determine a move
        isready();
        engineIn.write("go movetime " + LVL_MOVETIMES[level] + " depth " + LVL_DEPTHS[level] + "\n");
        engineIn.flush();
        // Move will be in the format "bestmove e2e4 ponder e7e6", we extract the 'e2e4' segment
        // Wait for the move output
        String line;
        do {
            line = engineOut.nextLine();
            //System.out.println(line);
        } while (!line.startsWith("bestmove"));
        // Extract output from engine and return it
        return line.split("\\s")[1];
    }
}
