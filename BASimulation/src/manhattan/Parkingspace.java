package manhattan;

import main.Coordinate;
import main.Enums;
import main.Enums.oType;

public class Parkingspace implements MapObject{

	private oType type;
	private boolean doubled;
	int capacity = 0;
	int in = 0;
	boolean free = true;;
	final boolean app;
	final Coordinate loc;
	
	public Parkingspace(oType type, boolean doubled, boolean app,Coordinate loc) {
		this.type = type;
		this.doubled = doubled;
		this.app=app;
		this.loc=loc;
		if(doubled)
		{
			capacity=2;
		}
		else
		{
			capacity=1;
		}
	}
	
	public boolean isApp() {
		return app;
	}
	
	public Coordinate getLoc()
	{
		return loc;
	}

	public boolean isDoubled() {
		return doubled;
	}

	public void setDoubled(boolean doubled) {
		this.doubled = doubled;
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
