package com.LogisticsOfLegos;

import com.LogisticsOfLegos.Movement.remoteRobot;
import lejos.remote.ev3.RMIRegulatedMotor;
import lejos.remote.ev3.RMISampleProvider;
import lejos.remote.ev3.RemoteEV3;

import java.net.MalformedURLException;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Init {
  public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
    /*boolean connected = false;
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
    RMISampleProvider color_sensor = ev3.createSampleProvider("S4",
            "lejos.hardware.sensor.EV3ColorSensor", "ColorID");
    float[] color_id= color_sensor.fetchSample();
    System.out.println("color = "+color_id[0]);
    color_sensor.close();
    RMIRegulatedMotor leftMotor = ev3.createRegulatedMotor("A",'L');
    leftMotor.rotate(180);
    leftMotor.close();
    */
    remoteRobot firstRobot = new remoteRobot("10.0.1.1");
    firstRobot.start();
    remoteRobot secondRobot = new remoteRobot("10.0.1.2");
    secondRobot.start();
  }
}
