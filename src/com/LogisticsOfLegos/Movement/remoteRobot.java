package com.LogisticsOfLegos.Movement;

import lejos.remote.ev3.RMIRegulatedMotor;
import lejos.remote.ev3.RemoteEV3;

import java.net.MalformedURLException;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

//import lejos.robotics.SampleProvider;

public class remoteRobot extends Thread{
  public boolean finishedAction = false;
  public boolean obstacle = false;
  private RemoteEV3 thisRobot= null;
  public boolean isInitialized = false;
  private String IP;
  private RMIRegulatedMotor leftMotor = null;
  private RMIRegulatedMotor rightMotor = null;
  private static LCSensorLeft leftSensor;
  private static LCSensorRight rightSensor;
  private static USSensor usSensor;
  private static GSensor gSensor;
  private boolean centered = false;

  public remoteRobot(String ip) {
    this.IP = ip;
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
    System.out.println("thisrobot: "+this.thisRobot);
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
    try{
    gSensor = new GSensor(thisRobot.createSampleProvider("S2","lejos.hardware.sensor.EV3GyroSensor","Angle"));
      System.out.println("Angle OK!");
    }
    catch (Exception e){
      leftMotor.close();
      rightMotor.close();
      leftSensor.close();
      rightSensor.close();
      usSensor.close();
      System.exit(-1);
    }
    leftSensor.setDaemon(true);
    rightSensor.setDaemon(true);
    usSensor.setDaemon(true);
    gSensor.setDaemon(true);
    leftSensor.start();
    rightSensor.start();
    usSensor.start();
    gSensor.start();

    //LCD.drawString("Press any Key.", 0, 0);
    //Button.waitForAnyPress();
    //readSensors();
    /*
    leftMotor.setSpeed(400);
    rightMotor.setSpeed(400);
    this.leftMotor.forward();
    this.rightMotor.forward();
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    this.leftMotor.setSpeed(800);
    this.rightMotor.setSpeed(800);
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("leftmotor after forward: "+this.leftMotor);
    System.out.println("rightmotor after forward: "+this.rightMotor);
    System.out.println("robot after forward: "+this.thisRobot);
    this.leftMotor.stop(false);
    this.rightMotor.stop(false);
    this.leftMotor.rotate(180);
    this.rightMotor.rotate(180);
    this.rightMotor.close();
    this.leftMotor.close();
    */
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

  public void followLine() throws RemoteException {
      leftSensor.suspended = false;
      rightSensor.suspended = false;
    while(leftSensor.getColor() != 7 || rightSensor.getColor() != 7) {
      try {
        Thread.sleep(200);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    /*if(usSensor.getDistance() <=0.3){
      leftSensor.suspended = true;
      rightSensor.suspended = true;
    }*/
    }
    if(leftSensor.getColor() == 7 && rightSensor.getColor() == 7){
      leftSensor.suspended = true;
      rightSensor.suspended = true;
      System.out.println("finished");
      finishedAction = true;
      //todo hier tobi schnittstelle
    }
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
    gSensor.close();
    usSensor.close();
  }
}

