package com.example.a2048;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {

    private GameLogic gameLogic;
    private GridLayout gridLayout;
    private TextView scoreTextView;
    private TextView highScoreTextView;
    private TextView movesTextView;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Initialize views
        gridLayout = findViewById(R.id.game_grid);
        scoreTextView = findViewById(R.id.score_text);
        highScoreTextView = findViewById(R.id.high_score_text);
        movesTextView = findViewById(R.id.moves_text);

        // Initialize game logic
        gameLogic = new GameLogic(this);

        // Set up gesture detection
        gestureDetector = new GestureDetector(this, new GestureListener());

        // Set up exit button
        findViewById(R.id.exit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Set up new game button
        findViewById(R.id.new_game_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartGame();
                // Show toast message when new game is started
                Toast.makeText(GameActivity.this, "New game started!", Toast.LENGTH_SHORT).show();
            }
        });

        // Initialize game
        initializeGame();
    }

    private void initializeGame() {
        // Clear existing views
        gridLayout.removeAllViews();

        // Initialize grid
        int gridSize = gameLogic.getGridSize();
        gridLayout.setColumnCount(gridSize);
        gridLayout.setRowCount(gridSize);

        // Create tiles with square aspect ratio
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                TileView tile = new TileView(this);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 0;
                params.height = 0;
                params.columnSpec = GridLayout.spec(j, 1f);
                params.rowSpec = GridLayout.spec(i, 1f);
                params.setMargins(4, 4, 4, 4);
                tile.setLayoutParams(params);
                gridLayout.addView(tile);
                gameLogic.setTileView(i, j, tile);
            }
        }

        // Ensure the grid maintains square aspect ratio
        gridLayout.post(new Runnable() {
            @Override
            public void run() {
                int size = Math.min(gridLayout.getWidth(), gridLayout.getHeight());
                ViewGroup.LayoutParams params = gridLayout.getLayoutParams();
                params.width = size;
                params.height = size;
                gridLayout.setLayoutParams(params);
            }
        });

        // Start new game
        gameLogic.newGame();
        updateUI();
    }

    private void restartGame() {
        gameLogic.newGame();
        updateUI();
    }

    private void updateUI() {
        scoreTextView.setText("Score: " + gameLogic.getScore());
        highScoreTextView.setText("High: " + gameLogic.getHighScore());
        movesTextView.setText("Moves: " + gameLogic.getMovesCount());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();

                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            gameLogic.move(GameLogic.DIRECTION_RIGHT);
                        } else {
                            gameLogic.move(GameLogic.DIRECTION_LEFT);
                        }
                        result = true;
                    }
                } else {
                    if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            gameLogic.move(GameLogic.DIRECTION_DOWN);
                        } else {
                            gameLogic.move(GameLogic.DIRECTION_UP);
                        }
                        result = true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (result) {
                updateUI();
            }
            return result;
        }
    }
}