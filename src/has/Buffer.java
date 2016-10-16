package has;

import java.util.LinkedList;

public class Buffer {
	int minBuf;
	int maxBuf;
	LinkedList<Integer> currentBuf = new LinkedList<Integer>();
	
	public Buffer(int minBuf, int maxBuf){
		this.minBuf = minBuf;
		this.maxBuf= maxBuf;
	}
	public Boolean isTooSmall(){
		if(currentBuf.size()<minBuf){
			return true;
		}else{
			return false;
		}
	}
	public Boolean isTooBig(){
		if(currentBuf.size()>maxBuf){
			return true;
		}else{
			return false;
		}
	}
	public Boolean isEmpty(){
		if(currentBuf.size()==0){
			return true;
		}else{
			return false;
		}
	}
	public void addFragment(int quality){
		currentBuf.add(quality);
	}
	public int getSize(){
		return currentBuf.size();
	}
	public int removeFragment(){
		return currentBuf.pop()/1000;
	}
}
