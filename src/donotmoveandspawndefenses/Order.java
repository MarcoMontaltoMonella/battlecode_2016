package donotmoveandspawndefenses;

public enum Order {
	STEP_AHEAD_0,			
	GATHER_1,				
	KEEP_MOVING_FOWARD_2;
	
	public int getValue(){
		if(this == STEP_AHEAD_0) return 0;
		if(this == GATHER_1) return 1;
		if(this == KEEP_MOVING_FOWARD_2) return 2;
		else return -1;
	}
}