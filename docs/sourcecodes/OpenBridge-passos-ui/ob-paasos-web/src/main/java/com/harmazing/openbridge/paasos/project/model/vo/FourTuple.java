package com.harmazing.openbridge.paasos.project.model.vo;

public class FourTuple<A,B,C,D> extends ThreeTuple<A,B,C>{
	
	public final D d;

	public FourTuple(A a, B b,C c,D d) {
		super(a,b,c);
		this.d=d;
	}
	
	
	

}
