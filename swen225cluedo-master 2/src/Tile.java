import javax.swing.ImageIcon;

public class Tile {

    public ImageIcon image;
    public boolean isRoom;
    public boolean isHallway;
    public boolean isDoor;
    public boolean outOfBounds;
    public int x;
    public int y;
    public String roomName;

    public ImageIcon getImage() {
        return image;
    }

    public void setImage(ImageIcon image) {
        this.image = image;
    }

    public boolean isRoom() {
        return isRoom;
    }

    public void setRoom(boolean room) {
        isRoom = room;
    }

    public boolean isHallway() {
        return isHallway;
    }

    public void setHallway(boolean hallway) {
        isHallway = hallway;
    }

    public boolean isDoor() {
        return isDoor;
    }

    public void setDoor(boolean door) {
        isDoor = door;
    }

    public boolean isOutOfBounds() {
        return outOfBounds;
    }

    public void setOutOfBounds(boolean outOfBounds) {
        this.outOfBounds = outOfBounds;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getRoomName(){
        return roomName;
    }

    public Tile(ImageIcon i, boolean isRoom, boolean isHallway , boolean isDoor, boolean outOfBounds, int x, int y, String roomName){

        this.image=i;
        this.isRoom =isRoom;
        this.isHallway = isHallway;
        this.isDoor = isDoor;
        this.outOfBounds = outOfBounds;
        this.x =x;
        this.y = y;
        this.roomName =roomName;
    }


}
