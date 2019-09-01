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
		
		public void clearJob() {
			job = new Job();
		}
		
		public void changeJobStatus(JobStatus jobStatus) {
			job.jobStatus = jobStatus;
		}
	}
	
	public Robot firstRobot = new Robot(7, Status.IDLE, 3);
	public Robot secondRobot = new Robot(8, Status.IDLE, 1);
	
	public void changeStatusToIdle(boolean isFirstRobot) {	//created for Mike to Change Status to idle
		if(isFirstRobot)
		{
			firstRobot.status = Status.IDLE;
		}
		else
		{
			secondRobot.status = Status.IDLE;
		}
	}

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
		if(firstRobot.job.jobStatus != JobStatus.NONEASSIGNED && firstRobot.job.reachedPickup
				&& firstRobot.position == firstRobot.job.dumpPosition && firstRobot.status == Status.IDLE)
		{
			firstRobot.clearJob();  //Job done. Not important for GUI, because after accepting they are already moved to done
		}
		else if(secondRobot.job.jobStatus != JobStatus.NONEASSIGNED && secondRobot.job.reachedPickup
				&& secondRobot.position == secondRobot.job.dumpPosition && secondRobot.status == Status.IDLE)
		{
			secondRobot.clearJob();	//Job done. Not important for GUI, because after accepting they are already moved to done
		}
	}
	
	public int[] getLowestValueInQueue()		//PLACEHOLDER; should be Peter's Method
	{
		int[] a = {1,2};
		return a;
	}
	public int[] acceptJob(int jobId)		//PLACEHOLDER; should be Peter's Method
	{
		int[] a = {1,2};
		return a;
	}
	public int[] acceptJob(int jobId, int jobId2)		//PLACEHOLDER; should be Peter's Method
	{
		int[] a = {1,2};
		return a;
	}
	
	public void checkJobChanges() {
		int[] priorityAndJobId = getLowestValueInQueue();
		int lowestPriority = priorityAndJobId[0];
		int jobId = priorityAndJobId[1];
		
		if(firstRobot.job.jobStatus == JobStatus.NONEASSIGNED || firstRobot.job.jobId == 0)
		{
			int[] pickupAndDumpLocation = acceptJob(jobId);
			firstRobot.job.changeJob(lowestPriority, pickupAndDumpLocation[0], pickupAndDumpLocation[1], JobStatus.MOVINGTOPICKUP, firstRobot.position, jobId);
		}
		else if(firstRobot.job.jobPriority > lowestPriority && (!firstRobot.job.reachedPickup))
		{
			int[] pickupAndDumpLocation = acceptJob(jobId, firstRobot.job.jobId);
			firstRobot.job.changeJob(lowestPriority, pickupAndDumpLocation[0], pickupAndDumpLocation[1], JobStatus.MOVINGTOPICKUP, firstRobot.position, jobId);
		}
		if(secondRobot.job.jobStatus == JobStatus.NONEASSIGNED || secondRobot.job.jobId == 0)
		{
			int[] pickupAndDumpLocation = acceptJob(jobId);
			secondRobot.job.changeJob(lowestPriority, pickupAndDumpLocation[0], pickupAndDumpLocation[1], JobStatus.MOVINGTOPICKUP, secondRobot.position, jobId);
		}
		else if(secondRobot.job.jobPriority > lowestPriority && (!secondRobot.job.reachedPickup))
		{
			int[] pickupAndDumpLocation = acceptJob(jobId, secondRobot.job.jobId);
			secondRobot.job.changeJob(lowestPriority, pickupAndDumpLocation[0], pickupAndDumpLocation[1], JobStatus.MOVINGTOPICKUP, secondRobot.position, jobId);
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
		//here MikeDrive(currentRobot.position, goal);
		currentRobot.status = Status.WORKINGONJOB;
		currentRobot.job.jobStatus = currentRobot.job.reachedPickup ? JobStatus.MOVINGTODUMP : JobStatus.MOVINGTOPICKUP;
		if(currentRobot.job.jobId == 0) { currentRobot.job.jobStatus = JobStatus.MOVINGTOPARKING; }
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
		navigateToNextPoint(isFirstRobot, currentRobot.job.getNextStation());
	}
	
	public int getRequiredCardinalDirection(int position, int goal) {	//position and goal have to next to each other!
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
