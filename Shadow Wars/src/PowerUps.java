import org.newdawn.slick.Input;

/**
 * PowerUp class for the world, inherited from the Sprite Class.
 * Handles movement of PowerUps.
 * 
 * @author  Hanze Hu 911700
 */
public class PowerUps extends Sprite {
	// integer indicating the type of powerUp it is, 0 for shield and 1 for faster rate of fire.
	private int type;
	
	// Different image for each type of powerUp.
	private static String imageSource[] = new String[] {
	"res/shield-powerup.png", "res/shotspeed-powerup.png"};
	
	// The duration it should go for and how fast it moves.
	public static final int DURATION = 5000;
	private float moveSpeed = (float) 0.1;
	
	/** Constructor for the PowerUp Class.
	 * Creates a PowerUp object at the given location with a given type.
	 * @param type An integer indicating the type of powerUp it is.
	 * @param x A float point indicating the X position of the Sprite initially.
	 * @param y A float point indicating the Y position of the Sprite initially.
	 */
	public PowerUps(int type, float x, float y) {
		super(imageSource[type], x, y);
		this.type = type;
		activate();
	}
	
	// Moves the powerUps down the page.
	@Override
	public void update(Input input, int delta) {
		addY(moveSpeed * delta);
	}
	
	// Deactivates the PowerUp when it is picked up by the player.
	@Override
	public void contactSprite(Sprite other) {
		this.deactivate();
	}
	
	// Get the type of PowerUp this is.
	public int getType() { return type; }
}
