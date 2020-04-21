package main;

import java.util.Random;

public class NodeGenerator {
	// TODO: Implement skipping and repeating
	public static String makeInstructionSequence () {
		Random random = new Random ();
		String is = "";
		int numInstrs = random.nextInt(500)+500;
		for (int i = 0; i < numInstrs; i++) {
			if (random.nextInt(3) > 0) {
				is += randomCommand();
				is += ";";
			} else {
				is += "19;";
				int ts = random.nextInt(50);
				for (int j = 0; j < ts; j++) is += randomMoveCommand() + ";";
				is += "20;";
			}
		}
		return is;
	}
	
	/** Generate a random command, with weighted probability as shown below:
	 * 
	 * 00 - Copy - 30%
	 * 01 - Read - 2%
	 * 
	 * 02 - Toggle state value - 3%
	 * 03 - Advance state cursor - 3%
	 * 04 - Regress state cursor - 3%
	 * 05 - Reset state cursor - 1%
	 * 06 - Reset state under cursor - 1%
	 * 07 - Skip next instructions if state under cursor is true - 0% (inserted separately)
	 * 08 - End skip - 0% (inserted separately)
	 * 
	 * 09 - Read buffer pixel to states - 5%
	 * 10 - Write states to buffer pixel - 5%
	 * 
	 * 11 - Increment buffer x cursor - 5%
	 * 12 - Decrement buffer x cursor - 5%
	 * 13 - Increment buffer y cursor - 5%
	 * 14 - Decrement buffer y cursor - 5%
	 * 15 - Increment dream x cursor - 5%
	 * 16 - Decrement dream x cursor - 5%
	 * 17 - Increment dream y cursor - 5%
	 * 18 - Decrement dream y cursor - 5%
	 * 
	 * 19 - Begin area copy operation - 0% (inserted separately)
	 * 20 - End area copy operation - 0% (inserted separately)
	 * 
	 * 21 - Repeat next instructions based on value of states - 0% (inserted separately)
	 * 22 - End repeat - 0% (inserted separately)
	 * 
	 * 23 - Radial copy - 4%
	 * 24 - Jump - 3%
	 * 
	 * @return String The command 
	 */
	private static String randomCommand () {
		Random r = new Random ();
		int i = r.nextInt(100);
		if (i < 30) {
			return "0";
		} else if (i < 32) {
			return "1";
		} else if (i < 35) {
			return "2";
		} else if (i < 38) {
			return "3";
		} else if (i < 41) {
			return "4";
		} else if (i < 42) {
			return "5";
		} else if (i < 43) {
			return "6";
		} else if (i < 48) {
			return "9";
		} else if (i < 53) {
			return "10";
		} else if (i < 58) {
			return "11";
		} else if (i < 63) {
			return "12";
		} else if (i < 68) {
			return "13";
		} else if (i < 73) {
			return "14";
		} else if (i < 78) {
			return "15";
		} else if (i < 83) {
			return "16";
		} else if (i < 88) {
			return "17";
		} else if (i < 93) {
			return "18";
		} else if (i < 97) {
			return "23";
		}
		return "24";
	}

	private static String randomMoveCommand () {
		Random r = new Random ();
		if (r.nextBoolean()) return Integer.toString(r.nextInt(8) + 11);
		return "24";
	}
}
