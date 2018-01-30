package util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import sigblock.HashUtil;
import transactions.SigTransaction;

public class Util {

	
	//Tacks in array of transactions and returns a merkle root.
	public static String getMerkleRoot(ArrayList<SigTransaction> transactions) throws UnsupportedEncodingException {
			
		int count = transactions.size();
		ArrayList<String> previousTreeLayer = new ArrayList<String>();
		
		for(SigTransaction transaction : transactions) {
			previousTreeLayer.add((new String(transaction.getID(), "UTF-8")));
		}
		
		ArrayList<String> treeLayer = previousTreeLayer;		
		while(count > 1) {
			
			treeLayer = new ArrayList<String>();
			for(int i=1; i < previousTreeLayer.size(); i++) {
				treeLayer.add(HashUtil.applySha256(previousTreeLayer.get(i-1) + previousTreeLayer.get(i)));
			}
			count = treeLayer.size();
			previousTreeLayer = treeLayer;
		}
		String merkleRoot = (treeLayer.size() == 1) ? treeLayer.get(0) : "";
		return merkleRoot;
	}
}
