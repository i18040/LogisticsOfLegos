// Erstellt von Peter Rauschenberger
package com.LogisticsOfLegos.GUI.logic;

import javax.swing.*;
import java.util.Collections;

public class Systemelemente {
	public static int index =1;
public static void eingabefenster() {
	new Listen();
	Integer[] prioint = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
	String[] abholstationStrings = { "Station 1", "Station 2", "Station 3", "Station 4", "Station 5", "Station 6" };	
	String[] abgabestationStrings = { "Station 1", "Station 2", "Station 3", "Station 4", "Station 5", "Station 6" };	
	
	JTextField bezeichnung = new JTextField();
	JComboBox<Integer> prio = new JComboBox<Integer> (prioint);
	JComboBox<String> abholstation = new JComboBox<String> (abholstationStrings);
	JComboBox<String> abgabestation = new JComboBox<String> (abgabestationStrings);
	
	Object[] message = {"Bezeichnug", bezeichnung, "Abholstation", abholstation, "Abgabestation", abgabestation, "Priorit�t", prio};

    JOptionPane pane = new JOptionPane( message, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
    pane.createDialog(null, "Auftrag hinzuf�gen").setVisible(true);
    
    	if ((bezeichnung.getText().length() == 0) ) {
    		JOptionPane.showMessageDialog(null,"Bitte Bezeichnung hinzuf�gen!","Keine Bezeichnug", JOptionPane.PLAIN_MESSAGE);
    	} else if (abholstation.getSelectedItem() == abgabestation.getSelectedItem()) {
    		JOptionPane.showMessageDialog(null,"Die beiden Stationen sind gleich!","Gleiche Stationen", JOptionPane.PLAIN_MESSAGE);
    	} else {
    		Listen.ausstehend.add(new Auftrag(bezeichnung.getText(), (String) abholstation.getSelectedItem(), (String) abgabestation.getSelectedItem(), (int) prio.getSelectedItem(), index));
       		index++;
    	}
    Collections.sort(Listen.ausstehend, new SortTable());
	}

public static int[] getLowestValueInQueue() {
	Collections.sort(Listen.ausstehend, new SortTable());
	int a = Listen.ausstehend.get(1).Prio;
	int b = Listen.ausstehend.get(1).Index;
	int [] c = {a, b};
	return c;

	}

public static int  getIndexAusstehend (int jobId) {
	for(int index = 0; index < Listen.ausstehend.size(); index++) {
		if (Listen.ausstehend.get(index).Index == jobId)
		{
			return index;
		}
	}
	return -1;
}

public static int  getIndexAbgearbeitet (int jobId) {
	for(int index = 0; index < Listen.abgearbeitet.size(); index++) {
		if (Listen.abgearbeitet.get(index).Index == jobId)
		{
			return index;
		}
	}
	return -1;
}

public static int [] acceptJob (int jobId) {
	int s = getIndexAusstehend (jobId);
	Listen.abgearbeitet.add(Listen.ausstehend.get(s));
	String abgabeort = Listen.ausstehend.get(s).Abgabeort;
	String abholort = Listen.ausstehend.get(s).Abholort;
	int a = (int) abgabeort.charAt(8);
	int b = (int) abholort.charAt(8);
	int[] c = { a , b };
	Listen.ausstehend.remove(s);
	Collections.sort(Listen.ausstehend, new SortTable());
	return c;
	
	}

public static int [] acceptJob (int jobId, int jobId2) {
	if(jobId2 == 0) acceptJob(jobId);
	int s = getIndexAusstehend (jobId);
	Listen.abgearbeitet.add(Listen.ausstehend.get(s));
	String abgabeort = Listen.ausstehend.get(s).Abgabeort;
	String abholort = Listen.ausstehend.get(s).Abholort;
	int a = (int) abgabeort.charAt(8);
	int b = (int) abholort.charAt(8);
	int[] c = { a , b };
	Listen.ausstehend.remove(s);
	int g = getIndexAbgearbeitet (jobId2);
	Listen.ausstehend.add(Listen.abgearbeitet.get(g));
	Listen.abgearbeitet.remove(g);
	Collections.sort(Listen.ausstehend, new SortTable());
	return c;
	}
}







