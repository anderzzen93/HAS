package has;

public class Fragment {
	private int totalSizeLeft;
	private double fragmentsLeft=4;
	private int time = 0;
	private int quality;
	private boolean finished = false;
	
	public Fragment(int quality){
		this.totalSizeLeft = 4*quality;
		this.quality = quality;
	}
	//Returns number of fragments downloaded in 1 second
	public int download(int bandwidth){
		time++;
		if(bandwidth>totalSizeLeft){
			finished = true;
			return (int)Math.ceil(fragmentsLeft);
		}
		else{
			double downloadedThis;
			downloadedThis =(double) bandwidth/quality;
			double temp = fragmentsLeft-downloadedThis;
			
			double fragsToReturn = Math.ceil(fragmentsLeft)-Math.ceil(temp);
			fragmentsLeft = temp;
			totalSizeLeft = (int)(temp*quality);
			return (int) fragsToReturn;
					
			
//			totalSizeLeft -= bandwidth;
//			fragmentsLeft -= Math.floor(bandwidth/quality);
//			unfinishedFrags= (bandwidth/quality)-fragmentsLeft;
		}
		
	}
	public boolean isDone(){
		return finished;
	}
	public int getTime(){
		return this.time;
	}
	public int getQuality() {
	
		return quality;
	}
}
