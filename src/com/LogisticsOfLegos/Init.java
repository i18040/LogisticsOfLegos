package com.LogisticsOfLegos;

import com.LogisticsOfLegos.Movement.remoteRobot;
import com.LogisticsOfLegos.Movement.turndirection;
import com.LogisticsOfLegos.Traversal_Logic.Navigation;
import com.LogisticsOfLegos.Traversal_Logic.Status;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Init {
  private static remoteRobot firstRobot = null;
  private static remoteRobot secondRobot = null;
  public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {

    Navigation a = new Navigation();

    //Startseite seite = new Startseite();
    //seite.setVisible(true);
    /*
    boolean connected = false;
    int i = 0;
    RemoteEV3 ev3 = null;
    while (!connected)
    {
      try {
        ev3 = new RemoteEV3("10.0.1.1");
        connected = true;
      } catch (ConnectException e) {
        i++;
        System.out.println("Failed to Connect, retrying. Attempt: " + i + "/5");
      }
    }
    ev3.setDefault();

    RMISampleProvider color_sensor_right = ev3.createSampleProvider("S4",
            "lejos.hardware.sensor.EV3ColorSensor", "ColorID");
    float[] color_id_right= color_sensor_right.fetchSample();
    System.out.println("color = "+color_id_right[0]);
    color_sensor_right.close();
    RMISampleProvider color_sensor_left = ev3.createSampleProvider("S1",
            "lejos.hardware.sensor.EV3ColorSensor", "ColorID");
    float[] color_id_left= color_sensor_left.fetchSample();
    System.out.println("color = "+color_id_left[0]);
    color_sensor_left.close();
    RMISampleProvider gyro_sensor = ev3.createSampleProvider("S2",
            "lejos.hardware.sensor.EV3GyroSensor", "Angle");
    float[] angle= gyro_sensor.fetchSample();
    System.out.println("angle = "+angle[0]);
    gyro_sensor.close();
    RMISampleProvider distance_sensor = ev3.createSampleProvider("S3",
            "lejos.hardware.sensor.EV3UltrasonicSensor", "Distance");
    float[] distance= distance_sensor.fetchSample();
    System.out.println("distance = "+distance[0]);
    distance_sensor.close();
    RMIRegulatedMotor leftMotor = ev3.createRegulatedMotor("A",'L');
    leftMotor.rotate(180);
    leftMotor.close();
    */
    firstRobot = new remoteRobot("10.0.1.1");
    firstRobot.start();
    secondRobot = new remoteRobot("10.0.1.4");
    secondRobot.start();
    while(!firstRobot.isInitialized) {
      try {
        Thread.sleep(200);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    while(!secondRobot.isInitialized) {
      try {
        Thread.sleep(200);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    a.firstRobot = new Navigation.Robot(7, Status.IDLE, 3, firstRobot);
    a.secondRobot = new Navigation.Robot (8, Status.IDLE, 1, secondRobot);

    //firstRobot.startMove(0);
    //firstRobot.readSensors();
    firstRobot.go();
    System.out.println("Follow");
    firstRobot.followLine();
    System.out.println("LEFT");
    firstRobot.turn(turndirection.LEFT);
    System.out.println("Follow");
    firstRobot.followLine();
    System.out.println("NONE");
    firstRobot.turn(turndirection.NONE);
    System.out.println("Follow");
    firstRobot.followLine();
    System.out.println("RIGHT");
    firstRobot.turn(turndirection.RIGHT);
    System.out.println("Follow");
    firstRobot.followLine();
    firstRobot.park();
    firstRobot.turnaround();
    //firstRobot.load_unload(true);
    firstRobot.closePorts();
    //remoteRobot secondRobot = new remoteRobot("10.0.1.2");
    //secondRobot.start();
  }
  public static void waitforfinish(remoteRobot robot){
    while(!robot.finishedAction) {
      try {
        Thread.sleep(200);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
  public static void close(){
    if (firstRobot != null) {
      try {
        firstRobot.closePorts();
      } catch (RemoteException e) {
        e.printStackTrace();
      }
    }
    if (secondRobot != null) {
      try {
        secondRobot.closePorts();
      } catch (RemoteException e) {
        e.printStackTrace();
      }
    }
    System.exit(0);
  }
}
