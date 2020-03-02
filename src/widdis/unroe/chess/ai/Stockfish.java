package widdis.unroe.chess.ai;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Scanner;

// Communicates with the Stockfish UCI Engine: https://stockfishchess.org/
public class Stockfish implements AutoCloseable {
    private int level;
    private static final int[] LVL_SKILL = new int[]{0, 3, 6, 10, 14, 18, 20};
    private static final int[] LVL_MOVETIMES = new int[]{50, 100, 150, 200, 300, 400, 500, 1000};
    private static final int[] LVL_DEPTHS = new int[]{1, 1, 2, 3, 5, 8, 13, 22};
    private Process engine;
    private Scanner engineOut;
    private OutputStreamWriter engineIn;

    public Stockfish(int level) throws IOException {
        this.setLevel(level);
        this.initialize();
        this.setLevelProperties();
    }

    public void close() throws IOException {
        if (engine == null) return;
        engineIn.write("quit\n");
        engineIn.close();
        engineOut.close();
    }

    private void initialize() {
        try {
            ProcessBuilder pb = new ProcessBuilder("stockfish_11_x64.exe");
            engine = pb.start();
            engineOut = new Scanner(engine.getInputStream());
            engineIn = new OutputStreamWriter(engine.getOutputStream());
            engineIn.write("uci\n");
        } catch (IOException ex) {
            throw new RuntimeException("Error starting Stockfish process");
        }
    }

    public void isready() throws IOException {
        engineIn.write("isready\n");
        while (!engineOut.nextLine().equals("readyok")) {
            continue;
        }
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
        int lvl = this.getLevel();
        int hashSize = 496 / 8 * (lvl + 1) + 16;
        engineIn.write(String.format(
                "setoption name Hash value %d\n" +
                "setoption name Skill Level value %d\n",
                hashSize,
                LVL_SKILL[lvl]));
    }
}
