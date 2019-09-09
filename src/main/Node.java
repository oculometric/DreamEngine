package main;

public class Node {
	public void compute (Seed seed) {}
	
	protected void moveOn (Seed seed, Node n) {
		if (n != null) n.compute(seed);
		else engine.exportDream ();
	}
	
	protected DreamEngine engine = null;
	public int type = -1;
	public Data data = null;
}
