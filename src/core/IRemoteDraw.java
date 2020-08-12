package edu.unimelb.core;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface IRemoteDraw extends Remote {
	
	public void freeDrawOnMousePressed(Double x, Double y) throws RemoteException;
	
	public void freeDrawOnMouseDragged(Double x, Double y) throws RemoteException;
	
	public void drawLine(Double x1, Double y1, Double x2, Double y2) throws RemoteException;
	
	public void strokeOval(Double x, Double y, Double w, Double h) throws RemoteException;
	
	public void fillOval(Double x, Double y, Double w, Double h) throws RemoteException;
	
	public void strokeRect(Double x, Double y, Double w, Double h) throws RemoteException;
	
	public void fillRect(Double x, Double y, Double w, Double h) throws RemoteException;
	
	public void strokeText(String string, Double x, Double y) throws RemoteException;

	public void refreshCanvas() throws RemoteException;
	
	public void setColor(String color) throws RemoteException;
	
	public void setFillColor (String color) throws RemoteException;
	
	public void setSize(Double size) throws RemoteException;
	
	public void placeImage(String string) throws RemoteException;

	public void refreshUser() throws RemoteException;

	public void broadcastMessage(String message) throws RemoteException;

	public void exitApplication() throws RemoteException;

	
}
