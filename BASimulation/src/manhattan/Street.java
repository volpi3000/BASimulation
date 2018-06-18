package manhattan;

import main.Enums;
import main.Enums.direction;
import main.Enums.oType;
import main.Enums.orientation;

public class Street implements MapObject{
	private oType type;
	private orientation or;
	private int x;
	private int y;	

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

	public Street(int x, int y, orientation or) {
		this.type = oType.STREET;
		this.x = x;
		this.y= y;
		this.or = or;
	}

	@Override
	public oType getType() {
		// TODO Auto-generated method stub
		return this.type;
	}
	public orientation getOrientation() {
		// TODO Auto-generated method stub
		return this.or;
	}

}
