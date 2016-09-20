package size;

public class TestSize {
	
	public static void main(String[] args) {
		ObjectSizeMeasure.VERBOUS = true;
		TestObject o = new TestObject();
		System.out.println(ObjectSizeMeasure.getSizeInByte(o));
		System.out.println("--------------");
		System.out.println(ObjectSizeMeasure.getSizeInByte(o.object));
		System.out.println("--------------");
		System.out.println(ObjectSizeMeasure.getSizeInByte(o.array));
		System.out.println("--------------");
		System.out.println(ObjectSizeMeasure.getSizeInByte(o.integer));
		System.out.println("--------------");
		System.out.println(ObjectSizeMeasure.getSizeInByte(o.l));
		
		System.out.println("--------------");
		ObjectSizeMeasure.VERBOUS = false;
		Integer[] array = new Integer[10];
		System.out.println("array size:" + ObjectSizeMeasure.getSizeInByte(array));
		for (int i = 0; i < array.length; i++) {
			array[i] = Integer.valueOf(i);
			System.out.println("array size:" + ObjectSizeMeasure.getSizeInByte(array));
		}
		for (int i = 0; i < array.length; i++) {
			array[i] = Integer.valueOf(100);
			System.out.println("array size:" + ObjectSizeMeasure.getSizeInByte(array));
		}
		
	}
}

class TestObject {
	Object object = new Object();
	Object[] array = new Object[10];
	int integer = 1;
	long l = 10;
	
	public TestObject() {
		for (int i = 0; i < array.length; i++) {
			array[i] = Integer.valueOf(i);
		}
			
	}
}

