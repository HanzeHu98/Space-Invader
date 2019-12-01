import org.newdawn.slick.Input;

/**
 * Abstract Enemy class for the world, inherited from the Sprite Class.
 * Contains the delay for when each enemy should appear and if they can fire lazers.
 * Handles dropping powerUps with a given Chance.
 * More to be further implemented in seperate subclasses for each type of enemy.
 * @author  Hanze Hu 911700
 */
public abstract class Enemy extends Sprite{
	// The Chance to drop a random powerUp
	private float chanceToDrop = 0.05f;
	
	// Counter and delay time for the enemy object.
	private int delay;
	private int delayCounter = 0;
	
	// If the Enemy could shoot or if it has been activated to be shown on screen.
	private boolean isShooter = false;
	private boolean activated = false;
	
	// The Score to add to the player when the enemy is destroyed.
	private int score;
	
	/** Constructor for the Enemy Class.
	 * Creates a Enemy object with the given location, image source, delay and if it could shoot.
	 * All Enemy objects starts deactivated so it won't interact with player lazers.
	 * @param imageSrc The String indicating the location(Source) of the image of this object.
	 * @param x A float point indicating the X position of the Sprite initially.
	 * @param y A float point indicating the Y position of the Sprite initially.
	 * @param enemyDelay An integer indicating the amount of time before the enemy starts moving on screen.
	 * @param shooter A boolean that indicates if the enemy could fire lazers.
	 */
	public Enemy(String imageSrc, float x, float y, int enemyDelay, boolean Shooter) {
		super(imageSrc, x, y);
		delay = enemyDelay;
		deactivate();
		isShooter = Shooter;
	}
	
	/** Update method for the enemy Class.
	 * Handles when the enemy should start moving onto the screen.
	 * Deactivates the enemy when it leaves the screen.
	 * Movement to be handled by subclasses.
	 * @param input The player Input class that gives directions to how the player behaves (ignored).
	 * @param delta Time passed since last frame (milliseconds).
	 */
	public void update(Input input, int delta) {
		// time the delay and checks if it the delay has been reached, activate it when it moves onto screen.
		delayCounter += delta;
		if(delay < delayCounter && ! activated && getY() > 0 - this.getImage().getHeight()/2) {
			this.activate();
			activated = true;
		}
		// If it moves off screen, deactivate it.
		if(this.getY() > App.SCREEN_HEIGHT + this.getImage().getHeight()/2) {
			deactivate();
		}
	}
	
	/** ContactSprite method for the enemy.
	 * Handles the dropping of powerUps and deactivate the enemy when it has been hit.
	 * @param other The Sprite object that has come into contact with the player (ignored).
	 */
	public void contactSprite(Sprite other) {
		if(Math.random() < chanceToDrop) {
			int type = (int)Math.round(Math.random());
			World.createPower(type, getX(), getY());
		}
		this.deactivate();
	}
	
	// Get how much the enemy is worth.
	public int getScore() { return this.score; }
	
	// Getter for the delay and the delay timer for the enemy class.
	public int getDelay() { return this.delay; }
	public int getCounter() { return this.delayCounter; }
	
	// Returns if this enemy could shoot lazers.
	public boolean isShooter() { return this.isShooter; }

}
