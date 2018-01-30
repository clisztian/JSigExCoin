package blockchain;

import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBException;
import org.iq80.leveldb.DBIterator;

import static org.iq80.leveldb.impl.Iq80DBFactory.factory;
import org.iq80.leveldb.Options;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import sigblock.SigBlock;
import workproof.ProofOfWork;

public class SigExBlockchain {


	ProofOfWork pow;
	String tipHash; 
	
	DB levelDBStore;
	Options options;
	
	public SigExBlockchain(int pow_difficulty) throws IOException {
		
		pow = new ProofOfWork(pow_difficulty);	
		
		Options options = new Options().createIfMissing(true);
		levelDBStore = factory.open(new File("levelDBStorePractice"), options);
		
		if(levelDBStore.get((new String("l")).getBytes()) == null) {
			
			System.out.println("Creating genesis block");
			SigBlock genesisBlock = initializeBlockChain((new String("Genesis block")).getBytes(), "0");
			
			levelDBStore.put(genesisBlock.getCurrentHashBytes(), serialize(genesisBlock));
			levelDBStore.put((new String("l")).getBytes(), genesisBlock.getCurrentHashBytes());
			
			tipHash = new String(levelDBStore.get((new String("l")).getBytes()), "UTF-8");
		}
		else {
			
			System.out.println("Get pointer to lead hash in blockchain");
			tipHash = new String(levelDBStore.get((new String("l")).getBytes()), "UTF-8");
			System.out.println(tipHash);
		}
		
	}
	
	public void iterateBlockchain() throws ClassNotFoundException, IOException {
		
		DBIterator iterator = levelDBStore.iterator();	
		iterator.seekToFirst();
		
		while (iterator.hasNext()) {
			
			byte[] key = iterator.peekNext().getKey();
		    byte[] value = iterator.peekNext().getValue();
		    
		    String stringKey = (new String(key, "UTF-8"));
		    System.out.println(stringKey);
		    
		    if(!stringKey.equals("l")) {
		    	
		    	SigBlock block = (SigBlock) deserialize(value);
		    	String blockData = (new String(block.getData(), "UTF-8"));
			    System.out.println(blockData);
		    }
		    
		    iterator.next();
		}
	}
	
	public SigBlock initializeBlockChain(byte[] data, String prevHash) {
		
		SigBlock genesisBlock = pow.newBlock(data, prevHash);
		
		return genesisBlock;
		
	}
	

    public void setTip(String tip) {
    	this.tipHash = tip;
    }
    
    public void addBlock(byte[] data) throws DBException, IOException {
    	
    	String prevHash = new String(levelDBStore.get((new String("l")).getBytes()), "UTF-8");
    	SigBlock newBlock = pow.newBlock(data, prevHash);
	
    	levelDBStore.put(newBlock.getCurrentHashBytes(), serialize(newBlock));
		levelDBStore.put((new String("l")).getBytes(), newBlock.getCurrentHashBytes());
		
		tipHash = new String(levelDBStore.get((new String("l")).getBytes()), "UTF-8");	
    }
    

    
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		
		
		SigExBlockchain block = new SigExBlockchain(4);
		
		block.addBlock((new String("First real block")).getBytes());
		block.addBlock((new String("Second real block")).getBytes());
		block.addBlock((new String("Third real block")).getBytes());
		block.addBlock((new String("Fourth real block")).getBytes());
		
		block.iterateBlockchain();

	}
	
	
    public static byte[] serialize(Object obj) throws IOException {
        try(ByteArrayOutputStream b = new ByteArrayOutputStream()){
            try(ObjectOutputStream o = new ObjectOutputStream(b)){
                o.writeObject(obj);
            }
            return b.toByteArray();
        }
    }

    public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        try(ByteArrayInputStream b = new ByteArrayInputStream(bytes)){
            try(ObjectInputStream o = new ObjectInputStream(b)){
                return o.readObject();
            }
        }
    }
	
	
	

//	DB levelDBStore;
//	Options options = new Options().createIfMissing(true);
//	levelDBStore = factory.open(new File("levelDBStorePractice"), options);
//	
//	
//	SigExBlockchain blockchain = new SigExBlockchain(4);
//	SigBlock genesisBlock = blockchain.initializeBlockChain((new String("First genesis block")).getBytes(), "0");
//	
//	levelDBStore.put(genesisBlock.getCurrentHashBytes(), serialize(genesisBlock));
//	levelDBStore.put((new String("l")).getBytes(), genesisBlock.getCurrentHashBytes());
//	
//	blockchain.addBlock((new String("Second block")).getBytes(), genesisBlock.getCurrentHash());
//	
//	SigBlock latest = blockchain.getLast();
//	
//	levelDBStore.put(latest.getCurrentHashBytes(), serialize(latest));
//	levelDBStore.put((new String("l")).getBytes(), latest.getCurrentHashBytes());
//	
//	byte[] lastBlock = levelDBStore.get((new String("l")).getBytes());
//	
//	String lasthash = new String(lastBlock, "UTF-8");
//	System.out.println(lasthash);
	
	
	
}



