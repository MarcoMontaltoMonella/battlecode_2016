package infectionteam;

import java.util.ArrayList;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

public class Movement {
	
	static int[] possibleDirections = new int[]{0,1,-1,2,-2,3,-3,4};
	static ArrayList<MapLocation> pastLocations = new ArrayList<MapLocation>();

	/**
	 * 
	 * @param ahead
	 * @param rc
	 * @return true if the robot steps forward.
	 * @throws GameActionException
	 */
	public static boolean moveForwardish(Direction ahead, AdvancedRobotController rc) throws GameActionException {
		if(rc.isCoreReady()){
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
					return true;
				}
			}
		}
		return false;
		
	}
}
