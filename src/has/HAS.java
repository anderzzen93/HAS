package has;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class HAS {
	private static int time = 0;
	
	private static boolean playing=false;
	private static ArrayList<Integer> bandwidth = new ArrayList<Integer>();
	private static int[] qualitySize = {250000, 500000, 850000, 1300000};
	private static Buffer buffer;
	private static Fragment currentFrag;
	private static int currentQuality = 0;
	private static LinkedList<Integer> qualityHistory = new LinkedList<Integer>();
	private static LinkedList<Integer> ocupancyHistory = new LinkedList<Integer>();
	private static LinkedList<Integer> requestTimings = new LinkedList<Integer>();
	private static LinkedList<Integer> downloadTime = new LinkedList<Integer>();
	
	
	public static void main(String[] args) throws IOException {
		loadTrace();
		buffer = new Buffer(4, 6);
		currentFrag = new Fragment(qualitySize[currentQuality]);
		
		while(time<241){
			System.out.print(playing + " ");
			System.out.print(buffer.getSize());
			ocupancyHistory.add(buffer.getSize());
			//FIx this! Ask about displaying not playing in the graph
			//qualityHistory.add()
			
			if(buffer.isEmpty())
				playing = false;
			if(!playing && !buffer.isTooSmall())
				playing = true;
			if(playing){
				qualityHistory.add(buffer.removeFragment());
			}else{
				qualityHistory.add(-1);
			}
			
			
			
			if(!buffer.isTooBig() && !currentFrag.isDone()){
				int temp = currentFrag.download(bandwidth.get(time));
				for (int i = 0; i < temp ; i++){
					buffer.addFragment(qualitySize[currentQuality]);				
				}
			}
			if (currentFrag.isDone() && !buffer.isTooBig()){
				determiningFragment();
			}
			time++;
			}
			System.out.println();
			for (int i =0;i<241;i++){
				System.out.print(ocupancyHistory.pop()+ " ");
				System.out.print(qualityHistory.pop() + " ");
			}
		
		
		}
	
	private static void determiningFragment() {
		//Här är någon skit fel!! 
		downloadTime.add(currentFrag.getTime());
		//Decide quality
		//the estimated available bandwidth is equal to the size of the most recently downloaded fragment divided by the time it took to download this fragment. 
		double estBandwidth = 4*currentFrag.getQuality()/downloadTime.peekLast();
		//Estimates how many bytes to be able to download before buffer is empty
		double estimatedNextBytes = (buffer.getSize()+3)*estBandwidth;
				if (estimatedNextBytes > qualitySize[currentQuality]*4){
					if (currentQuality <3){
						if (estimatedNextBytes > qualitySize[currentQuality+1]*4){
							currentQuality ++;
						}
					} 
						
				}else{
					for (int i = currentQuality; i >= 0; i-- ){
						if ((estimatedNextBytes > qualitySize[currentQuality-i]*4)){
							currentQuality = i;
							break;
						}
					}
				}
				currentFrag = new Fragment(qualitySize[currentQuality]);
				requestTimings.add(time);
	}


	@SuppressWarnings("unused")
	private static void generateGraph() {
		// TODO Generate graph
		System.out.println();
	}

	public static void loadTrace(){
	String  thisLine = null;
      try{
    	  FileReader input = new FileReader(new File("D:\\Users\\oskan\\workspace\\HAS\\src\\has\\trace.log "));
	         // open input stream test.txt for reading purpose.
	         @SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(input);
	         while ((thisLine = br.readLine()) != null) {
	        	 String[] splitted = thisLine.split(" ");
    	        	 bandwidth.add(Integer.parseInt(splitted[4]));
    	         }       
    	      }catch(Exception e){
    	         e.printStackTrace();
    	      }
}
	
}
