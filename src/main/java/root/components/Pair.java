package root.components;

import java.io.Serializable;

public class Pair<T> implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final T first;
	private final T second;

	public Pair(T first, T second) {
		this.first = first;
		this.second = second;
	}

	@Override
	public boolean equals(Object obj) {
		@SuppressWarnings("unchecked")
		Pair<T> p = (Pair<T>) obj;
		if (first == p.getFirst() && second == p.getSecond())
			return true;
		return false;
	}

	public T getFirst() {
		return first;
	}

	public T getSecond() {
		return second;
	}
}