//changed.
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.String;
import java.util.ArrayList;
public class BankersAlgorithm {
	int nr;
	static int client;
	int m;
	int[] e;//total existed
	int[] p;
	int[] a;//available
	int[] tempa;
	int[][] c;//allocated 
	int[][] r;//Need matrix 
	int[][]max;
	static int finishCount=0; // Counting which processes are completed and released resources
	static boolean[] endr;
	public static PrintWriter writer;
	public void initializeAll() throws FileNotFoundException, UnsupportedEncodingException
	{   //getting all the values from user to start with
		writer = new PrintWriter("output.txt", "UTF-8"); 
		System.out.println("Enter the number of types of resources bank has to deal with: ");
		writer.println("Enter the number of types of resources bank has to deal with: ");
		Scanner scan = new Scanner(System.in);
		nr = scan.nextInt();//taking number of resources from user.
		writer.println(nr);
		System.out.println("\nEnter the Number of clients bank will be dealing with:");
		writer.println("\nEnter the Number of clients bank will be dealing with:");
		client= scan.nextInt(); //taking total number of processes (here clients) from user.
		writer.println(client);
		System.out.println("\nEnter the Existing resources of each type of the "+nr+ " resources: ");
		writer.println("\nEnter the Existing resources of each type of the "+nr+ " resources: ");
		
		e= new int[nr];
		p=new int[nr];
		a=new int[nr];
		tempa=new int[nr];
		c=new int[client][nr];
		r= new int[client][nr];
		endr=new boolean[client];
		max=new int[client][nr];
		m=0;
		for(int i=0;i<nr;i++)
		{
			 p[i]=0;
			 a[i]=0;
			 tempa[i]=0;
		}
		
		
		for(int i=0;i<nr;i++)
		{
			e[i] = scan.nextInt();
			writer.print(e[i] + " ");
		}
		for(int i=0;i<client;i++)
		{
			System.out.println("\nEnter the current number of resources of each type assigned to client "+(i+1)+":");
			writer.println("\nEnter the current number of resources of each type assigned to client "+(i+1)+":");
			writer.println();
			for(int j=0;j<nr;j++)
			{
				c[i][j]=scan.nextInt();
				writer.print(c[i][j] + " ");
			}
			writer.println();
			
		}
		for(int i=0;i<client;i++)
		{
			System.out.println("\n\nEnter the  number of resources of each type still needed to assign to the client "+(i+1)+":");
			writer.println("\nEnter the  number of resources of each type still needed to assign to the client "+(i+1)+":");
			writer.println();
			for(int j=0;j<nr;j++)
			{
				r[i][j]=scan.nextInt();
				writer.print(r[i][j] + " ");
				
			}
			endr[i]=false;//marking all clients initially as unmet resource needs
		}

		for(int i=0; i<nr; i++)
		{
			int temp=0;
			for(int j=0;j<client;j++)
			{
				temp=c[j][i]+temp;
			}
			a[i]=e[i]-temp; //finding the value of available resources 
		}
	}
	
	public boolean check(int Cl, int[] av)
	{
		  for(int j=0;j<nr;j++) 
		       if(av[j]<r[Cl][j])
		          return false; // if the resource value of requested client is greater than the available, deny the allocation
		    
		    return true;
	}
	
	
	public boolean decideIfSafe(int Cl)
	{
		boolean[] finish = new boolean[client];
		int[] 	  tempa = new int[nr];
		
		
		for(int i =0; i<client; i++)
		{
			finish[i] = endr[i];
		}
		
		for(int i=0; i<nr;i++)
		{
			tempa[i] = a[i];
		}
		
		 if(!check(Cl,tempa))
		 {
			 System.out.println("Sorry! Number of requested resources are greater than available");
			 writer.println("Sorry! Number of requested resources are greater than available");
			 return false;
		 }
		 else
		 {
			 finish[Cl] = true;
			 for(int k =0; k<nr;k++)
			 {
				 tempa[k] = c[Cl][k] + tempa[k];
			 }
			 
		 }
		 int j = 0;
		 for(int i=0; i<client; i++)
		 {
			 if(finish[i])
				 j++;
				 
		 }
		
		 
		 while(j<client){  //until all process assigned the resources successfully
			 
		       boolean assigned=false;//is at least one assigned
		       
		       for(int i=0;i<client;i++)
		       {
		    	   if(!finish[i] && check(i, tempa)){  //checking the safe zone to satisfy remaining clients
		    		   	for(int k=0;k<nr;k++)
		    		   	{
		    		   		tempa[k]= c[i][k] + tempa[k];
		    		   	}
		    		   	assigned=finish[i]=true;
		    		   	j++;
		             }
		       }
		       if(!assigned) break;  //if no allocation
		 }
		 
		 if(j==client) //if all clients are allocated
		 {		
		        System.out.println("\nProcessing succeeded");
		        writer.println("\nProcessing succeeded");
		        for(int k=0; k<nr;k++)
		        {
		        	a[k] = c[Cl][k] + a[k];
		        }
		        endr[Cl] = true;
		        finishCount++;
		        return true;
		 }
		  else
		  {
			    System.out.println("\nSorry! There is a Deadlock possibility"); //not able to find any client which can be completed after allocation to requested client, thus not safe zone.Possible deadlock.
		        writer.println("\nSorry! There is a Deadlock possibility");
			    return false;
		  }
	
	}
	public void printDecision(boolean b,int Cl)
	{
		if(b==false)
		{
			System.out.println("\nUnsafe!!Sorry Client: " +(Cl+1) +" the resource cannot be granted at this time please try later!!");
			writer.println("\nUnsafe!!Sorry Client: " +(Cl+1) +" the resource cannot be granted at this time please try later!!");
		}
		else
		{
			System.out.println("\nCongratulations!!Client " +(Cl+1)+" is granted the resource & client has repaid all the credit");
			writer.println("\nCongratulations!!Client " +(Cl+1)+" is granted the resource & client has repaid all the credit");
			System.out.println("\nPlease find below the revised Available resources for your reference");
			writer.println("\nPlease find below the revised Available resources for your reference");
			for(int j=0;j<nr;j++)
			{
					System.out.print(a[j]+" ");
					writer.print(a[j]+" ");
			}
			
		}
	}

	public static void askClient(BankersAlgorithm ba1)
	{
		for (int i=0;i<client;i++) //cycle through each client asking for the requests
		{
			if(endr[i]!=true) // for completed clients , skip and check the next client
			{
				System.out.println("\n\nHello Client "+(i+1)+ ",Do you want your resources? input 1 for=> yes or 0 for=> no");
				writer.println("\n\nHello Client "+(i+1)+ ",Do you want your resources? input 1 for=> yes or 0 for=> no");
				Scanner scan = new Scanner(System.in);
				int wish= scan.nextInt();
				writer.print(wish);
				if(wish==1)
				{
					boolean Z=ba1.decideIfSafe(i);
					ba1.printDecision(Z, i);
				}
			}
		}
	}
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		// TODO Auto-generated method stub
		
		BankersAlgorithm ba=new BankersAlgorithm();
		ba.initializeAll();
		while(finishCount!=client)// keep asking all the clients until every client is satisfied,then stop.
		{
			askClient(ba);
		}
		writer.close();
		
	}

}
