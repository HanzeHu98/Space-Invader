import org.newdawn.slick.Input;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Graphics;

/**
 * Unique World class for the game.
 * Handles initialisation, updating, rendering and interaction of Sprites, as well as the scrolling of
 * the background image.
 * 
 * @author  Hanze Hu 911700
 */
public class World {
    // The position of the background, used for calculating the scroll.
    private float backGround;
    
    private Graphics score = new Graphics(200, 100);
    
    // Declare the integers used to calculate tiling of the background.
    private int goIntoHeight;
    private int goIntoWidth;
    
    // The initial Y position of all enemy Sprite.
    private final int enemyY = -64;
    
    // Declare and create the Sprite or arrays of Sprite used for this world .
    Player player = new Player();
    private ArrayList<Enemy> enemyList = new ArrayList<>();
    private static ArrayList<PowerUps> powerUpList = new ArrayList<>();
    private Image imageLife;
    
    // Initialise the background image and get its height and width.
    private Image background = new Image("res/space.png");
	private final int height = background.getHeight();
	private final int width = background.getWidth();
    
	/** Constructor method for the World class.
	 * Creates all the enemy Sprite given by the waves.txt file.
	 */
	public World() throws SlickException  {
		// Perform initialisation logic.
		imageLife = new Image("res/lives.png");
		
		// Reads the waves.txt file and create enemy Sprite to place into the enemy ArrayList.
		try(BufferedReader br = new BufferedReader(new FileReader("res/waves.txt"))){
			String text;
			while((text = br.readLine()) != null) {
				if(text.charAt(0) == '#') {
					continue;
				}
				else {
					String words[] = text.split(",");
					int enemyX = Integer.parseInt(words[1]);
					int enemyDelay = Integer.parseInt(words[2]);
					if(words[0].equals("BasicEnemy")) {
						enemyList.add(new BasicEnemy(enemyX, enemyY, enemyDelay));
					}
					else if (words[0].equals("SineEnemy")) {
						enemyList.add(new SineEnemy(enemyX, enemyY, enemyDelay));
					}
					else if (words[0].equals("BasicShooter")) {
						enemyList.add(new BasicShooter(enemyX, enemyY, enemyDelay));
					}
					else if (words[0].equals("Boss")) {
						enemyList.add(new Boss(enemyX, enemyY, enemyDelay));
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** Create a powerUp object to place into the powerUp object ArrayList.
	 * @param type an integer that indicates the type of powerUp to create, 0 for shield and 1 for shooting speed.
	 * @param x A float that indicates the X position to create the object.
	 * @param y A float that indicates the Y position to create the object.
	 */
	public static void createPower(int type, float x, float y) {
		powerUpList.add(new PowerUps(type, x, y));
	}
	
	/** Update everything contained in the World class.
	 * Including background, player, Enemy objects, Lazer objects and PowerUp objects, and test their interactions.
	 * @param input The player Input class that gives directions to how the player behaves.
	 * @param delta Time passed since last frame (milliseconds).
	 */
	public void update(Input input, int delta)
			throws SlickException{
		
		// Makes the game move 5 times faster.
		if (input.isKeyDown(Input.KEY_S)){
			delta = delta * 5;
		}
		
		// Updates the location of the scrolling background.
		backGround += 0.2*delta;
    	if(backGround >= height) {
    		backGround = 0;
    	}
    	
        // Testing for contact between enemy and player, only if the enemy player is active.
        for(Enemy enemy: enemyList) {
        	enemy.update(input, delta);
        	// Test for contact between enemies and player.
        	if(enemy.getBoundingBox().intersects(player.getBoundingBox()) && enemy.getActive() && player.getImmune()<0) {
        		enemy.contactSprite(player);
        		player.contactSprite(enemy);
        	}
        	// Test for contact between enemy's Lazer and player, from BasicShooter objects.
        	if(enemy instanceof BasicShooter) {
        		for(Lazer lazer: ((BasicShooter)enemy).getLazers()){
        			if(player.getBoundingBox().intersects(lazer.getBoundingBox()) && lazer.getActive()) {
        				player.contactSprite(lazer);
        				lazer.contactSprite(player);
        			}
        		}
        	}
        	// Test for contact between enemy's Lazer and player, from Boss objects.
        	else if(enemy instanceof Boss) {
        		for(Lazer lazer: ((Boss)enemy).getLazers()){
        			if(player.getBoundingBox().intersects(lazer.getBoundingBox()) && lazer.getActive()) {
        				player.contactSprite(lazer);
        				lazer.contactSprite(player);
        			}
        		}
        	}
        	
        	// Test for contact between player's lazer and enemy.
        	for(Lazer lazer: player.getLazers()) {
        		if(enemy.getBoundingBox().intersects(lazer.getBoundingBox()) && lazer.getActive() && enemy.getActive()) {
        			enemy.contactSprite(player);
        			lazer.contactSprite(enemy);
        		}
        	}
        }
        
        // Update the player sprite if it is supposed to move.
        player.update(input, delta);
        
        // Update the list of PowerUps
        for(PowerUps powerUp: powerUpList) {
        	powerUp.update(input, delta);
        	if(powerUp.getBoundingBox().intersects(player.getBoundingBox()) && powerUp.getActive()){
        		player.addPowerUpTime(powerUp.getType(), 5000);
        		powerUp.contactSprite(player);
        	}
        }
	}
	
	/** Render everything contained in the World class.
	 * Including background, player, Enemy objects, Lazer objects and PowerUp objects.
	 */
	public void render() throws SlickException {
		// Renders and scrolls through the background according to the backGround count.
		
		// Calculate how many copies of background to tile and render (number of times it goes into
		// the screen height and width based on its position.
    	if (App.SCREEN_HEIGHT % height==0){
        	goIntoHeight = App.SCREEN_HEIGHT/height;
    	}else {
    		goIntoHeight = App.SCREEN_HEIGHT/height + 1;
    	}
    	
    	if (App.SCREEN_WIDTH % width==0){
        	goIntoWidth = App.SCREEN_WIDTH/width;
    	}else {
    		goIntoWidth = App.SCREEN_WIDTH/width + 1;
    	}
    	
    	// Render the background based on the number of tiles that should be present.
    	for(int i = 0; i <= goIntoWidth; i++) {
			for(int j = 0; j<=goIntoHeight; j++) {
				background.draw(i*width, backGround- App.SCREEN_HEIGHT + j*height);
			}
		}
    	
    	// Render the amount of lives left and the player's score.
    	for(int i = 0; i< player.getLife(); i++) {
    		imageLife.draw(20 + i * 40, 696);
    	}
    	score.drawString("Score: " + player.getScore(), 20, 738);
    	
    	// Render the player Sprite after update.
    	player.render();
    	
		// Draw all of the other Sprite in the game.
		for(Enemy enemy : enemyList) {
			enemy.render();
		}
        for(PowerUps powerUp: powerUpList) {
        	powerUp.render();
        }
	}
}
