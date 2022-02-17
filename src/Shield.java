import org.newdawn.slick.Input;

/**
 * Unique Shield class for the world, inherited from the Sprite Class, owned by the player.
 * Handles movement of the shield.
 * 
 * @author  Hanze Hu 911700
 */
public class Shield extends Sprite{
	private static String imageSrc = "res/shield.png";
	
	// Initialises the shield with the given image at the player's position.
	public Shield(float x, float y) {
		super(imageSrc, x, y);
	}
	
	// (ignored), updated by the player that owns it.
	@Override
	public void update(Input input, int delta) {
	}
	
	// Activate or deactive it depending on the timer of the Player's powerUp.
	public void update(int time) {
		if(time <= 0) 
			this.deactivate();
		else
			this.activate();
	}
	
	// If it comes into contact with enemy lazers, destroy the enemy lazer.
	@Override
	public void contactSprite(Sprite other) {
		if(other instanceof Lazer && !((Lazer)other).getFriendly()) {
			other.deactivate();
		}
	}

}
