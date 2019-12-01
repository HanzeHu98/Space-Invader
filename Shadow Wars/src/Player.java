import org.newdawn.slick.Input;
import java.util.ArrayList;

/**
 * Unique Player class for the world, inherited from the Sprite Class.
 * Handles movement and shooting of player and it's usable poweruUps
 * 
 * @author  Hanze Hu 911700
 */
public class Player extends Sprite implements IsShooter{
	// Preset ImageSource, location and movement speed of the unique Player Class.
	public final static String PLAYER_SPRITE_PATH = "res/spaceship.png";
	public final static int PLAYER_INITIAL_X = 512;
	public final static int PLAYER_INITIAL_Y = 688;
	private final float SPEED = 0.5f;
	
	//Create an ArrayList for Lazer objects to store the Lazers shot by the player.
	ArrayList<Lazer> lazerList = new ArrayList<>();
	
	// Create a unique shield object for the player at it's location.
	private Shield shield = new Shield(PLAYER_INITIAL_X, PLAYER_INITIAL_Y);
	
	// Live, score and PowerUpTimer for the player.
	private int lives = 3;
	private int score = 0;
	private int[] powerUpTimer = new int[2]; 
	
	// Variables Used to Count when the player could shoot.
	private int rateOfFire = 350;
	private int shotInterval = 350;
	
	/** Constructor for the Player Class.
	 * Create a Player object at the designated location, with a shield lasting 3000 ms.
	 */
	public Player() {
		super(PLAYER_SPRITE_PATH, PLAYER_INITIAL_X, PLAYER_INITIAL_Y);
		powerUpTimer[0] = 3000;
	}
	
	/** Update method for the Player Class.
	 * Handles the movement, powerUps, and shooting of the player.
	 * @param input The player Input class that gives directions to how the player behaves.
	 * @param delta Time passed since last frame (milliseconds).
	 */
	@Override
	public void update(Input input, int delta) {
		// Count the time since the last shot was fired.
		shotInterval += delta;
		
		// Calls the doMovement method to handle the player's movement.
		doMovement(input, delta);
		for(int i = 0; i < powerUpTimer.length; i++) {
			powerUpTimer[i] -= delta;
		}
		
		// Shoot if space key is held down.
		if (input.isKeyDown(Input.KEY_SPACE)) {
			fire(delta);
		}
		
		// Update the Lazer and Shield objects, and remove lazers that has moved off screen. 
		shield.update(powerUpTimer[0]);
		Lazer remove = null;
		for(Lazer lazer : lazerList) {
			lazer.update(input, delta);
			if(lazer.getY() > App.SCREEN_HEIGHT || lazer.getY() < 0) {
				remove = lazer;
			}
		}
		lazerList.remove(remove);
	}
	
	/** doMovement method for the Player Class.
	 * Handles the movement of the Player Class and it's shield.
	 * @param input The player Input class that gives directions to how the player behaves.
	 * @param delta Time passed since last frame (milliseconds).
	 */
	private void doMovement(Input input, int delta) {
		// Handle the player's movements.
		float dx = 0;
		if (input.isKeyDown(Input.KEY_LEFT)) {
			dx -= SPEED * delta;
		}
		if (input.isKeyDown(Input.KEY_RIGHT)) {
			dx += SPEED * delta;
		}
		float dy = 0;
		if (input.isKeyDown(Input.KEY_UP)) {
			dy -= SPEED * delta;
		}
		if (input.isKeyDown(Input.KEY_DOWN)) {
			dy += SPEED * delta;
		}
		addX(dx);
		addY(dy);
		clampToScreen();
		
		// Set the shield to the location of the player.
		shield.setX(getX());
		shield.setY(getY());
	}
	
	/** clampToScreen method for the Player Class.
	 * Keeps the player on Screen by moving it back within the boarders if it goes out of bound.
	 */
	public void clampToScreen() {
		float imageWidth = getImage().getWidth()/2;
		float imageHeight = getImage().getHeight()/2;
		this.setX(Math.max(getX(), imageWidth));
		this.setX(Math.min(getX(), App.SCREEN_WIDTH - imageWidth));
		this.setY(Math.max(getY(), imageHeight));
		this.setY(Math.min(getY(), App.SCREEN_HEIGHT - imageHeight));
	}
 	
	/** fire method for the player class.
	 * Taking into account the Shooting rate powerUp, it creates a new friendly lazer object for the player
	 * every 350 (150) miliseconds and adds it to the lazerList ArrayList.
	 * @param delta Time passed since last frame (milliseconds) (ignored).
	 */
	@Override
	public void fire(int delta) {
		if(powerUpTimer[1] > 0)
			rateOfFire = 150;
		else
			rateOfFire = 350;
		if(shotInterval > rateOfFire) {
			shotInterval = 0;
			lazerList.add(new Lazer(0, getX(), getY()));
		}
	}
	
	/** 
	 * Render method for the Player Class, renders the Player at its location (using super from the Sprite Class).
	 * Also renders the shield class and lazers owned by the player object.
	 */
	@Override
	public void render() {
		super.render();
		shield.render();
		for(Lazer lazer : lazerList) {
			lazer.render();
		}
	}
	
	/** ContactSprite method for the player.
	 * It checks if the player is immune (shield is up), if not the player looses a life
	 * and puts up a temporary shield.
	 * This is only called when the player comes into contact with an enemy or enemyLazer.
	 * @param other The Sprite object that has come into contact with the player.
	 */
	@Override
	public void contactSprite(Sprite other) {
		if(powerUpTimer[0] < 0) {
			lives -= 1;
			if( lives<=0 ) {
				App.endGame(true);
			}
			powerUpTimer[0] = 3000;
		}
	}
	
	/** addPowerUpTimer method for the player class.
	 * Adds duration to the powerUp if it is already active, or set it to a certain duration.
	 * @param type An integer indicating the type of powerUp that will have its duration extended.
	 * @param duration An integer that indicates the amount of time to add on or set to.
	 */
	public void addPowerUpTime(int type, int duration) { 
		if(powerUpTimer[type]>0){
				powerUpTimer[type] += duration;}
		else {
			powerUpTimer[type] = duration;
		}
	}
	
	// Getter and setter for the Score variable.
	public void addScore(int points) { score += points; }
	public int getScore() { return score; }
	
	// Getter and setter for the Lives variable.
	public void loseLife() { lives -= 1; }
	public int getLife() { return lives; }
	
	// Getter for the ArrayList of Lazers owned by the player.
	public ArrayList<Lazer> getLazers() { return lazerList; }
	
	// Getter for how long the player is immune for.
	public int getImmune() { return powerUpTimer[0]; }
	
}
