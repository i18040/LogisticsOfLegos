import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;

import org.jetbrains.annotations.Contract;

public class commands {
  private static final RegulatedMotor leftMotor = Motor.A;
  private static final RegulatedMotor rightMotor = Motor.D;
  private static LCSensorLeft leftSensor;
  private static LCSensorRight rightSensor;
  private static GSensor gSensor;
  private boolean centered = false;

  public commands() {
  }

  public static void main(String[] args) {
    leftMotor.resetTachoCount();
    rightMotor.resetTachoCount();
    leftMotor.rotateTo(0);
    rightMotor.rotateTo(0);
    leftSensor = new LCSensorLeft();
    rightSensor = new LCSensorRight();
    gSensor = new GSensor();
    leftSensor.setDaemon(true);
    rightSensor.setDaemon(true);
    gSensor.setDaemon(true);
    leftSensor.start();
    rightSensor.start();
    gSensor.start();
    LCD.drawString("Press any Key.", 0, 0);
    Button.waitForAnyPress();
  }

  public void followLine() {
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

  public void turn(turndirection direction) {
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

  public void load_unload(boolean stop) {
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
      start(0);
  }

  private void start(int move) {
    gSensor.resetAngle();
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
      start(20);
  }
}

class LCSensorLeft extends Thread {
  private final EV3ColorSensor cs = new EV3ColorSensor(SensorPort.S1);
  private final SampleProvider sp = cs.getColorIDMode();
  private int color = 0;

  LCSensorLeft() {
  }

  @Contract(pure = true)
  int getColor() {
    return color;
  }

  public synchronized void run() {
    while (true) {
      float[] sample = new float[sp.sampleSize()];
      sp.fetchSample(sample, 0);
      color = (int) sample[0];
    }
  }
}

class LCSensorRight extends Thread {
  private final EV3ColorSensor cs = new EV3ColorSensor(SensorPort.S4);
  private final SampleProvider sp = cs.getColorIDMode();
  private int color = 0;

  LCSensorRight() {
  }

  @Contract(pure = true)
  int getColor() {
    return color;
  }

  public synchronized void run() {
    while (true) {
      float[] sample = new float[sp.sampleSize()];
      sp.fetchSample(sample, 0);
      color = (int) sample[0];
    }
  }
}

class GSensor extends Thread {
  private final EV3GyroSensor gs = new EV3GyroSensor(SensorPort.S2);
  private final SampleProvider sp = gs.getAngleMode();
  private int angle = 0;

  GSensor() {
  }

  int getAngle() {
    return angle;
  }

  void resetAngle() {
    gs.reset();
  }

  public synchronized void run() {
    while (true) {
      float[] sample = new float[sp.sampleSize()];
      sp.fetchSample(sample, 0);
      angle = (int) sample[0];
    }
  }
}
