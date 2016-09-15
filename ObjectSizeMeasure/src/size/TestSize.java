package size;


public class TestSize {
	
//	int i = 0;
	
	X[][] objectArray = new X[2][2];
	
	public TestSize() {
	}

	
	public static void main(String[] args) {
		TestSize o = new TestSize();
		
//		System.out.println(ObjectSizeMeasure.getSizeInByte(o));
		
		o.objectArray[0][1] = new X();
		o.objectArray[1][0] = new X();
		o.objectArray[1][1] = new X();
		
		System.out.println(ObjectSizeMeasure.getSizeInByte(o));
	}
}

class X {
	Object i = new Object();
}