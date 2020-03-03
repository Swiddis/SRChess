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
        this.setLevelProperties();
    }

    public void close() throws IOException {
        if (engine == null) return;
        engWrite("quit");
        engineIn.close();
        engineOut.close();
        engine.destroy();
    }

    private void initialize() {
        try {
            ProcessBuilder pb = new ProcessBuilder(ENGINE_PATH);
            engine = pb.start();
            engineOut = new Scanner(engine.getInputStream());
            engineIn = new BufferedWriter(new OutputStreamWriter(engine.getOutputStream()));
            engWrite("uci");
        } catch (IOException ex) {
            throw new RuntimeException("Error starting Stockfish process");
        }
    }

    public void engWrite(String s) throws IOException {
        engineIn.write(s + "\n");
        engineIn.flush();
    }

    public void isready() throws IOException {
        engWrite("isready");
        String line;
        do {
            line = engineOut.nextLine();
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
        engWrite(String.format(
                "setoption name Hash value %d\n" +
                "setoption name Skill Level value %d",
                hashSize,
                LVL_SKILL[level]));
    }

    public void resetGame() throws IOException {
        isready();
        engWrite("ucinewgame");
    }

    public String makeMove(ArrayList<String> moves) throws IOException {
        // Setup position
        isready();
        if (moves.size() > 0)
            engWrite("position startpos moves " + String.join(" ", moves));
        else
            engWrite("position startpos");
        // Tell engine to determine a move
        isready();
        engWrite("go movetime " + LVL_MOVETIMES[level] + " depth " + LVL_DEPTHS[level] + "\n");
        // Move will be in the format "bestmove e2e4 ponder e7e6", we extract the 'e2e4' segment
        // Wait for the move output
        String line;
        do {
            line = engineOut.nextLine();
        } while (!line.startsWith("bestmove"));
        // Extract output from engine and return it
        return line.split("\\s")[1];
    }
}
