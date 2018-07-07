package manhattan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import main.Coordinate;
import main.Enums;
import main.Enums.direction;
import main.Enums.oType;
import main.Enums.orientation;

public class Manhattan
{

	int entrances;
	int roadlength;
	int spotsperroad;

	boolean debug = false;

	ArrayList<Parkingspace> parkingspots = new ArrayList<Parkingspace>();
	ArrayList<Street[]> streets = new ArrayList<Street[]>();
	ArrayList<Entrance> entranceList = new ArrayList<Entrance>();
	ArrayList<Intersection> intersections = new ArrayList<Intersection>();

	public MapObject[][] map;

	public int getRoadlength()
	{
		return roadlength;
	}

	public void setRoadlength(int roadlength)
	{
		this.roadlength = roadlength;
	}

	public ArrayList<Intersection> getIntersections()
	{
		return intersections;
	}

	public void setIntersections(ArrayList<Intersection> intersections)
	{
		this.intersections = intersections;
	}

	public ArrayList<Entrance> getEntranceList()
	{
		return entranceList;
	}

	public void setEntranceList(ArrayList<Entrance> entranceList)
	{
		this.entranceList = entranceList;
	}

	public int getEntrances()
	{
		return entrances;
	}

	public void setEntrances(int entrances)
	{
		this.entrances = entrances;
	}

	public ArrayList<Street[]> getStreets()
	{
		return streets;
	}

	public void setStreets(ArrayList<Street[]> streets)
	{
		this.streets = streets;
	}

	public oType getType(int x, int y)
	{
		return null;
	}

	public Manhattan(int entrances, int roadlenght, int spotsperroad, int appParkingSpots) throws Exception
	{
		this.entrances = entrances;
		this.roadlength = roadlenght;
		this.spotsperroad = spotsperroad;
		if (spotsperroad > roadlenght * 2)
		{
			throw new Exception();

		}

		// create Map
		int totalLength = (roadlenght * (entrances + 1)) + entrances;
		map = new MapObject[totalLength][totalLength];

		// fill Map
		for (int y = 0; y < totalLength; y++)
		{
			// check for entrance
			for (int x = 0; x < totalLength; x++)
			{
				map[x][y] = determineType(x, y);
			}
		}
		// detect streets
		detectStreets(map, roadlenght);
		// addParking spots
		addParkingSpots(roadlenght, spotsperroad, appParkingSpots);

	}

	private void addParkingSpots(int roadlenght, int spotsperroad2, int appParkingSpots)
	{

		// Alle Stra�en haben hier selbe verteilung das kann hier aber ge�ndert werden
		int[] pos = getSpotPos(roadlenght, spotsperroad2);
		
		
		int[] posApp = getAppSpotPos(pos.length, appParkingSpots);

		// Parkpl�tz einf�gen
		for (Street[] item : streets)
		{
			int counter = 0;
			for (int i : pos)
			{
				boolean app = false;
				if (contains(posApp, counter))
				{
					app = true;
				}

				if (i <= roadlenght)
				{
					// links
					int x = item[i - 1].getX();
					int y = item[i - 1].getY();
					if (item[0].getOrientation() == orientation.H)
					{
						y -= 1;
					} else
					{
						x -= 1;
					}

					// auf �berschneidung pr�fen
					if (map[x][y].getType() != oType.PARKINGSPOT)
					{
						Parkingspace ps = new Parkingspace(oType.PARKINGSPOT, false, app,new Coordinate(x, y));
						parkingspots.add(ps);
						map[x][y] = ps;
					} else
					{
						Parkingspace ps = new Parkingspace(oType.PARKINGSPOT, true, app,new Coordinate(x, y));
						parkingspots.add(ps);
						map[x][y] = ps;
					}

				} else
				{
					// rechts
					int x = item[i - 1 - roadlenght].getX();
					int y = item[i - 1 - roadlenght].getY();
					if (item[0].getOrientation() == orientation.H)
					{
						y += 1;
					} else
					{
						x += 1;
					}
					// auf �berschneidung pr�fen
					if (map[x][y].getType() != oType.PARKINGSPOT)
					{
						Parkingspace ps = new Parkingspace(oType.PARKINGSPOT, false, app, new Coordinate(x, y));
						parkingspots.add(ps);
						map[x][y] = ps;
					} else
					{
						Parkingspace ps = new Parkingspace(oType.PARKINGSPOT, true, app,new Coordinate(x, y));
						parkingspots.add(ps);
						map[x][y] = ps;
					}

				}
				
				counter++;
			}

		}

	}

	private boolean contains(int[] posApp, int i)
	{
		for (int x : posApp)
		{
			if (x == i)
			{
				return true;
			}
		}
		return false;
	}

	private int[] getSpotPos(int roadlenght, int spotsperroad2)
	{

		int max = roadlenght * 2;

		if (max < spotsperroad2)
		{
			throw new IllegalArgumentException("Can't ask for more numbers than are available");
		}
		Random rng = new Random(); // Ideally just create one instance globally
		// Note: use LinkedHashSet to maintain insertion order
		Set<Integer> generated = new LinkedHashSet<Integer>();
		while (generated.size() < spotsperroad2)
		{
			Integer next = rng.nextInt(max) + 1;
			// As we're adding to a set, this will automatically do a containment check
			generated.add(next);
		}

		Integer[] targetArray = generated.toArray(new Integer[generated.size()]);
		int[] intArray = Arrays.stream(targetArray).mapToInt(Integer::intValue).toArray();
		return intArray;
	}

	private int[] getAppSpotPos(int roadlenght, int spotsperroad2)
	{

		int max = roadlenght - 1;

		if (max < spotsperroad2)
		{
			//this comes into play if all spots are app spots
			
			int[] out = new int[spotsperroad2];
			for(int i=0;i<out.length;i++)
			{
				out[i]=i;
			}
			return out;
		}
		Random rng = new Random(); // Ideally just create one instance globally
		// Note: use LinkedHashSet to maintain insertion order
		Set<Integer> generated = new LinkedHashSet<Integer>();
		while (generated.size() < spotsperroad2)
		{
			Integer next = rng.nextInt(max);
			// As we're adding to a set, this will automatically do a containment check
			generated.add(next);
		}

		Integer[] targetArray = generated.toArray(new Integer[generated.size()]);
		int[] intArray = Arrays.stream(targetArray).mapToInt(Integer::intValue).toArray();
		return intArray;
	}

	private void detectStreets(MapObject[][] map2, int roadlength)
	{

		int totalLength = map[0].length;
		Street[] street = new Street[roadlength];
		int partCounter = 0;
		boolean last = false;

		// Vertical
		for (int x = 0; x < totalLength; x++)
		{

			// check if entrance
			if (map[x][0].getType() == oType.ENTRANCE)
			{
				for (int y = 0; y < totalLength; y++)
				{
					if (map[x][y].getType() == oType.INTERSECTION)
					{

						last = true;

					} else
					{
						street[partCounter] = (Street) map[x][y];
						partCounter++;
						if (y == totalLength - 1)
						{
							last = true;
						}
					}

					// add to list
					if (last)
					{
						streets.add(street);
						street = new Street[roadlength];
						partCounter = 0;

						last = false;
					}
				}
			}
		}

		// Vertical
		for (int y = 0; y < totalLength; y++)
		{

			// check if entrance
			if (map[0][y].getType() == oType.ENTRANCE)
			{
				for (int x = 0; x < totalLength; x++)
				{
					if (map[x][y].getType() == oType.INTERSECTION)
					{

						last = true;

					} else
					{
						street[partCounter] = (Street) map[x][y];
						partCounter++;
						if (x == totalLength - 1)
						{
							last = true;
						}
					}

					// add to list
					if (last)
					{
						streets.add(street);
						street = new Street[roadlength];
						partCounter = 0;

						last = false;
					}
				}
			}
		}

	}

	public MapObject getMapObject(Coordinate c)
	{
		return map[c.getX()][c.getY()];
	}

	boolean o = false;

	private MapObject determineType(int x, int y)
	{

		int totalLength = map[0].length;

		if (y == 0)
		{
			if ((x + 1) % (roadlength + 1) == 0 && x != 0)
			{
				if (debug)
				{
					System.out.println(x + " " + y);
				}
				return addEntrance(new Entrance(direction.NORTH, x, y, orientation.V));
			}

		}
		if (y == totalLength - 1)
		{
			if ((x + 1) % (roadlength + 1) == 0 && x != 0)
			{
				if (debug)
				{
					System.out.println(x + " " + y);
				}
				return addEntrance(new Entrance(direction.SOUTH, x, y, orientation.V));
			}

		}

		if (x == 0)
		{
			if ((y + 1) % (roadlength + 1) == 0 && y != 0)
			{
				if (debug)
				{
					System.out.println(x + " " + y);
				}
				return addEntrance(new Entrance(direction.WEST, x, y, orientation.H));
			}

		}
		if (x == totalLength - 1)
		{
			if ((y + 1) % (roadlength + 1) == 0 && y != 0)
			{
				if (debug)
				{
					System.out.println(x + " " + y);
				}
				return addEntrance(new Entrance(direction.EAST, x, y, orientation.H));
			}

		}

		// Roads X
		if ((x + 1) % (roadlength + 1) == 0 && x != 0)
		{
			// check for intersection
			if ((y + 1) % (roadlength + 1) == 0 && y != 0)
			{
				return addIntersection(new Intersection(x, y));
			}

			return new Street(x, y, orientation.V);
		}
		// Roads Y
		if ((y + 1) % (roadlength + 1) == 0 && y != 0)
		{
			return new Street(x, y, orientation.H);
		}

		// Parking Spots

		// Fallback / remaining
		return new Dummy(oType.DUMMY);

	}

	private MapObject addIntersection(Intersection intersection)
	{
		intersections.add(intersection);
		return intersection;
	}

	private MapObject addEntrance(Entrance entrance)
	{
		entranceList.add(entrance);
		return entrance;
	}

	public Intersection getClosestIntersection(Coordinate target)
	{
		// as the navigation only drives to Intersections we need to find the closest
		// intersection
		Street targetStreet = (Street) map[target.getX()][target.getY()];

		boolean e1 = false;
		boolean e2 = false;
		boolean e3 = false;
		boolean e4 = false;

		if (targetStreet.getOrientation() == orientation.H)
		{

			for (int i = 1; i < roadlength; i++)
			{
				if (!e1)
				{
					if (map[target.getX() + i][target.getY()].getType() == oType.ENTRANCE)
					{
						e1 = true;
					}
				}
				if (!e2)
				{
					if (map[target.getX() - i][target.getY()].getType() == oType.ENTRANCE)
					{
						e2 = true;
					}
				}
				if (!e1)
				{
					if (map[target.getX() + i][target.getY()].getType() == oType.INTERSECTION)
					{
						if (debug)
						{
							System.out.println("Foundclosest");
						}
						return (Intersection) map[target.getX() + i][target.getY()];
					}
				}
				if (!e2)
				{
					if (map[target.getX() - i][target.getY()].getType() == oType.INTERSECTION)
					{
						if (debug)
						{
							System.out.println("Foundclosest");
						}
						return (Intersection) map[target.getX() - i][target.getY()];
					}
				}
			}
		} else
		{
			for (int i = 1; i < roadlength; i++)
			{
				if (!e3)
				{
					if (map[target.getX()][target.getY() + i].getType() == oType.ENTRANCE)
					{
						e3 = true;
					}
				}
				if (!e4)
				{
					if (map[target.getX()][target.getY() - i].getType() == oType.ENTRANCE)
					{
						e4 = true;
					}
				}
				if (!e3)
				{
					if (map[target.getX()][target.getY() + i].getType() == oType.INTERSECTION)
					{
						if (debug)
						{
							System.out.println("Foundclosest");
						}
						return (Intersection) map[target.getX()][target.getY() + i];
					}
				}
				if (!e4)
				{
					if (map[target.getX()][target.getY() - i].getType() == oType.INTERSECTION)
					{
						if (debug)
						{
							System.out.println("Foundclosest");
						}
						return (Intersection) map[target.getX()][target.getY() - i];
					}
				}
			}

		}
		System.err.println("Intersection search error");
		return null;
	}

	public int getTotalSpots()
	{
		int total = 0;
		for (Parkingspace item : parkingspots)
		{
			total++;
			// count double
			if (item.isDoubled())
			{
				total++;
			}
		}

		return total;
	}

	public int getAvailableSpots()
	{

		int total = 0;
		for (Parkingspace item : parkingspots)
		{

			if (item.free)
			{
				total++;
			}
		}

		return total;

	}

	public int getTotalAppSpots()
	{
		int total = 0;
		for (Parkingspace item : parkingspots)
		{
			if (item.isApp())
			{
				total++;
				// count double
				if (item.isDoubled())
				{
					total++;
				}
			}
		}

		return total;
	}

	public Parkingspace getAppinRange(Coordinate target)
	{
		List<Parkingspace> avail = new ArrayList<Parkingspace>();
		
		//check x and y absolut distance if < roadlengh
		for(Parkingspace app:parkingspots)
		{
			if(!app.isApp())
			{
				
			}
			else
			{
				
				//range check
				int x = Math.abs(app.loc.getX() - target.getX());
				int y = Math.abs(app.loc.getY() - target.getY());
				
				if(x <= roadlength+2&&y <= roadlength+2)
				{
					if(app.checkFree())
					{
					avail.add(app);
					}
				}
				
				
			}
			
			
		}
		
		//check if there is available spot
		if(avail.size()==0)
		{
			return null;
		}
		//determine closest spot
		Parkingspace temp = avail.get(0);
		
		Coordinate d = target.difference(temp.getLoc());
		int xD =Math.abs(d.getX());
		int yD =Math.abs(d.getY());
		
		int dis = yD+xD;
		
		for(Parkingspace item:avail)
		{
			d = target.difference(item.getLoc());
			xD =Math.abs(d.getX());
			yD =Math.abs(d.getY());
			
			if(xD+yD < dis)
			{
				dis=xD+yD;
				temp=item;
			}
			
		}
		
		return temp;
		
		
		
		
		
	}

	public int getAvailableAppSpots()
	{
		int total = 0;
		for (Parkingspace item : parkingspots)
		{
			if (item.isApp()&&item.checkFree())
			{
				total++;
				// count double
				if (item.isDoubled())
				{
					total++;
				}
			}
		}

		return total;
	}

}
