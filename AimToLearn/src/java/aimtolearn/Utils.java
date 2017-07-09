package aimtolearn;

public class Utils {

	public static <T extends Enum<T>> T getEnum(String name, Class<T> type) throws IllegalArgumentException {
		return Enum.valueOf(type, name.trim().toUpperCase());
	}

}
