package edu.unimelb.core;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import edu.unimelb.application.MainController;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;


//Remote interface
public class GraphicsContextRMI extends UnicastRemoteObject implements IRemoteDraw
{
	
	/**
	 * 
	 */
	// For serial number
	private static final long serialVersionUID = -99820376343127012L;
	
	private GraphicsContext graphicsContext;
	
	private ObservableList<String> chatData;
	
	private MainController mainController;
	
	private Canvas canvas;

	//Constructor no parameter
	public GraphicsContextRMI() throws RemoteException
	{

	}

	//Constructor with parameters
	public GraphicsContextRMI(GraphicsContext graphicsContext, MainController mainController, Canvas canvas, ObservableList<String> chatData) throws RemoteException
	{
		this.graphicsContext = graphicsContext;
		this.mainController = mainController;
		this.canvas = canvas;
		this.chatData = chatData;
	}
	
	


	@Override
	public synchronized void freeDrawOnMousePressed(Double x, Double y) throws RemoteException
	{
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.beginPath();
		gc.lineTo(x, y);
		gc.stroke();
    }

	public synchronized void refreshCanvas() throws RemoteException {
		graphicsContext.clearRect(canvas.getLayoutX(),canvas.getLayoutY(), canvas.getWidth(), canvas.getHeight());

	}



	@Override
	public synchronized void freeDrawOnMouseDragged(Double x, Double y) throws RemoteException
	{
		// TODO Auto-generated method stub
		GraphicsContext gc = canvas.getGraphicsContext2D();
		//gc.beginPath();
		gc.lineTo(x, y);
		gc.stroke();
		//graphicsContext.lineTo(x, y);
		//graphicsContext.stroke();
	}



	@Override
	public void setColor(String string) throws RemoteException
	{
		// TODO Auto-generated method stub
		Color color = Color.web(string);
		graphicsContext.setStroke(color);
	}
	
	@Override
	public void setSize(Double size) throws RemoteException
	{
		// TODO Auto-generated method stub
		graphicsContext.setLineWidth(size);
	}




	@Override
	public void drawLine(Double x1, Double y1, Double x2, Double y2) throws RemoteException {
		// TODO Auto-generated method stub
		graphicsContext.strokeLine(x1, y1, x2, y2);
	}

	@Override
	public void strokeOval(Double x, Double y, Double w, Double h) throws RemoteException {
		// TODO Auto-generated method stub
		graphicsContext.strokeOval(x, y, w, h);
	}

	@Override
	public void fillOval(Double x, Double y, Double w, Double h) throws RemoteException {
		// TODO Auto-generated method stub
		graphicsContext.fillOval(x, y, w, h);
	}

	@Override
	public void strokeRect(Double x, Double y, Double w, Double h) throws RemoteException {
		// TODO Auto-generated method stub
		graphicsContext.strokeRect(x, y, w, h);
	}

	@Override
	public void fillRect(Double x, Double y, Double w, Double h) throws RemoteException {
		// TODO Auto-generated method stub
		graphicsContext.fillRect(x, y, w, h);
	}

	@Override
	public void setFillColor(String string) throws RemoteException {
		// TODO Auto-generated method stub
		Color color = Color.web(string);
		graphicsContext.setFill(color);
	}
	
	@Override
	public void strokeText(String string, Double x, Double y) throws RemoteException {
		// TODO Auto-generated method stub
		graphicsContext.fillText(string, x, y);
	}

	@Override
	public void placeImage(String string) throws RemoteException {
		// TODO Auto-generated method stub
		Image image = new Image(string);
		graphicsContext.drawImage(image, 0, 0);
	}



	@Override
	public void refreshUser() throws RemoteException {
		Platform.runLater(() ->{
			mainController.refreshUser();
		});
	}


	@Override
	public void broadcastMessage(String message) throws RemoteException {
		
		
		Platform.runLater(() -> {
			chatData.add(message);
			mainController.refreshUser();
		});
	}
	
	@Override
	public void exitApplication() {
		Platform.runLater(() ->{
			mainController.exitApplication();
		});
	}



}
