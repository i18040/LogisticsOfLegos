package com.LogisticsOfLegos.Movement;

import lejos.remote.ev3.RMIRegulatedMotor;
import lejos.remote.ev3.RMISampleProvider;

import java.rmi.RemoteException;

class LCSensorRight extends Thread {
  public boolean isInitialized = false;
  private RMISampleProvider sp = null;
  private RMIRegulatedMotor rightMotor = null;
  public boolean suspended = false;
  public boolean turnleft = false;
  public boolean turnright = false;
  public boolean turnaround = false;
  public boolean skip = false;
  public boolean go = false;
  public boolean park = false;
  private int color = 0;

  LCSensorRight(RMISampleProvider sampleProvider, RMIRegulatedMotor rm) {
    rightMotor = rm;
    sp = sampleProvider;
  }

  public void close(){
    try {
      sp.close();
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }

  int getColor() {
    return color;
  }

  public synchronized void run() {
    while (true) {
      float[] sample = new float[0];
      try {
        sample = sp.fetchSample();
        isInitialized = true;
      } catch (RemoteException e) {
        e.printStackTrace();
      }
      color = (int) sample[0];
      if (!suspended) {
        if (color != 7) {
          try {
            rightMotor.setSpeed(50);
            rightMotor.forward();
          } catch (RemoteException e) {
            e.printStackTrace();
          }
        } else {
          try {
            rightMotor.setSpeed(0);
            rightMotor.stop(false);
          } catch (RemoteException e) {
            e.printStackTrace();
          }
        }
      }
      if(turnleft){
        try {
          rightMotor.setSpeed(50);
          rightMotor.rotate(380);
        } catch (RemoteException e) {
          e.printStackTrace();
        }
        turnleft = false;
        }
      if(turnright){
        try {
          rightMotor.setSpeed(50);
          rightMotor.rotate(20);
        } catch (RemoteException e) {
          e.printStackTrace();
        }
        turnright = false;
      }
      if(skip) {
        try {
          rightMotor.setSpeed(50);
          rightMotor.rotate(180);
        } catch (RemoteException e) {
          e.printStackTrace();
        }
        skip = false;
      }
      if(go) {
        try {
          rightMotor.setSpeed(50);
          rightMotor.rotate(90);
        } catch (RemoteException e) {
          e.printStackTrace();
        }
        go = false;
      }
      if(park) {
        try {
          rightMotor.setSpeed(50);
          rightMotor.rotate(180);
        } catch (RemoteException e) {
          e.printStackTrace();
        }
        park = false;
      }
      if(turnaround) {
        try {
          rightMotor.setSpeed(50);
          rightMotor.rotate(360);
        } catch (RemoteException e) {
          e.printStackTrace();
        }
        turnaround = false;
      }
    }
  }
}
