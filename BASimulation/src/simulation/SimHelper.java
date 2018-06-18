package simulation;

import java.util.Random;

import main.Coordinate;
import main.Enums.direction;
import main.Enums.oType;
import manhattan.Entrance;
import manhattan.Intersection;
import manhattan.Manhattan;
import manhattan.Street;

public class SimHelper {
	
	public static direction getEnteringDirection(Coordinate pos, Manhattan matrix) {
		direction dir = null;
		Entrance et = (Entrance) matrix.map[pos.getX()][pos.getY()];
		
		switch(et.getDir())
		{
		case NORTH:
			dir = direction.SOUTH;
			break;
		case EAST:
			dir = direction.WEST;
			break;
		case SOUTH:
			dir = direction.NORTH;
			break;
		case WEST:
			dir = direction.EAST;
			break;
		}
		return dir;
	}
	
	public static Coordinate getDirectionStep(direction dir) {
		switch(dir)
		{
		case NORTH:
			return new Coordinate(0, -1);			
		case EAST:
			return new Coordinate(1, 0);
		case SOUTH:
			return new Coordinate(0, 1);			
		case WEST:
			return new Coordinate(-1, 0);
			
		}
		return null;
	}
	
	public static Intersection getFirstIntersection(Coordinate pos, direction dir, Manhattan matrix) {
		int rl = matrix.getRoadlength();
		int x =0;
		int y =0;
		switch(dir)
		{
		case NORTH:
			y = -rl  ;
			break;
		case EAST:
			x = rl;
			break;
		case SOUTH:
			y = rl ;
			break;
		case WEST:
			x = -rl ;
			break;
		}
		
		return (Intersection) matrix.map[pos.getX()+x][pos.getY()+y];
	}
	
	public static Coordinate selectTarget(Manhattan matrix) {
		int e = getRandomNumberInRange(1, matrix.getStreets().size());
		Street[] y = matrix.getStreets().get(e-1);
		Street x = null;
		while(true)
		{
		int i = getRandomNumberInRange(1, y.length);
		x = y[i -1 ];
		
		//Kein eingang als Ziel
		if(x.getType()!=oType.ENTRANCE)
		{
			break;
		}
		}
		
		return new Coordinate(x.getX(), x.getY());
		
	}

	public static Coordinate selectEntrance(Manhattan matrix) {
		//randomly selects entrance
		int e = getRandomNumberInRange(1, matrix.getEntranceList().size());
		Entrance x = matrix.getEntranceList().get(e-1);
		return new Coordinate(x.getX(), x.getY());
	}
	
	public static int getRandomNumberInRange(int min, int max) {

		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}

		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}

	public static direction getRightTurnDirection(direction dir) {
		switch(dir)
		{
		case NORTH:
			return direction.EAST;			
		case EAST:
			return direction.SOUTH;
		case SOUTH:
			return direction.WEST;
		case WEST:
			return direction.NORTH;
		}
		return null;
	}
	public static direction getOppositeDirection(direction dir) {
		switch(dir)
		{
		case NORTH:
			return direction.SOUTH;			
		case EAST:
			return direction.WEST;
		case SOUTH:
			return direction.NORTH;
		case WEST:
			return direction.EAST;
		}
		return null;
	}

	public static direction getLeftTurnDirection(direction dir) {
		switch(dir)
		{
		case NORTH:
			return direction.WEST;			
		case EAST:
			return direction.NORTH;
		case SOUTH:
			return direction.EAST;
		case WEST:
			return direction.SOUTH;
		}
		return null;
	}

}
