package manhattan;

import main.Enums;
import main.Enums.oType;

public class Dummy implements MapObject{

	private oType type;
	public Dummy(oType type) {
		this.type = type;
	}

	@Override
	public oType getType() {
		// TODO Auto-generated method stub
		return this.type;
	}

}
