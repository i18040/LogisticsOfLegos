// Erstellt von Peter Rauschenberger
package com.LogisticsOfLegos.GUI.logic;

import java.util.Comparator;
public class SortTable implements Comparator<Auftrag> {

	@Override
	public int compare(Auftrag o1, Auftrag o2) {
		return o1.getPrio() - o2.getPrio();
	}

}
