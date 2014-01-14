package com.stubborneagle.videoStreamSample;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.media.*;
import javax.media.protocol.*;
import javax.media.format.*;
import javax.media.control.TrackControl;
import javax.media.control.QualityControl;

import com.sun.corba.se.spi.ior.MakeImmutable;

import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;

/**
 * A basic JFC 1.1 based application.
 */
public class VideoStream extends javax.swing.JFrame
{
	public VideoStream() throws SocketException
	{
		setLocationRelativeTo(null);
		setResizable(false);
		//{{INIT_CONTROLS
		setTitle("JPEG\\RTP VideoChat");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout(0,0));
		setSize(294,248);
		setVisible(false);
		MainPanel.setToolTipText("Enter the url , file or the sound source name here");
		MainPanel.setLayout(null);
		getContentPane().add(BorderLayout.CENTER, MainPanel);
		MainPanel.setBounds(0,0,488,309);
		ipaddLbl.setText("Remote IP");
		MainPanel.add(ipaddLbl);
		ipaddLbl.setBounds(10,79,71,20);
		label3.setText("Port");
		MainPanel.add(label3);
		label3.setBounds(10,106,60,20);
		MainPanel.add(ipAddr);
		ipAddr.setBounds(83,75,143,24);
		MainPanel.add(port);
		port.setBounds(83,105,71,24);
		mainLabel.setText("JMF Audio\\Video Streaming");
		mainLabel.setAlignment(java.awt.Label.CENTER);
		MainPanel.add(mainLabel);
		mainLabel.setForeground(Color.BLACK);
		mainLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		mainLabel.setBounds(0,10,278,24);
		capsendBtn.setLabel("Capture & Send");
		MainPanel.add(capsendBtn);
		capsendBtn.setBackground(java.awt.Color.lightGray);
		capsendBtn.setForeground(Color.BLACK);
		capsendBtn.setFont(new Font("Dialog", Font.PLAIN, 12));
		capsendBtn.setBounds(21,142,118,28);
		cancelBtn.setEnabled(false);
		cancelBtn.setLabel("Cancel Tranmission");
		MainPanel.add(cancelBtn);
		cancelBtn.setBackground(java.awt.Color.lightGray);
		cancelBtn.setForeground(Color.BLACK);
		cancelBtn.setFont(new Font("Dialog", Font.PLAIN, 12));
		cancelBtn.setBounds(155,142,118,28);
			
		comboBox.setBounds(83, 48, 143, 24);
		MainPanel.add(comboBox);
		
		Label label = new Label();
		label.setText("Local IP");
		label.setBounds(10, 48, 65, 24);
		MainPanel.add(label);
		
		listenBtn.setLabel("Listen for input Streams");
		listenBtn.setForeground(Color.BLACK);
		listenBtn.setFont(new Font("Dialog", Font.PLAIN, 12));
		listenBtn.setBackground(Color.LIGHT_GRAY);
		listenBtn.setBounds(83, 176, 143, 28);
		MainPanel.add(listenBtn);
		//}}

		//{{INIT_MENUS
		//}}

		//{{REGISTER_LISTENERS
		SymWindow aSymWindow = new SymWindow();
		this.addWindowListener(aSymWindow);
		SymAction lSymAction = new SymAction();
		SymItem lSymItem = new SymItem();
		capsendBtn.addActionListener(lSymAction);
		cancelBtn.addActionListener(lSymAction);
		listenBtn.addActionListener(lSymAction);
		
		enumNic();
		//}}
	}

    /**
     * Creates a new instance of JFrame1 with the given title.
     * @param sTitle the title for the new frame.
     * @throws SocketException 
     * @see #JFrame1()
     */
	public VideoStream(String sTitle) throws SocketException
	{
		this();
		setTitle(sTitle);
	}
	
	/**
	 * The entry point for this application.
	 * Sets the Look and Feel to the System Look and Feel.
	 * Creates a new JFrame1 and makes it visible.
	 */
	static public void main(String args[])
	{
		try {
		    // Add the following code if you want the Look and Feel
		    // to be set to the Look and Feel of the native system.
		    /*
		    try {
		        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		    } 
		    catch (Exception e) { 
		    }
		    */
			//Create a new instance of our application's frame, and make it visible.
			(new VideoStream()).setVisible(true);
		} 
		catch (Throwable t) {
			t.printStackTrace();
			//Ensure the application exits with an error condition.
			System.exit(1);
		}
	}

    /**
     * Notifies this component that it has been added to a container
     * This method should be called by <code>Container.add</code>, and 
     * not by user code directly.
     * Overridden here to adjust the size of the frame if needed.
     * @see java.awt.Container#removeNotify
     */
	public void addNotify()
	{
		// Record the size of the window prior to calling parents addNotify.
		Dimension size = getSize();
		
		super.addNotify();
		
		if (frameSizeAdjusted)
			return;
		frameSizeAdjusted = true;
		
		// Adjust size of frame according to the insets and menu bar
		javax.swing.JMenuBar menuBar = getRootPane().getJMenuBar();
		int menuBarHeight = 0;
		if (menuBar != null)
		    menuBarHeight = menuBar.getPreferredSize().height;
		Insets insets = getInsets();
		setSize(insets.left + insets.right + size.width, insets.top + insets.bottom + size.height + menuBarHeight);
	}

	// Used by addNotify
	boolean frameSizeAdjusted = false;

	//{{DECLARE_CONTROLS
	javax.swing.JPanel MainPanel = new javax.swing.JPanel();
	java.awt.Label ipaddLbl = new java.awt.Label();
	java.awt.Label label3 = new java.awt.Label();
	java.awt.TextField ipAddr = new java.awt.TextField();
	java.awt.TextField port = new java.awt.TextField();
	java.awt.Label mainLabel = new java.awt.Label();
	java.awt.Button capsendBtn = new java.awt.Button();
	java.awt.Button cancelBtn = new java.awt.Button();
	JComboBox comboBox = new JComboBox();	
	Button listenBtn = new Button();
	AVTransmit2 vt;
	AVReceive2 avReceive;
	//}}

	//{{DECLARE_MENUS
	//}}

	void exitApplication()
	{
		try {
			// Beep
			Toolkit.getDefaultToolkit().beep();
			// Show a confirmation dialog
			int reply = JOptionPane.showConfirmDialog(this, 
					"Do you really want to exit?", 
					"JFC Application - Exit" , 
					JOptionPane.YES_NO_OPTION, 
					JOptionPane.QUESTION_MESSAGE);
			// If the confirmation was affirmative, handle exiting.
			if (reply == JOptionPane.YES_OPTION)
			{
				this.setVisible(false);    // hide the Frame
				this.dispose();            // free the system resources
				System.exit(0);            // close the application
			}
		} catch (Exception e) {
		}
	}

	class SymWindow extends java.awt.event.WindowAdapter
	{
		public void windowClosing(java.awt.event.WindowEvent event)
		{
			Object object = event.getSource();
			if (object == VideoStream.this)
				VideoStream_windowClosing(event);
		}
	}

	void VideoStream_windowClosing(java.awt.event.WindowEvent event)
	{
		// to do: code goes here.
	}

	class SymAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
			if (object == capsendBtn)
				capsendBtn_ActionPerformed(event);
			if (object == cancelBtn)
				try {
					cancelBtn_ActionPerformed(event);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			
			if (object == listenBtn)
				listenBtn_ActionPerformed(event);
		}
	}

	class SymItem implements java.awt.event.ItemListener
	{
		public void itemStateChanged(java.awt.event.ItemEvent event)
		{
		}
	}

	void capsendBtn_ActionPerformed(java.awt.event.ActionEvent event)
	{

		String ip   = ipAddr.getText();
		String prt  = port.getText();

		if(ip.isEmpty() || prt.isEmpty()){   
			JOptionPane optionPane = new JOptionPane("Insert all parameters", JOptionPane.ERROR_MESSAGE);    
			JDialog dialog = optionPane.createDialog("Failure");
			dialog.setAlwaysOnTop(true);
			dialog.setVisible(true);
			return;
		}

		vt = new AVTransmit2(null, comboBox.getSelectedItem().toString(),ipAddr.getText(), prt, null);
		
		// Start the transmission
		new Thread()
		{
			public void run() {
				capsendBtn.setEnabled(false);
				cancelBtn.setEnabled(true);
				String result = vt.start();
				// result will be non-null if there was an error. The return
				// value is a String describing the possible error. Print it.
				if (result != null) {
					System.err.println("Error : " + result);
					System.exit(0);
				}
				System.err.println("Start transmission for 60 seconds...");
			}
		}.start();

		
	}

	void cancelBtn_ActionPerformed(java.awt.event.ActionEvent event) throws InterruptedException
	{
	    // Stop the transmission
		if(capsendBtn.isEnabled() == true) return;
		vt.stop();
		System.err.println("...transmission ended.");
		Thread.sleep(1000);
		capsendBtn.setEnabled(true);
		cancelBtn.setEnabled(false);
			 
	}

	void listenBtn_ActionPerformed(java.awt.event.ActionEvent event)
	{
		String ip   = ipAddr.getText();
		String prt  = port.getText();

		if(ip.isEmpty() || prt.isEmpty()){   
			JOptionPane optionPane = new JOptionPane("Insert all parameters", JOptionPane.ERROR_MESSAGE);    
			JDialog dialog = optionPane.createDialog("Failure");
			dialog.setAlwaysOnTop(true);
			dialog.setVisible(true);
			return;
		}

		String[] sessions = {ipAddr.getText() + "/" + prt + "/" + "1",ipAddr.getText() + "/" + (Integer.parseInt(prt)+2) + "/" + "1"};
		avReceive = new AVReceive2(comboBox.getSelectedItem().toString(), sessions);    	
		new Thread()
		{
			public void run() {
		    	if (!avReceive.initialize()) {
		    		System.err.println("Failed to initialize the sessions.");
		    		System.exit(-1);
		    	}
			}
		}.start();
	}
	
	private void enumNic() throws SocketException{
        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        for (NetworkInterface netint : Collections.list(nets)){
        	Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
        	for (InetAddress inetAddress : Collections.list(inetAddresses)) {
        		if( inetAddress.isSiteLocalAddress())
        		comboBox.addItem(inetAddress.toString().substring(1));
        		//out.printf("InetAddress: %s\n", inetAddress);
        	}
        }
	}

}
