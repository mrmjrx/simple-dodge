package com.mjrx.dodge;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

public class DodgeGame extends ApplicationAdapter {
	public static final int VIEW_WIDTH = 800;
	public static final int VIEW_HEIGHT = 480;
	public static final String HIGH_SCORE_PREF = "highscore";
	public static final String PREF_APP_NAME = "mjrx-simpledodge";
	public long timeBetweenSpawns = (long) (2L * Math.pow(10, 8));
	public Player player;
	private SpriteBatch batch;
	private OrthographicCamera cam;
	private BitmapFont boldFont;
	private BitmapFont regFont;
	private Texture enemyTexture;
	private Array<Enemy> enemies = new Array<>();
	private long lastEnemySpawnTime = System.nanoTime();
	public int livesRemaining = 10;
	public int score = 0;
	private int highscore;
	private Preferences prefs;
	private boolean isNewHighScore = false;
	public GameState state;


	@Override
	public void create () {
		enemyTexture = new Texture(Gdx.files.internal("enemy.png"));

		batch = new SpriteBatch();

		cam = new OrthographicCamera();
		cam.setToOrtho(false, VIEW_WIDTH, VIEW_HEIGHT);

		boldFont = new BitmapFont(Gdx.files.internal("Inter-Black.fnt"));
		boldFont.setColor(200f, 0f, 0f, 255f);

		regFont = new BitmapFont(Gdx.files.internal("Inter-Reg.fnt"));
		regFont.setColor(255f, 255f, 255f, 255f);

		player = new Player(400, 235);

		prefs = Gdx.app.getPreferences(PREF_APP_NAME);
		highscore = prefs.getInteger(HIGH_SCORE_PREF, 0);

		Gdx.input.setInputProcessor(new InputHandler(this));

		state = GameState.GAME_ACTIVE;
	}

	@Override
	public void render () {
		// Game logic

		if (isGameActive() && (System.nanoTime() - lastEnemySpawnTime) >= timeBetweenSpawns) {
			spawnEnemy();
			lastEnemySpawnTime = System.nanoTime();

			timeBetweenSpawns = (long) (MathUtils.clamp(100d / (double) score, .05, 10) * Math.pow(10, 8));
		}


		if (enemies.size > 0) {
			Rectangle playerCollisionRect = player.getCollisionRect();
			Array.ArrayIterator<Enemy> iterator = enemies.iterator();

			for (Enemy enemy = iterator.next(); iterator.hasNext(); enemy = iterator.next()) {
				if (!enemy.isValidPos()) {
					iterator.remove();
					enemy.dispose();
					continue;
				}

				if (playerCollisionRect.overlaps(enemy.getCollisionRect())) {
					iterator.remove();
					enemy.dispose();

					livesRemaining--;
					score *= 0.9;

					if (livesRemaining == 0) {
						state = GameState.GAME_OVER;

						if (score > highscore) {
							isNewHighScore = true;
							prefs.putInteger(HIGH_SCORE_PREF, score);
							prefs.flush();
						}
					}
				}
			}
		}

		if (isGameActive()) {
			player.update();

			for (Enemy enemy : enemies) {
				enemy.update();
			}
		}

		// Rendering
		ScreenUtils.clear(0, 0, 0, 1);
		cam.update();
		batch.setProjectionMatrix(cam.combined);
		batch.begin();

		if (isGameActive()) {
			drawCentreText(boldFont, Integer.valueOf(score).toString());
		}

		player.draw(batch);

		for (Enemy enemy : enemies) {
			enemy.draw(batch);
		}

		if (isGameActive()) {
			regFont.draw(batch, "Lives remaining: " + livesRemaining, 10, 30);
		} else {
			drawCentreText(boldFont, "GAME OVER");

			String scoreText = isNewHighScore ?
					"Score: " + score + " (NEW HIGH!)" :
					"Score: " + score + " (HIGH: " + highscore + ")";

			drawCentreText(regFont, scoreText, (VIEW_HEIGHT / 2) - 100, VIEW_WIDTH - 200);
		}

		batch.end();
	}

	public void spawnEnemy() {
		float x, y;

		do {
			x = MathUtils.random((int) -Enemy.SIZE - 5, (int) (VIEW_WIDTH + Enemy.SIZE + 5));
			y = MathUtils.random((int) -Enemy.SIZE - 5, (int) (VIEW_HEIGHT + Enemy.SIZE + 5));
		} while (
				Utils.isInRange((int) x, 0, VIEW_WIDTH) && Utils.isInRange((int) y, 0, VIEW_HEIGHT)
		);


		float maxSpeed = (float) MathUtils.clamp(score / 75f, 1f, Enemy.MAX_SPEED);

		float velX = (float) Math.sqrt(MathUtils.random(1, (int) Math.pow(maxSpeed, 2d)));

		if (x >= VIEW_WIDTH / 2) {
			velX *= -1;
		}

		float velY = (float) Math.sqrt(MathUtils.random(1, (int) Math.pow(maxSpeed, 2d)));

		if (y >= VIEW_HEIGHT / 2) {
			velY *= -1;
		}

		Enemy newEnemy = new Enemy(x, y, velX, velY, enemyTexture);
		enemies.add(newEnemy);

		score++;
	}

	private boolean isGameActive() {
		return state == GameState.GAME_ACTIVE;
	}

	public void drawCentreText(BitmapFont font, String text) {
		drawCentreText(font, text, VIEW_HEIGHT / 2);
	}

	public void drawCentreText(BitmapFont font, String text, float y) {
		drawCentreText(font, text, y, 100f);
	}

	public void drawCentreText(BitmapFont font, String text, float y, float width) {
		if (!batch.isDrawing()) return;

		font.draw(batch, text, (VIEW_WIDTH / 2) - (width / 2), y, width, Align.center, false);
	}

	@Override
	public void dispose () {
		enemyTexture.dispose();
		batch.dispose();
		player.dispose();
	}
}
