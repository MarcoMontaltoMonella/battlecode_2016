package donotmoveandspawndefenses;

import battlecode.common.Clock;
import battlecode.common.GameActionException;
import battlecode.common.GameConstants;
import battlecode.common.MapLocation;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;

public class TTMBot {

	static AdvancedRobotController rc;
	static final int SAFETY_DISTANCE_FOR_DELAY_BETWEEN_UNPACK_AND_FIRE = 4; 
	
	public static void ttmCode(AdvancedRobotController rcIn) {
		rc = rcIn;
		
		while(true){
			try{
				RobotInfo[] enemies = rc.senseHostileRobots(rc.getLocation(), RobotType.TTM.sensorRadiusSquared);
				if(enemies.length > 0){
					for(RobotInfo r : enemies){
						MapLocation enemyLocation = r.location;
		
						if(enemyLocation.distanceSquaredTo(rc.getLocation()) >= GameConstants.TURRET_MINIMUM_RANGE 
								+ SAFETY_DISTANCE_FOR_DELAY_BETWEEN_UNPACK_AND_FIRE){
							rc.unpack();
							return;
						}else{
							Movement.moveForwardish(enemyLocation.directionTo(rc.getLocation()), rc);
						}
					}
				} else {
					// listen to orders
					Strategy.listenToOrders(rc);
					Clock.yield();
				}
			}catch(GameActionException e){
				e.printStackTrace();
			}
		}

	}
}
