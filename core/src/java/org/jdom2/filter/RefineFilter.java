package org.jdom2.filter;

final class RefineFilter<T> extends AbstractFilter<T> {
	
	private final Filter<T> refiner;
	
	public RefineFilter(Filter<T> refiner) {
		super();
		this.refiner = refiner;
	}

	@Override
	public T filter(Object content) {
		return refiner.filter(content);
	}

}
