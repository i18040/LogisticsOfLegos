//Erstellt von Peter Rauschenberger
package com.LogisticsOfLegos.GUI.ui;

import com.LogisticsOfLegos.GUI.logic.Systemelemente;
import com.LogisticsOfLegos.Init;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Startseite extends JFrame implements ActionListener {
		Button erstellen;
		Button close;
		
		public Startseite() {
			super ("Auftrag erstellen");
			GridBagLayout seite = new GridBagLayout();
			setLayout(seite);
			
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.fill =  GridBagConstraints.HORIZONTAL;
			gbc.insets = new Insets(5, 5, 5, 5);
			
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.gridwidth = 10;
			gbc.gridheight = 1;
			erstellen = new Button ("Auftrag erstellen");
			erstellen.addActionListener(this);
			seite.setConstraints(erstellen, gbc);
			add(erstellen);
			
			//gbc.gridx = 0;
			//gbc.gridy = 1;
			//gbc.gridwidth = 10;
			//gbc.gridheight = 1;
			//DefaultTableModel tableModel = new DefaultTableModel(line, 0);
			//for 
			//erstellen.addActionListener(this);
			//seite.setConstraints(erstellen, gbc);
			//add(erstellen);
			
			gbc.gridx = 0;
			gbc.gridy = 2;
			gbc.gridwidth = 10;
			gbc.gridheight = 1;
			close = new Button ("Threads beenden");
			close.addActionListener(this);
			seite.setConstraints(close, gbc);
			add(close);
			
			
			
			
			
					
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == erstellen) {
				Systemelemente.eingabefenster();
			}
			if (e.getSource() == close)  {
				Init.close();
				this.dispose();
			}
}
}
