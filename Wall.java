import java.awt.*;
import java.applet.Applet;

/**Wall
 * @author Bradley Johns
 * An instance of a barrier separating the player from
 * the enemies in space invaders. Walls have several reigons
 * that take several shots from the player or colission with
 * an enemy unit to break through.
 */

public class Wall {
    
    private int[] _segments; //The reigons of the wall and their damage count
    private int _xPosition;
    private int _yPosition;
    private int _xSize;
    private int _ySize;

    private Applet _game;

    public Wall(int xSize, int ySize, int startX, int startY, Applet newGame) {
        _segments = new int[xSize + 1];
        for (int i = 0; i < _segments.length; i++) {
            _segments[i] = 0;
        }

        _xSize = xSize;
        _ySize = ySize;
        _xPosition = startX;
        _yPosition = startY;

        _game = newGame;
    }

    /*Check to see if a shot will pass through the wall at a given position*/
    public boolean canPass(int position) {
        return _segments[position - _xPosition] >= _ySize;
    }

    /*Increment the hit counter on the wall*/
    public void hit(int position) {
        _segments[position - _xPosition] += 1;
    }

    public int getX() {
        return _xPosition;
    }

    public int getY() {
        return _yPosition;
    }

    public void paint(Graphics buffer) {
        buffer.setColor(Color.green);
        buffer.fillRect(_xPosition, _yPosition, _xSize, _ySize);

        //Draw the gaps from where the wall was pierced
        buffer.setColor(Color.black);
        for (int i = 0; i < _segments.length; i++) {
            int startX = _xPosition + i;
            int startY = _yPosition + (_ySize - _segments[i]);
            buffer.fillRect(startX, startY, 3, _segments[i]);
        }
    }

    /*States whether or not a pixel falls within the wall*/
    public boolean contains(int[] position) {
        if (position[0] >= _xPosition && position[0] <= (_xPosition + _xSize)) {
            return (position[1] > _yPosition && position[1] < (_yPosition + _ySize));
        }
        return false;
    }
}
