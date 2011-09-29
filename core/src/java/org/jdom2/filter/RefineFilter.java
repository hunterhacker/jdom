package org.jdom2.filter;

final class RefineFilter<T> extends AbstractFilter<T> {

	private final Filter<?> base;
	private final Filter<T> refiner;

	public RefineFilter(Filter<?> base, Filter<T> refiner) {
		if (base == null || refiner == null) {
			throw new NullPointerException("Cannot have a null base or refiner filter");
		}
		this.base = base;
		this.refiner = refiner;
	}

	@Override
	public T filter(Object content) {
		Object o = base.filter(content);
		if (o != null) {
			return refiner.filter(content);
		}
		return null;
	}
	
	@Override
	public int hashCode() {
		return base.hashCode() ^ (~refiner.hashCode());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof RefineFilter<?>) {
			RefineFilter<?> them = (RefineFilter<?>)obj;
			return base.equals(them.base) && refiner.equals(them.refiner);
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		return "[RefinerFilter: Refine " + base + " with " + refiner + "]";
	}

}
