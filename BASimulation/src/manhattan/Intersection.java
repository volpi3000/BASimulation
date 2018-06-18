package manhattan;

import main.Coordinate;
import main.Enums;
import main.Enums.oType;

public class Intersection implements MapObject  {
	
	private Coordinate loc;	

	public int getX() {
		return loc.getX();
	}

	public void setX(int x) {
		loc.setX(x);
	}

	public int getY() {
		return loc.getY();
	}

	public void setY(int y) {
		loc.setY(y);
	}

	public Coordinate getLoc() {
		return loc;
	}

	

	private oType type;
	public Intersection(int x, int y) {
		this.type = oType.INTERSECTION;
		loc = new Coordinate(x, y);
	}

	@Override
	public oType getType() {
		// TODO Auto-generated method stub
		return this.type;
	}

}
