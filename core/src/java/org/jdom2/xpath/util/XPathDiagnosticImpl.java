package org.jdom2.xpath.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jdom2.filter.Filter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathDiagnostic;

/**
 * A diagnostic implementation useful for diagnosing problems in XPath results.
 * <p>
 * This class tries to make all the data available as part of the internal
 * structure which may assist people who are stepping-through the code from 
 * a debugging environment.
 * 
 * @author Rolf Lear
 *
 * @param <T> The generic type of the results from the {@link XPathExpression}
 */
public class XPathDiagnosticImpl<T> implements XPathDiagnostic<T> {
	
	/*
	 * Keep nice list references here to help users who debug and step through
	 * code. They can inspect the various lists directly.
	 */
	private final Object dcontext;
	private final XPathExpression<T> dxpath;
	private final List<Object> draw;
	private final List<Object> dfiltered;
	private final List<T> dresult;
	private final boolean dfirstonly;
	
	/**
	 * Create a useful Diagnostic instance for tracing XPath query results.
	 * @param dcontext The context against which the XPath query was run.
	 * @param dxpath The {@link XPathExpression} instance which created this diagnostic.
	 * @param inraw The data as returned from the XPath library. 
	 * @param dfirstonly If the XPath library was allowed to terminate after the first result.
	 */
	public XPathDiagnosticImpl(Object dcontext, XPathExpression<T> dxpath,
			List<?> inraw, boolean dfirstonly) {
		
		final int sz = inraw.size();
		final List<Object> raw = new ArrayList<Object>(sz);
		final List<Object> filtered = new ArrayList<Object>(sz);
		final List<T> result = new ArrayList<T>(sz);
		final Filter<T> filter = dxpath.getFilter();
		
		for (Object o : inraw) {
			raw.add(o);
			T t = filter.filter(o);
			if (t == null) {
				filtered.add(o);
			} else {
				result.add(t);
			}
		}

		this.dcontext = dcontext;
		this.dxpath = dxpath;
		this.dfirstonly = dfirstonly;
		
		this.dfiltered = Collections.unmodifiableList(filtered);
		this.draw = Collections.unmodifiableList(raw);
		this.dresult = Collections.unmodifiableList(result);
		
	}

	@Override
	public Object getContext() {
		return dcontext;
	}

	@Override
	public XPathExpression<T> getXPathExpression() {
		return dxpath;
	}

	@Override
	public List<T> getResult() {
		return dresult;
	}

	@Override
	public List<Object> getFilteredResults() {
		return dfiltered;
	}

	@Override
	public List<Object> getRawResults() {
		return draw;
	}

	@Override
	public boolean isFirstOnly() {
		return dfirstonly;
	}
	
	@Override
	public String toString() {
		return String.format("[XPathDiagnostic: '%s' evaluated (%s) against " +
				"%s produced  raw=%d discarded=%d returned=%d]",
				dxpath.getExpression(), (dfirstonly ? "first" : "all"),
				dcontext.getClass().getName(), draw.size(), dfiltered.size(),
				dresult.size());
	}

}
