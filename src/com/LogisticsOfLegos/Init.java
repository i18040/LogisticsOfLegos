package com.LogisticsOfLegos;

import com.LogisticsOfLegos.GUI.ui.Startseite;
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


    firstRobot = new remoteRobot("10.0.1.1", true);
    firstRobot.start();
    secondRobot = new remoteRobot("10.0.1.4", false);
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
    Navigation navi = new Navigation();
    Startseite seite = new Startseite();
    seite.setVisible(true);
    navi.firstRobot = new Navigation.Robot(7, Status.IDLE, 3, firstRobot);
    navi.secondRobot = new Navigation.Robot (8, Status.IDLE, 1, secondRobot);

    //firstRobot.startMove(0);
    //firstRobot.readSensors();
    firstRobot.go();
    firstRobot.followLine();
    firstRobot.turn(turndirection.LEFT);
    firstRobot.followLine();
    firstRobot.turn(turndirection.NONE);
    firstRobot.followLine();
    firstRobot.turn(turndirection.RIGHT);
    firstRobot.followLine();
    firstRobot.park();
    firstRobot.turnaround();
    firstRobot.closePorts();
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
