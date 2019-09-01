//Erstellt von Tobias Götz
package com.LogisticsOfLegos.Traversal_Logic;

import com.LogisticsOfLegos.Movement.remoteRobot;
import com.LogisticsOfLegos.Movement.turndirection;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Navigation {			//expect from GUI to have checked that pickup & dump locations are different!
	private static final Set<Integer> randpositionen = new HashSet<>(Arrays.asList(1,2,3,4,5,6,7,8));
	public static class Robot{

		public int position;
		public Status status;
		public int cardinalDirection;		//Direction robot is facing. 0N, 1E, 2S, 3W
		public remoteRobot physicalBot = null;
		public Job job = new Job();
		
		public Robot(int position, Status status, int cardinalDirection, remoteRobot physicalBot)
		{
			this.position = position;
			this.status = status;
			this.cardinalDirection = cardinalDirection;
			this.physicalBot = physicalBot;
		}
		
		public void clearJob() {
			job = new Job();
		}
		
		public void changeJobStatus(JobStatus jobStatus) {
			job.jobStatus = jobStatus;
		}
	}
	
	public static Robot firstRobot = null;
	public static Robot secondRobot = null;
	
	public static void changeStatusToIdle(boolean isFirstRobot) {	//created for Mike to Change Status to idle
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
		if(currentRobot.cardinalDirection == requiredCardinalDirection)
		{
			try {
				currentRobot.physicalBot.turn(turndirection.NONE);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		else if(((currentRobot.cardinalDirection+1)%4) == requiredCardinalDirection)
		{
			try {
				currentRobot.physicalBot.turn(turndirection.RIGHT);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			currentRobot.cardinalDirection = (currentRobot.cardinalDirection+1)%4;
		}
		else if(((currentRobot.cardinalDirection+2)%4) == requiredCardinalDirection)
		{
			currentRobot.physicalBot.turnaround();
			currentRobot.cardinalDirection = (currentRobot.cardinalDirection+2)%4;
		}
		else if(((currentRobot.cardinalDirection+3)%4) == requiredCardinalDirection)
		{
			try {
				currentRobot.physicalBot.turn(turndirection.LEFT);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			currentRobot.cardinalDirection = (currentRobot.cardinalDirection+3)%4;
		}

		try {
			boolean startIsRandposition = randpositionen.contains(currentRobot.position);
			boolean goalIsRandposition = randpositionen.contains(goal);
			currentRobot.physicalBot.followLine(goalIsRandposition, startIsRandposition);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		currentRobot.status = Status.WORKINGONJOB;
		currentRobot.job.jobStatus = currentRobot.job.reachedPickup ? JobStatus.MOVINGTODUMP : JobStatus.MOVINGTOPICKUP;
		if(currentRobot.job.jobId == 0)
		{ 
			currentRobot.job.jobStatus = JobStatus.MOVINGTOPARKING;
		}
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
