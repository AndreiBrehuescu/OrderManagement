package validators;

public interface Validator<T> {
	
	/**
	 * Se valideaza obiectul transmis ca parametru in functie de validatori
	 * @param t - obiect cu clasa generica
	 */
	public void validate(T t);
	
}
