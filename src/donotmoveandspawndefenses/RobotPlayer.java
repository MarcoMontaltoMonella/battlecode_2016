package donotmoveandspawndefenses;

import battlecode.common.Clock;
import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;
import battlecode.common.Signal;

public class RobotPlayer {
	
	static AdvancedRobotController rc;
	static int spawnedUnits;
	
	/*
	 * DO NOT MOVE AND KEEP SPAWNING DEFENCES
	 */
	public static void run(RobotController rcIn) {
		rc = new AdvancedRobotController(rcIn);
		spawnedUnits = 0;

		while(true){
			try {
				if(rc.getType()==RobotType.ARCHON){
					archonCode();
				}else if(rc.getType()==RobotType.GUARD){
					guardCode();
				}

			} catch (GameActionException e) {
				e.printStackTrace();
			}
			Clock.yield();
		}
	}
	

	private static void guardCode() throws GameActionException {
		RobotInfo[] enemies = rc.senseHostileRobots(rc.getLocation(), RobotType.GUARD.sensorRadiusSquared);
		if(enemies.length > 0){
			for(RobotInfo r : enemies){
				MapLocation enemyLocation = r.location;

				// attack everything if adjacent
				if(enemyLocation.isAdjacentTo(rc.getLocation())){
					if(rc.isWeaponReady() && rc.canAttackLocation(enemyLocation)){
						rc.attackLocation(enemyLocation);
					}
				} else if(Utility.canAttackMeFromAfar(r) && !rc.isIsolated()){ 
					// attack it if it's ranged, but not alone.
					if(rc.isCoreReady()){
						moveToLocation(enemyLocation);
					}
				} else {
					MapLocation friendLocation = locationOfNearbyFriendInDanger();
					if(friendLocation!=null){
						if(rc.isCoreReady()){
							moveToLocation(friendLocation);
						}
					}

				}
			}
		} else {
			// listen to orders
			Signal incomingSignal = rc.readAllySignal();
			if(incomingSignal != null){
				int[] message = incomingSignal.getMessage();
				if(message != null){
					// MessageSignal with 2 ints
					Strategy.applyStrategy(message, incomingSignal.getLocation() ,rc);
				} else {
					// Basic message
				}
			}
		}

	}


	private static MapLocation locationOfNearbyFriendInDanger() {
		MapLocation location = null;
		RobotInfo[] alliesInSight = rc.senseNearbyRobots(rc.getType().sensorRadiusSquared, rc.getTeam());
		for(RobotInfo r : alliesInSight){
			if(r.maxHealth-r.health>0){
				location = r.location;
				break;
			}
		}
		return location;
	}


	private static void moveToLocation(MapLocation location) throws GameActionException {
		Direction directionToLocation = rc.getLocation().directionTo(location);
		Utility.moveForwardish(directionToLocation,rc);
	}
	
	


	private static void archonCode() throws GameActionException {		
		if(rc.senseAlliesAroundMe().length > 3){
			leaveSomeSpaceAroundMe();
		}
		spawnGuards();
		
	}


	private static void leaveSomeSpaceAroundMe() throws GameActionException {
		rc.broadcastMessageSignal(2, 0, 2);
	}


	private static void spawnGuards() throws GameActionException {
		
		if(rc.isCoreReady()){
			if(rc.hasBuildRequirements(RobotType.GUARD)){
				int occupiedLocations = 0;
				MapLocation currentLocation = rc.getLocation();
				for(Direction d : Direction.values()){
					if(rc.isLocationOccupied(currentLocation.add(d))){
						occupiedLocations++;
						continue;
					}
					if(rc.canBuild(d, RobotType.GUARD)){
						rc.build(d, RobotType.GUARD);
						spawnedUnits++;
						return;
					}
				}
				if(occupiedLocations==8){
					//surrounded. Tell the guards to move away.
					
				}
			}
		}
		
	}
	
}
