package main;

public class OperationalNode extends Node {

	@Override
	public void compute(Seed seed) {
		if (operation == null || operation.length() != 2) {
			moveOn (seed, nextNode);
			return;
		}
		char c1 = operation.charAt(0);
		char c2 = operation.charAt(1);
		if (c1 == 'a') seed.a = completeAction (seed.a, c2);
		if (c1 == 'b') seed.b = completeAction (seed.b, c2);
		if (c1 == 'c') seed.c = completeAction (seed.c, c2);
		
		moveOn (seed, nextNode);
	}
	
	private int completeAction (int on, char operation) {
		int difference = 0;
		switch (operation) {
		case '0':
			difference = 1;
			break;
		case '1':
			difference = 5;
			break;
		case '2':
			difference = on;
			break;
		case '3':
			difference = on * 2;
			break;
		case '4':
			difference = -(on/2);
			break;
		case '5':
			difference = -5;
			break;
		case '6':
			difference = -1;
			break;
		}
		return on + difference;
	}
	
	private Node nextNode;
	public String operation;
	
	public OperationalNode (DreamEngine e, Data d, String o) {
		engine = e;
		type = 0;
		data = d;
		operation = o;
	}

}
