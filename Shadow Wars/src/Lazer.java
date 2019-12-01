import org.newdawn.slick.Input;

/**
 * Lazer class for the world, inherited from the Sprite Class.
 * Handles movement of Lazers.
 * 
 * @author  Hanze Hu 911700
 */
public class Lazer extends Sprite {
	// Determines if it is a player lazer or enemy lazer.
	private boolean friendly;
	
	// Different lazer will have different speed and image file.
	private float friendlyShotSpeed = 3f;
	private float enemyShotSpeed = 0.7f;
	private static String imageSource[] = new String[] {
			"res/shot.png", "res/enemy-shot.png"};
	
	/** Constructor for the PowerUp Class.
	 * Creates a PowerUp object at the given location with a given type.
	 * @param type An integer indicating the type of lazer (enemy/ friend) it is.
	 * @param x A float point indicating the X position of the Sprite initially.
	 * @param y A float point indicating the Y position of the Sprite initially.
	 */
	public Lazer(int type, float x, float y) {
		super(imageSource[type], x, y);
		if(type == 0) {
			friendly = true;
		}
		else {
			friendly = false;
		}
	}

	// Moves the lazer up or down the screen depending on the type of lazer it is.
	@Override
	public void update(Input input, int delta) {
		if(this.friendly) {
			addY(-(friendlyShotSpeed * delta));
		}
		else {
			addY(enemyShotSpeed * delta);
		}
	}

	// Deactivates this lazer when it has made contact with an enemy object.
	@Override
	public void contactSprite(Sprite other) {
		this.deactivate();
	}
	
	// Get if this is a player's lazer.
	public boolean getFriendly() { return this.friendly; }
}
