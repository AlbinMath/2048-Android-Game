package com.example.a2048;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class GameLogic {
    public static final int DIRECTION_UP = 0;
    public static final int DIRECTION_RIGHT = 1;
    public static final int DIRECTION_DOWN = 2;
    public static final int DIRECTION_LEFT = 3;

    private static final String PREFS_NAME = "Game2048Prefs";
    private static final String HIGH_SCORE_KEY = "high_score";

    private int gridSize = 4;
    private int[][] grid;
    private TileView[][] tileViews;
    private int score = 0;
    private int highScore = 0;
    private int movesCount = 0;
    private boolean gameActive = true;
    private Context context;

    public GameLogic(Context context) {
        this.context = context;
        grid = new int[gridSize][gridSize];
        tileViews = new TileView[gridSize][gridSize];
        loadHighScore();
    }

    public void setTileView(int i, int j, TileView tile) {
        tileViews[i][j] = tile;
    }

    public int getGridSize() {
        return gridSize;
    }

    public int getScore() {
        return score;
    }

    public int getHighScore() {
        return highScore;
    }

    public int getMovesCount() {
        return movesCount;
    }

    public void newGame() {
        grid = new int[gridSize][gridSize];
        score = 0;
        movesCount = 0;
        gameActive = true;
        addRandomTile();
        addRandomTile();
        updateAllTiles();
    }

    public void move(int direction) {
        if (!gameActive) return;

        boolean moved = false;
        Set<String> mergePositions = new HashSet<>();

        switch (direction) {
            case DIRECTION_UP:
                moved = processMoveUp(mergePositions);
                break;
            case DIRECTION_RIGHT:
                moved = processMoveRight(mergePositions);
                break;
            case DIRECTION_DOWN:
                moved = processMoveDown(mergePositions);
                break;
            case DIRECTION_LEFT:
                moved = processMoveLeft(mergePositions);
                break;
        }

        if (moved) {
            movesCount++;
            addRandomTile();
            updateAllTiles();

            if (score > highScore) {
                highScore = score;
                saveHighScore();
            }

            if (isGameOver()) {
                gameOver();
            } else if (hasWon()) {
                playerWins();
            }
        }
    }

    private boolean processMoveUp(Set<String> mergePositions) {
        boolean moved = false;
        for (int j = 0; j < gridSize; j++) {
            ArrayList<Integer> column = new ArrayList<>();
            for (int i = 0; i < gridSize; i++) {
                if (grid[i][j] != 0) {
                    column.add(grid[i][j]);
                }
            }

            ArrayList<Integer> merged = new ArrayList<>();
            int i = 0;
            while (i < column.size()) {
                if (i + 1 < column.size() && column.get(i).equals(column.get(i + 1))) {
                    int mergedValue = column.get(i) * 2;
                    merged.add(mergedValue);
                    mergePositions.add((merged.size() - 1) + "," + j);
                    score += mergedValue;
                    i += 2;
                } else {
                    merged.add(column.get(i));
                    i += 1;
                }
            }

            while (merged.size() < gridSize) {
                merged.add(0);
            }

            for (int k = 0; k < gridSize; k++) {
                if (grid[k][j] != merged.get(k)) {
                    moved = true;
                }
                grid[k][j] = merged.get(k);
            }
        }
        return moved;
    }

    private boolean processMoveRight(Set<String> mergePositions) {
        boolean moved = false;
        for (int i = 0; i < gridSize; i++) {
            ArrayList<Integer> row = new ArrayList<>();
            for (int j = gridSize - 1; j >= 0; j--) {
                if (grid[i][j] != 0) {
                    row.add(grid[i][j]);
                }
            }

            ArrayList<Integer> merged = new ArrayList<>();
            int j = 0;
            while (j < row.size()) {
                if (j + 1 < row.size() && row.get(j).equals(row.get(j + 1))) {
                    int mergedValue = row.get(j) * 2;
                    merged.add(mergedValue);
                    mergePositions.add(i + "," + (gridSize - 1 - (merged.size() - 1)));
                    score += mergedValue;
                    j += 2;
                } else {
                    merged.add(row.get(j));
                    j += 1;
                }
            }

            while (merged.size() < gridSize) {
                merged.add(0);
            }

            for (int k = 0; k < gridSize; k++) {
                int newValue = merged.get(gridSize - 1 - k);
                if (grid[i][k] != newValue) {
                    moved = true;
                }
                grid[i][k] = newValue;
            }
        }
        return moved;
    }

    private boolean processMoveDown(Set<String> mergePositions) {
        boolean moved = false;
        for (int j = 0; j < gridSize; j++) {
            ArrayList<Integer> column = new ArrayList<>();
            for (int i = gridSize - 1; i >= 0; i--) {
                if (grid[i][j] != 0) {
                    column.add(grid[i][j]);
                }
            }

            ArrayList<Integer> merged = new ArrayList<>();
            int i = 0;
            while (i < column.size()) {
                if (i + 1 < column.size() && column.get(i).equals(column.get(i + 1))) {
                    int mergedValue = column.get(i) * 2;
                    merged.add(mergedValue);
                    mergePositions.add((gridSize - 1 - (merged.size() - 1)) + "," + j);
                    score += mergedValue;
                    i += 2;
                } else {
                    merged.add(column.get(i));
                    i += 1;
                }
            }

            while (merged.size() < gridSize) {
                merged.add(0);
            }

            for (int k = 0; k < gridSize; k++) {
                int newValue = merged.get(gridSize - 1 - k);
                if (grid[k][j] != newValue) {
                    moved = true;
                }
                grid[k][j] = newValue;
            }
        }
        return moved;
    }

    private boolean processMoveLeft(Set<String> mergePositions) {
        boolean moved = false;
        for (int i = 0; i < gridSize; i++) {
            ArrayList<Integer> row = new ArrayList<>();
            for (int j = 0; j < gridSize; j++) {
                if (grid[i][j] != 0) {
                    row.add(grid[i][j]);
                }
            }

            ArrayList<Integer> merged = new ArrayList<>();
            int j = 0;
            while (j < row.size()) {
                if (j + 1 < row.size() && row.get(j).equals(row.get(j + 1))) {
                    int mergedValue = row.get(j) * 2;
                    merged.add(mergedValue);
                    mergePositions.add(i + "," + (merged.size() - 1));
                    score += mergedValue;
                    j += 2;
                } else {
                    merged.add(row.get(j));
                    j += 1;
                }
            }

            while (merged.size() < gridSize) {
                merged.add(0);
            }

            for (int k = 0; k < gridSize; k++) {
                if (grid[i][k] != merged.get(k)) {
                    moved = true;
                }
                grid[i][k] = merged.get(k);
            }
        }
        return moved;
    }

    private void addRandomTile() {
        ArrayList<int[]> emptyCells = new ArrayList<>();
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (grid[i][j] == 0) {
                    emptyCells.add(new int[]{i, j});
                }
            }
        }

        if (!emptyCells.isEmpty()) {
            Random random = new Random();
            int[] cell = emptyCells.get(random.nextInt(emptyCells.size()));
            grid[cell[0]][cell[1]] = (random.nextFloat() < 0.3) ? 4 : 2;
        }
    }

    private void updateAllTiles() {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                tileViews[i][j].setValue(grid[i][j]);
            }
        }
    }

    private boolean hasWon() {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (grid[i][j] == 2048) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isGameOver() {
        // Check for empty cells
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (grid[i][j] == 0) {
                    return false;
                }
            }
        }

        // Check for possible merges
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (j + 1 < gridSize && grid[i][j] == grid[i][j + 1]) {
                    return false;
                }
                if (i + 1 < gridSize && grid[i][j] == grid[i + 1][j]) {
                    return false;
                }
            }
        }

        return true;
    }

    private void gameOver() {
        gameActive = false;
        Toast.makeText(context, "Game Over! Score: " + score, Toast.LENGTH_LONG).show();

        // Add a delay before restarting the game to allow the toast to be visible
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        newGame();
                    }
                },
                2000); // 2 seconds delay
    }
    private void playerWins() {
        Toast.makeText(context, "You Win! Score: " + score, Toast.LENGTH_LONG).show();
    }

    private void loadHighScore() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        highScore = prefs.getInt(HIGH_SCORE_KEY, 0);
    }

    private void saveHighScore() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(HIGH_SCORE_KEY, highScore);
        editor.apply();
    }
}