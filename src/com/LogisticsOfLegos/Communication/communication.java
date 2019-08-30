package com.LogisticsOfLegos.Communication;

import lejos.hardware.*;
import lejos.remote.ev3.RemoteEV3;
import lejos.utility.Delay;

public class communication {

    public static void remoteTestManuell()
    {
        // Test-Methode
        // Wird auf dem Host-Roboter ("Roboter1") ausgeführt
        // spricht den anderen Roboter im Netzwerk an
        // Betätigt bei allen erreichbaren Robotern die LEDs

        // Name & IP der Roboter müssen manuell eingetragen werden:
        String[] names = {"Roboter1", "Roboter2"};
        String[] ip  = {"10.0.0.1", "10.0.0.2"};
        Brick[] bricks = new Brick[names.length];
        try {
            bricks[0] = BrickFinder.getLocal();
            for(int i = 1; i < bricks.length; i++) //Anzahl der Bricks
            {
                //System.out.println("Connect " + names[i]);
                bricks[i] = new RemoteEV3(ip[i]);
            }
            LED[] leds = new LED[bricks.length];
            for(int i = 0; i < bricks.length; i++)
                leds[i] = bricks[i].getLED();
            int i = 0;
            int pat = 1;
            while(Button.ENTER.isUp())
            {
                leds[(i++) % leds.length].setPattern(0);
                if (i % leds.length == 0)
                {
                    pat = ((pat + 1) % 3) + 1;
                }
                leds[(i) % leds.length].setPattern(pat);
                Delay.msDelay(100);
            }
            for(LED l : leds)
                l.setPattern(0);
        }
        catch (Exception e)
        {
            System.out.println("Got exception " + e);
        }
    }

    public static void remoteTestAuto()
    {
        //

        try{
            BrickInfo[] brickInfos = BrickFinder.discover();
            for(BrickInfo brickInfo: brickInfos)
            {
                Brick brick = new RemoteEV3(brickInfo.getIPAddress());

                Audio audio = brick.getAudio();
                audio.systemSound(0);
            }
        }
        catch (Exception e)
        {
            System.out.println("Got exception " + e);
        }
    }

    public static void main(String[] args)
    {
        remoteTestAuto();       // try finding automatically
//        remoteTestManuell();    // try finding via IP-Adress
    }

}
