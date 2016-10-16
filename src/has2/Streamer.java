package has2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Streamer {

	private static double Î = 0.4; 
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		

	    FileReader in = new FileReader("/home/johsj748/workspace/TDDD66/src/MediaStreamer/trace");
	    BufferedReader br = new BufferedReader(in);
	    
	    //Creates printwriters for the output files
	    PrintWriter writer1 = new PrintWriter("input1.txt", "UTF-8");
	    PrintWriter writer2 = new PrintWriter("input2.txt", "UTF-8");
	    PrintWriter writer3 = new PrintWriter("input3.txt", "UTF-8");

	    
	    
	    String[] temparray = new String[6]; 
	    final int MINBUF = 4;
	    final int MAXBUF = 6;
	    int currentBuf = 0;
	    float estimatedBandwidth = 0;
	    int currentQuality = 0;
	    String line;
	    boolean playback = false;
	    boolean downloading = false;
	    int[] qualityLimits = {250,500,850,1300};
	    int currentDownload = 0;
	    int timeLastFragment = 0;
	    int request = 0;
	    boolean newRequest = false;
	    int time = 0;
	   
	    while ((line = br.readLine()) != null) {
	      	temparray = line.split(" ");
	    	
	    	
	    	//If playing video, remove 1 second from buffer
	    	if (playback){
	    		currentBuf--;
	    	}
	    	// If downloading we increase the amount of data downloaded with the bandwith for this second. Also if the downloaded bits are larger than a chunk we increase the buffer with 
	    	// 4 secs and resets the amount of bits downloaded.
	    	if(downloading){
	    		currentDownload += 8*Integer.parseInt(temparray[4])/Integer.parseInt(temparray[5]);
	    		timeLastFragment++;
	    		if(currentDownload >= 4*qualityLimits[currentQuality]){
	    			//estimatedBandwidth = estimateMethod1(4*qualityLimits[currentQuality],timeLastFragment);
	    			estimatedBandwidth = estimateMethod2(estimateMethod1(4*qualityLimits[currentQuality],timeLastFragment),estimatedBandwidth);
	    			currentBuf += 4;
	    			downloading = false;
	    			currentDownload = 0;
	    			timeLastFragment = 0;
	    		}
	    	}
	    	// If not downloading and buffer is less than MAXBUF we start the downloading process, and with the estimated bandwith we choose the quality
	    	if(!downloading){
	    		if(currentBuf < MAXBUF){
	    			downloading = true;
	    			newRequest = true;
	    			if(estimatedBandwidth < 500){
	    				request = 0;
	    			}
	    			else if(estimatedBandwidth < 850){
	    				request = 1;
	    			}
	    			else if(estimatedBandwidth < 1300){
	    				request = 2;
	    			}
	    			else{
	    				request = 3;
	    			}
	    		}
	    		//Tries to match the quality request and see if it is allowed, otherwise choose the closest allowed. 
	    		if(request > currentQuality){
	    			if(!(currentQuality==3))
	    				currentQuality++;
	    		}else if(request < currentQuality){
	    			if(currentQuality<=2)
	    				currentQuality=request;
	    			else if(currentQuality==3 && request <=1){
	    				currentQuality = 1;
	    			}else
	    				currentQuality--;
	    		}	
	    	}
	    	// Decides when to stop and start playing the video.
	    	if(playback)
	    		playback = (currentBuf > 0);
	    	else
	    		playback = (currentBuf >= MINBUF);
	    	
	    	//Write buffersize and fragment quality in textfile.
	    	System.out.println("currentBuf = " + currentBuf);
	    	writer1.println(time + " " + currentBuf);
	    	System.out.println("currentQuality = " + currentQuality);
	    	writer2.println(time + " " + currentQuality);
	    	
	    	//Marks new request with the quality when a downloading request was done
	    	if(newRequest){
	    		writer3.println(time + " " +request); 
	    		newRequest = false;
	    	}else
	    		writer3.println();
	    	
	    	//counter for time
	    	time++;
	    }
	       
	    in.close();
	    writer1.close();
	    writer2.close();
	    writer3.close();
	    
	}
	/**
	 * Estimates bandwidth capacity for last downloaded fragment only
	 * @param chunkSize
	 * @param time
	 * @return
	 */
		public static float estimateMethod1(int chunkSize,int time){
		return chunkSize/time;
	}
		
		/**
		 * Estimates bandwidth by putting together old estimation and an estimation based on last fragment. 
		 * @param newEstimate
		 * @param oldEstimate
		 * @return
		 */
		public static float estimateMethod2(float newEstimate, float oldEstimate){
			if(oldEstimate == 0)
				oldEstimate = newEstimate;			
			float estimatedBandwidth = (float) ((1-Î) * oldEstimate + Î * newEstimate);
			return estimatedBandwidth;			
		}

}
