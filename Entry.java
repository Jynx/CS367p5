/**
 * This class is used for creating a suitable hashCode for use in the hastable
 * 
 * bugs: None known
 * 
 * @author Steven Volocyk & UoFW Madison CS department
 * 
 */
public class Entry {
	
	public static final int STATIC = 0;
	public static final int STRING = 1;
	public static final int LONG = 2;
	public static final int BOTH = 3;
	
	private String name; // name of indivdual
	private long phone;// phone number
	private int hashType; // type of hash
	private int mHash; // for hashcode 
	
	public Entry(String name, long phone, int hashType) {
		this.name = name;
		this.phone = phone;
		this.hashType = hashType;
	}
	
	@Override
	public String toString() {
		return name + ":" + phone;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof Entry) {
			Entry that = (Entry) other;
			if (that.name.equals(this.name) && that.phone == this.phone)
				return true;
		}
		
		return false;
	}
	
	/**
	 * Returns a hashCode for this object. You should complete the three
	 * different hash functions marked below.
	 * 
	 * Make note that when you write a hash function, it must always return
	 * the same value for the same object. In other words, you should not use
	 * any randomness to generate a hash code.
	 */
	@Override
	public int hashCode() {
		if (hashType == STRING) {
			mHash = name.hashCode();
			/* Hash on the String name only. Java has a built-in hashing 
			 * function for Strings; see 
			 * http://docs.oracle.com/javase/1.5.0/docs/api/java/lang/String.html#hashCode%28%29
			 * */			
			//Replace me with your hash function
			return mHash;
		}
		else if (hashType == LONG) {
			/* Hash on the phone number only. You may write whatever hash 
			 * function you like, as long as it involves the phone number 
			 * in some way. Mod and/or division both work well for this. */
			mHash = 0;
			String newLong = Long.toString(phone);  // conversion for character array
			char[] charArray = newLong.toCharArray();
			for (int i = 0; i < newLong.length(); i++) {
				mHash += Integer.parseInt(charArray[i] + ""); // adds all elements in array.
			}

			return mHash;
		}
		else if (hashType == BOTH) {
			/* Come up with your own hashing function. Your hashing function
			 * should have better performance than each of the other functions 
			 * on at least one of the given input files. 
			 * You may use the name, phone number, or both. */
			
			// uses both name and digits of phone number to compute a hashcode.
			int a,b,c = 0;
			int finalNum = 0;
			String newPhone = Long.toString(phone);
			a = newPhone.charAt(newPhone.length()-1);
			b = newPhone.charAt(newPhone.length()-2);
			c = newPhone.charAt(newPhone.length()-3);
			finalNum = a*b*c;
			mHash = name.hashCode() + finalNum;
			return mHash;
		}
		else {
			//Fixed hash function
			return 11;
		}
	}
}
