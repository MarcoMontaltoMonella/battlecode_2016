package donotmoveandspawndefenses;

import java.util.ArrayList;

import battlecode.common.Clock;
import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;


public class ArchonBot {
	
	static AdvancedRobotController rc;
	static int lastOrderRadius = RobotType.ARCHON.sensorRadiusSquared;
	static int broadcastingFrequence = 30;
	static int spawnedUnits;
	static Direction lastDirectionOppositeToAttack = Direction.NONE;
	static ArrayList<MapLocation> partsLocationsDetected = new ArrayList<MapLocation>();
	static final double A_LOT_OF_PARTS = 150.0;
	
	
	public static void archonCode(AdvancedRobotController rcIn){	
		rc = rcIn;
		spawnedUnits = 0;
		
		while(true){
			try{
				
				int currentTurn = rc.getRoundNum();
			
			if(rc.senseAlliesAroundMe().length > 4){
				leaveSomeSpaceAroundMe();
			}
			
			boolean result_sguards = false;
//			boolean result_sturrets = false;
//			result_sturrets = spawnTurrets(); 
			
			if(Math.random() < 0.96){
				result_sguards = spawnGuards();
			} else {
				result_sguards = tryToSpawn(RobotType.VIPER);
			}
			
			if(!result_sguards){
				MapLocation cl = rc.getLocation();
				boolean result_lfacp = false;
				boolean result_lfara = false;
				
				
				if(( Math.random() >= 0.5)){
					
					// look for parts and collect
					result_lfacp = lookForAndCollectParts(cl);
					
					
				} else {
					
					// look for injured allies and repair
					result_lfara = lookForAndRepairAllies(cl);
					
				}
				
				// if there are no parts and no injured allies and we were not able to build
				if(!result_lfacp && !result_lfara){
					
					// look for and activate neutral troops
					lookForAndActivateNeutralRobots();
				}
				
				
			}
			
			if(!rc.hasFullHealth()){
				RobotInfo[] enemies = rc.senseHostileRobots(rc.getLocation(), rc.getType().sensorRadiusSquared);
				if(enemies.length>0){
					lastDirectionOppositeToAttack = enemies[0].location.directionTo(rc.getLocation());
					Movement.moveForwardish(lastDirectionOppositeToAttack, rc);
					
				}
				else{
					// TODO Not tested it, but added, to prevent an Archon to stay still if a ranged Turret is shooting at him.
					Movement.moveRandomly(rc);
				}
			}
			
			// if an Archon has few guards around him
			if(rc.senseNearbyAllies().length <= 4 && !rc.hasFullHealth()){
				callForHelp();
				if(!(rc.getRelativeLife()>0.75)){
					//flee
				}
			}
			
			Strategy.listenToOrders(rc);
			
			Clock.yield();
			} catch(GameActionException e){
				e.printStackTrace();
			}
		}
		
		
	}
	
	@SuppressWarnings("unused")
	private static boolean spawnTurrets() throws GameActionException {

		if(rc.isCoreReady()){
			MapLocation currentLocation = rc.getLocation();
			
			// if we can build
			if(rc.hasBuildRequirements(RobotType.TURRET)){
				
				for(Direction d : Direction.values()){
					if(rc.isLocationOccupied(currentLocation.add(d))){
						continue;
					}
					if(rc.canBuild(d, RobotType.TURRET) && ( Math.random() >= 0.95) || rc.senseNearbyAllies().length<4){
						rc.build(d, RobotType.TURRET);
						spawnedUnits++;
						return true;
					}
				}
			
			}
		}
		return false;
	}
	
	@SuppressWarnings("unused")
	private static boolean tryToSpawn(RobotType rt) throws GameActionException {

		if(rc.isCoreReady()){
			MapLocation currentLocation = rc.getLocation();
			
			// if we can build
			if(rc.hasBuildRequirements(rt)){
				
				for(Direction d : Direction.values()){
					if(rc.isLocationOccupied(currentLocation.add(d))){
						continue;
					}
					if(rc.canBuild(d, rt) && ( Math.random() >= 0.95) || rc.senseNearbyAllies().length<4){
						rc.build(d, rt);
						spawnedUnits++;
						return true;
					}
				}
			
			}
		}
		return false;
	}

	private static void lookForAndActivateNeutralRobots() throws GameActionException {
		RobotInfo[] neutralInSight = rc.senseNeutralRobots();
		if(neutralInSight.length>0){
			MapLocation neutralBotAdjeacentLocation = rc.getAdjacentNeutralBotLocation();
			if(neutralBotAdjeacentLocation!=null){
				rc.activate(neutralBotAdjeacentLocation);
			}
			rc.moveToLocation(neutralInSight[0].location);
		}
		
	}

	private static boolean lookForAndRepairAllies(MapLocation cl) throws GameActionException {

		RobotInfo[] alliesNearby = rc.senseAlliesWhoNeedsToBeRepaired();
		if(alliesNearby.length > 0){
			for(RobotInfo r : alliesNearby){
				if((r.maxHealth-r.health!=0) && r.type!=RobotType.ARCHON){
					// Once per turn
					
					// HEALING RED LINE
					rc.setIndicatorLine(cl, r.location, 255, 0, 0);
					
					rc.repair(r.location);
					return true;
				}
			}
		}
		return false;
		
	}

	private static void leaveSomeSpaceAroundMe() throws GameActionException {
		int mex = Order.KEEP_MOVING_FOWARD_2.getValue();
		if(lastOrderRadius <= 0){
			lastOrderRadius = RobotType.ARCHON.sensorRadiusSquared;
		}
		
		if(rc.getRoundNum() % broadcastingFrequence==0){
			rc.broadcastMessageSignal(mex, 0, lastOrderRadius);
			lastOrderRadius = lastOrderRadius - 2;
		}
		
	}
	
	private static void callForHelp() throws GameActionException {
		int mex = Order.GATHER_1.getValue();
		rc.broadcastMessageSignal(mex, 0, 5000);
	}


	


	private static boolean spawnGuards() throws GameActionException {
		
		if(rc.isCoreReady()){
			MapLocation currentLocation = rc.getLocation();
			
			// if we can build
			if(rc.hasBuildRequirements(RobotType.GUARD)){
//				int occupiedLocations = 0;
				
				for(Direction d : Direction.values()){
					if(rc.isLocationOccupied(currentLocation.add(d))){
//						occupiedLocations++;
						continue;
					}
					if(rc.canBuild(d, RobotType.GUARD) && ( Math.random() >= 0.95) || rc.senseNearbyAllies().length<4){
						rc.build(d, RobotType.GUARD);
						spawnedUnits++;
						return true;
					}
				}
//				if(occupiedLocations==8){
//					//surrounded. Tell the guards to move away.
//					
//				}
			
			}
		}
		return false;
		
	}

	private static boolean lookForAndCollectParts(MapLocation currentLocation) throws GameActionException {

		if(partsLocationsDetected.contains(currentLocation)){
			partsLocationsDetected.remove(currentLocation);
		}
		
		if(!partsLocationsDetected.isEmpty()){
			Movement.moveForwardish(currentLocation.directionTo(partsLocationsDetected.get(0)), rc);
		} else {
			
			// scan for parts
			MapLocation[] partsInSightLocations = rc.sensePartLocations(rc.getType().sensorRadiusSquared);
			for(MapLocation mp : partsInSightLocations){
				if(!partsLocationsDetected.contains(mp)){
					partsLocationsDetected.add(mp);
					// YELLOW DOT
					rc.setIndicatorDot(rc.getLocation(), 255, 255, 0);
					
					Movement.moveForwardish(currentLocation.directionTo(partsLocationsDetected.get(0)), rc);
					return true;
				}
			}
		
		}
		return false;
	}
}
