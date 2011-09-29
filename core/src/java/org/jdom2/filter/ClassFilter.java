package org.jdom2.filter;

final class ClassFilter<T> extends AbstractFilter<T> {

	private final Class<? extends T> fclass;

	public ClassFilter(Class<? extends T> tclass) {
		fclass = tclass;
	}

	@Override
	public T filter(Object content) {
		return fclass.isInstance(content) ? fclass.cast(content) : null;
	}
	
	@Override
	public String toString() {
		return "[ClassFilter: Class " + fclass.getName() + "]";
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof ClassFilter<?>) {
			return fclass.equals(((ClassFilter<?>)obj).fclass);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return fclass.hashCode();
	}

	

}