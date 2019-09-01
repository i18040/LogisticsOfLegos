package com.LogisticsOfLegos.Movement;

import lejos.remote.ev3.RMIRegulatedMotor;
import lejos.remote.ev3.RemoteEV3;

public class LineFollower extends Thread{

  private RemoteEV3 robot;
  private int leftvelocity;
  private int rightvelocity;

  private RMIRegulatedMotor leftMotor = null;
  private RMIRegulatedMotor rightMotor = null;
  public void LineFollower(RemoteEV3 bot){
    robot = bot;
    leftMotor = robot.createRegulatedMotor("A",'L');
    rightMotor = robot.createRegulatedMotor("D",'L');
  }
  public void follow (){

  }
  public void suspendAction(){

  }
}
