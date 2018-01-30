package transactions;

public class TXInput {

	byte[] TxID;
	String digitalSignature;
	int Vout;
	
	
	public TXInput(byte[] ID, int V, String signature) {
		
		this.TxID = ID;
		this.Vout = V;
		this.digitalSignature = signature; 
	}
	
	
}
