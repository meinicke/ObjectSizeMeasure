package size;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class TestSize {

	public static void main(String[] args) {
		for (int size = 1; size <= 10_000; size *= 10) {
			System.out.println("\nSize = " + size);
			List<Object> ll = new LinkedList<>();
			List<Object> al = new ArrayList<>();
			List<Object> alInit = new ArrayList<>(size);
			Set<Object> hset = new HashSet<>();
			Set<Object> tset = new TreeSet<>();
			Object[] oarray = new Object[size];
			for (int i = 0; i < size; i++) {
				Integer o = Integer.valueOf(i);
				ll.add(o);
				al.add(o);
				alInit.add(o);
				hset.add(o);
				tset.add(o);
				oarray[i] = o;
			}
			System.out.println("LinkedList:\t" + ObjectSizeMeasure.getSizeInByte(ll));
			System.out.println("ArrayList:\t" + ObjectSizeMeasure.getSizeInByte(al));
			System.out.println("ArrayList init:\t" + ObjectSizeMeasure.getSizeInByte(alInit));
			System.out.println("HashSet:\t" + ObjectSizeMeasure.getSizeInByte(hset));
			System.out.println("TreeSet:\t" + ObjectSizeMeasure.getSizeInByte(tset));
			System.out.println("Array:\t\t" + ObjectSizeMeasure.getSizeInByte(oarray));
		}
	}
}
