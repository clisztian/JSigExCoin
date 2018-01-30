package workproof;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import sigblock.SigBlock;

public class ProofOfWork {

	int targetMessageDifficulty;
	String targetMessage;


	public ProofOfWork(int difficulty) {
		
		this.targetMessageDifficulty = difficulty;
		targetMessage = new String(new char[difficulty]).replace('\0', '0');
	}

	public SigBlock newBlock(byte[] data, String prevHash) {
		  
		SigBlock sigBlock = new SigBlock(data, prevHash);
		long nonce = 1L;
		
		while(nonce < Long.MAX_VALUE) {
			
          sigBlock.setNonce(nonce);  
		  String tryMe = sigBlock.computeHashOfBlock();
		  
		  if(tryMe.substring(0, targetMessageDifficulty).equals(targetMessage)) {
			  System.out.println(tryMe);
			  sigBlock.setCurrentHash(tryMe);
			  break;
		  }
		  else {
				nonce = nonce + 1L;
		  }
		}
        return sigBlock;			
	}
	
	public void newBlock(SigBlock sigBlock) {
		  
		long nonce = 1L;
		
		while(nonce < Long.MAX_VALUE) {
			
          sigBlock.setNonce(nonce);  
		  String tryMe = sigBlock.computeHashOfBlock();
		  
		  if(tryMe.substring(0, targetMessageDifficulty).equals(targetMessage)) {
			  System.out.println(tryMe);
			  sigBlock.setCurrentHash(tryMe);
			  break;
		  }
		  else {
				nonce = nonce + 1L;
		  }
		}			
	}	
	
	private static String bytesToHex(byte[] hash) {
	    
		StringBuffer hexString = new StringBuffer();	    
		for (int i = 0; i < hash.length; i++) {
			
			String hex = Integer.toHexString(0xff & hash[i]);
			if(hex.length() == 1) hexString.append('0');
			hexString.append(hex);
	    }
	    return hexString.toString();
	}
	
    public static byte[] longToBytes(BigInteger bigInteger) {
        
    	byte[] array = bigInteger.toByteArray();
    	if (array[0] == 0) {
    	    byte[] tmp = new byte[array.length - 1];
    	    System.arraycopy(array, 1, tmp, 0, tmp.length);
    	    array = tmp;
    	}
    	return array;
    }

    public static long bytesToLong(byte[] b) {
        long result = 0;
        for (int i = 0; i < 8; i++) {
            result <<= 8;
            result |= (b[i] & 0xFF);
        }
        return result;
    }
    
    static long byteArrayToLong(byte[] array) {
        return ByteBuffer.wrap(array).getLong();
    }
	
    public static long getUnsignedInt(byte[] data) {
        long result = 0;

        for (int i = 0; i < data.length; i++) {
            result += data[i] << 8 * (data.length - 1 - i);
        }
        return result;
    }
    
    
	public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		
		String originalString = "This is the JSigExCoin";
		
		long nonce = 1L; 
		int difficulty = 4; 
		String target = new String(new char[difficulty]).replace('\0', '0');
		
		MessageDigest digest = MessageDigest.getInstance("SHA-256");	
		System.out.println("Doing proof-of-work...");
		
		while(nonce < Long.MAX_VALUE) {
			
			String tryMe = originalString + nonce;			
			byte[] thishash = digest.digest(tryMe.getBytes(StandardCharsets.UTF_8));
			
			String hash = bytesToHex(thishash);
			if(hash.substring(0, difficulty).equals(target)) {
				System.out.println(hash);
				break;
			}
			else {
				nonce = nonce + 1L;
			}
		}
	}
}
