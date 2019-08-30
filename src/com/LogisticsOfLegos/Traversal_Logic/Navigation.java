package com.LogisticsOfLegos.Traversal_Logic;

public class Navigation {			//expect from GUI to have checked that pickup & dump locations are different!

	private class Robot{

		public int position;
		public Status status;
		public int cardinalDirection;		//Direction robot is facing. 0N, 1E, 2S, 3W
		public Job job = new Job();
		
		public Robot(int position, Status status, int cardinalDirection)
		{
			this.position = position;
			this.status = status;
			this.cardinalDirection = cardinalDirection;
		}
		
		public void assignJob(int jobPriority, int pickupPosition, int dumpPosition, JobStatus jobStatus) {
			job.changeJob(jobPriority, pickupPosition, dumpPosition, jobStatus, position);
		}
		
		public void clearJob() {
			job = new Job();
		}
		
		public void changeJobStatus(JobStatus jobStatus) {		// Useful for Mike - change to idle after reaching points, or to waitingfor.. when necessary
			job.jobStatus = jobStatus;
		}
	}
	
	public Robot firstRobot = new Robot(7, Status.IDLE, 3);
	public Robot secondRobot = new Robot(8, Status.IDLE, 1);
	
	public void checkChanges() {		//THIS is the most important method for others. Call it when anything happens.
		checkIfJobDone();
		checkJobChanges();
		if(firstRobot.status == Status.IDLE)
		{
			if(firstRobot.job.jobStatus != JobStatus.NONEASSIGNED)
			{
				navigateToNextPoint(true, firstRobot.job.getNextStation());
			}
			else if(firstRobot.position != 7)
			{
				generateParkingJob(true);
			}
		}
		if(secondRobot.status == Status.IDLE)
		{
			if(secondRobot.job.jobStatus != JobStatus.NONEASSIGNED)
			{
				navigateToNextPoint(false, secondRobot.job.getNextStation());
			}
			
			else if(secondRobot.position != 8)
			{
				generateParkingJob(false);
			}
		}
	}
	public void checkIfJobDone() {
		if(firstRobot.job.jobStatus != JobStatus.NONEASSIGNED && firstRobot.job.reachedPickup == true
				&& firstRobot.position == firstRobot.job.dumpPosition && firstRobot.status == Status.IDLE)
		{
			firstRobot.clearJob();  //Job done. Important for GUI: could be the parking job which is done!
		}
		else if(secondRobot.job.jobStatus != JobStatus.NONEASSIGNED && secondRobot.job.reachedPickup == true
				&& secondRobot.position == secondRobot.job.dumpPosition && secondRobot.status == Status.IDLE)
		{
			secondRobot.clearJob();	//Job done. Important for GUI: could be the parking job which is done!
		}
	}
	
	public void checkJobChanges() {
		int lowestpriorityjobinqueue = 1;		//get this value from GUI!
		if(firstRobot.job.jobStatus == JobStatus.NONEASSIGNED || firstRobot.job.jobStatus == JobStatus.MOVINGTOPARKING)
		{
			//assign Job with lowest priority value to first robot!
		}
		else if(firstRobot.job.jobPriority > lowestpriorityjobinqueue && firstRobot.job.jobStatus == JobStatus.MOVINGTOPICKUP)
		{
			//assign Job with lowest priority value to first robot, and add the job it was working on to the queue again!
		}
		if(secondRobot.job.jobStatus == JobStatus.NONEASSIGNED || secondRobot.job.jobStatus == JobStatus.MOVINGTOPARKING)
		{
			//assign Job with lowest priority value to second robot!
		}
		else if(secondRobot.job.jobPriority > lowestpriorityjobinqueue && secondRobot.job.jobStatus == JobStatus.MOVINGTOPICKUP)
		{
			//assign Job with lowest priority value to second robot, and add the job it was working on to the queue again!
		}
	}
	
	public void navigateToNextPoint(boolean isFirstRobot, int goal) {
		Robot currentRobot;
		if(isFirstRobot)
		{
			currentRobot = firstRobot;
		}
		else
		{
			currentRobot = secondRobot;
		}
		int requiredCardinalDirection = getRequiredCardinalDirection(currentRobot.position, goal);
		while(currentRobot.cardinalDirection != requiredCardinalDirection)
		{
			//rotate by 90� to the right
			currentRobot.cardinalDirection = (firstRobot.cardinalDirection+1)%4;
		}
		//initiate driving to the next spot which is goal; after reaching there - set Status to idle!(Mike)
		currentRobot.status = Status.WORKINGONJOB;
		currentRobot.job.jobStatus = currentRobot.job.reachedPickup ? JobStatus.MOVINGTODUMP : JobStatus.MOVINGTOPICKUP;
		currentRobot.position = goal;
	}
	public void generateParkingJob(boolean isFirstRobot) {
		Robot currentRobot;
		if(isFirstRobot)
		{
			currentRobot = firstRobot;
		}
		else
		{
			currentRobot = secondRobot;
		}
		currentRobot.clearJob();
		currentRobot.job.generateParkingJob(currentRobot.position, isFirstRobot);
		checkChanges();
	}
	
	public int getRequiredCardinalDirection(int position, int goal) {
		if(position == 1 || position == 4 || position == 7)
		{
			return 1;
		}
		else if(position == 3 || position == 6 || position == 8)
		{
			return 3;
		}
		else if(position == 2)
		{
			return 2;
		}
		else if(position == 5)
		{
			return 0;
		}
		else if(position == 9 || position == 11)
		{
			if(position == 9 && goal == 10)
			{
				return 2;
			}
			else if(position == 11 && goal == 10)
			{
				return 0;
			}
			else	//position is at 9 or 11, and the goal has to be (1,2,3) for position=9, or (4,5,6) for position=11
			{
				if((goal%3) == 0)
				{
					return 1;
				}
				else if((goal%3) == 1)
				{
					return 3;
				}
				else if(position == 9 && goal == 2)
				{
					return 0;
				}
				else
				{
					return 2;	//has to be position 11 and goal 5
				}
			}
		}
		else		//position==10
		{
			switch (goal)
			{
			case 7: return 3;
			case 8: return 1;
			case 9: return 0;
			case 11: return 2;
			default: //should never happen
				return -1;
			}
		}
	}
}
