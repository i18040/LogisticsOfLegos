// Erstellt von Peter Rauschenberger
package com.LogisticsOfLegos.GUI.logic;

public class Auftrag {
	public String Bezeichnung;
	public String Abholort;
	public String Abgabeort;
	public int Index;
	public int Prio;
	
	public Auftrag(String Bezeichnung, String Abholort, String Abgabeort, int Prio, int Index) {
		this.Bezeichnung = Bezeichnung;
		this.Abholort = Abholort;
		this.Abgabeort = Abgabeort;
		this.Prio = Prio;
		this.Index = Index;
	}
	
	public int getPrio() {
	return this.Prio;
	}
}

