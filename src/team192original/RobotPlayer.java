package team192original;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import battlecode.common.Clock;
import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;

public class RobotPlayer {
	
	static RobotController rc;
	
	/*
	 * Robot properties for engaging purposes.
	 */
	static enum robotProperties {
		ATTACK_POWER,
		HEALTH_CURRENT,
		HEALTH_MAX,
		DISTANCE
	}
	
	/* 
	 * 0 = straight ahead;
	 * 1 = 45° rotation to the right;
	 * 2 = 90° rotation to the right;
	 * 3 = 135° rotation to the right;
	 * 4 = 180° rotation to the right;
	 * 
	 * Negative values = rotation to the left;
	 * 
	 */
	static int[] possibleDirections = new int[]{0,1,-1,2,-2,3,-3,4};
	static ArrayList<MapLocation> pastLocations = new ArrayList<MapLocation>();
	
	// TODO we should find the starting corner, to decide in which opposite corner the enemy is. 
	static Direction movingDirection = Direction.NORTH_EAST;
	
	public static void run(RobotController rcIn){
		rc = rcIn;
		
		while(true){
			try {
				repeatedActions();
				Clock.yield();
			} catch(GameActionException e){
				e.printStackTrace();
			}
		
		}
	}

	private static void repeatedActions() throws GameActionException {
		RobotInfo[] enemies = rc.senseHostileRobots(rc.getLocation(), rc.getType().attackRadiusSquared);
		
		/*
		 *  Here we can choose the parameter to prioritise in an attack (e.g. the foe with the least attacking power or the nearest)
		 *  
		 *  CONS: we are sorting the list each turn, even if the hostileRobots detected are the same, so it's useless if for example we are sorting by HEALTH_MAX
		 */
		ArrayList<RobotInfo> enemiesSorted = sortBy(enemies, robotProperties.HEALTH_CURRENT);
		
		// destroy anything in your path, attacking the weakest (by property previously chosen) robot first.
		if(enemies.length > 0 && rc.getType().canAttack()){
			
			if(rc.isWeaponReady()){
				MapLocation enemyLocation = enemiesSorted.get(0).location;
				if(rc.canAttackLocation(enemyLocation)){
					rc.attackLocation(enemyLocation);
				}
			}
		} else { 
			// move forward
			if(rc.isCoreReady()){
				moveForwardish(movingDirection);
			}
			
		}
	}
	
	

	private static ArrayList<RobotInfo> sortBy(RobotInfo[] enemies, robotProperties property) {
		ArrayList<RobotInfo> enemiesSorted = new ArrayList<RobotInfo>(Arrays.asList(enemies));
		switch(property){
		case ATTACK_POWER:
			Collections.sort(enemiesSorted, (r1, r2) -> Double.compare(r1.attackPower,r2.attackPower));
			break;
		case DISTANCE:
			Collections.sort(enemiesSorted, new Comparator<RobotInfo>() {
				
				@Override
				public int compare(RobotInfo o1, RobotInfo o2) {
					
					return Integer.compare(rc.getLocation().distanceSquaredTo(o1.location),
							rc.getLocation().distanceSquaredTo(o2.location));
				}
				
			});
			break;
		case HEALTH_CURRENT:
			Collections.sort(enemiesSorted, (r1, r2) -> Double.compare(r1.health,r2.health));
			break;
		case HEALTH_MAX:
			Collections.sort(enemiesSorted, (r1, r2) -> Double.compare(r1.maxHealth,r2.maxHealth));
			break;
		default:
			break;
		}
		return enemiesSorted;
	}

	
	
	/*
	 * Forecasting movements before actually move = less effort, ore efficiency! (Hint in YouTube video: Battlecode 2016 Exiting Pockets part 2)
	 * TODO Improve by first looking sense let's say the next 20 tiles, if we find a path, then we can move.
	 */
	private static void moveForwardish(Direction ahead) throws GameActionException {
		for(int i:possibleDirections){
			Direction candidateDirection = Direction.values()[(ahead.ordinal()+i+8)%8];
			MapLocation candidateLocation = rc.getLocation().add(candidateDirection);
			
			// check if we can move to a candidate direction which it has not been visited before (in the last 20 locations) 
			if(rc.canMove(candidateDirection) && !pastLocations.contains(candidateLocation)){
				pastLocations.add(rc.getLocation());
				if(pastLocations.size() > 20){
					//remove oldest position
					pastLocations.remove(0);
				}
				rc.move(candidateDirection);
				break;
			}
		}
		
	}
}
