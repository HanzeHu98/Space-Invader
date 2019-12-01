import org.newdawn.slick.Input;

/**
 * BasicEnemy class for the world, inherited from the Enemy Class.
 * Handles movement for the BasicEnemy and adding its score to the player when destroyed.
 * @author  Hanze Hu 911700
 */
public class BasicEnemy extends Enemy {
	
	// Source of the image file.
	private static String imageSrc = "res/basic-enemy.png";
	
	// The score a basic enemy is worth and how fast it moves.
	private int score = 50;
	private float moveSpeed = 0.2f;
	
	/** Constructor for the BasicEnemy Class.
	 * Creates a BasicEnemy object at the given location and delay, with imageSrc, and it can't shoot. 
	 * @param x A float point indicating the X position of the Sprite initially.
	 * @param y A float point indicating the Y position of the Sprite initially.
	 * @param enemyDelay An integer indicating the amount of time before the enemy starts moving on screen.
	 */
	public BasicEnemy(float x, float y, int enemyDelay) {
		super(imageSrc, x, y, enemyDelay, false);
	}
	
	/** Update method for the BasicEnemy Class.
	 * Handles the BaiscEnemy's movement.
	 * @param input The player Input class that gives directions to how the player behaves (ignored).
	 * @param delta Time passed since last frame (milliseconds).
	 */
	@Override
	public void update(Input input, int delta) {
		super.update(input, delta);
		if(getDelay() < getCounter()) {
			addY(moveSpeed * delta);
		}

	}
	
	/** ContactSprite method for the BasicEnemy.
	 * Calls the super class to handle dropping powerUp, and adds its score to the player class.
	 * This should only be called when it comes into contact with the player's lazer.
	 * @param other The (Player)Sprite object that will have its score added.
	 */
	@Override
	public void contactSprite(Sprite other) {
		super.contactSprite(other);
		((Player)other).addScore(score);
	}
}
