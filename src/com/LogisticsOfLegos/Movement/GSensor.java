package com.LogisticsOfLegos.Movement;

import lejos.remote.ev3.RMISampleProvider;

import java.rmi.RemoteException;

class GSensor extends Thread {
  public boolean isInitialized = false;
  private RMISampleProvider sp = null;
  private int angle = 0;

  GSensor(RMISampleProvider sampleProvider) {
    sp = sampleProvider;
  }

  public void close(){
    try {
      sp.close();
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }

  int getAngle() {
    return angle;
  }

  public synchronized void run() {
    while(true){
      float[] sample = new float[0];
      try {
        sample = sp.fetchSample();
        isInitialized = true;
      } catch (RemoteException e) {
        e.printStackTrace();
      }
      angle = (int) sample[0];
    }
  }
}
