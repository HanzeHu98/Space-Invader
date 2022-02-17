import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import utilities.BoundingBox;

/**
 * abstract Sprite class
 * Contains the position, image (and thus the BoundingBox) and active (for rendering) of a Sprite.
 * Handles the updating and rendering of the Sprite.
 * 
 * @author  Hanze Hu 911700
 */

public abstract class Sprite {
	private Image image = null;
	private float posX;
	private float posY;
	private boolean active = true;
	
	/** Constructor for the Sprite Class.
	 * Creates the Image and set the position of the Sprite based on the given image source and position.
	 * @param imageSrc The String indicating the location(Source) of the image of this object.
	 * @param x A float point indicating the X position of the Sprite initially.
	 * @param y A float point indicating the Y position of the Sprite initially.
	 */
	public Sprite(String imageSrc, float x, float y) {
		try {
			image = new Image(imageSrc);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		this.posX = x;
		this.posY = y;
	}
	
	/** abstract update method for the Sprite Class, to be handled differently by subclasses.
	 * Creates the Image and set the position of the Sprite based on the given image source and position.
	 * @param input The player Input class that gives directions to how the player behaves.
	 * @param delta Time passed since last frame (milliseconds).
	 */
	public abstract void update(Input input, int delta);
	
	/** 
	 * Render method for the Sprite Class, draw the sprite at it's location if it is active.
	 */
	public void render() {
		// Only render Objects that are active
		if(this.getActive()) {
			image.drawCentered(posX, posY);
		}
	}
	
	/** abstract contactSprite method for the Sprite Class, to be handled differently by subclasses.
	 * Creates the Image and set the position of the Sprite based on the given image source and position.
	 * @param other The other sprite that this object is colliding with.
	 */
	public abstract void contactSprite(Sprite other);
	
	// The getters for the variables in this class. 
	public boolean getActive() { return active; }
	public BoundingBox getBoundingBox() {return new BoundingBox(image, posX, posY);}
	public Image getImage() { return this.image; }
	public float getX() { return posX; }
	public float getY() { return posY; }
	
	// The setters for the varaibles in this class.
	public void deactivate() { active = false; }
	public void activate() { active = true; }
	
	public void addX(float dx) { posX += dx; }
	public void setX(float dx) { posX = dx; }
	
	public void addY(float dy) { posY += dy; }
	public void setY(float dy) {  posY = dy; }

}
