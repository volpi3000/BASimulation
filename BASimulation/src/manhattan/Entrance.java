package manhattan;

import main.Enums;
import main.Enums.direction;
import main.Enums.oType;
import main.Enums.orientation;

public class Entrance extends Street{
	
	private direction dir;
	private oType type;
	

	

	public direction getDir() {
		return dir;
	}

	public void setDir(direction dir) {
		this.dir = dir;
	}

	public Entrance(direction dir,int x, int y, orientation or) {
		super(x,y,or);
		this.dir = dir;
		this.type = oType.ENTRANCE;
		
	}

	@Override
	public oType getType() {
		// TODO Auto-generated method stub
		return this.type;
	}

}
