package manhattan;

import main.Enums;
import main.Enums.oType;

public class Parkingspace implements MapObject{

	private oType type;
	private boolean doubled;
	
	public Parkingspace(oType type, boolean doubled) {
		this.type = type;
		this.doubled = doubled;
	}
	@Override
	public oType getType() {
		// TODO Auto-generated method stub
		return this.type;
	}

}
