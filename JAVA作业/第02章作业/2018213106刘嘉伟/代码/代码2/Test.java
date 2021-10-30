public class Test{
	public static void main(String[] args){
		Cat cat = new Cat();
		Dog dog = new Dog();
		makeCry(cat);
		makeCry(dog);
	}
	public static void makeCry(Animal ani){
			ani.CanCry();
		}
}