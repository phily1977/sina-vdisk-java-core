package vdiskcore;

import java.util.EventObject;

public class vdiskEvent extends EventObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double _progress;
	private long _speed;
	
	public vdiskEvent(Object arg0,double progress,long speed) {
		super(arg0);
		
		// TODO Auto-generated constructor stub
		this._progress = progress;
		this._speed = speed;
	}
	
	public double progress()
	{
		return this._progress;
	}

	public long speed()
	{
		return this._speed;
	}
}
