package size;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Measures the size of a given object including the sizes all fields.
 * 
 * @see <a href=
 *      "http://stackoverflow.com/questions/52353/in-java-what-is-the-best-way-to-determine-the-size-of-an-object">stackoverflow</a>
 * 
 * @author Jens Meinicke
 */
public class ObjectSizeMeasure {

	public static boolean VERBOUS = false;

	private static final Collection<IdentityWrapper> MEASURED_OBJECTS = new HashSet<>();

	private static class IdentityWrapper {

		final Object object;

		public IdentityWrapper(Object object) {
			this.object = object;
		}

		@Override
		public int hashCode() {
			return System.identityHashCode(object);
		}

		@Override
		public boolean equals(Object obj) {
			return object == ((IdentityWrapper) obj).object;
		}
	}
	
	/** 
	 * 
	 * For debug purpose only.
	 *
	 */
	private static class LogWrapper {
		int depth;
		Object o;
		
		public LogWrapper(Object o, int depth) {
			this.o = o;
			this.depth = depth;
		}
	}

	private static final Set<Class<?>> BLACK_LIST = new HashSet<>();

	/**
	 * Ignore the given class if its size should not be measured.
	 */
	public static void ignoreClass(Class<?> c) {
		BLACK_LIST.add(c);
	}

	public static long getSizeInByte(Object o) {
		try {
			long objectSize = ObjectSizeFetcher.getObjectSize(o) / 8;
			printLog(0, o.getClass().getSimpleName(), objectSize);
			return objectSize + getDeepSize(o);
		} finally {
			MEASURED_OBJECTS.clear();
		}
	}

	private static long getDeepSize(Object o) {
		final LinkedList<LogWrapper> objectsToMeasure = new LinkedList<>();
		objectsToMeasure.push(new LogWrapper(o, 0));

		long size = 0;
		while (!objectsToMeasure.isEmpty()) {
			
			final LogWrapper currentWrapper = objectsToMeasure.pop();
			final Object currentObject = currentWrapper.o;
			final int depth = currentWrapper.depth;
			MEASURED_OBJECTS.add(new IdentityWrapper(currentObject));

			final List<Field> fields = new ArrayList<>();
			getAllFields(currentObject.getClass(), fields);

			for (Field f : fields) {
				if (Modifier.isStatic(f.getModifiers())) {
					continue;
				}
				if (f.getType().isPrimitive()) {
					continue;
				}
				final boolean isAccesible = f.isAccessible();
				f.setAccessible(true);
				Object child;
				try {
					child = f.get(currentObject);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
					continue;
				}
				if (child == null || BLACK_LIST.contains(child.getClass())
						|| MEASURED_OBJECTS.contains(new IdentityWrapper(child))) {
					continue;
				}

				long objectSize = ObjectSizeFetcher.getObjectSize(child) / 8;
				size += objectSize;
				printLog(depth, f.toString(), objectSize);
				f.setAccessible(isAccesible);
				objectsToMeasure.push(new LogWrapper(child, depth + 1));
			}

			if (currentObject.getClass().isArray()) {
				if (currentObject instanceof Object[]) {
					Object[] objectArray = (Object[]) currentObject;
					int index = 0;
					for (Object object : objectArray) {
						if (object == null || BLACK_LIST.contains(object.getClass())) {
							printLog(depth, "[" + index + "] null", 0);
							index++;
							continue;
						}
						if (MEASURED_OBJECTS.contains(new IdentityWrapper(object))) {
							printLog(depth, "[" + index + "] " + object.getClass(), 0);
							index++;
							continue;
						}
						long objectSize = ObjectSizeFetcher.getObjectSize(object) / 8;
						size += objectSize;
						printLog(depth, "[" + index + "] " + object.getClass(), objectSize);
						objectsToMeasure.push(new LogWrapper(object, depth + 1));
						index++;
					}
				}
			}
		}
		return size;
	}

	private static void printLog(int depth, String field, long objectSize) {
		if (VERBOUS) {
			for (int i = 0; i < depth; i++) {
				System.out.print(" ");
			}
			System.out.println(field + " - " + objectSize);
		}
	}

	private static void getAllFields(Class<?> o, List<Field> fields) {
		fields.addAll(Arrays.asList(o.getDeclaredFields()));
		if (o.getSuperclass() != null) {
			getAllFields(o.getSuperclass(), fields);
		}
	}
}
