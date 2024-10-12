//Code written by ysabaoun, free to use

import java.util.*;


public class Main {

   private static final Finch myBot = new Finch("A");                 				//declares Finch robot object
   private static int def = 0;                                               			//stands for definition - indicates how many increments the bot will make at each turn (a higher definition value would mean a more detailed understanding of the surrounding area)
   private static final ArrayList<Integer> distances = new ArrayList<>();    			//declares collection of the surrounding distances
   private static final int[] color1 = {0,100,0};
   private static final int[] color2 = {100,100,0};
   private static final int[] color3 = {100,0,0};


   public static void getDistances(int slices){
       double angle = (double)360/def;
       for(int i = 0; i < slices; i++){
           distances.add(myBot.getDistance());
           myBot.setTurn("R", angle, 100);							//built-in Finch turn function which moves the robot’s motors using the direction of the turn, angle of the turn, and speed of the turn as parameters
       }
   }


   public static void calculateTurn(boolean slowSpeed){
       System.out.println(""+distances);
       int greatest = distances.getFirst();
       int greatestIndex = 0;
       for(int i = 1; i < distances.size(); i++){     						//finds index with the greatest distance from the bot
           if(distances.get(i) > greatest){
               greatest = distances.get(i);
               greatestIndex = i;
           }
       }
       System.out.println(""+greatest);


       double angle = (greatestIndex*((double)360/def))-((((double)def/2)-1)*((double)360/def));


       String dir = "R";
       if(angle < 0){          									//determines if it would be faster to have motors turn left or right
           dir = "L";
       }


       double speed = 100;
       if(slowSpeed){          									//parameter determines if the robot is in good conditions to turn fast
           speed = 50;
       }


       myBot.setTurn(dir, Math.abs(angle), speed);
   }


   public static void goTo(boolean mute){
       int d;
       while(true){
           d = myBot.getDistance();
           if(d <= 8){
               myBot.setBeak(0, 0, 0);								//built-in Finch LED function that changes the colors of the 
               break;
           } else if(d <= 10){
               myBot.setMove("F", 1, 100);
               if(!mute){
                   myBot.playNote(70,.25);
               }
               myBot.setBeak(color3[0], color3[1], color3[2]);
           } else if(d <= 25){
               myBot.setMove("F", 2, 100);
               if(!mute){
                   myBot.playNote(55, .5);
               }
               myBot.setBeak(color2[0], color2[1], color2[2]);
           } else {
               myBot.setMove("F", 3, 100);
               if(!mute){
                   myBot.playNote(40,1);
               }
               myBot.setBeak(color1[0], color1[1], color1[2]);
           }
       }
       myBot.setBeak(0, 0, 0);
   }


   public static int getUserInput(String question){
       int userData;
       Scanner input = new Scanner(System.in);


       while(true){
           System.out.print(question + " : ");
           try{                                                                                 //prevents runtime error in the case that user inputs a value that isn't an integer
               userData = input.nextInt();
               break;
           } catch (Exception e) {
               System.out.print("Please input an integer.");
           }
       }


       return userData;
   }


   public static void caller(int i, boolean s, boolean m){
       myBot.setTurn("L", myBot.getCompass(), 100);                              		//set bot to North
       myBot.setTurn("L", (double)((def/2)-1)*((double)360/def), 100);     			//set bot to starting angle
       getDistances(i);                                                                         //input procedure that gathers distance data at different angles
       myBot.setTurn("L", myBot.getCompass(), 100);                              		//set bot to North
       calculateTurn(s);                                                                        //main procedure that analyzes the input data and turns the bot
       goTo(m);                                                                                	//output procedure that moves robot along angle from calculateTurn
   }


   public static void main(String[] args){
       int mazeTurns = getUserInput("How many turns are in the maze?");
       def = getUserInput("How many turns should the bot make at each intersection?");
       int completedTurns = 1;
       caller(def, true, true);                							//first call with slow, cautionary turn speed; check behind as there is no threat of the bot getting stuck in a loop between two positions
       while(completedTurns < mazeTurns){
           caller(def-1, false, true);    							//subsequent calls with fast turn speed; no check behind to prevent the bot from getting stuck in a loop between two positions
           distances.clear();                      						//clears input data list for next call
           completedTurns++;
       }
   }
}