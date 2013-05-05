import java.awt.*;
import java.net.URL;
import java.applet.Applet;

/**Cloud
 * @author Bradley Johns
 * The cloud of enemy units in Space invaders. Keeps track of
 * the number of enemies which it actually contains as well
 * as other valuable information indicating the state and
 * position of the cluster of enemy units and their relation
 * to the end of the game
 */

public class Cloud {
    
    private Enemy[][] _cluster;
    private Enemy _lowest; //The lowest enemy in the cluster
    private int _direction;

    private Applet _game;

    public Cloud(int sizeX, int sizeY, Applet newGame){
        _cluster = new Enemy[sizeX][sizeY];
        int xCord = 3;
        for (int i = 0; i < _cluster.length; i++) {
            xCord += 47;
            int yCord = 0;
            for (int j = 0; j < _cluster[i].length; j++) {
                _cluster[i][j] = new Enemy(xCord, yCord, _game);
                yCord += 35;
            }
        }
        _direction = 30;
        _game = newGame;
    }

    /*In the situation where the lowest value is removed find the
     * new lowest*/
    public int lowest() {
        for (int i = _cluster.length - 1; i >= 0; i--) {
            for (int j = 0; j < _cluster[i].length; j++) {
                if (!_cluster[i][j].isDestroyed()) {
                    _lowest = _cluster[i][j];
                }
            }
        }
        return _lowest.getY() + 30;
    }

    public boolean isEmpty() {
        int size = _cluster.length * _cluster[0].length;
        return getDestroyed() >= size;
    }

    /*Move all enemies in the clous at an equal pace*/
    private void shiftX(int increment) {
        for (int i = 0; i < _cluster.length; i++) {
            for (int j = 0; j < _cluster[i].length; j++) {
                _cluster[i][j].shiftX(increment);
            }
        }
    }

    private void shiftY(int increment) {
        for (int i = 0; i < _cluster.length; i++) {
            for (int j = 0; j < _cluster[i].length; j++) {
                _cluster[i][j].shiftY(increment);
            }
        }
    }

    /*Check whether or not a shot has hit a valid target*/
    public boolean checkShot(int xCord, int yCord) {
        for (int i = 0; i < _cluster.length; i++) {
            for (int j = _cluster[i].length - 1; j >= 0; j--) {
                if (!_cluster[i][j].isDestroyed() 
                    &&_cluster[i][j].contains(xCord, yCord)) {
                    _cluster[i][j].damage();
                    return true;
                }
            }
        }
        return false;
    }

    /*Get the right and left ends of the cloud*/
    private int right() {
        for (int i = _cluster.length - 1; i >= 0; i--) {
            for (int j = 0; j < _cluster[i].length; j++) {
                if (!_cluster[i][j].isDestroyed()) return _cluster[i][j].getX() + 30;
            }
        }
        return -1;
    }

    private int left() {
        for (int i = 0; i < _cluster.length; i++) {
            for (int j = 0; j < _cluster[i].length; j++) {
                if (!_cluster[i][j].isDestroyed()) return _cluster[i][j].getX();
            }
        }
        return -1;
    }

    /*Figure out the number of destroyed enemies in the cloud*/
    public int getDestroyed() {
        int destroyed = 0;
        for (int i = 0; i < _cluster.length; i++) {
            for (int j = 0; j < _cluster[i].length; j++) {
                if (_cluster[i][j].isDestroyed()) destroyed++;
            }
        }
        return destroyed;
    }

    /*Manage the full movement of the cloud*/
    public void moveCloud() {
        if (_direction > 0 && right() + _direction > _game.getWidth()) {
            _direction *= -1;
            shiftY(15);
            return;
        } else if (_direction < 0 && left() + _direction < 0) {
            _direction *= -1;
            shiftY(30);
            return;
        }

        shiftX(_direction);
    }

    public void paint(Graphics buffer) {
        Image drawing;
        for (int i = 0; i < _cluster.length; i++) {
            for (int j = 0; j < _cluster[i].length; j++) {
                if (j == 0) {
                    drawing = SpaceInvaders._sprites[0];
                } else if (j == 1 || j == 2) {
                    drawing = SpaceInvaders._sprites[1];
                } else {
                    drawing = SpaceInvaders._sprites[2];
                }
                if (!_cluster[i][j].isDestroyed()) {
                    _cluster[i][j].paint(buffer, drawing);
                }
            }
        }
    }
}
