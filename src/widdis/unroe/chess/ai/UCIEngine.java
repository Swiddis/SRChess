package widdis.unroe.chess.ai;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class UCIEngine implements AutoCloseable {
    // We use the stockfish engine (https://stockfishchess.org/).
    // On our specific hardware, using the popcnt version ("modern" in the source repo) is better, we use
    // standard for more brevity.
    private static final String ENGINE_PATH = "engine/stockfish_11_x64.exe";
    private int level;
    // Level difficulty presets are the tried and true versions provided by lichess:
    // https://github.com/niklasf/fishnet/blob/master/fishnet.py#L116
    // Note they apply to an older version, they use version 8 while we use version 11.
    private static final int[] LVL_SKILL = new int[]{0, 3, 6, 10, 14, 16, 18, 20};
    private static final int[] LVL_MOVETIMES = new int[]{50, 100, 150, 200, 300, 400, 500, 1000};
    private static final int[] LVL_DEPTHS = new int[]{1, 1, 2, 3, 5, 8, 13, 22};
    private Process engine;
    private Scanner engineOut;
    private BufferedWriter engineIn;

    public UCIEngine(int level) throws IOException {
        this.initialize();
        this.setLevel(level);
    }

    /**
     * Safely close the engine process, letting it manage its own memory deallocation and shutdown processes.
     *
     * @throws IOException IOException during process write
     */
    public void close() throws IOException {
        if (engine == null) return;
        engWrite("quit");
        engineIn.close();
        engineOut.close();
    }

    /**
     * Initialize the engine, configure it to use the UCI protocol
     */
    private void initialize() {
        try {
            ProcessBuilder pb = new ProcessBuilder(ENGINE_PATH);
            engine = pb.start();
            engineOut = new Scanner(engine.getInputStream());
            engineIn = new BufferedWriter(new OutputStreamWriter(engine.getOutputStream()));
            engWrite("uci");
        } catch (IOException ex) {
            throw new RuntimeException("Error starting Engine process");
        }
    }

    /**
     * Write a string into the engine and manage the output buffer.
     *
     * @param s The string to be written.
     * @throws IOException IOException during process write
     */
    public void engWrite(String s) throws IOException {
        engineIn.write(s + "\n");
        engineIn.flush();
    }

    /**
     * Verify that the engine is ready to receive further input.
     *
     * @throws IOException IOException during process write
     */
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

    /**
     * Set the level of the engine to the given value, based on the the constants defined for level configuration.
     * @param level The level to be set, ranging from 1 to 8.
     */
    public void setLevel(int level) throws IOException {
        if (level < 1 || level > 8) {
            throw new IllegalArgumentException("Invalid level: " + level);
        }
        this.level = level - 1;
        this.setLevelProperties();
    }

    /**
     * Set the properties of the given engine level. This includes hash size and skill level.
     * If an engine doesn't support skill levels, it will ignore the input with a warning message.
     * We don't need to worry about the level if this is the case.
     * @throws IOException IOException during process write
     */
    private void setLevelProperties() throws IOException {
        isready();
        int hashSize = 62 * (level + 1) + 16;
        engWrite(String.format(
                "setoption name Hash value %d\n" +
                "setoption name Skill Level value %d",
                hashSize,
                LVL_SKILL[level]));
        engineIn.flush();
    }

    /**
     * Tell the engine to clear its current hash table and prepare a new game.
     *
     * @throws IOException IOException during process write
     */
    public void resetGame() throws IOException {
        isready();
        engWrite("ucinewgame");
    }

    /**
     * Tell the engine to make a move.
     *
     * @param moves A history of the moves made during the game, in long algebraic notation. The engine needs the full
     *              movelist for some of its own processing, so the UCI protocol requires it be passed this way.
     * @return The string representation of the move, in long algebraic notation.
     * @throws IOException IOException during process write
     */
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
            //System.out.println(line);
        } while (!line.startsWith("bestmove"));
        // Extract output from engine and return it
        return line.split("\\s")[1];
    }
}
