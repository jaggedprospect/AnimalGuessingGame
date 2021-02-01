package animalguessingai;

/**
 *
 * @author Nate Heppard
 */

import java.io.Serializable;

public class Brain implements Serializable{
    
    private final String INITQ="Is it a vertebrate?";
    private final String INITA[]={"Cat","Worm"};
    
    private class Node implements Serializable{
        
        String data;
        Node left,right;
        
        public Node(String data){
            this.data=data;
            left=right=null;
        }
    }
    
    Node root;
    Node current;
    Node previous;
    
    public Brain(){
        root=null;
        current=root;
        previous=null;
    }
    
    public void init(){
        current=root=new Node(INITQ);
        this.root.left=new Node(INITA[1]);
        this.root.right=new Node(INITA[0]);
    }
    
    public Node getCurrentNode(){
        return current;
    }
    
    public String getData(Node n){
        return n.data;
    }
    
    public void advanceCurrent(int n){
        if(n==1){
            previous=current;
            current=this.current.right;
        }else if(n==0){
            previous=current;
            current=this.current.left;
        }else{
            System.out.println(">: Brain Error. Could Not Advance Current Node.");
            System.exit(n);
        }
    }
    
    public void addNode(String q,String a,int n){
        Node node=new Node(q);
        Node temp;
        
        temp=current;
        current=node;
        current.left=temp;
        current.right=new Node(a);
        
        if(n==1)
            previous.right=current;
        else if(n==0)
            previous.left=current;
        else{
            System.out.println(">: Brain Error. Could Not Add New Node.");
            System.exit(n);
        }
        
        // reset to start at root
        reset();
        
        System.out.println(">: New Node Added To Brain.");
    }
    
    public boolean hasLeaf(){
        return (current.left==null && current.right==null);
    }
    
    public void reset(){
        current=root;
    }
    
    /*
    ========================================
    */
    public String getCurrentLeft(){
        return this.current.left.data;
    }
    
    public String getCurrentRight(){
        return this.current.right.data;
    }
    /*
    ========================================
    */
}
