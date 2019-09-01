//Erstellt von Mike Sütö & Nils Becker
package com.LogisticsOfLegos.Movement;

import com.LogisticsOfLegos.Traversal_Logic.Navigation;
import lejos.remote.ev3.RMIRegulatedMotor;
import lejos.remote.ev3.RemoteEV3;

import java.net.MalformedURLException;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;


public class remoteRobot extends Thread{
  private RemoteEV3 thisRobot= null;
  public boolean isInitialized = false;
  private String IP;
  private RMIRegulatedMotor leftMotor = null;
  private RMIRegulatedMotor rightMotor = null;
  private static LCSensorLeft leftSensor;
  private static LCSensorRight rightSensor;
  private static USSensor usSensor;
  private boolean isFirstRobot;
  public Navigation navi = null;

  public remoteRobot(String ip, boolean isFirstRobot) {
    this.IP = ip;
    this.isFirstRobot = isFirstRobot;
  }

  public synchronized void initialize() throws RemoteException, MalformedURLException, NotBoundException {
    boolean connected = false;
    int i = 0;
    while (!connected)
    {
      try {
        thisRobot = new RemoteEV3(IP);
        connected = true;
      } catch (ConnectException e) {
        i++;
        System.out.println("Failed to Connect, retrying. Attempt: " + i + "/5");
        if(i==5){
          System.exit(-1);
        }
      }
    }
    //thisRobot.setDefault();
    this.leftMotor = thisRobot.createRegulatedMotor("A",'L');
    this.rightMotor = thisRobot.createRegulatedMotor("D",'L');
    this.leftMotor.rotateTo(0);
    this.rightMotor.rotateTo(0);
    try {
      leftSensor = new LCSensorLeft(thisRobot.createSampleProvider("S1", "lejos.hardware.sensor.EV3ColorSensor", "ColorID"), leftMotor);
      leftSensor.suspended = true;
      System.out.println("Left Sensor OK!");
    }
    catch (Exception e){
      leftMotor.close();
      rightMotor.close();
      System.exit(-1);
    }
    try{
    rightSensor = new LCSensorRight(thisRobot.createSampleProvider("S4","lejos.hardware.sensor.EV3ColorSensor","ColorID"), rightMotor);
    rightSensor.suspended = true;
      System.out.println("Right Sensor OK!");
    }
    catch (Exception e){
      leftMotor.close();
      rightMotor.close();
      leftSensor.close();
      System.exit(-1);
    }
    try{
    usSensor = new USSensor(thisRobot.createSampleProvider("S3","lejos.hardware.sensor.EV3UltrasonicSensor","Distance"));
      System.out.println("Distance OK!");
    }
    catch (Exception e){
      leftMotor.close();
      rightMotor.close();
      leftSensor.close();
      rightSensor.close();
      System.exit(-1);
    }
    leftSensor.setDaemon(true);
    rightSensor.setDaemon(true);
    usSensor.setDaemon(true);
    leftSensor.start();
    rightSensor.start();
    usSensor.start();


    while(leftSensor.getColor() == 0 || rightSensor.getColor() == 0)
    {
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    isInitialized = true;
  }

  public void followLine(boolean isParked, boolean shouldPark) throws RemoteException {
    if(isParked) go();
      leftSensor.suspended = false;
      rightSensor.suspended = false;
    while(leftSensor.getColor() != 7 || rightSensor.getColor() != 7) {
      if(usSensor.getDistance() <=0.3) {
        if (!leftSensor.suspended && !rightSensor.suspended){
          leftSensor.suspended = true;
          rightSensor.suspended = true;
        }
      }
      else{
        if(leftSensor.suspended || rightSensor.suspended) {
          leftSensor.suspended = false;
          rightSensor.suspended = false;
        }
      }
      try {
        Thread.sleep(200);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    if(leftSensor.getColor() == 7 && rightSensor.getColor() == 7){
      leftSensor.suspended = true;
      rightSensor.suspended = true;
    }
    if(shouldPark) park();
    navi.changeStatusToIdle(isFirstRobot);
    navi.checkChanges();
  }

  public void turn(turndirection direction) throws RemoteException{
    if(direction == turndirection.LEFT){
      rightSensor.turnleft = true;
      leftSensor.turnleft = true;
      while(rightSensor.turnleft || leftSensor.turnleft){
        try {
          Thread.sleep(200);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      navi.changeStatusToIdle(isFirstRobot);
      navi.checkChanges();
    }
    if(direction == turndirection.RIGHT){
      leftSensor.turnright = true;
      rightSensor.turnright = true;
      while(leftSensor.turnright || rightSensor.turnright){
        try {
          Thread.sleep(200);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      navi.changeStatusToIdle(isFirstRobot);
      navi.checkChanges();
    }
    if(direction == turndirection.NONE){
      leftSensor.skip = true;
      rightSensor.skip = true;
      while(leftSensor.skip || rightSensor.skip){
        try {
          Thread.sleep(200);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
    navi.changeStatusToIdle(isFirstRobot);
    navi.checkChanges();
  }
  public void park(){
    leftSensor.park = true;
    rightSensor.park = true;
    while(leftSensor.park || rightSensor.park){
      try {
        Thread.sleep(200);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  public void go() {
    leftSensor.go = true;
    rightSensor.go = true;
    while(leftSensor.go || rightSensor.go){
      try {
        Thread.sleep(200);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  public void turnaround(){
    leftSensor.turnaround = true;
    rightSensor.turnaround = true;
    while(leftSensor.turnaround || rightSensor.turnaround){
      try {
        Thread.sleep(200);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    navi.changeStatusToIdle(isFirstRobot);
    navi.checkChanges();
  }
  public synchronized void run(){
    try {
      initialize();
    } catch (RemoteException e) {
      e.printStackTrace();
    } catch (NotBoundException e) {
      e.printStackTrace();
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
  }

  public void closePorts() throws RemoteException {
    leftMotor.close();
    rightMotor.close();
    leftSensor.close();
    rightSensor.close();
    usSensor.close();
  }
}

