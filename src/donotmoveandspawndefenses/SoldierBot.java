package donotmoveandspawndefenses;

import battlecode.common.Clock;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;

public class SoldierBot {

	static AdvancedRobotController rc;
	
	public static void soldierCode(AdvancedRobotController rcIn) {
		rc = rcIn;
		
		while(true){
			try{
				RobotInfo[] enemies = rc.senseHostileRobots(rc.getLocation(), RobotType.SOLDIER.sensorRadiusSquared);
				if(enemies.length > 0){
					for(RobotInfo r : enemies){
						MapLocation enemyLocation = r.location;
		
						// attack if enemy is not adjacent
						if(!enemyLocation.isAdjacentTo(rc.getLocation())){
							if(rc.isWeaponReady() && rc.canAttackLocation(enemyLocation)){
								rc.attackLocation(enemyLocation);
							} else{
								rc.moveToLocation(enemyLocation);
							}
						} else if((Utility.canAttackMeFromAfarOrZombieDen(r) && !rc.isIsolated()) || Utility.isAnArchon(r)){ 
							// attack it if it's ranged, but not alone.
							if(rc.isCoreReady()){
								rc.moveToLocation(enemyLocation);
							}
						} else {
							MapLocation friendLocation = rc.locationOfNearbyFriendInDanger();
							if(friendLocation!=null){
								if(rc.isCoreReady()){
									rc.moveToLocation(friendLocation);
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
