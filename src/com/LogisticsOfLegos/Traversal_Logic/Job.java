//Erstellt von Tobias Götz
package com.LogisticsOfLegos.Traversal_Logic;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Job{
	public int jobPriority;
	public int pickupPosition;
	public int dumpPosition;
	public int jobId;
	public int[] stations = new int[10];
	public JobStatus jobStatus;
	public int visitedStations = 1;
	public boolean reachedPickup;
	
	private static final Set<Integer> topPositions = new HashSet<>(Arrays.asList(1,2,3));
	private static final Set<Integer> middlePositions = new HashSet<>(Arrays.asList(7,8));
	private static final Set<Integer> bottomPositions = new HashSet<>(Arrays.asList(4,5,6));
	
	public Job() {
		this.jobStatus = JobStatus.NONEASSIGNED;
		this.jobId = 0;
		this.jobPriority = 11;
	}
	
	public void generateParkingJob(int currentPosition, boolean isFirstRobot)
	{
		this.jobPriority = 11;		// Priority ranges from 0 to 10 for actual jobs
		this.jobStatus = JobStatus.MOVINGTOPARKING;
		this.pickupPosition = currentPosition;
		this.dumpPosition = isFirstRobot ? 7 : 8;
		this.jobId = 0;
		generateAllStations(currentPosition);
	}
	
	public void changeJob(int jobPriority, int pickupPosition, int dumpPosition, JobStatus jobStatus, int currentPosition, int jobId) {
		this.jobPriority = jobPriority;
		this.pickupPosition = pickupPosition;
		this.dumpPosition = dumpPosition;
		this.jobStatus = jobStatus;
		this.reachedPickup = (currentPosition == pickupPosition);
		this.jobId = jobId;
		generateAllStations(currentPosition);
	}
	
	public int checkNextStation() {
		return stations[visitedStations];
	}
	
	public int getNextStation() {
		if(stations[visitedStations-1] == pickupPosition)		//if pickup position was just visited, now the robot is on its way to the dumping position
		{
			reachedPickup = true;
		}
		return stations[visitedStations++];
	}
	
	public void generateAllStations(int currentPosition) {
		stations[0] = currentPosition;
		boolean reachedPickupPosition = currentPosition == pickupPosition;
		
		for(int i=1; stations[i-1] != dumpPosition; i++)
		{
			if(!reachedPickupPosition)
			{
				stations[i] = calculateNextStation(stations[i-1], pickupPosition);
				if(stations[i] == pickupPosition)
				{
					reachedPickupPosition = true;
				}
			}
			else
			{
					stations[i] = calculateNextStation(stations[i-1], dumpPosition);
			}
		}
	}
	
	private int calculateNextStation(int start, int goal) {		//start != goal checked by caller
		if(topPositions.contains(start))
		{
			return 9;
		}
		else if(middlePositions.contains(start))
		{
			return 10;
		}
		else if(bottomPositions.contains(start))
		{
			return 11;
		}
		else if(start == 9)
		{
			if(topPositions.contains(goal))
			{
				return goal;
			}
			else
			{
				return 10;
			}
		}
		else if(start ==10)
		{
			if(topPositions.contains(goal) || goal == 9)
			{
				return 9;
			}
			else if(middlePositions.contains(goal))
			{
				return goal;
			}
			else if(bottomPositions.contains(goal) || goal ==11)
			{
				return 11;
			}
		}
		else if (start == 11)
		{
			if(bottomPositions.contains(goal))
			{
				return goal;
			}
			else 
			{
				return 10;
			}
		}
		return 0;
	}
}