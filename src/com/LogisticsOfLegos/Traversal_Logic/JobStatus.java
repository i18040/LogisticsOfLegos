package com.LogisticsOfLegos.Traversal_Logic;

public enum JobStatus {
	NONEASSIGNED,
	MOVINGTOPICKUP,
	MOVINGTODUMP,
	MOVINGTOPARKING,
	WAITINGATPICKUPLOCATION,
	WAITINGATDUMPINGLOCATION,
	WAITINGDUETOCOLLISIONPREVENTION,
	WAITINGFOROBSTACLEREMOVAL
}