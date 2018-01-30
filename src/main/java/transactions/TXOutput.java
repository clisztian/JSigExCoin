package transactions;

public class TXOutput {

	int Value;
	String scriptPubKey;
	
	public TXOutput(int v, String towhom) {
		this.Value = v;
		this.scriptPubKey = towhom;
	}
}
