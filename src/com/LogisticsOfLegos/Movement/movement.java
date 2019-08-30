package com.LogisticsOfLegos.Movement;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
//import org.jetbrains.annotations.Contract;

public class movement {
  static RegulatedMotor leftMotor = Motor.A;
  static RegulatedMotor rightMotor = Motor.D;
  //static LCSensorLeft leftSensor;
  //static LCSensorRight rightSensor;
  //static turndirection turnDirection;

  public movement() {
  }

  public static void main(String[] args) {
    //asdawdawd
    //leftMotor.resetTachoCount();
    //rightMotor.resetTachoCount();
    //leftMotor.rotateTo(0);
    //rightMotor.rotateTo(0);
    /*leftSensor = new LCSensorLeft();
    rightSensor = new LCSensorRight();
    leftSensor.setDaemon(true);
    rightSensor.setDaemon(true);
    leftSensor.start();
    rightSensor.start();*/
    Behavior FOLLOW = new followLine();
    //Behavior TURN = new turn();
    //Behavior LOADUNLOAD = new loadUnload();
    Behavior[] behaviorList =
            {
                    FOLLOW//, TURN//, LOADUNLOAD
            };
    Arbitrator arbitrator = new Arbitrator(behaviorList);
    //LCD.drawString("Press Button to start!", 0,4);
    //Button.waitForAnyPress();
    arbitrator.start();
    //run();
  }
}

/*class LCSensorLeft extends Thread{
  EV3ColorSensor cs = new EV3ColorSensor(SensorPort.S1);
  SampleProvider sp = cs.getColorIDMode();
  private static int color = 0;
  LCSensorLeft()
  {}
  @Contract(pure = true)
  public static int getColor(){
    return color;
  }
  public void run()
  {
    while(true)
    {
      float[] sample = new float[sp.sampleSize()];
      sp.fetchSample(sample,0);
      color = (int)sample[0];
    }
  }
}*/

/*class LCSensorRight extends Thread{
  EV3ColorSensor cs = new EV3ColorSensor(SensorPort.S4);
  SampleProvider sp = cs.getColorIDMode();
  private static int color = 0;
  LCSensorRight()
  {}
  @Contract(pure = true)
  public static int getColor(){
    return color;
  }
  public void run()
  {
    while(true)
    {
      float[] sample = new float[sp.sampleSize()];
      sp.fetchSample(sample,0);
      color = (int)sample[0];
    }
  }
}*/

class followLine implements Behavior{

  private boolean _suppressed = false;

  public boolean takeControl() {
    //if(LCSensorLeft.getColor() != 7 || LCSensorRight.getColor() != 7)
    return true;
    //else return false;
  }
  public void suppress(){_suppressed = true;}

  public void action()
  {
    _suppressed = false;
    //while(!_suppressed)
    //{
      //if(LCSensorRight.getColor() != 7 && LCSensorLeft.getColor() != 7) {
      //main.leftMotor.setSpeed(400);
      //main.rightMotor.setSpeed(400);
      //main.leftMotor.forward();
      //main.rightMotor.forward();
      //}
      /*if(LCSensorRight.getColor() == 7 && LCSensorLeft.getColor() != 7){
        //main.leftMotor.setSpeed(main.leftMotor.getSpeed()+25);
        //main.rightMotor.setSpeed(main.rightMotor.getSpeed()-25);
        //main.rightMotor.setSpeed(300);
      //}
      //if(LCSensorRight.getColor() != 7 && LCSensorLeft.getColor() == 7){
        //main.rightMotor.setSpeed(main.rightMotor.getSpeed()+25);
        //main.leftMotor.setSpeed(main.leftMotor.getSpeed()-25);
       // main.leftMotor.setSpeed(300);
      //}
      //else _suppressed = true;*/
      //Thread.yield();
    //}
  }
}
/*class turn implements Behavior{
  private boolean _suppressed = false;
  private boolean centered = false;

  public boolean takeControl() {
    LCD.drawString("Press Button to start!", 0,1);
    Button.waitForAnyPress();
    if((LCSensorLeft.getColor() == 7 && LCSensorRight.getColor() == 7) && (main.turnDirection == turndirection.LEFT || main.turnDirection == turndirection.RIGHT))
      return true;
    else return false;
  }

  public void action() {
    LCD.drawString("Press Button to start!", 0,1);
    Button.waitForAnyPress();
    while(!_suppressed){
      if(!centered){
        main.leftMotor.rotate(180);
        main.rightMotor.rotate(180);
        centered = true;
      }
      if(main.turnDirection == turndirection.LEFT){
        main.leftMotor.rotate(-180, true);
        main.rightMotor.rotate(180);
      }
      if (main.turnDirection == turndirection.RIGHT){
        main.leftMotor.rotate(180, true);
        main.rightMotor.rotate(-180);
      }
    }
  }

  public void suppress() {
    _suppressed = true;
  }
}

class loadUnload implements Behavior{

  public boolean takeControl() {
    return false;
  }

  public void action() {

  }

  public void suppress() {

  }
}*/
