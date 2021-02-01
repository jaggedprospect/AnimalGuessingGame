package animalguessingai;

/**
 *
 * @author Nate Heppard
 */

import java.io.*;
import java.util.Scanner;

public class ThoughtProcess{
    
    private final String VALID_INPUT="{1}?(yes)|{1}?(no)|{1}?(quit)";
    
    private Brain brain;
    private Stream stream;
    private Scanner in;
    
    public ThoughtProcess(){
        stream=new Stream();
        in=new Scanner(System.in);
        
        if(stream.hasFile()){
            stream.readFromSave();
            System.out.println(">: Brain Loaded From Save.");
        }else{
            brain=new Brain();
            brain.init();
            System.out.println(">: New Brain Created.");
        }
        
        System.out.println();
    }
    
    public void run(){
        // save at start
        stream.writeToSave();
        
        try{
            Thread.sleep(2000);
            greet();
            Thread.sleep(2000);
        }catch(InterruptedException e){
            System.err.println(e);
        }
        
        for(;;){
            String current=brain.getData(brain.getCurrentNode());
            int response=-1, last=-1;
            
            while(!brain.hasLeaf()){
                response=inquire(current);
                last=response;

                // possibly redundant
                checkResponse(response);

                brain.advanceCurrent(response);
                current=brain.getData(brain.getCurrentNode());
            }

            try{
                Thread.sleep(2000);
                System.out.println("\nI think I have it.");
                Thread.sleep(2000);
            }catch(InterruptedException e){
                System.err.print(e);
            }

            System.out.println("Is your animal...a "+current+"?");

            response=getResponse();

            // possibly redundant
            checkResponse(response);

            if(response==1)
                System.out.println("\nHuzzah! Victory for the Computers!");
            else{
                System.out.println("\nYou beat me! Oh well...");
                learn(last);  
            }

            brain.reset();
            
            System.out.println("\nWould you like to play again?");
            
            if(getResponse()==0){
                System.out.println("\nGoodbye.");
                break;
            }
            
            System.out.println();
            greet();
        }        
    }
    
    public void greet(){
        try{
            System.out.println("Hello. I am Brain.");
            Thread.sleep(1500);
            System.out.println("Think of an animal. I will try to guess what it is.");
            Thread.sleep(1500);
            System.out.println("Please, only answer \"yes\" or \"no.\"\n");
        }catch(InterruptedException e){
            System.err.println(e);
        }
    }
    
    public int inquire(String q){        
        if(q!=null){
            System.out.print(q+"\n");
            return getResponse();
        }else
            System.out.println("There is no question for me to ask.");
        
        return -1;
    }
    
    public void learn(int last){
        String question="",targetAnimal="";
        boolean listening=true;
        
        while(listening){
            targetAnimal=getAnimal();
            
            question=getQuestion();

            System.out.println("\nConfirm that the following is correct:");
            System.out.println("\""+question.toUpperCase()+"\" is applicable to"
                    +" a(n) "+targetAnimal.toUpperCase()+"?");

            if(getResponse()==1){
                // add question to Brain
                listening=false;
            }
        }
        
        System.out.println();
        
        brain.addNode(question,targetAnimal,last);
        
        stream.writeToSave();
        System.out.println(">: New Data Saved.");
    }
    
    /*
    ============================================================
                    UTILITY METHODS ONLY
    ============================================================
    */
    
    public int getResponse(){
        String input;
        
        System.out.print(">> ");
        input=in.next();
        
        while(!input.matches(VALID_INPUT)){
            System.out.println(">: Invalid Input.");
            System.out.print(">> ");
            input=in.next();
        }
        
        if(input.equalsIgnoreCase("yes")){
            //System.out.println(">: Answered Yes.");
            return 1;
        }else if(input.equalsIgnoreCase("no")){
            //System.out.println(">: Answered No.");
            return 0;
        }else if(input.equalsIgnoreCase("quit")){
            System.out.println(">: Exiting Program.");
            System.exit(0);
        }else
            System.out.println(">: Input Error. Adjust Logic.");
        
        return -1;
    }
    
    public String getAnimal(){
        String targetAnimal;
        
        System.out.println("What animal were you thinking of?");
        System.out.print(">> ");
        in.nextLine();
        targetAnimal=in.next().toLowerCase();
        
        return targetAnimal.substring(0,1).toUpperCase()+targetAnimal.substring(1);
    }

    public String getQuestion(){
        String question;

        System.out.println("What is a question I could have asked to "
                +"correctly guess your animal?");
        System.out.print(">> ");
        in.nextLine();
        question=format(in.nextLine()).toLowerCase();

        return question.substring(0,1).toUpperCase()+question.substring(1);
    }
    
    public void checkResponse(int r){
        if(r==-1){
            System.out.println(": Inquiry Error. Adjust Logic.");
            System.exit(r);
        }
    }
    
    public String format(String s){
        return s.replaceAll("\\s+"," ") .trim();
    }
    
    /*
    ============================================================
    
    ============================================================
    */
    
    
    private class Stream{
        
        private final String SAVE_FILE="brain_save.txt";
        
        /*
        ===> IMPLEMENT BACKUP SAVING (IN EVENT OF ERROR)
        */
        
        public Stream(){
            System.out.println(">: Stream Instantiated.");
        }
        
        public void readFromSave(){            
            try{
                FileInputStream fis=new FileInputStream(SAVE_FILE);
                ObjectInputStream ois=new ObjectInputStream(fis);
                brain=(Brain)ois.readObject(); // must cast to ArrayList
                ois.close();
                
            }catch(FileNotFoundException e){
                System.out.println(e); // couldn't find specified file
            }catch(IOException | ClassNotFoundException e){
                System.out.println(e); // error while writing/reading
            }
        }
        
        public void writeToSave(){
            try{
                FileOutputStream fos=new FileOutputStream(SAVE_FILE);
                ObjectOutputStream oos=new ObjectOutputStream(fos);
                oos.writeObject(brain);
                oos.close();
                
            }catch(FileNotFoundException e){
                System.out.println(e); // couldn't find specified file
            }catch(IOException e){
                System.out.println(e); // error while writing/reading
            }
        }
        
        public boolean hasFile(){
            return new File(SAVE_FILE).isFile();
        }
    }
}
