import org.newdawn.slick.Input;
import java.util.ArrayList;
import java.util.Random;

/**
 * BasicShooter class for the world, inherited from the Enemy Class and with shooting implemented.
 * Handles movement and shooting for the BasicShooter and adding its score to the player when destroyed.
 * @author  Hanze Hu 911700
 */
public class BasicShooter extends Enemy implements IsShooter{
	// The variables used to handle shooting, a lazer is fired as soon as it has reached its final position.
	ArrayList<Lazer> lazerList = new ArrayList<>();
	private int rateOfFire = 3500;
	// This could be modified to 0 if the enemy is to wait 3.5 seconds before firing the first shot.
	private int shotInterval = 3500;
	
	// The final Y coordinate of the BasicShooter class, and a boolean to check if it has reached that position.
	private int yCoordinate;
	private boolean inPosition = false;
	
	// The location of the source file for its image.
	private static String imageSrc = "res/basic-shooter.png";
	
	// The score a basic shooter is worth and how fast it moves.
	private int score = 200;
	private float moveSpeed = (float)0.2;
	
	// This is used to generate a 
	private Random rand = new Random();
	
	/** Constructor for the BasicShoooter Class.
	 * Creates a BasicShooter object at the given location and delay, with imageSrc, and it can shoot. 
	 * Also generates the final random Y coordinate that is should move to.
	 * @param x A float point indicating the X position of the Sprite initially.
	 * @param y A float point indicating the Y position of the Sprite initially.
	 * @param enemyDelay An integer indicating the amount of time before the enemy starts moving on screen.
	 */
	public BasicShooter(float x, float y, int enemyDelay) {
		super(imageSrc, x, y, enemyDelay, true);
		yCoordinate = rand.nextInt(416) + 49;
	}
	
	/** Update method for the BasicShooter Class.
	 * Handles the BaiscShooter's movement and it's firing pattern.
	 * @param input The player Input class that gives directions to how the player behaves (ignored).
	 * @param delta Time passed since last frame (milliseconds).
	 */
	@Override
	public void update(Input input, int delta) {
		super.update(input, delta);
		
		// Update the Lazer objects, and remove lazers that has moved off screen. 
		Lazer remove = null;
		for(Lazer lazer : lazerList) {
			lazer.update(input, delta);
			if(lazer.getY() > App.SCREEN_HEIGHT) {
				remove = lazer;
			}
		}
		lazerList.remove(remove);
		
		// Handles movement until the object has reached its final Y position.
		if(getDelay() < getCounter()) {
			if(this.getY() < yCoordinate)
				addY(moveSpeed * delta);
			else
				inPosition = true;
		}
		
		// If it has reached its final position, it could start firing.
		if(inPosition && getActive())
			fire(delta);
	}
	
	/** ContactSprite method for the BasicShooter.
	 * Calls the super class to handle dropping powerUp, and adds its score to the player class.
	 * This should only be called when it comes into contact with the player's lazer.
	 * @param other The (Player)Sprite object that will have its score added.
	 */
	@Override
	public void contactSprite(Sprite other) {
		((Player)other).addScore(score);
		super.contactSprite(other);
	}
	
	/** 
	 * Render method for the BasicShooter Class.
	 * Renders the BasicShooter at its location (using super from the Sprite Class).
	 * Also renders all the Lazer owned by this Basic Shooter object.
	 */
	public void render() {
		super.render();
		for(Lazer lazer : lazerList) {
			lazer.render();
		}
	}
	
	/** fire method for the BasicShooter class.
	 * It creates a new enemy lazer object for the BasicShooter once every 3.5 seconds and
	 * adds it to the lazerList ArrayList.
	 * @param delta Time passed since last frame (milliseconds).
	 */
	@Override
	public void fire(int delta) {
		shotInterval += delta;
		if(shotInterval > rateOfFire) {
			shotInterval = 0;
			lazerList.add(new Lazer(1, getX(), getY()));
		}
	}
	
	// Getter for the ArrayList of Lazers owned by the BasicShooter.
	public ArrayList<Lazer> getLazers() { return lazerList; }
}
