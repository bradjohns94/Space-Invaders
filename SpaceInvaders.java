import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.awt.Font.*;
import java.awt.Image.*;
import java.awt.geom.Rectangle2D;

// <applet width="600" height="600" code="SpaceInvaders.class"> Applet </applet>

/** SpaceInvaders
 * @author Bradley Johns
 * A clone of the classic video game "Space Invaders" in which a player pilots
 * a ship on a horizontal axis trying to destroy a cloud of enemies before they
 * hit the "ground"
 */

public class SpaceInvaders extends Applet {

    private Player _player;
    private Cloud _cluster;
    private Wall[] _walls;

    private Image _offScreen;
    private Graphics _buffer; //The image stored for double-buffering

    private int _tickCounter; //The number of times paint is called
    private int _roundCounter = 0; //The number of rounds the player is on

    //Score Variables
    private int _score = 0; //The players score in the game

    private boolean _isGameStarted = false; //states if the game has started 
    private boolean _isGameOver; //tells if the game is over

    public static Image[] _sprites = new Image[3];
    
    public void init() {
        //Start screen variables
        _offScreen = createImage(getWidth(), getHeight());
        _buffer = _offScreen.getGraphics();
        addKeyListener(new Keyboard(this));
        addMouseListener(new Mouse(this));
        
        //Initialize Game variables
        _player = new Player(getWidth() / 2, getHeight() - 30, this);
        _cluster = new Cloud(11, 5, this);
        _walls = new Wall[4];
        
        //Initializing walls
        int distance = getWidth() / _walls.length;
        int start = (distance / 2) - 25;
        for (int i = 0; i < _walls.length; i++) {
            _walls[i] = new Wall(50, 60, start, getHeight() - 125, this);
            start += distance;
        }
        addKeyListener(new Keyboard(this));

        //Initialize sprites
        _sprites[0] = getImage(getCodeBase(), "sprite1.png");
        _sprites[1] = getImage(getCodeBase(), "sprite2.png");
        _sprites[2] = getImage(getCodeBase(), "sprite3.png");

        _tickCounter = 0;
        _isGameOver = false;
    }

    public void paint(Graphics g) {
        /*Start by calling applet paint and cleaning up the display*/
        super.paint(g);
        Graphics2D g2 = (Graphics2D)_buffer;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);

        /*Display title/game over screen at the appropriate times*/
        if (!_isGameStarted) {
            titleScreen(g);
            repaint();
            return;
        }

        if (_isGameOver) {
            gameOver(g);
            return;
        }

        /*Draw basic elements that make up the game*/
        _buffer.setColor(Color.black);
        _buffer.fillRect(0, 0, getWidth(), getHeight()); //Draw the background

        _player.paint(_buffer); //Draw the player
        _cluster.paint(_buffer); //Draw the enemy cloud

        _buffer.setColor(Color.green);
        _buffer.setFont(new Font("Serif", Font.PLAIN, 15));
        FontMetrics metric = _buffer.getFontMetrics();
        String text = "Score: " + Integer.toString(_score);
        Rectangle2D textBox = metric.getStringBounds(text, _buffer);
        _buffer.drawString(text, 5, 20); 

        for (Wall i : _walls) {
            i.paint(_buffer); //Draw each wall
        }

        /*This game implements double bufferring, thus all data is stored into
         * a buffer before being drawn to the screen and not showing gaps between
         * images, the following line replaces the curent screen with the buffer
         */
        g.drawImage(_offScreen, 0, 0, this);

        _tickCounter++; //Increment the frame counter
        repaint();
    }

    public void update(Graphics g) {

        if (!_isGameStarted) {
            paint(g);
            return;
        }

        //Set a time to move the cluster at
        int time = 300 - (3 * _cluster.getDestroyed()) - (_roundCounter * 20);
        if (time < 50) time = 50;

        _player.incrementLasers(3);

        //Hit the walls and destroy the laser if there is a wall collission
        for (int i = 0; i < _walls.length; i++) {
             if (_walls[i].contains(_player.getLaserPosition())
                    && !_walls[i].canPass(_player.getLaserPosition()[0])) {
                    _walls[i].hit(_player.getLaserPosition()[0]);
                    _player.resetLaser();
             }
        }

        //Manage laser collissions with enemy units
        int[] lasers = _player.getLaserPosition();
        if (_cluster.checkShot(lasers[0], lasers[1])) {
            _player.resetLaser();    
            _score += (300 - time);
        }

        if (_tickCounter % time == 0) {
            _cluster.moveCloud(); //move the cluster
        }

        if (_cluster.isEmpty()) {
            changeRound(g); //Start the next round when the cloud is empty
        }

        if (_cluster.lowest() > _walls[0].getY()) {
            _isGameOver = true; //Prepare to display game over screen
        }

        paint(g);
    }

    public void titleScreen(Graphics g) {
        _buffer.setColor(Color.black);
        _buffer.fillRect(0, 0, getWidth(), getHeight());

        //Draw the actual text
        _buffer.setColor(Color.green);
        _buffer.setFont(new Font("Serif", Font.BOLD, 42));
        FontMetrics metric = _buffer.getFontMetrics();
        Rectangle2D title = metric.getStringBounds("Space Invaders", _buffer);
        int textX = (int)(getWidth() / 2) - (int)(title.getWidth() / 2);
        _buffer.drawString("Space Invaders", textX, (int)(getHeight() / 4));

        //Draw the subtext
        _buffer.setFont(new Font("Serif", Font.BOLD, 30));
        metric = _buffer.getFontMetrics();
        title = metric.getStringBounds("Click to Start", _buffer);
        textX = (int)(getWidth() / 2) - (int)(title.getWidth() / 2);
        _buffer.drawString("Click to Start", textX, (int)(getHeight() / 2));

        g.drawImage(_offScreen, 0, 0, this);
    }

    public void gameOver(Graphics g) {
        _buffer.setColor(Color.black);
        _buffer.fillRect(0, 0, getWidth(), getHeight());

        //Draw the actual text
        _buffer.setColor(Color.red);
        _buffer.setFont(new Font("Serif", Font.BOLD, 42));
        FontMetrics metric = _buffer.getFontMetrics();
        Rectangle2D gameOver = metric.getStringBounds("Game Over", _buffer);
        int textX = (int)((getWidth() / 2) - (gameOver.getWidth() / 2));
        _buffer.drawString("Game Over", textX, (int)(getHeight() / 4));

        //Draw your score
        _buffer.setFont(new Font("Serif", Font.BOLD, 30));
        metric = _buffer.getFontMetrics();
        String text = "Your Score: " + Integer.toString(_score);
        Rectangle2D textBox = metric.getStringBounds(text, _buffer);
        textX = (int)(getWidth() / 2) - (int)(textBox.getWidth() / 2);
        _buffer.drawString(text, textX, (int)(getHeight() / 2));

        //paint kill screen
        g.drawImage(_offScreen, 0, 0, this);
    }

    public void changeRound(Graphics g) {
        _roundCounter++;
        _buffer.setColor(Color.black);
        _buffer.fillRect(0, 0, getWidth(), getHeight());

        //Draw the actual text
        _buffer.setColor(Color.green);
        _buffer.setFont(new Font("Serif", Font.BOLD, 42));
        String text = "Round " + Integer.toString(_roundCounter + 1);
        FontMetrics metric = _buffer.getFontMetrics();
        Rectangle2D display = metric.getStringBounds(text, _buffer);
        int textX = (int)(getWidth() / 2) - (int)(display.getWidth() / 2);
        _buffer.drawString(text, textX, (int)(getHeight() / 4));

        g.drawImage(_offScreen, 0, 0, this);

        //Wait a second, then continue the program
        try {
            Thread.sleep(1000);
            init();
        } catch (InterruptedException e) {
            System.out.println("This code fails at failing.");
        }
    }

    private class Keyboard implements KeyListener {
        
        private Applet parent = null;

        private Keyboard(Applet newParent) {
            parent = newParent;
        }

        public void keyPressed(KeyEvent e) {
            int code = e.getKeyCode();

            /*Manage cases of player movement, delay is added into
             * player movement to prevent the program from accidentally
             * reading multiple movements at a time.
             */
            if (code == KeyEvent.VK_LEFT) {
                _player.shift(-3);
                try {
                    Thread.sleep(5);
                } catch (InterruptedException ex){
                    System.out.println("Cannot allow movement gap");
                }
            } else if(code == KeyEvent.VK_RIGHT) {
                _player.shift(3);
                try {
                    Thread.sleep(5);
                } catch (InterruptedException ex){
                    System.out.println("Cannot allow movement gap");
                }
            } else if (code == KeyEvent.VK_UP) {
                _player.fireLaser();
            }
        }

        public void keyReleased(KeyEvent e) { }
        public void keyTyped(KeyEvent e) { }

    }

    /*This class essentially exists for the purpose of allowing the player
     * to click the mouse and have control of the applet in order to move
     * past the title screen and begin the game
     */
    private class Mouse implements MouseListener {
        
        private Applet parent = null;

        public Mouse(Applet newParent) {
            parent = newParent;
        }

        public void mouseClicked(MouseEvent e) {
            _isGameStarted = true;
        }

        public void mouseEntered(MouseEvent e) { }
        public void mouseExited(MouseEvent e) { }
        public void mousePressed(MouseEvent e) { }
        public void mouseReleased(MouseEvent e) { }

    }
}
