package com.LogisticsOfLegos.Movement;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.remote.ev3.RemoteEV3;
import lejos.remote.ev3.RMIRegulatedMotor;
import lejos.remote.ev3.RMISampleProvider;
//import lejos.robotics.SampleProvider;

import org.jetbrains.annotations.Contract;

import java.net.MalformedURLException;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class remoteRobot extends Thread{
  private RemoteEV3 thisRobot= null;
  String IP;
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

    RMISampleProvider color_sensor = thisRobot.createSampleProvider("S4",
            "lejos.hardware.sensor.EV3UltrasonicSensor", "Distance");
    float[] color_id= color_sensor.fetchSample();
    color_sensor.close();
    System.out.println("color = "+color_id[0]);

    //leftMotor.resetTachoCount();
    //rightMotor.resetTachoCount();
    this.leftMotor.rotateTo(0);
    this.rightMotor.rotateTo(0);
    /*
    leftSensor = new LCSensorLeft(thisRobot);
    rightSensor = new LCSensorRight(thisRobot);
    usSensor = new USSensor(thisRobot);
    gSensor = new GSensor(thisRobot);
    leftSensor.setDaemon(true);
    rightSensor.setDaemon(true);
    usSensor.setDaemon(true);
    gSensor.setDaemon(true);
    leftSensor.start();
    rightSensor.start();
    usSensor.start();
    gSensor.start();
    /*
     */
    //LCD.drawString("Press any Key.", 0, 0);
    //Button.waitForAnyPress();
    //readSensors();
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
  }

  public void followLine() throws RemoteException {
    centered = false;
    long startTime = System.currentTimeMillis();
    boolean crossing = false;
    while (!crossing) {
      if (rightSensor.getColor() != 7 && leftSensor.getColor() != 7) {
        leftMotor.setSpeed(400);
        rightMotor.setSpeed(400);
        leftMotor.forward();
        rightMotor.forward();
      }
      if (rightSensor.getColor() == 7 && leftSensor.getColor() != 7 && (System.currentTimeMillis() / startTime) <= 0) {
        //leftMotor.setSpeed(leftMotor.getSpeed()+25);
        rightMotor.setSpeed(rightMotor.getSpeed() - 25);
        //rightMotor.setSpeed(300);
        //madeRightTurn = true;
      }
      if (rightSensor.getColor() != 7 && leftSensor.getColor() == 7 && (System.currentTimeMillis() / startTime) <= 0) {
        //rightMotor.setSpeed(rightMotor.getSpeed()+25);
        leftMotor.setSpeed(leftMotor.getSpeed() - 25);
        //leftMotor.setSpeed(300);
        //madeLeftTurn = true;
      } else crossing = true;
    }
  }

  public void turn(turndirection direction) throws RemoteException {
    if (!centered) {
      leftMotor.rotate(180);
      rightMotor.rotate(180);
      centered = true;
    }
    if (direction == turndirection.LEFT) {
      leftMotor.rotate(-180, true);
      rightMotor.rotate(180);
    }
    if (direction == turndirection.RIGHT) {
      leftMotor.rotate(180, true);
      rightMotor.rotate(-180);
    }
  }

  public void load_unload(boolean stop) throws RemoteException {
    while (gSensor.getAngle() % 180 != 0) {
      if (gSensor.getAngle() > 0) {
        if (gSensor.getAngle() - 180 > 0)
          leftMotor.setSpeed(50);
        else
          rightMotor.setSpeed(50);
      } else {
        if (gSensor.getAngle() + 180 > 0)
          leftMotor.setSpeed(50);
        else
          rightMotor.setSpeed(50);
      }
    }
    leftMotor.rotate(360);
    rightMotor.rotate(360);
    leftMotor.rotate(180);
    rightMotor.rotate(-180);
    LCD.drawString("Please Confirm.", 0, 0);
    Button.waitForAnyPress();
    if (!stop)
      startMove(0);
  }

  private void startMove(int move) throws RemoteException {
    int rotate = 0;
    if (move == 0) {
      move = 360;
    }
    leftMotor.rotate(move);
    rightMotor.rotate(move);
    rightMotor.rotate(90);
    leftMotor.rotate(-90);
    while ((leftSensor.getColor() != 7 && rightSensor.getColor() != 7) || rotate == 18) {
      rightMotor.rotate(-10);
      leftMotor.rotate(10);
      rotate++;
    }
    if (leftSensor.getColor() != 7 && rightSensor.getColor() != 7)
      startMove(20);
  }
  private void readSensors(){
      //LCD.drawString(Integer.toString(leftSensor.getColor()), 0, 2);
      //LCD.drawString(Integer.toString(rightSensor.getColor()), 0, 4);
      //LCD.drawString(Integer.toString(gSensor.getAngle()), 0, 4);
      LCD.drawString(Double.toString(usSensor.getDistance()), 0, 2);
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
  }
}

class LCSensorLeft extends Thread {
  private RMISampleProvider sp = null;
  private int color = 0;

  LCSensorLeft(RemoteEV3 thisRobot) {
    sp = thisRobot.createSampleProvider("S1","lejos.hardware.sensor.EV3ColorSensor","ColorID");
  }

  @Contract(pure = true)
  int getColor() {
    return color;
  }

  public synchronized void run() {
    while (true) {
        float[] sample = new float[0];
      try {
        sample = sp.fetchSample();
      } catch (RemoteException e) {
        e.printStackTrace();
      }
      color = (int) sample[0];
    }
  }
}

class LCSensorRight extends Thread {
  private RMISampleProvider sp = null;
  private int color = 0;

  LCSensorRight(RemoteEV3 thisRobot) {
    sp = thisRobot.createSampleProvider("S4","lejos.hardware.sensor.EV3ColorSensor","ColorID");
  }

  @Contract(pure = true)
  int getColor() {
    return color;
  }

  public synchronized void run() {
    while (true) {
      float[] sample = new float[0];
      try {
        sample = sp.fetchSample();
      } catch (RemoteException e) {
        e.printStackTrace();
      }
      color = (int) sample[0];
    }
  }
}

class GSensor extends Thread {
  private RMISampleProvider sp = null;
  private int angle = 0;

  GSensor(RemoteEV3 thisRobot) {
    sp = thisRobot.createSampleProvider("S2","lejos.hardware.sensor.EV3GyroSensor","Angle");
  }

  int getAngle() {
    return angle;
  }

  public synchronized void run() {
    while(true){
      float[] sample = new float[0];
      try {
        sample = sp.fetchSample();
      } catch (RemoteException e) {
        e.printStackTrace();
      }
      angle = (int) sample[0];
    }
  }
}

class USSensor extends Thread {
  private RMISampleProvider sp = null;
  //private final EV3UltrasonicSensor uss = new EV3UltrasonicSensor(SensorPort.S3);
  //private final RMISampleProvider sp = uss.getDistanceMode();
  private double distance;

  USSensor(RemoteEV3 thisRobot) {
    sp = thisRobot.createSampleProvider("S3","lejos.hardware.sensor.EV3UltrasonicSensor","Distance");
  }
  double getDistance(){
    return distance;
  }
  public synchronized void run(){
    while(true){
      float[] sample = new float[0];
      try {
        sample = sp.fetchSample();
      } catch (RemoteException e) {
        e.printStackTrace();
      }
      distance = (double) sample[0];
    }
  }
}
