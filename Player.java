import java.awt.*;
import java.applet.Applet;

/** Player
 * @author Bradley Johns
 * Representation of the player's unit in space invaders.
 * This class keeps track of the coordinate positions of both
 * the player themself and any lasers that may have been fired
 */

public class Player {
    
    private int _playerX; //The Center X coordinate of the player
    private int _playerY; //The upper edge of the character
    private int _laserX;
    private int _laserY;

    private Applet _game; //A reference to obtain game variables

    public Player(int startX, int startY, Applet newGame) {
        _playerX = startX;
        _playerY = startY;
        _laserX = -1;
        _laserY = -1;
        _game = newGame;
    }

    /*Simple methods allowing for access/mutation of the players
     * position. Players in space invaders can only move along the
     * horizontal axis*/

    public int getX() {
        return _playerX;
    }

    public int getY() {
        return _playerY;
    }

    public void shift(int increment) {
        _playerX += increment;
        if (_playerX > _game.getWidth()) _playerX = _game.getWidth() - 5;
        if (_playerX < 0) _playerX = 5;
    }

    /*Methods relating to the lasers checking whether or not
     * one may be fired as well as giving the positions of the
     * lasers.*/

    /*Checks if a laser can fire by iterating through the list of
     * positions. Returns the index if a laser can be fired, otherwise
     * returns -1*/

    public boolean canFire() {
        return _laserX == -1;
    }

    /*Set an initial firing point for the lasers then increment them
     * at a regular pace*/

    public void fireLaser() {
        if (canFire()) {
            _laserX = _playerX;
            _laserY = _playerY;
        }
    }

    public void incrementLasers(int increment) {
        _laserY -= increment;
        if (_laserY < 0) {
            resetLaser();
        }
    }

    public int[] getLaserPosition() {
        int[] position = new int[2];
        position[0] = _laserX;
        position[1] = _laserY;
        return position;
    }

    /*Set the laser back to its default location when finished*/

    public void resetLaser() {
        _laserX = -1;
        _laserY = -1;
    }

    public void paint(Graphics buffer) {
        buffer.setColor(Color.green);
        int[] xCords = {_playerX, _playerX - 5,
                        _playerX, _playerX + 5};
        int[] yCords = {_playerY, _playerY + 20,
                        _playerY + 15, _playerY + 20};
        buffer.fillPolygon(xCords, yCords, 4);
        //Note: Player is 10x20

        buffer.setColor(Color.red);
        buffer.fillOval(_laserX - 1, _laserY - 1, 3, 3);
    }
}
