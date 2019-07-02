import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class printIP {

	//Global Scanner input
	static Scanner input = new Scanner(System.in);
	static int illegalInput = 0;

	public static void main(String[] args) {

		System.out.println(
				"-------------------------------------------------------------------------------------------------------\r\n"+
						"  _____                     _     _                      _____                           _             \r\n" + 
						" |_   _|           /\\      | |   | |                    / ____|                         | |            \r\n" + 
						"   | |  _ __      /  \\   __| | __| |_ __ ___  ___ ___  | |  __  ___ _ __   ___ _ __ __ _| |_ ___  _ __ \r\n" + 
						"   | | | '_ \\    / /\\ \\ / _` |/ _` | '__/ _ \\/ __/ __| | | |_ |/ _ \\ '_ \\ / _ \\ '__/ _` | __/ _ \\| '__|\r\n" + 
						"  _| |_| |_) |  / ____ \\ (_| | (_| | | |  __/\\__ \\__ \\ | |__| |  __/ | | |  __/ | | (_| | || (_) | |   \r\n" + 
						" |_____| .__/  /_/    \\_\\__,_|\\__,_|_|  \\___||___/___/  \\_____|\\___|_| |_|\\___|_|  \\__,_|\\__\\___/|_|   \r\n" + 
						"       | |                                                                                             \r\n" + 
						"       |_|                                                                                             \r\n"+
				"--------------------------------------------------------------------------------------------------------");
		System.out.println("Welcome to the IP Address Range Generator!\n"
				+ "\nInput a IP address range and the program will output the\nresults to a text file called 'IP_Range_{IPADDRESS}.txt'\n"
				+ "\nExample Input: 10.0.2.1-255\n"
				+ "Example Input: 192,210.168.1.1-100\n"
				+ "Example Input: 10-15.8,10,22,34.6-15.2-200\n"
				+ "Example Input: 3,2-15.3,8.10,10-15.17-25"
				+ "\n\nMake sure you format your IP Address Correctly! Examples provided above. Thank you :)");
		
		//User Input in main
		String in = new String();
		
		//Main driver
		do {
			//Catches some errors and trys again! :)
			try {
				driver();
			} catch (Exception e) {
				System.out.println("WHOOPS! Something went wrong! Try again :)");continue;
			}
			

			System.out.print("\n> Would you like to print another IP Address? (Yes/No): ");
			in = input.nextLine();
			in = in.toLowerCase();

			if(in.contains("n")) {
				System.out.println("\nThanks come again! :)");
				break;
			}
			else {
				continue;
			}

		} while (true);

		input.close();
		return;
	}


	public static void driver() {
		System.out.print("> Please input IP Address Range: ");
		String ipAddr = new String();
		String fileName = new String();
		int periods, illegalChar = 0;

		do {

			//Set and reset to 0
			periods = 0;
			illegalChar = 0;

			//Get user input
			ipAddr = input.nextLine();

			//Gets rid of accidental? spaces before||after input
			ipAddr = ipAddr.trim();
			for (int i = 0; i < ipAddr.length(); i++) {
				//Count periods
				if(ipAddr.charAt(i)=='.') {
					periods+=1;
				}
				//If the string contains an illegal Character
				if(!ipAddr.substring(i, i+1).matches(".*[0-9,.-].*")) {
					illegalChar += 1;
				}

			}

			//Bad Formatting Cases (that I could think of)
			if(illegalChar > 0) {
				System.out.print("\nYou did not enter a correctly forrmatted IP Address range. Please try again.\n> Please input IP Address Range: ");
				continue;
			}
			else if(periods!=3) {
				System.out.print("\nYou did not enter a correctly forrmatted IP Address range. Please try again.\n> Please input IP Address Range: ");
				continue;
			}
			else if(ipAddr.length()<7) {
				System.out.print("\nYou did not enter a correctly forrmatted IP Address range. Please try again.\n> Please input IP Address Range: ");
				continue;
			}
			else if(ipAddr.contains("..")||ipAddr.contains(",,")||
					ipAddr.contains("--")||ipAddr.contains(",-")||
					ipAddr.contains("-,")||ipAddr.contains(",.")||
					ipAddr.contains(".,")
					) {
				System.out.print("\nYou did not enter a correctly forrmatted IP Address range. Please try again.\n> Please input IP Address Range: ");
				continue;
			}
			else {
				break;
			}
		} while (true);

		//Set fileName
		fileName = "IP_Range_"+ipAddr.replace(",", "_")+".txt";

		try {
			//Text file we are going to write too
			PrintWriter outFile = new PrintWriter(fileName);

			//If there are no commas, then we just need to write the IP to the file
			if(!ipAddr.contains(",")) {
				writeIPs(ipAddr, outFile);
			}else {
				//Get all the IP's that we want to print
				ArrayList<String> ips = ParseCommas(ipAddr);
				//For each IP in Arraylist. Call writeIP
				for (String ip : ips) {
					
					//Check if there was illegal input, if so, reprompt the user
					if(illegalInput==1) {
						illegalInput=0;
						return;
					}
					
					writeIPs(ip,outFile);
				}
			}
			//Close outFile
			outFile.close();

			//Check if there was illegal input, if so, reprompt the user
			if(illegalInput==1) {
				illegalInput=0;
				return;
			}


		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("***FATAL ERROR***\n*****SHOULD NEVER GET HERE******\n****IF REACHED PLEASE CONTACT ME ON GITHUB******");
			System.exit(1);
		}
		System.out.println("\nWrote output to: "+fileName);
	}

	//Method to parseCommas
	public static ArrayList<String> ParseCommas(String ipAddr) {
		String ip = ipAddr;

		//Gets the indices of all the periods in the IP Address
		int i1 = 0,i2 = 0,i3 = 0;
		i1 = ip.indexOf(".");
		i2 = ip.indexOf(".", i1+1);
		i3 = ip.indexOf(".", i2+1);

		//Grabs all the octets
		String p1 = "",p2= "",p3= "",p4= "";
		p1 = ip.substring(0, i1);
		p2 = ip.substring(i1+1, i2);
		p3 = ip.substring(i2+1, i3);
		p4 = ip.substring(i3+1, ip.length());

		//Return Array To store all the values to be printed
		ArrayList<String> vals = new ArrayList<String>();

		//Temp array for holding the comma separated values
		ArrayList<String> tempVals = new ArrayList<String>();


		StringTokenizer st; 
		//Part 1
		if (p1.contains(",")) {
			st = new StringTokenizer(p1, ",", false);
			while(st.hasMoreTokens()) {
				vals.add(st.nextToken());
			}
		}else {
			vals.add(p1);
		}

		//Part 2
		if (p2.contains(",")) {
			st = new StringTokenizer(p2, ",", false);
			while(st.hasMoreTokens()) {
				tempVals.add(st.nextToken());
			}

			int p1Size = vals.size();

			//Makes vals Array the right size
			for(int i=0; i<tempVals.size()-1; i++) {
				for(int j=0; j<p1Size; j++) {
					vals.add(vals.get(j));
				}
			}

			//copy values
			int el = 0;
			for(int i=0; i<tempVals.size(); i++) {
				for(int j=0; j<p1Size; j++) {
					vals.set(el, vals.get(el).concat("."+tempVals.get(i)));
					el++;
				}
			}
			tempVals.removeAll(tempVals);
		}else {
			for(int i = 0; i < vals.size(); i++) {
				vals.set(i, vals.get(i).concat("."+p2));
			}
		}

		//Part 3
		if (p3.contains(",")) {
			st = new StringTokenizer(p3, ",", false);
			int p2Size = vals.size();

			while(st.hasMoreTokens()) {
				tempVals.add(st.nextToken());
			}

			//Makes vals Array the right size
			for(int i=0; i<tempVals.size()-1; i++) {
				for(int j=0; j<p2Size; j++) {
					vals.add(vals.get(j));
				}
			}

			//copy values
			int el = 0;
			for(int i=0; i<tempVals.size(); i++) {
				for(int j=0; j<p2Size; j++) {
					vals.set(el, vals.get(el).concat("."+tempVals.get(i)));
					el++;
				}
			}
			tempVals.removeAll(tempVals);
		}else {
			for(int i = 0; i < vals.size(); i++) {
				vals.set(i, vals.get(i).concat("."+p3));
			}
		}

		//Part 4
		if (p4.contains(",")) {
			st = new StringTokenizer(p4, ",", false);			
			while(st.hasMoreTokens()) {
				tempVals.add(st.nextToken());
			}

			//Size of array before editing
			int p3Size = vals.size();

			//Makes vals Array the right size
			for(int i=0; i<tempVals.size()-1; i++) {
				for(int j=0; j<p3Size; j++) {
					vals.add(vals.get(j));
				}
			}

			//copy values into the vals arrayList
			int el = 0;
			for(int i=0; i<tempVals.size(); i++) {
				for(int j=0; j<p3Size; j++) {
					vals.set(el, vals.get(el).concat("."+tempVals.get(i)));
					el++;
				}
			}
			tempVals.removeAll(tempVals);
		}else {
			for(int i = 0; i < vals.size(); i++) {
				vals.set(i, vals.get(i).concat("."+p4));
			}
		}

		//Return the arrayList of values
		return vals;


	}

	//Only get here if there is a dash or comma in the IP Address
	public static void writeIPs(String ipAddr, PrintWriter outFile) {

		String tempAddress = new String();

		//Break up each IP Address Octet into an ArrayList
		ArrayList<String> address = new ArrayList<String>();		
		StringTokenizer st = new StringTokenizer(ipAddr, ".", false);
		while(st.hasMoreTokens()) {
			address.add(st.nextToken());
		}

		//Get upper and lower ranges of each IP Octet
		int p1L,p1U,p2L,p2U,p3L,p3U,p4L,p4U;
		p1L = getLower(address.get(0));
		p1U = getUpper(address.get(0));
		p2L = getLower(address.get(1));
		p2U = getUpper(address.get(1));
		p3L = getLower(address.get(2));
		p3U = getUpper(address.get(2));
		p4L = getLower(address.get(3));
		p4U = getUpper(address.get(3));

		if(p1L>p1U||p1L<0||p1U>255) {
			System.out.print("\nYou did not enter a correctly forrmatted IP Address range.\n");
			illegalInput = 1;
			return;
		}
		if(p2L>p2U||p2L<0||p2U>255) {
			System.out.print("\nYou did not enter a correctly forrmatted IP Address range.\n");
			illegalInput = 1;
			return;

		}
		if(p3L>p3U||p3L<0||p3U>255) {
			System.out.print("\nYou did not enter a correctly forrmatted IP Address range.\n");
			illegalInput = 1;
			return;

		}
		if(p4L>p4U||p4L<0||p4U>255) {
			System.out.print("\nYou did not enter a correctly forrmatted IP Address range.\n");
			illegalInput = 1;
			return;

		}

		//if lower>upper: exit

		//[10-15].[8-10].[64].[2-5]
		//   p1     p2    p3    p4
		//Prints out the IP address by going from the upper and lower range of each octet
		for (int p1 = p1L; p1 <= p1U; p1++) { 
			for (int p2 = p2L; p2 <= p2U; p2++) {
				for (int p3 = p3L; p3 <= p3U; p3++) {
					for (int p4 = p4L; p4 <= p4U; p4++) {
						//System.out.print(p1+"."+p2+"."+p3+"."+p4+"\n");
						tempAddress = p1+"."+p2+"."+p3+"."+p4+"\n";
						outFile.write(tempAddress);
					}
				}
			}
		}

	}

	public static int getLower(String str) {
		if (str.indexOf('-')==-1) {
			return Integer.parseInt(str);			
		}
		return Integer.parseInt(str.substring(0, str.indexOf('-')));
	}

	public static int getUpper(String str) {
		if (str.indexOf('-')==-1) {
			return Integer.parseInt(str);			
		}
		return Integer.parseInt(str.substring(str.indexOf('-')+1,str.length()));
	}

}

