package jogo.teste.com;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import jogo.teste.com.R;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    FrameLayout gameArea;
    Button player;
    TextView txtScore;

    int score = 0;
    Handler handler = new Handler();
    Random random = new Random();

    Runnable gameLoop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameArea = findViewById(R.id.gameArea);
        player = findViewById(R.id.player);
        txtScore = findViewById(R.id.txtScore);

        // Movimento do jogador (esquerda/direita)
        player.setOnTouchListener((v, event) -> {
            player.setX(event.getRawX() - player.getWidth() / 2);
            return true;
        });

        startGame();
    }

    private void startGame() {
        gameLoop = new Runnable() {
            @Override
            public void run() {

                criarObstaculo();

                score++;
                txtScore.setText("Score: " + score);

                handler.postDelayed(this, 1000);
            }
        };

        handler.post(gameLoop);
    }

    private void criarObstaculo() {
        View obs = new View(this);
        obs.setBackgroundColor(0xFFFF0000);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(100, 100);
        params.leftMargin = random.nextInt(gameArea.getWidth() - 100);

        gameArea.addView(obs, params);

        Handler moveHandler = new Handler();

        moveHandler.post(new Runnable() {
            int y = 0;

            @Override
            public void run() {
                y += 20;
                obs.setY(y);

                if (colidiu(obs, player)) {
                    gameOver();
                    return;
                }

                if (y < gameArea.getHeight()) {
                    moveHandler.postDelayed(this, 30);
                } else {
                    gameArea.removeView(obs);
                }
            }
        });
    }

    private boolean colidiu(View a, View b) {
        Rect r1 = new Rect();
        Rect r2 = new Rect();

        a.getHitRect(r1);
        b.getHitRect(r2);

        return Rect.intersects(r1, r2);
    }

    private void gameOver() {
        handler.removeCallbacks(gameLoop);
        txtScore.setText("GAME OVER\nScore: " + score);
    }
}
