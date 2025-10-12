import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;



public class HighscoreManager {

    private final Path file;

    public HighscoreManager() {
        this("highscores.txt");
    }

    public HighscoreManager(String filename) {
        this.file = Paths.get(filename);
        try {
            if (Files.notExists(file)) {
                Files.createFile(file);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not create highscore file: " + file, e);
        }
    }


    public synchronized void saveEntry(String name, int score, boolean victory) {
        String ts = Instant.now().toString();
        String line = escape(name) + "," + score + "," + (victory ? "1" : "0") + "," + ts;
        try (BufferedWriter w = Files.newBufferedWriter(file, StandardOpenOption.APPEND)) {
            w.write(line);
            w.newLine();
        } catch (IOException e) {
            System.err.println("Failed to save highscore: " + e.getMessage());
        }
    }


    public List<HighscoreEntry> loadTop(int n) {
        List<HighscoreEntry> all = loadAll();
        if (all.size() <= n) return all;
        return new ArrayList<>(all.subList(0, n));
    }


    public List<HighscoreEntry> loadAll() {
        List<HighscoreEntry> list = new ArrayList<>();
        try (BufferedReader r = Files.newBufferedReader(file)) {
            String line;
            while ((line = r.readLine()) != null) {
                HighscoreEntry entry = parseLine(line);
                if (entry != null) list.add(entry);
            }
        } catch (IOException e) {
            System.err.println("Failed to read highscores: " + e.getMessage());
        }


        Collections.sort(list, Comparator.comparingInt(HighscoreEntry::getScore).reversed()
                .thenComparing(HighscoreEntry::getTimestamp).reversed());
        return list;
    }


    public synchronized void clear() {
        try {
            Files.write(file, new byte[0], StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.err.println("Failed to clear highscores: " + e.getMessage());
        }
    }


    private HighscoreEntry parseLine(String line) {
        String[] parts = splitEscaped(line);
        if (parts.length != 4) return null;

        String name = unescape(parts[0]);

        int score;
        try {
            score = Integer.parseInt(parts[1]);
        } catch (NumberFormatException ex) {
            return null;
        }

        boolean victory = "1".equals(parts[2]);

        Instant ts;
        try {
            ts = Instant.parse(parts[3]);
        } catch (DateTimeParseException ex) {
            ts = Instant.EPOCH;
        }

        return new HighscoreEntry(name, score, victory, ts);
    }


    private String escape(String s) {
        return s.replace("\\", "\\\\").replace(",", "\\,");
    }

    private String unescape(String s) {
        return s.replace("\\,", ",").replace("\\\\", "\\");
    }


    private String[] splitEscaped(String line) {
        List<String> parts = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean esc = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (esc) {
                cur.append(c);
                esc = false;
            } else {
                if (c == '\\') {
                    esc = true;
                } else if (c == ',') {
                    parts.add(cur.toString());
                    cur.setLength(0);
                } else {
                    cur.append(c);
                }
            }
        }
        parts.add(cur.toString());
        return parts.toArray(new String[0]);
    }



    public static class HighscoreEntry {
        private final String name;
        private final int score;
        private final boolean victory;
        private final Instant timestamp;

        public HighscoreEntry(String name, int score, boolean victory, Instant timestamp) {
            this.name = name;
            this.score = score;
            this.victory = victory;
            this.timestamp = timestamp;
        }

        public String getName() { return name; }
        public int getScore() { return score; }
        public boolean isVictory() { return victory; }
        public Instant getTimestamp() { return timestamp; }

        @Override
        public String toString() {
            return name + " | score = " + score + " |" + timestamp.toString();

        }
    }
}