package donotmoveandspawndefenses;

import battlecode.common.Clock;
import battlecode.common.GameActionException;
import battlecode.common.GameConstants;
import battlecode.common.MapLocation;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;

public class TurretBot {

	static AdvancedRobotController rc;
	
	public static void turretCode(AdvancedRobotController rcIn) {
		rc = rcIn;
		
		while(true){
			try{
				RobotInfo[] enemies = rc.senseHostileRobots(rc.getLocation(), RobotType.TURRET.sensorRadiusSquared);
				if(enemies.length > 0){
					for(RobotInfo r : enemies){
						MapLocation enemyLocation = r.location;
		
						if(enemyLocation.distanceSquaredTo(rc.getLocation())<=GameConstants.TURRET_MINIMUM_RANGE){
							rc.pack();
							return;
						}else{
							// attack if enemy is not near
							if(!enemyLocation.isAdjacentTo(rc.getLocation())){
								if(rc.isWeaponReady() && rc.canAttackLocation(enemyLocation)){
									rc.attackLocation(enemyLocation);
								}
							} else if((Utility.canAttackMeFromAfarOrZombieDen(r) && !rc.isIsolated()) || Utility.isAnArchon(r)){ 
								// attack it if it's ranged, but not alone.
								if(rc.isCoreReady()){
									rc.attackLocation(enemyLocation);
								}
							}
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
