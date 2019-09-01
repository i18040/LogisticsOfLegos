//Erstellt von Mike Sütö & Nils Becker
package com.LogisticsOfLegos.Movement;

import lejos.remote.ev3.RMISampleProvider;

import java.rmi.RemoteException;

class USSensor extends Thread {
  public boolean isInitialized = false;
  private RMISampleProvider sp = null;
  private double distance;

  public void close(){
    try {
      sp.close();
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }

  USSensor(RMISampleProvider sampleProvider) {
    sp = sampleProvider;
  }
  double getDistance(){
    return distance;
  }
  public synchronized void run(){
    while(true){
      float[] sample = new float[0];
      try {
        sample = sp.fetchSample();
        isInitialized = true;
      } catch (RemoteException e) {
        e.printStackTrace();
      }
      distance = (double) sample[0];
    }
  }
}
