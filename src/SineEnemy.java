import org.newdawn.slick.Input;

/**
 * SineEnemy class for the world, inherited from the Enemy Class.
 * Handles movement for the SineEnemy and adding its score to the player when destroyed.
 * @author  Hanze Hu 911700
 */
public class SineEnemy extends Enemy{
	// The source of it's image and the amount of score its worth.
	private static String imageSrc = "res/sine-enemy.png";
	private int score = 100;
	
	// Variables used to handle movement of SineEnemy.
	private float moveSpeed = (float)0.15;
	private int amplitude = 96;
	private int period = 1500;
	
	// Clamps this to a set X coordinate.
	private float clampX = this.getX();
	
	/** Constructor for the SineEnemy Class.
	 * Creates a SineEnemy object at the given location and delay, with imageSrc, and it can't shoot. 
	 * @param x A float point indicating the X position of the Sprite initially.
	 * @param y A float point indicating the Y position of the Sprite initially.
	 * @param enemyDelay An integer indicating the amount of time before the enemy starts moving on screen.
	 */
	public SineEnemy(float x, float y, int enemyDelay) {
		super(imageSrc, x, y, enemyDelay, false);
	}
	
	/** Update method for the SineEnemy Class.
	 * Handles the SineEnemy's movement.
	 * @param input The player Input class that gives directions to how the player behaves (ignored).
	 * @param delta Time passed since last frame (milliseconds).
	 */
	@Override
	public void update(Input input, int delta) {
		super.update(input, delta);
		// Handles movement using the given equation.
		if(getDelay() < getCounter() ) {
			float dx = clampX + (float) (amplitude * Math.sin(2 * Math.PI * (getCounter() - getDelay()) / period));
			addY(moveSpeed * delta);
			setX(dx);
		}
	}

	/** ContactSprite method for the SineEnemy.
	 * Calls the super class to handle dropping powerUp, and adds its score to the player class.
	 * This should only be called when it comes into contact with the player's lazer.
	 * @param other The (Player)Sprite object that will have its score added.
	 */
	@Override
	public void contactSprite(Sprite other) {
		((Player)other).addScore(score);
		super.contactSprite(other);
	}
	
}
