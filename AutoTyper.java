package com.kiriloman.autotyper;

import java.awt.*;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;
import java.awt.event.*;

public class AutoTyper {

	private static int interval;
	private static String text;
	private static Timer timer;
	private static JTextField txtSpam;
	private static JTextField txtInterval;
	private static JButton btnStart;
	private static JButton btnStop;
	private static boolean isSpamming;
	private static long startTime, elapsedTime;
	private static SwingWorker spamWorker;

	public static void main(String[] args) throws InterruptedException {
		isSpamming = false;

		btnStart = new JButton("Start Spam");
		btnStop = new JButton("Stop Spam");
		btnStart.setBackground(Color.WHITE);
		btnStart.setForeground(Color.BLACK);
		btnStop.setBackground(Color.WHITE);
		btnStop.setForeground(Color.BLACK);
		JLabel lbl1 = new JLabel("Text to Spam");
		JLabel lbl2 = new JLabel("Interval:");
		txtSpam = new JTextField("", 13);
		txtSpam.setBackground(Color.WHITE);
		txtSpam.setForeground(Color.BLACK);
		txtInterval = new JTextField("3000", 3);
		txtInterval.setBackground(Color.WHITE);
		txtInterval.setForeground(Color.BLACK);
		btnStop.setEnabled(false);

		JPanel intervalpane = new JPanel();
		intervalpane.add(lbl2, BorderLayout.EAST);
		intervalpane.add(txtInterval, BorderLayout.WEST);

		JPanel bottompane = new JPanel();
		bottompane.add(btnStart, BorderLayout.EAST);
		bottompane.add(btnStop, BorderLayout.CENTER);
		bottompane.add(intervalpane, BorderLayout.WEST);

		JPanel toppane = new JPanel();
		toppane.add(lbl1, BorderLayout.EAST);
		toppane.add(txtSpam, BorderLayout.NORTH);

		JPanel pane = new JPanel(new BorderLayout());
		pane.add(toppane, BorderLayout.NORTH);
		pane.add(bottompane, BorderLayout.SOUTH);

		JFrame frame = new JFrame("Auto Typer");
		frame.setForeground(Color.BLACK);

		ActionListener beginSpam = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				spamWorker = new SwingWorker<Void, String>() {
					@Override
					protected Void doInBackground() {
						System.out.println(isSpamming);
						while (isSpamming) {
							try {
								Thread.sleep(randomLong());
								sendKeys(text);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						return null;
					}
				};
				btnStart.setEnabled(false);
				btnStop.setEnabled(true);
				txtInterval.setEnabled(false);
				isSpamming = true;
				text = txtSpam.getText();
				interval = Integer.parseInt(txtInterval.getText());
				spamWorker.execute();
			}
		};
		ActionListener stopSpam = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				spamWorker.cancel(true);
				isSpamming = false;
				btnStart.setEnabled(true);
				btnStop.setEnabled(false);
				txtInterval.setEnabled(true);
			}
		};

		btnStart.addActionListener(beginSpam);
		btnStop.addActionListener(stopSpam);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(pane);
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
	}

	private static void sendKeys(String text) {
		try {
			Robot robot = new Robot();
			for (int i = 0; i < text.length(); i++) {
				if (text.charAt(i) == ':') {
					robot.keyPress(KeyEvent.VK_SHIFT);
					robot.keyPress(KeyEvent.VK_COLON);
					robot.keyRelease(KeyEvent.VK_COLON);
					robot.keyRelease(KeyEvent.VK_SHIFT);
				} else {
					robot.keyPress(KeyEvent.getExtendedKeyCodeForChar(text.charAt(i)));
					robot.keyRelease(KeyEvent.getExtendedKeyCodeForChar(text.charAt(i)));
				}
			}
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
		} catch (java.awt.AWTException exc) {
			System.out.println("error");
		}
		System.out.println("keys sent");
	}
	
	private static int randomLong() {
		return (int) (3000 + Math.random() * 7000);
	}
}
