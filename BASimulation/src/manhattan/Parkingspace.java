package manhattan;

import main.Enums;
import main.Enums.oType;

public class Parkingspace implements MapObject{

	private oType type;
	private boolean doubled;
	int capacity = 0;
	int in = 0;
	boolean free = true;;
	
	public Parkingspace(oType type, boolean doubled) {
		this.type = type;
		this.doubled = doubled;
		if(doubled)
		{
			capacity=2;
		}
		else
		{
			capacity=1;
		}
	}
	@Override
	public oType getType() {
		// TODO Auto-generated method stub
		return this.type;
	}
	
	public boolean checkFree()
	{
		return free;
	}
	
	public void parkCar()
	{
		if(free)
		{
			in++;
			
			if(in==capacity)
			{
				free=false;
			}
	
		}
		else
		{
			System.err.println("Tried to park in a full parking spot, check the free boolean");
		}
	}
	
	public void removeCar()
	{
		in--;
		free=true;
	}

}
