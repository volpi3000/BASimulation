package main;

public class Coordinate {
	
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
	public Coordinate(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}
	public Coordinate(String c) {
		String[] xy = c.split(":");
		this.x = Integer.parseInt(xy[0]);
		this.y = Integer.parseInt(xy[1]);
	}
	
	public Coordinate difference(Coordinate a)
	{
		int x = this.x - a.getX();
		int y = this.y - a.getY();
		
		return new Coordinate(x,y);
	}
	
	@Override
	public boolean equals(Object other){
	    if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof Coordinate))return false;
	    
	    if ((other instanceof Coordinate))
	    {
	    	if(((Coordinate)other).x==this.x&&((Coordinate)other).y==this.y)
	    	{
	    		return true;
	    	}
	    }
	    
	    return false;
	   
	}
	@Override
	public String toString()
	{
		return this.x+":"+this.y;
	}
	
	
	public void add(Coordinate movement) {
		this.x +=movement.x;
		this.y +=movement.y;
		
	}
	

}
