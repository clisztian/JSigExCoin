package transactions;

import java.util.ArrayList;

public class SigTransaction {

	private static final int reward = 10;
	private byte[] ID;
	
	ArrayList<TXInput> Vin;
	ArrayList<TXOutput> Vout;
	
	public String transactionId;
	
	SigTransaction(String to, String data) {
		
		Vin = new ArrayList<TXInput>(); 
		Vout = new ArrayList<TXOutput>(); 
	}
	
	public SigTransaction NewCoinbaseTx(String to, String data) {
		
				
		if(data.equals("")) {
			data = new String("Reward to " + to);
		}
		
		SigTransaction coinbaseTx = new SigTransaction(to, data);
		
		coinbaseTx.setTxInput(new TXInput(new byte[]{}, -1, data));
		coinbaseTx.setTxOutput(new TXOutput(reward, to));
		
		
		return coinbaseTx;
		
		
	}
	
	private void setTxOutput(TXOutput txOutput) {
		
		Vout.add(txOutput);
	}

	private void setTxInput(TXInput txInput) {
		
		Vin.add(txInput);
	
	}

	public byte[] getID() {
		return ID;
	}
	
	public void setID(byte[] iD) {
		ID = iD;
	}
	
}
