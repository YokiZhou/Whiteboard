package edu.unimelb.application;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;
import javax.imageio.ImageIO;
import edu.unimelb.core.GraphicsContextRMI;
import edu.unimelb.core.IRemoteDraw;
import edu.unimelb.core.User;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.image.WritableImage;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.StageStyle;
import javafx.util.Pair;

public class MainController implements Initializable
{
	
	@FXML
	private ToolBar toolBar;
	
	@FXML
	private MenuBar menuBar;
	
	@FXML
	private TextField portTextField;
	
	@FXML
	private TextField hostTextField;
	
	@FXML
	private TextField usernameTextField;
	
	@FXML
	private ColorPicker penColorPicker;
	
	@FXML
	private Canvas mainCanvas;
	
	@FXML
	private Label penSizeLable;
	
	@FXML
	private Slider penSizeSlider;
	
	@FXML
	private Label eraserSizeLable;
	
	@FXML
	private Slider eraserSizeSlider;
	
	@FXML
	private ToggleButton penButton;
	
	@FXML
	private ToggleButton eraserButton;
	
	@FXML
	private ToggleButton shapeButton;
	
	@FXML
	private ToggleButton textButton;
	
	@FXML
	private TitledPane startTitledPane;
	
	@FXML
	private TitledPane penTitledPane;
	
	@FXML
	private TitledPane eraserTitledPane;
	
	@FXML
	private TitledPane shapeTitledPane;
	
	@FXML
	private TitledPane textTitledPane;
	
	@FXML
	private Pane pane;
	
	@FXML
	private ListView<String> onlineUserListView;
	
	@FXML
	private ListView<String> chatListView;
	
	@FXML
	private RadioButton lineRadioButton;
	
	@FXML
	private RadioButton rectRadioButton;
	
	@FXML
	private RadioButton joinRadioButton;
	
	@FXML
	private RadioButton createRadioButton;
	
	@FXML
	private RadioButton ovalRadioButton;
	
	@FXML
	private ColorPicker shapeColorPicker;
	
	@FXML
	private Slider shapeSizeSlider;
	
	@FXML
	private Slider ovalWidthSlider;
	
	@FXML
	private Slider ovalHeightSlider;
	
	@FXML
	private Slider rectwidthSlider;
	
	@FXML
	private Slider rectHeightSlider;
	
	@FXML
	private ColorPicker textColorPicker;
	
	@FXML
	private TextField textTextField;
	
	@FXML
	private TextField chatTextField;
	
	@FXML
	private Button sendButton;
	
	
	private Boolean isReady = true;
	
	
	private User currentUser = new User();


	private Registry registry;
	
	
	private GraphicsContext graphicsContext;
	
	private Pair<Double, Double> initialTouch;
	
	private ObservableList<String> onlineUserData = FXCollections.observableArrayList();
	
	private ObservableList<String> chatData = FXCollections.observableArrayList();
	
	HashMap<String, IRemoteDraw> workingPool = new HashMap<String, IRemoteDraw>();


	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		menuBar.setDisable(true);
		toolBar.setDisable(true);
		sendButton.setDisable(true);
		hostTextField.setDisable(true);
		startButtonListener();
		initPen();
		numberOnly();
		buttonListener();
		shapeListener();
		closeAllMenu();
		expandAllMenu();

		//user name
		UUID uuid = UUID.randomUUID();
		usernameTextField.setText(uuid.toString().replace("-", "").substring(0, 10).toUpperCase());
		usernameTextField.setEditable(true);
		usernameTextField.setMouseTransparent(false);
		usernameTextField.setFocusTraversable(false);


		onlineUserListView.setItems(onlineUserData);
		startTitledPane.setVisible(true);
		mainCanvas.setOnMouseEntered(new EventHandler()
		{
			@Override
			public void handle(Event event)
			{
				// TODO Auto-generated method stub
				mainCanvas.setCursor(Cursor.CROSSHAIR);
			}
    	});
	}
	
	/*
	 * This function is used to filter the invalid port number input.
	 */
	private void numberOnly()
	{
		portTextField.textProperty().addListener(new ChangeListener<String>()
		{
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, 
		        String newValue)
			{
		        if (!newValue.matches("\\d*"))
		        {
		        	portTextField.setText(newValue.replaceAll("[^\\d]", ""));
		        }
		    }
		});
	}

	public Boolean getIsApproved()
	{
		return true;
	}
	
	public void startButtonListener()
	{
		joinRadioButton.selectedProperty().addListener(((observable, oldValue, newValue) -> {
		    if (newValue) {
		    	hostTextField.setDisable(false);
			}
		}));
		
		createRadioButton.selectedProperty().addListener(((observable, oldValue, newValue) -> {
		    if (newValue) {
		    	hostTextField.setDisable(true);
			}
		}));
	}

	//pop up a new window WARNING
	public void alertPopUp(String string)
	{
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Warning");
		alert.setHeaderText(null);
		alert.setContentText(string);
		alert.initStyle(StageStyle.UTILITY);
		alert.showAndWait();
	}


	
	public Boolean dialogPopUp(String string)
	{
		Alert alert = new Alert(AlertType.CONFIRMATION, string, ButtonType.YES, ButtonType.NO);
		alert.showAndWait();
		if (alert.getResult() == ButtonType.YES)
		{
			return true;
		}
		else {
			return false;
		}
	}

	@FXML
	private void startServer() throws RemoteException
	{
		//create whiteboard is admin
		if (createRadioButton.isSelected())
		{
			try
			{
				isReady = true;
				if (portTextField.getText().isEmpty()) {
					alertPopUp("Please input port");
					return;
				}
				//get User name
				currentUser.setUsername(usernameTextField.getText());
				//Set this user as admin
				currentUser.setIsAdmin(true);

				//Start remote server
				IRemoteDraw serverRemote = new GraphicsContextRMI(graphicsContext, StartApplication.getController(), mainCanvas, chatData);
				//Start registy
				registry = LocateRegistry.createRegistry(Integer.parseInt(portTextField.getText()));
				registry.bind(currentUser.getUsername() + "_ADMIN", serverRemote);

				
			} catch (Exception e) {
				isReady = false;
				alertPopUp("Already Bound!\nPlease input another port!");
			} finally {
				if (isReady) {
					toolBar.setDisable(false);
					menuBar.setDisable(false);
					penButton.setSelected(true);
					sendButton.setDisable(false);
					refreshUser();
					for (Map.Entry<String, IRemoteDraw> entry : workingPool.entrySet()) {
						entry.getValue().refreshUser();
					}
				}
			}
		}

		//Join whiteboard
		else if (joinRadioButton.isSelected()) {
			try {
				isReady = true;
				currentUser.setUsername(usernameTextField.getText());
				currentUser.setIsAdmin(false);

				//remote execute
				registry = LocateRegistry.getRegistry(hostTextField.getText(), Integer.parseInt(portTextField.getText()));
				IRemoteDraw clientRemoteDraw = new GraphicsContextRMI(graphicsContext, StartApplication.getController(), mainCanvas, chatData);
				String userName = (currentUser.getUsername() +"_NEED_APPROVAL");
				registry.bind(userName , clientRemoteDraw);
			}
			catch (Exception e) {
				isReady = false;
				alertPopUp("Registry does not exist");
			} finally {
				if (isReady) {
					toolBar.setDisable(false);
					penButton.setSelected(true);
					sendButton.setDisable(false);
					refreshUser();
					for (Map.Entry<String, IRemoteDraw> entry : workingPool.entrySet()) {
						entry.getValue().refreshUser();
					}
				}
			}
			

		}

	}
	
	public void updateOnlineUser() {
		try {
			onlineUserData.clear();
			onlineUserListView.getItems().removeAll();
			String[] boundNames = registry.list();
			for (String name : boundNames){
				onlineUserData.add(name);
			}
			onlineUserListView.setItems(onlineUserData);
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void refreshUser() {
		try
		{
			chatListView.getItems().removeAll();
			chatListView.setItems(chatData);
			//userList.clear();
			workingPool.clear();
			//Obtain registry list
			String[] boundNames = registry.list();

			for (String name : boundNames) {
				if (!name.contains("_NEED_APPROVAL")) {
					IRemoteDraw remoteDraw = (IRemoteDraw) registry.lookup(name);
					workingPool.put(name, remoteDraw);
					//workPool.add(remoteDraw);
				}
				updateOnlineUser();
	        }
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}

	
	public void expandAllMenu() {
		startTitledPane.setExpanded(true);
		penTitledPane.setExpanded(true);
		eraserTitledPane.setExpanded(true);
		shapeTitledPane.setExpanded(true);
		textTitledPane.setExpanded(true);
	}
	
	public void closeAllMenu() {
		startTitledPane.setVisible(false);
		penTitledPane.setVisible(false);
		eraserTitledPane.setVisible(false);
		shapeTitledPane.setVisible(false);
		textTitledPane.setVisible(false);
	}
	
	public void clearEvent() {
		mainCanvas.setOnMousePressed(null);
    	mainCanvas.setOnMouseReleased(null);
    	mainCanvas.setOnMouseDragged(null);
    	pane.setOnMouseClicked(null);
	}
	
	public void initPen() {
		graphicsContext = mainCanvas.getGraphicsContext2D();
		penColorPicker.setValue(Color.BLACK);
		shapeColorPicker.setValue(Color.BLACK);
		textColorPicker.setValue(Color.BLACK);
		penSizeSlider.setValue(1);
	}
	
	public void initEraser()
	{
//		graphicsContext = mainCanvas.getGraphicsContext2D();
//    	graphicsContext.setStroke(Color.valueOf("F4F4F4"));
	}
	
	public void initShape()
	{
//		graphicsContext = mainCanvas.getGraphicsContext2D();
//		graphicsContext.setStroke(Color.BLACK);
//		shapeColorPicker.setValue(Color.BLACK);
//		shapeSizeSlider.setValue(1);
//		graphicsContext.setLineWidth(1);
//		ovalHeightSlider.setValue(20);
//		ovalWidthSlider.setValue(20);
//		rectHeightSlider.setValue(20);
//		rectwidthSlider.setValue(20);
	}
	
	public void initText()
	{
		
	}
	
	@FXML
	//Open button
	private void openFile()
	{
		FileChooser fileChooser = new FileChooser();
		File selectedFile = fileChooser.showOpenDialog(penButton.getScene().getWindow());
		if (selectedFile != null) {
			try {
				for (Map.Entry<String, IRemoteDraw> entry : workingPool.entrySet()) {
					entry.getValue().placeImage("file:///" + selectedFile.getPath().replace("\\", "/"));
				}
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	@FXML
	//save button
	private void saveFile() {
		WritableImage writableImage = new WritableImage(798, 724);
        mainCanvas.snapshot(null, writableImage);
        File outputfile = new File(System.getProperty("user.home") + "/save.png");
        RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
        try {
			ImageIO.write(renderedImage, "png", outputfile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			alertPopUp("Saved!");
		}
	}
	@FXML
	private void newFile() throws IOException {
		//saveFileAs();
		try {
			for (Map.Entry<String, IRemoteDraw> entry : workingPool.entrySet()) {
				entry.getValue().refreshCanvas();
			}
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@FXML
	private void saveFileAs() throws IOException {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("png files (*.png)", "*.png"),
				new FileChooser.ExtensionFilter("gif files (*.gif)", "*.gif"));
		fileChooser.setTitle("Save File As");
		File savedFile = fileChooser.showSaveDialog(penButton.getScene().getWindow());
		if (savedFile != null) {
			String chosenEx = fileChooser.getSelectedExtensionFilter().getExtensions().get(0);
			String exten = chosenEx.replaceAll("[^A-Za-z]+", "");
			WritableImage writableImage = new WritableImage(798, 724);
			mainCanvas.snapshot(null, writableImage);
			RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
			ImageIO.write(renderedImage, exten, savedFile);
		}

	}


	@FXML
	private void closeFile(ActionEvent event) {
		try {
			for (Map.Entry<String, IRemoteDraw> entry : workingPool.entrySet()) {
				entry.getValue().exitApplication();
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		System.exit(0);
	}
	
	public void exitApplication() {
		System.exit(0);
	}


	@FXML
	private void delete(ActionEvent event) {

	}
	
	public Boolean isApprovedChecker() {
		Boolean flagBoolean = false;
		for (Map.Entry<String, IRemoteDraw> entry : workingPool.entrySet()) {
			if (entry.getKey().contains(currentUser.getUsername())) {
				flagBoolean = true;
			}
		}
		return flagBoolean;
	}


	public void buttonListener()
	{
		penButton.selectedProperty().addListener(((observable, oldValue, newValue) ->
		{
		    if (newValue)
		    {
		    	closeAllMenu();
		    	clearEvent();
		    	penTitledPane.setVisible(true);
		    	initPen();
	    		try
				{
	    				mainCanvas.setOnMousePressed(e->{
	    					refreshUser();
	    	    			if (isApprovedChecker()) {
	    	    				try {
									for (Map.Entry<String, IRemoteDraw> entry : workingPool.entrySet()) {
										entry.getValue().setSize(penSizeSlider.getValue());
										entry.getValue().setColor(penColorPicker.getValue().toString());
										entry.getValue().freeDrawOnMousePressed(e.getX(), e.getY());
									}
								} catch (RemoteException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
	    	    			} else {
								alertPopUp("You are not approved or kicked");
							}
							

						});
						mainCanvas.setOnMouseDragged(e->{
							if (isApprovedChecker()) {
								try {
									for (Map.Entry<String, IRemoteDraw> entry : workingPool.entrySet()) {
										entry.getValue().setSize(penSizeSlider.getValue());
										entry.getValue().setColor(penColorPicker.getValue().toString());
										entry.getValue().freeDrawOnMouseDragged(e.getX(), e.getY());
									}
								} catch (RemoteException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							} else {
								alertPopUp("You are not approved or kicked");
							}
							
						});
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


				

				penSizeSlider.valueProperty().addListener(e->{
					double value = penSizeSlider.getValue();
					String string = String.format("%.1f", value);
					penSizeLable.setText("Size: " + string + " px");
					//graphicsContext.setLineWidth(value);
				});
			}
		}));
		
		eraserButton.selectedProperty().addListener(((observable, oldValue, newValue) -> {
		    if (newValue) {
		    	System.out.println("Eraser!");
		    	closeAllMenu();
		    	clearEvent();
		    	eraserTitledPane.setVisible(true);
		    	initEraser();
		    	try {
					
					mainCanvas.setOnMousePressed(e->{
						refreshUser();
    	    			if (isApprovedChecker()) {
    	    				try {
    							for (Map.Entry<String, IRemoteDraw> entry : workingPool.entrySet()) {
    								entry.getValue().setSize(eraserSizeSlider.getValue());
    								entry.getValue().setColor("#FFFFFF");
    								entry.getValue().freeDrawOnMousePressed(e.getX(), e.getY());
    							}
    						} catch (RemoteException e1) {
    							// TODO Auto-generated catch block
    							e1.printStackTrace();
    						}
    	    			} else {
    	    				alertPopUp("You are not approved or kicked");
						}
						

					});
					mainCanvas.setOnMouseDragged(e->{
						refreshUser();
    	    			if (isApprovedChecker()) {
    	    				try {
    							for (Map.Entry<String, IRemoteDraw> entry : workingPool.entrySet()) {
    								entry.getValue().setSize(eraserSizeSlider.getValue());
    								entry.getValue().setColor("#FFFFFF");
    								entry.getValue().freeDrawOnMouseDragged(e.getX(), e.getY());
    							}
    						} catch (RemoteException e1) {
    							// TODO Auto-generated catch block
    							e1.printStackTrace();
    						}
    	    			} else {
    	    				alertPopUp("You are not approved or kicked");
						}
						
					});
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				eraserSizeSlider.valueProperty().addListener(e->{
					double value = eraserSizeSlider.getValue();
					String string = String.format("%.1f", value);
					eraserSizeLable.setText("Size: " + string + " px");
					//graphicsContext.setLineWidth(value);
				});
			}
		}));

		sendButton.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event) {
				if (chatTextField.getText().isEmpty()) {
					alertPopUp("Your message is empty. Please write something before trying to the chat.");
				}
				else {
					try {
    					refreshUser();
    	    			if (isApprovedChecker()) {
    	    				try {
								for (Map.Entry<String, IRemoteDraw> entry : workingPool.entrySet()) {
									entry.getValue().broadcastMessage(currentUser.getUsername() + ": " + chatTextField.getText());
								}
								chatTextField.setText("");
							} catch (RemoteException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
    	    			} else {
							alertPopUp("You are not approved or kicked");
						}
//						for (IRemoteDraw remoteDraw: userList){
//							remoteDraw.getMessage(currentUser.getUsername(), messageText );
//							remoteDraw.broadcastMessage();
//						}
					} catch ( Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		onlineUserListView.setCellFactory(lv -> {

            ListCell<String> cell = new ListCell<>();

            ContextMenu contextMenu = new ContextMenu();


            MenuItem approveItem = new MenuItem();
            	approveItem.textProperty().bind(Bindings.format("Approve \"%s\"", cell.itemProperty()));
                approveItem.setOnAction(event -> {
                    String name = cell.getItem();
                    if (name.contains("_ADMIN") || !name.contains("_NEED_APPROVAL")) {
                    	alertPopUp("You can't approve it");
					} else {
						try {
    						IRemoteDraw remoteDraw = (IRemoteDraw) registry.lookup(name);
    						registry.rebind(name.split("_")[0], remoteDraw);
    						registry.unbind(name);
    						try {
    			                WritableImage writableImage = new WritableImage(798, 724);
    			                mainCanvas.snapshot(null, writableImage);
    			                File outputfile = new File(System.getProperty("user.home") + "/temp.png");
    			                RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
    			                ImageIO.write(renderedImage, "png", outputfile);
    			                refreshUser();
    			                for (Map.Entry<String, IRemoteDraw> entry : workingPool.entrySet()) {
    								entry.getValue().placeImage("file:///" + System.getProperty("user.home") + "/temp.png".replace("\\", "/"));
    								entry.getValue().refreshUser();
    							}

    			            } catch (IOException ex) {
    			            }
    						
    					} catch (AccessException e) {
    						// TODO Auto-generated catch block
    						e.printStackTrace();
    					} catch (RemoteException e) {
    						// TODO Auto-generated catch block
    						e.printStackTrace();
    					} catch (NotBoundException e) {
    						// TODO Auto-generated catch block
    						e.printStackTrace();
    					}
					}
                });
            
            MenuItem kickItem = new MenuItem();
            	kickItem.textProperty().bind(Bindings.format("Kick \"%s\"", cell.itemProperty()));
                kickItem.setOnAction(event -> {
                	String name = cell.getItem();
                	if (name.contains("_ADMIN")) {
                		alertPopUp("You can't kick it");
                	} else {
                		try {
    						registry.unbind(name);
			    			for (Map.Entry<String, IRemoteDraw> entry : workingPool.entrySet()) {
			    				entry.getValue().refreshUser();
			    			}
    					} catch (AccessException e) {
    						// TODO Auto-generated catch block
    						e.printStackTrace();
    					} catch (RemoteException e) {
    						// TODO Auto-generated catch block
    						e.printStackTrace();
    					} catch (NotBoundException e) {
    						// TODO Auto-generated catch block
    						e.printStackTrace();
    					}
					}
                	
                });
            
            if (currentUser.getIsAdmin()) {
            	contextMenu.getItems().addAll(approveItem, kickItem);
			}
            

            cell.textProperty().bind(cell.itemProperty());

            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    cell.setContextMenu(null);
                } else {
                    cell.setContextMenu(contextMenu);
                }
            });
            return cell;
        });


		shapeButton.selectedProperty().addListener(((observable, oldValue, newValue) -> {
		    if (newValue) {
		    	closeAllMenu();
		    	shapeListener();
		    	initShape();
		    	lineRadioButton.setSelected(true);
		    	shapeTitledPane.setVisible(true);
			}
		}));
		
		textButton.selectedProperty().addListener(((observable, oldValue, newValue) -> {
		    if (newValue) {
		    	closeAllMenu();
		    	textTitledPane.setVisible(true);
		    	//Clear the event of mainCanvas
		    	clearEvent();
		    	clearEvent();
		    	mainCanvas.setOnMousePressed(e -> {
		    		refreshUser();
	    			if (isApprovedChecker()) {
	    				try {
			    			for (Map.Entry<String, IRemoteDraw> entry : workingPool.entrySet()) {
			    				entry.getValue().setColor(textColorPicker.getValue().toString());
			    				entry.getValue().setFillColor(textColorPicker.getValue().toString());
			    				if (e.getButton() == MouseButton.PRIMARY) {
			    					entry.getValue().strokeText(textTextField.getText(), e.getX(), e.getY());
								}
			    			}
						} catch (RemoteException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
	    			} else {
	    				alertPopUp("You are not approved or kicked");
					}
		    		
		    	});
			}
		}));

	}
	
	public void shapeListener()
	{
		
		lineRadioButton.selectedProperty().addListener(((observable, oldValue, newValue) -> {
		    if (newValue) {
		    	clearEvent();
		    	graphicsContext = mainCanvas.getGraphicsContext2D();
		    	shapeSizeSlider.valueProperty().addListener(e->{
					double value = shapeSizeSlider.getValue();
					graphicsContext.setLineWidth(value);
				});
		    	
		    	try {
		    		mainCanvas.setOnMousePressed(e -> {
			    		initialTouch = new Pair<>(e.getX(), e.getY());
			    	});

		    		mainCanvas.setOnMouseReleased(e->{
		    			refreshUser();
		    			if (isApprovedChecker()) {
		    				try {
								
								for (Map.Entry<String, IRemoteDraw> entry : workingPool.entrySet()) {
				    				entry.getValue().setSize(shapeSizeSlider.getValue());
				    				entry.getValue().setColor(shapeColorPicker.getValue().toString());
				    				entry.getValue().drawLine(initialTouch.getKey(), initialTouch.getValue(), e.getX(), e.getY());
				    			}
							} catch (RemoteException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
		    			} else {
		    				alertPopUp("You are not approved or kicked");
						}
						
					});
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	
		    	
		    	
			}
		}));
		
		ovalRadioButton.selectedProperty().addListener(((observable, oldValue, newValue) -> {
		    if (newValue) {
		    	
		    	//Clear the event of mainCanvas
		    	clearEvent();
		    	mainCanvas.setOnMousePressed(e -> {
		    		refreshUser();
	    			if (isApprovedChecker()) {
	    				try {
			    			
							for (Map.Entry<String, IRemoteDraw> entry : workingPool.entrySet()) {
								entry.getValue().setSize(shapeSizeSlider.getValue());
								entry.getValue().setColor(shapeColorPicker.getValue().toString());
								entry.getValue().setFillColor(shapeColorPicker.getValue().toString());
								if (e.getButton() == MouseButton.PRIMARY) {
									entry.getValue().strokeOval(e.getX() - ovalWidthSlider.getValue() / 2, e.getY() - ovalHeightSlider.getValue() / 2, ovalWidthSlider.getValue(), ovalHeightSlider.getValue());
								}
					    		else if (e.getButton() == MouseButton.SECONDARY) {
					    			
					    			entry.getValue().fillOval(e.getX() - ovalWidthSlider.getValue() / 2, e.getY() - ovalHeightSlider.getValue() / 2, ovalWidthSlider.getValue(), ovalHeightSlider.getValue());
					    		}
					        }
						} catch (RemoteException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
	    			} else {
	    				alertPopUp("You are not approved or kicked");
					}
		    		
		    	});
			}
		}));
		
		rectRadioButton.selectedProperty().addListener(((observable, oldValue, newValue) -> {
		    if (newValue) {
		    	
		    	//Clear the event of mainCanvas
		    	clearEvent();
		    	mainCanvas.setOnMousePressed(e -> {
		    		refreshUser();
	    			if (isApprovedChecker()) {
	    				try {
			    			
							for (Map.Entry<String, IRemoteDraw> entry : workingPool.entrySet())
					        {
								entry.getValue().setSize(shapeSizeSlider.getValue());
								entry.getValue().setColor(shapeColorPicker.getValue().toString());
								entry.getValue().setFillColor(shapeColorPicker.getValue().toString());
								if (e.getButton() == MouseButton.PRIMARY) {
									entry.getValue().strokeRect(e.getX() - rectwidthSlider.getValue() / 2, e.getY() - rectHeightSlider.getValue() / 2, rectwidthSlider.getValue(), rectHeightSlider.getValue());
								}
					    		else if (e.getButton() == MouseButton.SECONDARY) {
					    			entry.getValue().fillRect(e.getX() - rectwidthSlider.getValue() / 2, e.getY() - rectHeightSlider.getValue() / 2, rectwidthSlider.getValue(), rectHeightSlider.getValue());
					    		}
					        }
						} catch (RemoteException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
	    			} else {
	    				alertPopUp("You are not approved or kicked");
					}
		    		
		    		
		    		//graphicsContext.strokeOval(e.getX() - ovalWidthSlider.getValue() / 2, e.getY() - ovalHeightSlider.getValue() / 2, ovalWidthSlider.getValue(), ovalHeightSlider.getValue());
		    	});
			}
		}));
	}


}
