package com.techelevator;

public class Television {
	
	private boolean isOn = false;
	private int currentChannel = 3;
	private int currentVolume = 2;

	public boolean isOn() {
		return isOn;
	}

	public int getCurrentChannel() {
		return currentChannel;
	}
	public int getCurrentVolume() {
		return currentVolume;
	}
	
	public void turnOff() {
		isOn = false;
	}
	public void turnOn() {
		isOn = true; 
		currentChannel = 3;
		currentVolume = 2;
	}
	public void changeChannel(int newChannel) {
		if (isOn == true) {
			if (newChannel > 2 && newChannel < 19) {
				currentChannel = newChannel;
			}
		}
	}
	public void channelUp() {
		if (isOn == true) {
			currentChannel+=1;
			if (currentChannel > 18) {
				currentChannel = 3;
			}
		}
	}
	public void channelDown() {
		if (isOn == true) {
			currentChannel-=1;
			if (currentChannel < 3) {
				currentChannel = 18;
			}
		}
	}
	public void raiseVolume() {
		if (isOn == true) {
			currentVolume+=1;
			if (currentVolume > 10) {
				currentChannel = 10;
			}
		}
	}
	public void lowerVolume() {
		if (isOn == true) {
			currentVolume-=1;
			if (currentVolume < 0) {
				currentVolume = 0;
			}
		}
	}
}
