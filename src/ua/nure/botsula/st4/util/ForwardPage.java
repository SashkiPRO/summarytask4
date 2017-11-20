package ua.nure.botsula.st4.util;

public class ForwardPage {

	public static String getForward(String referrer){
		String path = referrer;
		String array[] = path.split("/");
		StringBuilder existPath = new StringBuilder();
		existPath.append("/");
		existPath.append(array[array.length-1]);
		
		return existPath.toString();
	}
	public static boolean isJsp(String path){
		String temp = path;
		if(temp.substring(temp.length()-4, temp.length()).equals(".jsp")){
			System.out.println(temp.substring(temp.length()-4, temp.length()));
			return true;
		}else{
			return false;
		}
	}
}
