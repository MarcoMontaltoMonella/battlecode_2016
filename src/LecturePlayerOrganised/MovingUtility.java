package LecturePlayerOrganised;

import java.util.ArrayList;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.GameConstants;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

public class MovingUtility{
	static int [] possibleDirections = new int [] { 0 , 1 , -1 , 2 , -2 , -2 , 3,  -3, 4 };
	static ArrayList<MapLocation> VisitedNodes = new ArrayList< MapLocation >();
	static boolean Patience = true;

	

	public static void forwardish(Direction ahead , RobotController rc) throws GameActionException{
		// TODO Auto-generated method stub

		for(int i : possibleDirections){

			Direction candidateDirection = Direction.values()[(ahead.ordinal()+i)%8];
			MapLocation candidatelocation = rc.getLocation().add(candidateDirection);
			if(Patience == true){
				if(rc.canMove(candidateDirection)&& !VisitedNodes.contains(candidatelocation) ){

					VisitedNodes.add(candidatelocation);
					if(VisitedNodes.size()>20){
						VisitedNodes.remove(0);
					}
					rc.move(candidateDirection);
					return;
				}
			}else{
				if( rc.canMove(candidateDirection)){
					rc.move(candidateDirection);
					return;
				}else {
					if(rc.senseRubble(candidatelocation)>GameConstants.RUBBLE_OBSTRUCTION_THRESH){
						rc.clearRubble(candidateDirection);
						return;
					}
				}
			}

			Patience=false;

		}

	}
}