package simulation;

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

import main.Coordinate;
import main.Enums.direction;
import main.Enums.oType;
import main.Enums.orientation;
import main.Enums.search;
import main.Enums.status;
import manhattan.Entrance;
import manhattan.Intersection;
import manhattan.Manhattan;
import manhattan.MapObject;
import manhattan.Parkingspace;
import manhattan.Street;
import navigation.Navigation;
import navigation.Vertex;

public class Simulation
{

	Manhattan matrix;
	ArrayList<Car> cars = new ArrayList<Car>();
	ArrayList<Car> carsRemove = new ArrayList<Car>();
	ArrayList<Metric> metrics = new ArrayList<Metric>();
	ArrayList<SpotMetric> spotMetrics = new ArrayList<SpotMetric>();

	final Map<String, String> spawn;

	final Navigation nav;
	final int runtime;

	int globalTime = 1;

	int metersPerSecond;
	// int carsPerSecond;

	final int parkingDurationMin;
	final int parkingDurationMax;
	final double spawnMultiplikator;
	final double appUsers;

	boolean debug = false;

	public Simulation(Manhattan matrix, Navigation nav, int metersPerSecond, Map<String, String> spawn,
			double spawnMultiplikator, int runtime, int parkingDurationMin, int parkingDurationMax,
			double percentAppUser)
	{
		this.matrix = matrix;
		this.nav = nav;
		this.metersPerSecond = metersPerSecond;
		this.spawn = spawn;
		this.runtime = runtime;
		this.parkingDurationMin = parkingDurationMin;
		this.parkingDurationMax = parkingDurationMax;
		this.spawnMultiplikator = spawnMultiplikator;
		this.appUsers=percentAppUser;
	}

	public void run()
	{
		System.out.println("Start of Simulation");

		// main loop
		while (true)
		{
			// spawnCars
			createCars();
			// carLogic
			move();
			// collectMetrics
			collectMetrics();

			globalTime++;

			printProgress(runtime, globalTime);
			// end of simulation
			if (globalTime >= runtime)
			{
				endSimulation();
				System.out.println("End of Simulation");
				break;
			}
		}

	}

	private void collectMetrics()
	{
		int totalAvailable = 0;

		totalAvailable = matrix.getAvailableSpots();
		spotMetrics.add(new SpotMetric(globalTime, totalAvailable, 0));

	}

	private void endSimulation()
	{
		// Making sure all Metrics are there
		for (Car c : cars)
		{
			c.getData().setCarFailed(true);
		}
		carsRemove.addAll(cars);
		updateCarList();

	}

	double currentProgress = 0;

	private void printProgress(int runtime2, int globalTime2)
	{
		// every percent
		double intervall = 1.0;
		double current = ((globalTime2 * 1.0) / (runtime2 * 1.0)) * 100;
		if (current - currentProgress > intervall)
		{
			currentProgress = current;
			System.out.println("Progress :" + (int) current + "% " + cars.size());
		}

	}

	private void move()
	{
		// iterate through all cars and determine behaviour
		for (int i = 0; i < metersPerSecond; i++)
		{
			// remove cars
			updateCarList();

			for (Car car : cars)
			{
				switch (car.state)
				{
				case ONROUTE:
					navigateToTargetLocation(car);
					break;
				case SEARCHING:
					naturalSearch(car);
					break;
				case PARKED:
					parkingTimeChecker(car);
					break;
				case ENTERING:
					break;
				default:
					break;

				}
				if (car.getState() != status.PARKED)
				{
					// here the actual movement happens according to speed
					Coordinate movement = SimHelper.getDirectionStep(car.dir);
					// System.out.println("Moving Car by: "+movement.toString());
					car.getPosition().add(movement);
					car.getData().distancePlusOne();
					if (car.getState() == status.SEARCHING)
					{
						car.getData().distanceSearchingPlusOne();
					}

					if (debug)
					{
						System.out.println("Moving Car to: " + car.getPosition().toString());
					}
					// check for spot
					checkCarLoc(car);
					// remove car if car travelled maximum distance
					checkDistance(car);

				}

			}

		}

	}

	private void checkDistance(Car car)
	{

		if (car.getData().getDistanceSearchingTravelled() >= (matrix.getRoadlength() * 16) + 20)
		{
			car.getData().setCarFailed(true);
			car.getData().setTravelEndTime(0);
			carsRemove.add(car);
		}

	}

	private void updateCarList()
	{

		for (Car c : carsRemove)
		{
			// write Metrics to List;
			metrics.add(c.getData());
		}
		cars.removeAll(carsRemove);
		carsRemove.clear();

	}

	private void parkingTimeChecker(Car car)
	{

		if (globalTime - car.getParkingStart() >= car.getParkingDuration())
		{

			// remove Car
			carsRemove.add(car);
			// free Parkingspace
			Parkingspace current = (Parkingspace) matrix.getMapObject(car.getPosition());

			current.removeCar();
		}

	}

	private void naturalSearch(Car car)
	{
		MapObject loc = matrix.getMapObject(car.getPosition());
		// first car drives to the next intersection
		if (loc.getType() == oType.INTERSECTION)
		{

			if (debug)
			{
				System.out.println("Reached: " + car.getPosition().toString());
			}

			if ((Intersection) loc == car.getSearchStart())
			{

				if (!car.isIgnoreInit())
				{
					if (debug)
					{
						System.out.println("Reached initial");
					}
					// origin is reached change direction
					car.setDir(SimHelper.getLeftTurnDirection(car.getDir()));
				}

			} else
			{
				// setup right turn
				car.setDir(SimHelper.getRightTurnDirection(car.getDir()));
			}

		}
		if (loc.getType() == oType.ENTRANCE)
		{
			// Turn around when entrance is reached
			car.setDir(SimHelper.getOppositeDirection(car.getDir()));
		}

	}

	private void checkCarLoc(Car car)
	{
		// links und rechts nach parkplätzen schauen und parken , wenn auto im richtigen
		// modus#

		if (car.getState() == status.SEARCHING)
		{
			int x = 0;
			int y = 0;
			// Wenn in eingängen geparkt werden soll muss das hier eingefügt werden
			if (matrix.getMapObject(car.getPosition()).getType() == oType.STREET)
			{
				Street on = ((Street) matrix.getMapObject(car.getPosition()));

				if (on.getOrientation() == orientation.H)
				{
					// oben / unter

					y = 1;

				} else
				{
					// links rechts
					x = 1;
				}

			}

			Coordinate co = car.getPosition();

			MapObject check1 = matrix.map[co.getX() + x][co.getY() + y];
			MapObject check2 = matrix.map[co.getX() - x][co.getY() - y];
			Coordinate one = new Coordinate(co.getX() + x, co.getY() + y);
			Coordinate two = new Coordinate(co.getX() - x, co.getY() - y);
			boolean oneIsSpot = isParking(check1);
			boolean twoIsSpot = isParking(check2);
			boolean oneIsFree = isSpotFree(check1, oneIsSpot);
			boolean twoIsFree = isSpotFree(check2, twoIsSpot);

			if (oneIsFree)
			{

				((Parkingspace) check1).parkCar();
				car.setState(status.PARKED);
				car.setParkingStart(globalTime);
				car.getData().setTravelEndTime(globalTime);
				car.setPosition(one);
				if (debug)
				{
					System.out.println("Found parking spot!");
				}
			} else
			{
				if (twoIsFree)
				{
					((Parkingspace) check2).parkCar();
					car.setState(status.PARKED);
					car.setParkingStart(globalTime);
					car.getData().setTravelEndTime(globalTime);
					car.setPosition(two);
					if (debug)
					{
						System.out.println("Found parking spot!");
					}
				}
			}

		}

	}

	private boolean isParking(MapObject check1)
	{
		if (check1.getType() == oType.PARKINGSPOT)
		{
			// kein App Parkingspotsnehmen
			if (!((Parkingspace) check1).isApp())
			{
				return true;
			}
		}

		return false;
	}

	private boolean isSpotFree(MapObject check, boolean isSpot)
	{

		if (!isSpot)
		{
			return false;
		}

		if (((Parkingspace) check).checkFree())
		{
			return true;
		}

		return false;
	}

	private void navigateToTargetLocation(Car car)
	{
		// check if car is at intersection, if no navigation is necessary
		MapObject loc = matrix.map[car.getPosition().getX()][car.getPosition().getY()];
		if (loc.getType() == oType.INTERSECTION)
		{ // quasi ein assert
			if (!((Intersection) loc).getLoc().equals(car.path.getFirst().getMapObject().getLoc()))
			{
				System.err.println("evil error in navigation");
			}
			// überprüfen ob Ziel erreicht
			if (car.path.size() == 1)
			{
				if (debug)
				{
					System.out.println("Final Intersection reached, starting search");
				}
				// setze direction in richtung ziel
				car.setDir(nav.getNewDirection(car.getPosition(), car.target));
				// Car behavior changes
				car.setState(status.SEARCHING);
				// Remember where search started
				car.setSearchStart((Intersection) loc);
			} else
			{
				if (debug)
				{
					System.out.println(
							"Reached intersection: " + ((Intersection) loc).getX() + ":" + ((Intersection) loc).getY());
				}
				// remove reached location;
				car.path.removeFirst();
				car.setNextLoc(car.path.getFirst().getMapObject());
				if (debug)
				{
					System.out.println(
							"New Target Intersection is: " + car.getNextLoc().getX() + ":" + car.getNextLoc().getY());
				}
				// set up new travel direction
				car.setDir(nav.getNewDirection(car.getPosition(), car.getNextLoc().getLoc()));
				if (debug)
				{
					System.out.println("Direction of Travel is: " + car.getDir().name());
				}
			}
		}

	}

	private void createCars()
	{
		// Here we should decide if a new car should spawn depending on settings
		while (spawnCarInterval())
		{

			// select randaom entrance
			Coordinate pos = SimHelper.selectEntrance(matrix);
			// select random target
			Coordinate target = SimHelper.selectTarget(matrix);
			// select random parkingDuration
			int parkingDuration = determineParkingDuration();
			// getTime
			int creationTime = getTime();
			// setStatus
			status state = status.ONROUTE;
			// set Start direction
			direction dir = SimHelper.getEnteringDirection(pos, matrix);
			// set first target
			Intersection currentTarget = SimHelper.getFirstIntersection(pos, dir, matrix);
			// get navigation instructions
			LinkedList<Vertex> path = nav.getInstructions(currentTarget, matrix.getClosestIntersection(target));

			if (debug)
			{
				System.out.println("Created Car at: " + pos.toString());
			}
			if (debug)
			{
				System.out.println("Target is: " + target.toString());
			}
			if (debug)
			{
				System.out.println("Starting Intersection is: " + currentTarget.getLoc().toString());
			}

			// for (Vertex v : path) {
			// System.out.println("Path: " + v.getMapObject().getLoc().toString());
			// }

			// add Car to List
			cars.add(new Car(pos, search.NATURAL, target, parkingDuration, creationTime, state, dir, currentTarget,
					path));
		}

	}

	double carsSpawned = 0;

	private boolean spawnCarInterval()
	{

		String test = spawn.get("" + globalTime);

		if (test == null)
		{
			System.err.println("Couldn't find current Second: " + globalTime);
		}

		double amount = Double.parseDouble(test);
		carsSpawned += (amount / 3600) * spawnMultiplikator;
		if (carsSpawned > 1)
		{
			carsSpawned -= 1;
			return true;
		}

		return false;

	}

	private int getTime()
	{

		return globalTime;
	}

	private int determineParkingDuration()
	{

		return SimHelper.getRandomNumberInRange(parkingDurationMin, parkingDurationMax);
	}

	public ArrayList<Metric> getMetrics()
	{

		return metrics;
	}

	public ArrayList<SpotMetric> getSpotMetrics()
	{
		// TODO Auto-generated method stub
		return spotMetrics;
	}

}
