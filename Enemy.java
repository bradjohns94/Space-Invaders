import java.awt.*;
import java.applet.Applet;

/**Enemy
 * @author Bradley Johns
 * The enemy unit in space invaders. Shifts down with the cloud
 * in a predictable order gradually increasing speed as it moves.
 * The enemy itself keeps track of its position and essentially
 * nothing else
 */

public class Enemy {

    private int _positionX; //Center X coordinate
    private int _positionY; //Bottom Y coordinate
    private int _indexX; //The X index in relation to the cloud
    private int _indexY; //The Y index in relation to the cloud
    private int _damage; //Damage to the enemy unit

    private Applet _game;

    public Enemy(int startX, int startY, Applet newGame) {
        _positionX = startX;
        _positionY = startY;
        _indexX = -1;
        _indexY = -1;
        _damage = 0;

        _game = newGame;
    }

    public int getX() {
        return _positionX;
    }

    public int getY() {
        return _positionY;
    }

    /*Move the enemy units*/
        
    public void shiftX(int increment) {
        _positionX += increment;
    }

    public void shiftY(int increment) {
        _positionY += increment;
    }

    public int getXIndex() {
        return _indexX;
    }

    public int getYIndex() {
        return _indexY;
    }

    public void setIndex(int x, int y) {
        _indexX = x;
        _indexY = y;
    }

    /*Check if a location is within the hitbox*/
    public boolean contains(int x, int y) {
        if (x >= _positionX && x <= _positionX + 30) {
            return (y >= _positionY && y <= _positionY + 30);
        }
        return false;
    }

    /*Check if the unit has been hit*/
    public boolean isDestroyed() {
        return _damage > 0;
    }

    public void damage() {
        _damage++;
    }

    public void paint(Graphics buffer, Image drawing) {
        buffer.drawImage(drawing, _positionX, _positionY, 30, 30, _game);
    }
}
