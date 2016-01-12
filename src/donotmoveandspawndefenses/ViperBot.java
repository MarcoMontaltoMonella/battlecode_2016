package donotmoveandspawndefenses;

import battlecode.common.Clock;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;

public class ViperBot {

	static AdvancedRobotController rc;
	
	public static void viperCode(AdvancedRobotController rcIn) {
		rc = rcIn;
		
		while(true){
			try{
				RobotInfo[] enemies = rc.senseHostileRobots(rc.getLocation(), RobotType.VIPER.sensorRadiusSquared);
				if(enemies.length > 0){
					
					// pick the weakest enemy
					RobotInfo weakestEnemy = null;
					double weakestHealthDetected = 99999.9;
					for(RobotInfo r : enemies){
						if(r.health < weakestHealthDetected){
							weakestHealthDetected = r.health;
							weakestEnemy = r;
						}
					}
					
					MapLocation enemyLocation = weakestEnemy.location;
	
					// attack if enemy is not adjacent
					if(!enemyLocation.isAdjacentTo(rc.getLocation())){
						if(rc.isWeaponReady() && rc.canAttackLocation(enemyLocation)){
							rc.attackLocation(enemyLocation);
						} else{
							rc.moveToLocation(enemyLocation);
						}
					} else if((Utility.canAttackMeFromAfarOrZombieDen(weakestEnemy) && !rc.isIsolated()) || Utility.isAnArchon(weakestEnemy)){ 
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
