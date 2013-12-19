package com.cs5011.assignment2.PotentialFieldImpl;

import easyGui.EasyGui;
import geometry.FloatPoint;

public class ToolBox
{
    private final EasyGui gui;
    
    private  float     coordXID;
    private  float     coordYID;

    public ToolBox()
    {
        // Create a new EasyGui instance with a 500x500pixel graphics panel.
        gui = new EasyGui(600, 600);
        gui.clearGraphicsPanel();

        // Add a label to the cell in row 0 and column 0.
        gui.addLabel(0, 0, "Enter Goal Position coord X");
        gui.addLabel(1, 0, "Enter Goal Position coord Y");
   
        // Add a text field to the cell in row 1 and column 0. The returned ID
        // is
        // stored in fextFieldId to allow access to the field later on.
        coordXID = gui.addTextField(0, 1, "0");
        coordYID = gui.addTextField(1, 1, "0");  
        // Add a button in row 0 column 1. The button is labeled "ButtonA" and
        // when pressed it will call the method called buttonActionA in "this"
        // instance of the ToolBoxDemo class.
        gui.addButton(0, 2, "Potential Fields", this, "drawPF");
        
        // Add a button in row 1 column 1. It calls the method called
        // "buttonActionB".
        gui.addButton(1, 2, "RRTs", this, "drawRRT");
        gui.addButton(1, 3, "RRTs with Bias", this, "drawRRT");

    }
    
    public void runDemo()
    {
        // Displays the GUI i.e. makes it visible.
        gui.show();
        gui.clearGraphicsPanel();
    }
    
    public void drawPF()
    {
        gui.clearGraphicsPanel();
        gui.update();
        FloatPoint pointStart = new FloatPoint(Float.parseFloat(gui.getTextFieldContent((int) coordXID)), Float.parseFloat(gui.getTextFieldContent((int) coordYID)));
        int xCoord = (int) (Math.random() * 250 + 300);
        int yCoord = (int) (Math.random() * 300 + 300);
        FloatPoint goal = new FloatPoint(xCoord, yCoord);
        ObstacleContainer obstacle = new ObstacleContainer(gui, 40, 60);
        obstacle.drawObstacle();
        PotentialFieldsImplement method = new PotentialFieldsImplement(gui,
                                              obstacle, pointStart, goal, 2);
        method.runDemo();
    }
    
    public void drawRRT()
    {
        gui.clearGraphicsPanel();
        gui.update();
        FloatPoint pointStart = new FloatPoint(Float.parseFloat(gui.getTextFieldContent((int) coordXID)), Float.parseFloat(gui.getTextFieldContent((int) coordYID)));
        int xCoord = (int) (Math.random() * 250 + 300);
        int yCoord = (int) (Math.random() * 300 + 300);
        FloatPoint goalPoint = new FloatPoint(xCoord, yCoord);
        ObstacleContainer obstacle = new ObstacleContainer(gui, 40, 60);
        obstacle.drawObstacle();
        RRTImplement rapidlyTree = new RRTImplement(gui, obstacle, pointStart,
                goalPoint, 20);
        rapidlyTree.searchingTree();
        gui.update();
        gui.show();
    }
    

    public static void main(String[] args)
    {
        ToolBox demo = new ToolBox();
        demo.runDemo();
    }
}

//package com.cs5011.assignment2.PotentialFieldImpl;
//
//import java.util.Scanner;
//
//import easyGui.EasyGui;
//import geometry.FloatPoint;
//import geometry.IntPoint;
//
//import javax.swing.SwingUtilities;
//import javax.swing.UIManager;
//
//import Assignment.PotentialFields.PotentialFieldGUI;
//
//public class TestPotentialFields
//{
//  
//  public static void main(String[] args)
//  {
//      
//      Scanner scanner = new Scanner(System.in);
//      while (true)
//      {
//          System.out.println("Choose an option:");
//          System.out.println("1. RRT Implementation without bias.");
//          System.out.println("2. RRT Implementation with bias.");
//          System.out.println("3. Poten fields implementation.");
//          
//          System.out.println("6. Exit.");
//          System.out.print("Please enter your choice: ");
//          int choice = scanner.nextInt();
//          
//          if (1 == choice)
//          {
//              FloatPoint pointStart = new FloatPoint(0, 0);
//              int xCoord = (int) (Math.random() * 250 + 300);
//              int yCoord = (int) (Math.random() * 300 + 300);
//              FloatPoint goalPoint = new FloatPoint(xCoord, yCoord);
//              EasyGui gui = new EasyGui(600, 600);
//              ObstacleContainer obstacle = new ObstacleContainer(gui, 40, 60);
//              obstacle.drawObstacle();
//              RRTImplement rapidlyTree = new RRTImplement(gui, obstacle,
//                      pointStart, goalPoint, 20);
//              rapidlyTree.searchingTree();
//              gui.update();
//              gui.show();
//          }
//          if (2 == choice)
//          {
//              FloatPoint pointStart = new FloatPoint(0, 0);
//              int xCoord = (int) (Math.random() * 250 + 300);
//              int yCoord = (int) (Math.random() * 300 + 300);
//              FloatPoint goalPoint = new FloatPoint(xCoord, yCoord);
//              EasyGui gui = new EasyGui(600, 600);
//              ObstacleContainer obstacle = new ObstacleContainer(gui, 40, 60);
//              obstacle.drawObstacle();
//              RRTImplement rapidlyTree = new RRTImplement(gui, obstacle,
//                      pointStart, goalPoint, 20);
//              rapidlyTree.searchingWithBias();
//              gui.update();
//              gui.show();
//          }
//          if (3 == choice)
//          
//          {
//              FloatPoint pointStart = new FloatPoint(0, 0);
//              int xCoord = (int) (Math.random() * 250 + 300);
//              int yCoord = (int) (Math.random() * 300 + 300);
//              EasyGui gui = new EasyGui(600, 600);
//              FloatPoint goal = new FloatPoint(xCoord, yCoord);
//              ObstacleContainer obstacle = new ObstacleContainer(gui, 40, 60);
//              obstacle.drawObstacle();
//              PotentialFieldsImplement method = new PotentialFieldsImplement(
//                      gui, obstacle, pointStart, goal, 2);
//              method.runDemo();
//          }
//          
//          if (6 == choice)
//          {
//              
//              scanner.close();
//              System.exit(1);
//          }
//      }
//      
//  }
//}

