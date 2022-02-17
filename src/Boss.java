import org.newdawn.slick.Input;
import java.util.ArrayList;
import java.util.Random;

/**
 * Boss class for the world, inherited from the Enemy Class.
 * Handles movements and shooting for the Boss and adds its score to the player when destroyed.
 * @author  Hanze Hu 911700
 */
public class Boss extends Enemy implements IsShooter{
	// The variables used to handle shooting, a lazer is fired as soon as it has reached its final position.
	ArrayList<Lazer> lazerList = new ArrayList<>();
	private int rateOfFire = 200;
	private int shotInterval = 0;
	private int shotTimer = 7000;
	
	// The amount of hit points the Boss has.
	private int hitPoints = 60;
	
	// The target X and Y coodinate the Boss object should move to.
	private int yCoordinate = 72;
	private int xCoordinate;
	
	// The location of the source image for the boss and the amount of score it's worth.
	private static String imageSrc = "res/boss.png";
	private int score = 5000;
	
	// Variables used to handle movement of the Boss Class.
	private int timer = 0;
	private float moveSpeed = 0.05f;
	private boolean inPosition = false;
	private boolean cycled = false;
	private boolean restart = true;
	private boolean shoot = false;
	
	// The chance of dropping a powerUp.
	private float chanceToDrop = 0.05f;
	
	// Used for generating random X coordinates.
	private Random rand = new Random();
	
	/** Constructor for the Boss Class.
	 * Creates a Boss object at the given location and delay, with imageSrc, and it can shoot. 
	 * @param x A float point indicating the X position of the Sprite initially.
	 * @param y A float point indicating the Y position of the Sprite initially.
	 * @param enemyDelay An integer indicating the amount of time before the enemy starts moving on screen.
	 */
	public Boss(float x, float y, int enemyDelay) {
		super(imageSrc, x, y, enemyDelay, true);
	}
	
	/** Update method for the BasicShooter Class.
	 * Handles the BaiscShooter's movement and it's firing pattern.
	 * @param input The player Input class that gives directions to how the player behaves (ignored).
	 * @param delta Time passed since last frame (milliseconds).
	 */
	@Override
	public void update(Input input, int delta) {
		super.update(input, delta);
		// Update all the Lazer objects, and remove lazers that has moved off screen. 
		Lazer remove = null;
		for(Lazer lazer : lazerList) {
			lazer.update(input, delta);
			if(lazer.getY() > App.SCREEN_HEIGHT) {
				remove = lazer;
			}
		}
		lazerList.remove(remove);
		
		// Marks when the boss has moved into the correct Y position.
		if(this.getY() > yCoordinate && ! inPosition) {
			inPosition = true;
			xCoordinate = randNum();
			timer = 0;
		}
		// Reset the whole movement cycle.
		if(restart) {
			restart = false;
			cycled = false;
			timer = 0;
			xCoordinate = randNum();
		}
		
		// Times when the Boss could start shooting.
		if(shotTimer > 3000) {
			shotTimer = 0;
			shoot = false;
		}
		
		// Handles movement based on multiple flags and wait time.
		if(getDelay() < getCounter()) {
			if(! inPosition) {
				addY(moveSpeed * delta);
			}
			else if(5000 < timer && Math.abs(getX() - xCoordinate) > 2 && !cycled) {
				addX(moveSpeed * 4 * delta * -1 * Math.signum(this.getX() - xCoordinate));
			}
			else if(7000 < timer && Math.abs(getX() - xCoordinate) > 2) {
				addX(moveSpeed * 2 * delta * -1 * Math.signum(this.getX() - xCoordinate));
			}
			else if(Math.abs(getX() - xCoordinate) < 2) {
				if(5000 < timer && !cycled) {
					cycled = true;
					xCoordinate = randNum();
				}
				else {
					restart = true;
				}
			}
			else {
				timer += delta;
			}
			
			// Shoot for at least 3000 seconds.
			if(7000 < timer && cycled && shotTimer < 3000) {
				shoot = true;
			}
			// Creates lazer objects for the Boss Class.
			if(shoot) {
				shotTimer += delta;
				fire(delta);
			}
		}
	}
	
	/** randNum method for the enemy.
	 * Generates a random X coordinates within the given range and returns it.
	 */
	public int randNum() {
		return rand.nextInt(768) + 128;
	}
	
	/** ContactSprite method for the Boss.
	 * Looses hitPoints until it has been destroyed.
	 * When destroyed, handles dropping powerUp, and adds its score to the player class.
	 * This should only be called when it comes into contact with the player's lazer.
	 * @param other The (Player)Sprite object that will have its score added.
	 */
	@Override
	public void contactSprite(Sprite other) {
		if(hitPoints > 0) {
			hitPoints -= 1;
		}
		if(hitPoints == 0) {
			// Adds score to the player.
			((Player)other).addScore(score);
			this.deactivate();
			// Handles Dropping PowerUps.
			if(Math.random() < chanceToDrop) {
				int type = (int)Math.round(Math.random());
				World.createPower(type, getX(), getY());
			}
		}
	}
	
	/** fire method for the Boss class.
	 * It creates four new enemy lazer object for the BasicShooter once every 200 seconds and
	 * adds it to the lazerList ArrayList, with an X offset of +- 97 and +- 74 for each lazer.
	 * @param delta Time passed since last frame (milliseconds).
	 */
	@Override
	public void fire(int delta) {
		shotInterval += delta;
		if(shotInterval > rateOfFire && this.getActive()) {
			shotInterval = 0;
			lazerList.add(new Lazer(1, getX() -97, getY()));
			lazerList.add(new Lazer(1, getX() -74, getY()));
			lazerList.add(new Lazer(1, getX() +74, getY()));
			lazerList.add(new Lazer(1, getX() +97, getY()));
		}
	}
	
	/** 
	 * Render method for the Boss Class.
	 * Renders the Boss at its location (using super from the Sprite Class).
	 * Also renders all the Lazer owned by this Boss object.
	 */
	public void render() {
		// Only render Objects that are active
		super.render();
		for(Lazer lazer: lazerList) {
			lazer.render();
		}
	}
	
	// Getter for the ArrayList of Lazers owned by the player.
	public ArrayList<Lazer> getLazers() { return lazerList; }
}
