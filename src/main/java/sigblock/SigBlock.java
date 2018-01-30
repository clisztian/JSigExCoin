package sigblock;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import com.google.gson.GsonBuilder;

public class SigBlock implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	long timeStamp; 
	long sigNonce; 
	byte[] sigData; 	
	String previousHash;
	String currentHash; 

	public SigBlock(byte[] data, String prevHash) {
		
		this.sigData = data; 
		this.previousHash = prevHash;
		this.timeStamp = System.currentTimeMillis();		
	}
	
	public SigBlock(String genesis) throws UnsupportedEncodingException {
		
		this.sigData = genesis.getBytes("UTF-8");
		previousHash = "0";
		this.timeStamp = System.currentTimeMillis();		
	}
	
	public void setNonce(long newNonce) {
		sigNonce = newNonce;
	}
	
	public byte[] getData() {
		return this.sigData;
	}
	
	public String computeHashOfBlock() {
		
		String toHash = previousHash 
				     +  new String(sigData) 
				     +  Long.toString(timeStamp)
				     +  Long.toString(sigNonce);
		  
		return HashUtil.applySha256(toHash);		
	}

	public void setCurrentHash(String tryMe) {
		currentHash = new String(tryMe);
	}

	public byte[] getCurrentHashBytes() {
		return currentHash.getBytes();
	}
	
	public String getCurrentHash() {
		return currentHash;
	}
	
	public String returnGSON() {
		
		String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(this);
		return blockchainJson;
	}
}
