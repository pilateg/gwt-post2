package com.pils.post2;

import java.io.Serializable;

public class Tuple2<X, Y> implements Serializable {
	public X first;
	public Y second;

	public Tuple2(X first, Y second) {
		this.first = first;
		this.second = second;
	}
}
